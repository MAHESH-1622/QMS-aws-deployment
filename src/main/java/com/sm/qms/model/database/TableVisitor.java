package com.sm.qms.model.database;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Table(value = "visitors")
public class TableVisitor implements Serializable {

    @PrimaryKey
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String address;
    private String registrationDate;
    private List<String> appointmentId;

    public TableVisitor() {
    }

    public void addAppointmentId(String appointmentId) {
        if(this.appointmentId == null){
            this.appointmentId = new ArrayList<>();
        }
        this.appointmentId.add(appointmentId);
    }

    public TableVisitor(String firstName, String lastName, String email, String phone, String password, String address, String registrationDate, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.registrationDate = registrationDate;
        this.id = id;
        this.appointmentId = new ArrayList<>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<String> getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(List<String> appointmentId) {
        this.appointmentId = appointmentId;
    }


}