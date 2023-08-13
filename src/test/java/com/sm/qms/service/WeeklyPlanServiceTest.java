package com.sm.qms.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

class WeeklyPlanServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    public void test_weeklyPlanService() {

        //Booking constraints
        /*
        1. Max of 3 slots for a day
         */

        WeeklyPlanService.WeeklyPlan weeklyPlan = new WeeklyPlanService.WeeklyPlan();

        List<WeeklyPlanService.DayTimeSlot> mondayHours = getDayWorkingHours();
        List<WeeklyPlanService.DayTimeSlot> tuesdayHours = getDayWorkingHours();
        List<WeeklyPlanService.DayTimeSlot> wednesdayHours = getDayWorkingHours();
        List<WeeklyPlanService.DayTimeSlot> thursdayHours = getDayWorkingHours();
        List<WeeklyPlanService.DayTimeSlot> fridayHours = getDayWorkingHours();

        weeklyPlan.setMonday(new WeeklyPlanService.DayPlan(mondayHours));
        weeklyPlan.setTuesday(new WeeklyPlanService.DayPlan(tuesdayHours));
        weeklyPlan.setWednesday(new WeeklyPlanService.DayPlan(wednesdayHours));
        weeklyPlan.setThursday(new WeeklyPlanService.DayPlan(thursdayHours));
        weeklyPlan.setFriday(new WeeklyPlanService.DayPlan(fridayHours));

//        WeeklyPlanService wps = new WeeklyPlanService(weeklyPlan);
//        wps.set

        System.out.println("Test complete!");
    }

    private static List<WeeklyPlanService.DayTimeSlot> getDayWorkingHours() {

        /*

        *** Max of 3 slots for a day ***

        Pros
        * Only single appointment to manage (admin can approve once)

        Cons
        * Very problematic when comparing fixed appointment entity with the variable weekly plan
        * Weekly-plan can affect future un-forseen appointments (holiday, unplanned closures, etc.)

        [A1][start][end]
        [A2][start][end]
        [A3][start][end]

        [A][start ------ end] => consecutive - multiple slots

        Appointment {
        start = "12:00AM"
//        end = "1:00PM"

        end = "4:00PM"
        }
         */

        return Arrays.asList(
                //before lunch (4 slots)
                new WeeklyPlanService.DayTimeSlot("9:00AM", "10:00AM"),
                new WeeklyPlanService.DayTimeSlot("10:00AM", "11:00AM"),
                new WeeklyPlanService.DayTimeSlot("11:00AM", "12:00AM"),
                new WeeklyPlanService.DayTimeSlot("12:00AM", "1:00PM"),

                //Lunch break ("1:00PM", "2:00PM")

                //after lunch (4 slots)
                new WeeklyPlanService.DayTimeSlot("2:00PM", "3:00PM"),
                new WeeklyPlanService.DayTimeSlot("3:00PM", "4:00PM"),
                new WeeklyPlanService.DayTimeSlot("4:00PM", "5:00PM"),
                new WeeklyPlanService.DayTimeSlot("5:00PM", "6:00PM")
        );
    }

    @AfterEach
    void tearDown() {
    }
}
