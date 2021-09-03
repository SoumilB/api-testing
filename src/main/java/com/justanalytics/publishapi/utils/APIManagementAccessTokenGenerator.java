package com.justanalytics.publishapi.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class APIManagementAccessTokenGenerator {

    @Value("${api-management.identifier}")
    private String identifier;

    @Value("${api-management.key}")
    private String key;

    @Value("${api-management.expiration-delay-in-seconds}")
    private Integer expirationDelay;

    private static final String ALGORITHM = "HmacSHA512";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").withZone(ZoneId.of("UTC"));

    public String generateAccessToken() {
        final byte[] bytekey = key.getBytes(StandardCharsets.UTF_8);
        String expiry = DATE_FORMATTER.format(Instant.now().plusSeconds(expirationDelay)) + ":00.0000000Z";
        String stringToSign = String.format("%s\n%s", identifier, expiry);
        try {
            Mac encoder = Mac.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(bytekey, ALGORITHM);
            encoder.init(keySpec);
            byte[] encodedHash = encoder.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.getEncoder().encodeToString(encodedHash);
            String accessToken = String.format("SharedAccessSignature uid=%s&ex=%s&sn=%s", identifier, expiry, signature);
            return accessToken;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return "";
        }
    }

}
