package com.sm.qms.model.enums;

public enum AppointmentStatus {

    CREATED, //new
    CONFIRMED, //acknowledged
    COMPLETED, //processed and done
    CANCELLED, //visitor cancellation
    REJECTED, //admin dismissal
    PENDING, //not taken action yet but due
    PROGRESS, //in action
}
