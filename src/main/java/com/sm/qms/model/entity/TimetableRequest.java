package com.sm.qms.model.entity;

import com.sm.qms.model.database.TableDay;
import org.springframework.data.cassandra.core.mapping.Frozen;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class TimetableRequest {

    private String storeId;
    private TableDay monday;
    private TableDay tuesday;
    private TableDay wednesday;
    private TableDay thursday;
    private TableDay friday;
    private TableDay saturday;
    private TableDay sunday;

    private LocalTime startTime;
    private LocalTime endTime;
    private int timeSlotGapInMinutes;
    private LocalTime lunchStartTime;
    private LocalTime lunchEndTime;
    private List<DayOfWeek> holidays;


    public List<DayOfWeek> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<DayOfWeek> holidays) {
        this.holidays = holidays;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getTimeSlotGapInMinutes() {
        return timeSlotGapInMinutes;
    }

    public void setTimeSlotGapInMinutes(int timeSlotGapInMinutes) {
        this.timeSlotGapInMinutes = timeSlotGapInMinutes;
    }

    public LocalTime getLunchStartTime() {
        return lunchStartTime;
    }

    public void setLunchStartTime(LocalTime lunchStartTime) {
        this.lunchStartTime = lunchStartTime;
    }

    public LocalTime getLunchEndTime() {
        return lunchEndTime;
    }

    public void setLunchEndTime(LocalTime lunchEndTime) {
        this.lunchEndTime = lunchEndTime;
    }

    public TableDay getMonday() {
        return monday;
    }

    public void setMonday(TableDay monday) {
        this.monday = monday;
    }

    public TableDay getTuesday() {
        return tuesday;
    }

    public void setTuesday(TableDay tuesday) {
        this.tuesday = tuesday;
    }

    public TableDay getWednesday() {
        return wednesday;
    }

    public void setWednesday(TableDay wednesday) {
        this.wednesday = wednesday;
    }

    public TableDay getThursday() {
        return thursday;
    }

    public void setThursday(TableDay thursday) {
        this.thursday = thursday;
    }

    public TableDay getFriday() {
        return friday;
    }

    public void setFriday(TableDay friday) {
        this.friday = friday;
    }

    public TableDay getSaturday() {
        return saturday;
    }

    public void setSaturday(TableDay saturday) {
        this.saturday = saturday;
    }

    public TableDay getSunday() {
        return sunday;
    }

    public void setSunday(TableDay sunday) {
        this.sunday = sunday;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }



}
