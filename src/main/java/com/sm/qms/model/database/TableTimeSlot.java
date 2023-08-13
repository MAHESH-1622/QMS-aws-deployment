package com.sm.qms.model.database;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.time.LocalTime;
import java.util.Objects;

@UserDefinedType
public class TableTimeSlot {

    @JsonFormat(pattern = "HH:mm")
    public LocalTime start;
    @JsonFormat(pattern = "HH:mm")
    public LocalTime end;





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



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TableTimeSlot that = (TableTimeSlot) o;
        return start.equals(that.start) && end.equals(that.end);
    }

    @Override
    public String toString() {
        return start.toString() + " - " + end.toString();
    }


}
