package com.sm.qms.model.database;

import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;


@Entity
@Table(value = "appointment_cancellation")
public class TableAppointmentCancellation {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private UUID appointmentId;
    @Column
    private String storeId;
    @Column
    @JsonRawValue
    private String cancellationReason;
    @Column
    private Timestamp CancellationDateTime;
    public  TableAppointmentCancellation(){
        //Default constructor
    }

    public TableAppointmentCancellation(UUID id, UUID appointmentId, String storeId, String cancellationReason, Timestamp cancellationDateTime) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.storeId = storeId;
        this.cancellationReason = cancellationReason;
        CancellationDateTime = cancellationDateTime;
    }

    public TableAppointmentCancellation(UUID appointmentId,String storeId,String cancellationReason) {
        this.id = UUID.randomUUID();
        this.appointmentId = appointmentId;
        this.storeId=storeId;
        this.cancellationReason = cancellationReason;
        this.CancellationDateTime= new Timestamp(System.currentTimeMillis());


    }


    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCancellationDateTime() {
        LocalDateTime localDateTime = CancellationDateTime.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public void setCancellationDateTime(Timestamp cancellationDateTime) {
        CancellationDateTime = cancellationDateTime;
    }
// ...
}