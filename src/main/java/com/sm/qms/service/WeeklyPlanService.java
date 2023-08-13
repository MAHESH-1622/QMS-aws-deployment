package com.sm.qms.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WeeklyPlanService {

    public final WeeklyPlan weeklyPlan;

    public WeeklyPlanService(WeeklyPlan weeklyPlan) {
        this.weeklyPlan = weeklyPlan;
    }

    public static class DayTimeSlot {

//        public final LocalTime start;
        public final String start;
        public final String end;


        public DayTimeSlot(String start, String end) {
            this.start = start;
            this.end = end;
        }
    }

    public static class DayPlan {

        public final List<DayTimeSlot> workingHours = new ArrayList<>();

        public DayPlan(List<DayTimeSlot> workingHours) {
            this.workingHours.addAll(workingHours);
        }
    }
    public static class WeeklyPlan {

        private DayPlan monday;
        private DayPlan tuesday;
        private DayPlan wednesday;
        private DayPlan thursday;
        private DayPlan friday;
        private DayPlan saturday;
        private DayPlan sunday;

        public DayPlan getMonday() {
            return monday;
        }

        public void setMonday(DayPlan monday) {
            this.monday = monday;
        }

        public DayPlan getTuesday() {
            return tuesday;
        }

        public void setTuesday(DayPlan tuesday) {
            this.tuesday = tuesday;
        }

        public DayPlan getWednesday() {
            return wednesday;
        }

        public void setWednesday(DayPlan wednesday) {
            this.wednesday = wednesday;
        }

        public DayPlan getThursday() {
            return thursday;
        }

        public void setThursday(DayPlan thursday) {
            this.thursday = thursday;
        }

        public DayPlan getFriday() {
            return friday;
        }

        public void setFriday(DayPlan friday) {
            this.friday = friday;
        }

        public DayPlan getSaturday() {
            return saturday;
        }

        public void setSaturday(DayPlan saturday) {
            this.saturday = saturday;
        }

        public DayPlan getSunday() {
            return sunday;
        }

        public void setSunday(DayPlan sunday) {
            this.sunday = sunday;
        }
    }
}
