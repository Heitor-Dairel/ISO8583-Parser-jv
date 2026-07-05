package com.servnet.mastercard.exception;

public class IPMConfigException extends RuntimeException {

    public IPMConfigException(Throwable cause) {
        super(cause);
    }

    public IPMConfigException(String format, Object... args) {
        super(String.format(format, args));
    }

    public IPMConfigException(Throwable cause, String message) {
        super(message, cause);
    }

    public IPMConfigException(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

}
