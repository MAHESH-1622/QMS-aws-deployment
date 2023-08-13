package com.sm.qms.model.database;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.time.LocalTime;
import java.util.List;

@UserDefinedType
public class TableDay {

    private List<TableTimeSlot> workingHours;


    public List<TableTimeSlot> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<TableTimeSlot> workingHours) {
        this.workingHours = workingHours;
    }



}

