package com.sm.qms.common;

public class QmsException extends Exception {

    public QmsException(String message) {
        super(message);
    }

    public QmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public QmsException(Throwable cause) {
        super(cause);
    }
}
