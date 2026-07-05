package com.servnet.mastercard.exception;

public class IPMParseException extends RuntimeException {

    public IPMParseException(Throwable cause) {
        super(cause);
    }

    public IPMParseException(String format, Object... args) {
        super(String.format(format, args));
    }

    public IPMParseException(Throwable cause, String message) {
        super(message, cause);
    }

    public IPMParseException(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

}