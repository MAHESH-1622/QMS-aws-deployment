package com.sm.qms.model.entity;

import com.sm.qms.model.database.TableTimeSlot;

import java.util.UUID;

public class AppointmentResponse {
    private UUID id;
    private String visitorId;
 //   private String storeId;
 //   private String date;
    private TableTimeSlot timeSlot;
  //  private String timestamp;
    private String status;

    // Getters and setters

    public UUID getId() {
        return id;
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

  /*  public String getStoreId() {
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
    } */

    public TableTimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TableTimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

 /*   public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    } */

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}