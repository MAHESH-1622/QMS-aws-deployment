package com.sm.qms.controllers.admins;

import com.sm.qms.model.database.*;
import com.sm.qms.model.entity.AppointmentResponse;
import com.sm.qms.model.response.ApiResult;
import com.sm.qms.repository.*;
import com.sm.qms.service.NotificationService;
import com.sm.qms.utilities.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/api/Admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SuperAdminRepository superAdminRepository;
    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AppointmentCancellationRepository cancellationRepository; // Assuming you have a repository for cancellation details




    //TODO - Remove this endpoint after testing...
    @RequestMapping(method = RequestMethod.GET, path = "/appointment/hello", produces = "application/json")
    public ResponseEntity<ApiResult<String>> helloAppointment(@RequestParam(required = false) String name) {
        try {
            //TODO Insert appointments
            return ResponseEntity.ok()
                    .body(new ApiResult<>("true", "Hello Appointment!"));
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }



    //TODO - should the list of appointments contains details??
    @RequestMapping(method = RequestMethod.GET, path = "/appointment/list", produces = "application/json")
    public ResponseEntity<List<TableAppointment>> getAppointments(@RequestParam(required = false) String date) {
        try {
            List<TableAppointment> appointments = new ArrayList<>();
            appointmentRepository.findAll().forEach(appointments::add);
//            if (date != null) {
//                repository.findByDate(date).forEach(appointments::add);
//            } else {
//            }

//            Notification notification = new Notification.Builder()
//                    .setTitle("Test Appointments!")
//                    .setEmail("dileep.gdk@gmail.com")
//                    .setMessage("Getting Booked Appointments!")
//                    .build();
//            notificationService.sendNotification(notification);

//            emailService.sendEmail("filippo.eng@gmail.com", "Test Appointments!");
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/calendarAccess/{adminId}", produces = "application/json")
    public ResponseEntity<ApiResult> getCalendarAccess(@PathVariable String adminId) {
        try {
            Optional<TableAdmin> optionalAdmin = adminRepository.findById(adminId);
            if (optionalAdmin.isPresent()) {
                TableAdmin admin = optionalAdmin.get();
                List<String> calendarAccess = admin.getCalendarAccess();
                return ResponseEntity.ok().body(new ApiResult("true", calendarAccess));
            } else {
                return ResponseEntity.ok()
                        .body(new ApiResult("false", "Admin with id='" + adminId + "' does not exist."));
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult("false", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }



    @RequestMapping(method = RequestMethod.GET, path = "/timetable/{storeId}", produces = "application/json")
    public ResponseEntity<ApiResult> getTimetable(@PathVariable String storeId) {
        try {
            Optional<TableTimetable> optionalTimetable = timetableRepository.findById(storeId);
            if (optionalTimetable.isPresent()) {
                TableTimetable timetable = optionalTimetable.get();
                return ResponseEntity.ok().body(new ApiResult("true", timetable));
            } else {
                return ResponseEntity.ok().body(new ApiResult("false", "Timetable for store with ID '" + storeId + "' does not exist."));
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult("false", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(method = RequestMethod.GET, path = "/appointment/slots", produces = "application/json")
    public ResponseEntity<?> getAvailableSlots(@Param("date") String date /* 2023-06-28 */, @Param("storeId") String storeId) {

        try {
            // Check if the store ID exists in the database
            if (!storeRepository.existsById(storeId)) {
                return new ResponseEntity<>(new ApiResult<>(
                        "false", "Store not found"
                ), HttpStatus.NOT_FOUND);
            }
            // Check for date: not null, correct format, etc., valid range
            // Not accepting past dates, as it's not possible to book an appointment for an expired date
            LocalDate parsedDate = LocalDate.parse(date);
            if (!DateUtils.isValidForAppointment(parsedDate)) {
                return ResponseEntity.ok().body(new ApiResult("false", "Invalid Date selected"));
            }

            // Convert date into day = Friday
            String day = parsedDate.getDayOfWeek().toString();

            // TODO
            // Get working hours time slots by DAY ✅
            // Get all Appointments by date
            // Map free/booked time slots with Appointments
            //

            List<TableTimetable> timetables = timetableRepository.findAllByStoreId(storeId);


            if (timetables.isEmpty()) {
                // No booking because there's no weekly plan
                return new ResponseEntity<>(new ApiResult<>("false", "No weekly plan found"), HttpStatus.NOT_FOUND);
            } else {
                TableTimetable timetable = timetables.get(0);
                TableDay dayPlan = timetable.getDayPlan(day);

                if (dayPlan == null) {
                    return new ResponseEntity<>(new ApiResult<>("false", "No plan found on selected day."), HttpStatus.NOT_FOUND);
                } else {
                    List<TableAppointment> appointments = new ArrayList<>();
                    // Filter appointments by date and storeId
                    appointmentRepository.findByDateAndStoreId(date, storeId).forEach(appointment -> appointments.add((TableAppointment) appointment));

                    Map<TableTimeSlot, TableAppointment> appointmentMap = new LinkedHashMap<>();
                    for (TableAppointment appointment : appointments) {
                        appointmentMap.put(appointment.getTimeSlot(), appointment);
                    }

                    List<TableTimeSlot> availableTimeSlots = new ArrayList<>();
                    if (dayPlan.getWorkingHours().isEmpty()) {
                        return ResponseEntity.ok().body(new ApiResult("false", "No working hours on this day."));
                    }

                    for (TableTimeSlot workingHour : dayPlan.getWorkingHours()) {
                        boolean booked = false;
                        for (TableTimeSlot timeSlot : appointmentMap.keySet()) {
                            if (timeSlot.equals(workingHour)) {
                                booked = true;
                                break;
                            }
                        }
                        if (!booked) {
                            availableTimeSlots.add(workingHour);
                        }
                    }

                    return ResponseEntity.ok().body(availableTimeSlots);
                }
            }

        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResult("false", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET,path = "/appointment/cancellations", produces = "application/json")
    public ResponseEntity<List<TableAppointmentCancellation>>getAppointmentCancellations(){
        try {
            List<TableAppointmentCancellation> cancellations = (List<TableAppointmentCancellation>) cancellationRepository.findAll();
            if (!cancellations.isEmpty()){
                System.out.println("Cancellation history retrieved successfully");
                return new ResponseEntity<>(cancellations,HttpStatus.OK);
            }
            else {
                System.out.println("No cancellation history found");
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/timetable/view", produces = "application/json")
    public ResponseEntity<?> getTimetable() {
        List<TableTimetable> timetables = (List<TableTimetable>) timetableRepository.findAll();
        if (timetables.isEmpty()) {
            return new ResponseEntity<>(
                    new ApiResult<>("false", "Timetable not found!"), HttpStatus.NOT_FOUND
            );
        } else {
            return ResponseEntity.ok(timetables.get(0));
        }
    }


    @RequestMapping(method = RequestMethod.GET, path = "/visitor/list", produces = "application/json")
    public ResponseEntity<List<TableVisitor>> getVisitors() {
        try {
            List<TableVisitor> visitors = new ArrayList<>();
            visitorRepository.findAll().forEach(visitors::add);
            return new ResponseEntity<>(visitors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/visitors", produces = "application/json")
    public ResponseEntity<?> getAppointmentsByStoreIdAndDate(@RequestParam("storeId") String storeId, @RequestParam("date") String date) {
        try {
            Iterable<TableAppointment> appointments = appointmentRepository.findByDateAndStoreId(date, storeId);

            List<AppointmentResponse> appointmentResponses = new ArrayList<>();
            appointments.forEach(appointment -> {
                // Create AppointmentResponse object with necessary details
                AppointmentResponse appointmentResponse = new AppointmentResponse();
                appointmentResponse.setId(appointment.getId());
                appointmentResponse.setVisitorId(appointment.getVisitorId());
             //   appointmentResponse.setStoreId(appointment.getStoreId());
             //   appointmentResponse.setDate(appointment.getDate());
                appointmentResponse.setTimeSlot(appointment.getTimeSlot());
             //   appointmentResponse.setTimestamp(appointment.getTimestamp());
                appointmentResponse.setStatus(appointment.getStatus());

                appointmentResponses.add(appointmentResponse);
            });

            return ResponseEntity.ok().body(appointmentResponses);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
