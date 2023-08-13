package com.sm.qms.model.database;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;

@Table(value = "timetable")
public class TableTimetable {

    @PrimaryKey
    private String storeId;
    @Column
    private TableDay monday;
    @Column
    private TableDay tuesday;
    @Column
    private TableDay wednesday;
    @Column
    private TableDay thursday;
    @Column
    private TableDay friday;
    @Column
    private TableDay saturday;
    @Column
    private TableDay sunday;
    @Column("number_of_slots_available")
    private int numberOfSlotsAvailable;  // number of slots available for each slot


    private long timestamp;






    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }


    public void setMonday(TableDay monday) {
        this.monday = monday;
    }

    public void setTuesday(TableDay tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(TableDay wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(TableDay thursday) {
        this.thursday = thursday;
    }

    public void setFriday(TableDay friday) {
        this.friday = friday;
    }

    public void setSaturday(TableDay saturday) {
        this.saturday = saturday;
    }

    public void setSunday(TableDay sunday) {
        this.sunday = sunday;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStoreId() {
        return storeId;
    }



    public TableDay getMonday() {
        return monday;
    }

    public TableDay getTuesday() {
        return tuesday;
    }

    public TableDay getWednesday() {
        return wednesday;
    }

    public TableDay getThursday() {
        return thursday;
    }

    public TableDay getFriday() {
        return friday;
    }

    public TableDay getSaturday() {
        return saturday;
    }

    public TableDay getSunday() {
        return sunday;
    }


    public long getTimestamp() {
        return timestamp;
    }


    public int getNumberOfSlotsAvailable() {
        return numberOfSlotsAvailable;
    }

    public void setNumberOfSlotsAvailable(int numberOfSlotsAvailable) {
        this.numberOfSlotsAvailable = numberOfSlotsAvailable;
    }


    public TableDay getDayPlan(String day) {
        if ("monday".equalsIgnoreCase(day)) {
            return monday;
        }
        if ("tuesday".equalsIgnoreCase(day)) {
            return tuesday;
        }
        if ("wednesday".equalsIgnoreCase(day)) {
            return wednesday;
        }
        if ("thursday".equalsIgnoreCase(day)) {
            return thursday;
        }
        if ("friday".equalsIgnoreCase(day)) {
            return friday;
        }
        if ("saturday".equalsIgnoreCase(day)) {
            return saturday;
        }
        if ("sunday".equalsIgnoreCase(day)) {
            return sunday;
        }
        return null;
    }

    public void setDayPlan(String day, TableDay dayPlan) {
        if ("monday".equalsIgnoreCase(day)) {
            setMonday(dayPlan);
        } else if ("tuesday".equalsIgnoreCase(day)) {
            setTuesday(dayPlan);
        } else if ("wednesday".equalsIgnoreCase(day)) {
            setWednesday(dayPlan);
        } else if ("thursday".equalsIgnoreCase(day)) {
            setThursday(dayPlan);
        } else if ("friday".equalsIgnoreCase(day)) {
            setFriday(dayPlan);
        } else if ("saturday".equalsIgnoreCase(day)) {
            setSaturday(dayPlan);
        } else if ("sunday".equalsIgnoreCase(day)) {
            setSunday(dayPlan);
        }
    }

    public void setTimestamp(Instant now) {
    }
}
