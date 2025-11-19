package com.josephhieu.orderservice.entity;

public enum Status {

    PENDING_PAYMENT,
    PAID,
    PREPARING,
    READY_FOR_DISPATCH,
    DELIVERING,
    COMPLETED,
    CANCELLED,
    PAYMENT_FAILED
}
