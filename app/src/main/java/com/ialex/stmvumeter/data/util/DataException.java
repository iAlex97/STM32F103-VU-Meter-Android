package com.ialex.stmvumeter.data.util;

public class DataException extends RuntimeException {

    public Status status;

    public DataException(Status status) {
        this.status = status;
    }

    public DataException(Status status, Throwable throwable) {
        super(throwable);
        this.status = status;
    }

    public enum Status {
        UNKNOWN, NETWORK_ERROR, CUSTOM_ERROR
    }
}
