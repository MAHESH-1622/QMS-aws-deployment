package com.sm.qms.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sm.qms.model.database.TableTimeSlot;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentRequest {

    private String visitorId; //customer ID
    private String storeId; // service location ID
//    private String description;//optional info about the appointment query
    private String date;//appointment date (YYYY-MM-DD)
    private String category;//issue category

    @JsonFormat(pattern = "HH:mm")
    public LocalTime start;
    @JsonFormat(pattern = "HH:mm")
    public LocalTime end;
//    private String start;
//    private String end;

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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


}
