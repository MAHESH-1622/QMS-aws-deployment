package com.sm.qms.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class UpdateAppointmentRequest {

    //TODO - Add appointment status -> CONFIRMED => COMPLETED

    private String id; // appointment ID
    private String storeId; // service location ID
    private String date;//appointment date (YYYY-MM-DD)
    @JsonFormat(pattern = "HH:mm")
    public LocalTime start;
    @JsonFormat(pattern = "HH:mm")
    public LocalTime end;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
