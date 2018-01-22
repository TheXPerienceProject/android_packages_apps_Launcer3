package com.google.research.reflection.common;

public class UncertaintyException extends Exception {
    private long estimation;

    public UncertaintyException(final long estimation) {
        this.estimation = estimation;
    }
}
