package com.example.carapplication.exceptions;

import lombok.AllArgsConstructor;


public class OTPMissMatchException extends Exception{
    public OTPMissMatchException() {
    }

    public OTPMissMatchException(String message) {
        super(message);
    }

    public OTPMissMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public OTPMissMatchException(Throwable cause) {
        super(cause);
    }

    public OTPMissMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
