package com.justanalytics.publishapi.exceptions;

public class UnAccessibleSystemException extends BaseException {
    private static final long serialVersionUID = -5220937131925047808L;

    @Override
    public String getMessage() {
        return "Databsae was not found or was not accessible";
    }

}
