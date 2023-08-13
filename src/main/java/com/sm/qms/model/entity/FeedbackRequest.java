package com.sm.qms.model.entity;

import java.util.UUID;

public class FeedbackRequest {

    private String visitorId;
    private String appointmentId;
    private String message;
    private float rating; //1-5

    public String getVisitorId() {
        return visitorId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getMessage() {
        return message;
    }

    public float getRating() {
        return rating;
    }
}
