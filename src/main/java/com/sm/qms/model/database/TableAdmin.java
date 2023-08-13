package com.sm.qms.model.database;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.List;

@Table("admins")
public class TableAdmin implements Serializable {

    @PrimaryKey
    private String id;
    private String firstName;

    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String address;
    @Column("store_id")
    private String storeId;
    private String status;
    private String role;
    private List<String> calendarAccess;

    public TableAdmin(){

    }

    public TableAdmin(String storeId) {
        this.storeId = storeId;
    }

    public TableAdmin(String id, String firstName, String lastName, String email, String phone, String password, String address, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.role = role;
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCalendarAccess(List<String> days) {
        this.calendarAccess = days;
    }


    public List<String> getCalendarAccess() {
        return calendarAccess;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
