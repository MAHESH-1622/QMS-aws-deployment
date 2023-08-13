package com.sm.qms.model.entity;

import org.springframework.data.cassandra.core.mapping.Column;

import java.sql.Timestamp;
import java.util.UUID;

public class AppointmentCancellationRequest {

    private UUID appointmentId;
    private UUID storeId;
    private String cancellationReason;
    private Timestamp cancellationDateTime;


    public Timestamp getcancellationDateTime() {
        return cancellationDateTime;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public UUID getStoreId() {
        return storeId;
    }
}
