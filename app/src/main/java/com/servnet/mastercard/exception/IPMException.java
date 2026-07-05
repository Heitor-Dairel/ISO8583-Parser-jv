package com.servnet.mastercard.exception;

public class IPMException extends RuntimeException {

    public IPMException(Throwable cause) {
        super(cause);
    }

    public IPMException(String format, Object... args) {
        super(String.format(format, args));
    }

    public IPMException(Throwable cause, String message) {
        super(message, cause);
    }

    public IPMException(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

}
