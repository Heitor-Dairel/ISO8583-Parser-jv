package com.servnet.mastercard.exception;

public class IPMFileException extends RuntimeException {

    public IPMFileException(Throwable cause) {
        super(cause);
    }

    public IPMFileException(String format, Object... args) {
        super(String.format(format, args));
    }

    public IPMFileException(Throwable cause, String message) {
        super(message, cause);
    }

    public IPMFileException(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

}
