package com.sm.qms.model.database;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table(value = "feedbacks")
public class TableFeedback {

    @PrimaryKey
    private UUID id;
    private String visitorId;
    private UUID appointmentID;
    private String message;
    private float rating; //1-5
    private Long timestamp;//date

    public UUID getId() {
        return id;
    }

    public UUID getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(UUID appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
