package com.sm.qms.model.database;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(value = "appointments")
public class TableAppointment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PrimaryKey
    @Column
    private UUID id;
    private String visitorId;
    @Column
    private String storeId;
    private String date; // appointment date
    private TableTimeSlot timeSlot;

    private String timestamp; // appointment creation date

    private String status; // CONFIRMED

    public TableAppointment() {
    }

    public TableAppointment(UUID id, String visitorId, String storeId, String date, TableTimeSlot timeSlot, String timestamp, String status) {
        this.id = id;
        this.visitorId = visitorId;
        this.storeId = storeId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.timestamp = timestamp;
        this.status = status;
    }

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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TableTimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TableTimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }
}
