package com.justanalytics.publishapi.filters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
public class ClientCertValidationFilter extends OncePerRequestFilter {

    public static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERT = "-----END CERTIFICATE-----";

    @Value("${client-certificate.thumbprint}")
    private String THUMBPRINT;
    @Value("${client-certificate.header}")
    private String CERT_HEADER;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String certificateString = request.getHeader(CERT_HEADER);
        if (certificateString != null && !certificateString.isBlank()) {
            certificateString = certificateString.replaceAll(BEGIN_CERT, "").replaceAll(END_CERT, "");
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                byte[] base64Bytes = Base64.getDecoder().decode(certificateString);
                X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(base64Bytes));

                if(!this.certificateIsValid(x509Cert)) {
                    throw new CertificateException("Certificate Not Valid");
                }
                
                filterChain.doFilter(request, response);
            } catch (CertificateException | NoSuchAlgorithmException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid Certificate");
            } 
        } else {
            filterChain.doFilter(request, response);
        }
    }

    public boolean certificateIsValid(X509Certificate certificate) throws NoSuchAlgorithmException, CertificateEncodingException {
        return certificateHasNotExpired(certificate) && thumbprintIsValid(certificate);
    }

    private boolean certificateHasNotExpired(X509Certificate certificate) {        
        try {
            certificate.checkValidity(new Date());
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            return false;
        }
        return true;
    }

    private boolean thumbprintIsValid(X509Certificate certificate) throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] der = certificate.getEncoded();
        md.update(der);
        byte[] digest = md.digest();
        String digestHex = DatatypeConverter.printHexBinary(digest);
        return digestHex.toLowerCase().equals(THUMBPRINT.toLowerCase());
    }
    
}
