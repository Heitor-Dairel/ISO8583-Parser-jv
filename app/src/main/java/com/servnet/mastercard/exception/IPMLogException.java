package com.servnet.mastercard.exception;

public class IPMLogException extends RuntimeException {

    public IPMLogException(Throwable cause) {
        super(cause);
    }

    public IPMLogException(String format, Object... args) {
        super(String.format(format, args));
    }

    public IPMLogException(Throwable cause, String message) {
        super(message, cause);
    }

    public IPMLogException(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

}
