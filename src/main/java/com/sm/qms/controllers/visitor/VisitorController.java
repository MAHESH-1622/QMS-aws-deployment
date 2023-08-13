package com.sm.qms.controllers.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm.qms.model.database.*;
import com.sm.qms.model.entity.AppointmentRequest;
import com.sm.qms.model.entity.Notification;
import com.sm.qms.model.entity.UpdateAppointmentRequest;
import com.sm.qms.model.response.ApiResult;
import com.sm.qms.model.entity.VisitorRequest;
import com.sm.qms.repository.*;
import com.sm.qms.service.NotificationService;
import com.sm.qms.utilities.DateUtils;
import com.sm.qms.utilities.DateUtils2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class VisitorController {

    private static final String DATE_FORMAT = "ddMMyyyy";

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private NotificationService notificationService;
    private CustomDateFormatter customDateFormatter;
    @Autowired
    private AppointmentCancellationRepository cancellationRepository; // Assuming you have a repository for cancellation details




    // Define a class-level ArrayList to store the visitors
    private List<TableVisitor> visitors = new ArrayList<>();
    @RequestMapping(method = RequestMethod.POST, path = "/api/visitor/create", produces = "application/json")
    public ResponseEntity<ApiResult> createVisitor(@RequestBody VisitorRequest request) {
        try {
            // Check if the visitor table is empty
            boolean isTableEmpty = visitorRepository.count() == 0;

            // Get the current date
            Date currentDate = new Date();
            String currentDateStr = customDateFormatter.format(currentDate);


            // Check if the visitor with the same email or phone already exists
            Optional<TableVisitor> optVisitorByEmail = visitorRepository.findByEmail(request.getEmail());
            Optional<TableVisitor> optVisitorByPhone = visitorRepository.findByPhone(request.getPhone());

            if (!isTableEmpty && (optVisitorByEmail.isPresent() || optVisitorByPhone.isPresent())) {
                // Visitor with the same email or phone already exists
                if (optVisitorByEmail.isPresent()) {
                    return ResponseEntity.ok().body(new ApiResult("false", "Visitor with email='" + request.getEmail() + "' already exists"));
                } else {
                    return ResponseEntity.ok().body(new ApiResult("false", "Visitor with phone='" + request.getPhone() + "' already exists"));
                }
            } else {
                // Get the visitor count for the current date
                int visitorCount = visitorRepository.countByRegistrationDate(DateUtils2.getTimestampForDate(currentDateStr));

                // Increment the visitor count for the new visitor
                visitorCount++;

                // Generate the visitor ID
                String visitorId = generateVisitorId(currentDateStr, visitorCount);

                // Create a new visitor
                TableVisitor newVisitor = new TableVisitor(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPhone(),
                        request.getPassword(),
                        request.getAddress(),
                       currentDateStr,
                        visitorId
                );

                // Save the new visitor
                visitorRepository.save(newVisitor);

                Notification notification = Notification.create("Welcome to QMS", "Your visitor ID: " + visitorId);
                notificationService.sendNotification(notification);

                return ResponseEntity.ok().body(new ApiResult("true", "Visitor '" + visitorId + "' registered"));
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult("false", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    private int visitorCount = 0; // Add a visitor count variable

    private synchronized int getNextVisitorCount() {
        return ++visitorCount;
    }

    private String generateVisitorId(String currentDate, int visitorCount) {
        // Generate the visitor count
        int count = getNextVisitorCount();
        String visitorCountStr = String.format("%03d", count); // Use three digits for the count
        return currentDate + visitorCountStr;
    }



    @RequestMapping(method = RequestMethod.POST, path = "/api/appointment/create", produces = "application/json")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        try {
            String date = request.getDate(); // check validity
            LocalDate parsedDate = LocalDate.parse(date);

            if (DateUtils.isValidForAppointment(parsedDate)) {
                String visitorId = request.getVisitorId(); // check valid visitorID
                String storeId = request.getStoreId(); // check valid storeID

                Optional<TableVisitor> optVisitor = visitorRepository.findById(visitorId);
                if (optVisitor.isPresent()) {
                    Optional<TableStore> store = storeRepository.findById(storeId);
                    if (store.isPresent()) {
                        // Check if timetable exists for the selected store
                        Optional<TableTimetable> timetable = timetableRepository.findByStoreId(storeId);
                        if (timetable.isPresent()) {
                            List<TableAppointment> appointments = new ArrayList<>();
                            appointmentRepository.findByDate(date).forEach(appointments::add);

                            // TODO: Make sure not only by date, but also (visitorId, storeId, etc.)
                            Map<TableTimeSlot, TableAppointment> appointmentMap = new LinkedHashMap<>();
                            for (TableAppointment appointment : appointments) {
                                //check if the appointment has the same date,visitorId, and storeId
                                if (appointment.getDate().equals(date)
                                        && appointment.getVisitorId().equals(visitorId)
                                        && appointment.getStoreId().equals(storeId)) {
                                    appointmentMap.put(appointment.getTimeSlot(), appointment);
                                }
                            }

                            List<TableTimeSlot> availableTimeSlots = new ArrayList<>();

                            TableTimetable selectedTimetable = timetable.get();
                            String day = parsedDate.getDayOfWeek().toString();
                            TableDay dayPlan = selectedTimetable.getDayPlan(day);

                            if (dayPlan == null) {
                                return new ResponseEntity<>(new ApiResult<>(
                                        "false", "No plan found on the selected day."
                                ), HttpStatus.NOT_FOUND);
                            } else {
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

                                LocalTime startTime = request.getStart(); // check validity
                                LocalTime endTime = request.getEnd(); // check validity

                                // Check if the current date and time are after the appointment start time
                                LocalDateTime currentDateTime = LocalDateTime.now();
                                LocalDateTime appointmentStartDateTime = parsedDate.atTime(startTime);
                                if (currentDateTime.isAfter(appointmentStartDateTime)) {
                                    return new ResponseEntity<>(new ApiResult<>(
                                            "false", "Cannot book an appointment for a past time slot."
                                    ), HttpStatus.BAD_REQUEST);
                                }

                                TableTimeSlot timeSlot = new TableTimeSlot();
                                timeSlot.setStart(startTime);
                                timeSlot.setEnd(endTime);

                                if (availableTimeSlots.contains(timeSlot)) {
                                    // Check if the visitor has already booked an appointment for the same time slot
                                    boolean alreadyBooked = appointmentMap.containsKey(timeSlot);
                                    if (alreadyBooked) {
                                        return new ResponseEntity<>(new ApiResult<>(
                                                "false", "The selected time slot is not available for booking."
                                        ), HttpStatus.FORBIDDEN);
                                    }
                                    // create and book the appointment here
                                    UUID uuid = UUID.randomUUID();
                                    Long dateMillis = DateUtils.getTimestamp();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                                    String formattedTimestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateMillis), ZoneId.systemDefault()).format(formatter);

                                    TableAppointment appointment = new TableAppointment();
                                    appointment.setId(uuid);
                                    appointment.setVisitorId(visitorId);
                                    appointment.setStoreId(storeId);
                                    appointment.setTimestamp(formattedTimestamp);
                                    appointment.setDate(date);
                                    appointment.setTimeSlot(timeSlot);
                                    appointment.setStatus("CONFIRMED");

                                    appointmentRepository.save(appointment);

                                    TableVisitor visitor = optVisitor.get();
                                    visitor.addAppointmentId(uuid.toString());
                                    visitorRepository.save(visitor);

                                    Notification notification = Notification.create(
                                            "Appointment Created", "Appointment confirmed: Ref -> " + uuid
                                    );
                                    String visitorEmail = optVisitor.get().getEmail();

                                    notificationService.sendNotification(notification);

                                    return ResponseEntity.ok()
                                            .body(new ApiResult<>("true", "Appointment confirmed: Ref -> " + uuid));
                                } else {
                                    // error cannot book appointment - slot taken
                                    return new ResponseEntity<>(new ApiResult<>(
                                            "false", "Cannot book the appointment, please choose another time slot."
                                    ), HttpStatus.FORBIDDEN);
                                }
                            }
                        } else {
                            return new ResponseEntity<>(new ApiResult<>(
                                    "false", "No weekly plan found for the selected store."
                            ), HttpStatus.NOT_FOUND);
                        }
                    } else {
                        return new ResponseEntity<>(new ApiResult(
                                "false", "Invalid Store ID request"
                        ), HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>(new ApiResult(
                            "false", "Invalid Visitor ID request"
                    ), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new ApiResult<>(
                        "false", "Invalid Date for the appointment"
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.PUT, path = "/api/appointment/{id}/cancel", produces = "application/json")
    public ResponseEntity<ApiResult<String>> cancelAppointment(
            @PathVariable("id") UUID id, @RequestBody String cancellationReason) {
        try {
            System.out.println("Cancel appointment request received. ID: " + id);

            Optional<TableAppointment> optAppointment = appointmentRepository.findById(id);
            if (optAppointment.isPresent()) {
                TableAppointment appointment = optAppointment.get();

                // Check if the appointment is already canceled
                if (appointment.getStatus().equals("CANCELLED")) {
                    System.out.println("Appointment is already canceled");
                    return new ResponseEntity<>(new ApiResult<>("false", "Appointment is already canceled"), HttpStatus.BAD_REQUEST);
                }

                // Update the appointment status to "CANCELLED"
                appointment.setStatus("CANCELLED");
                appointmentRepository.save(appointment);
                System.out.println("Appointment status updated to CANCELLED");

                // Get the storeId from the appointment
                String storeId = appointment.getStoreId();

                // Create a new cancellation entry with the storeId
                UUID cancellationId = UUID.randomUUID();
                TableAppointmentCancellation cancellation = new TableAppointmentCancellation(id, storeId, cancellationReason);
                // Update storeID if it is null in the cancellation entry
                if (storeId == null) {
                    TableStore store = storeRepository.findById(appointment.getStoreId()).orElse(null);
                    if (store != null) {
                        cancellation.setStoreId(store.getStoreId());
                    }
                }

                cancellationRepository.save(cancellation);
                System.out.println("Cancellation entry created");

                // Update the available time slots for the canceled appointment
                LocalDate appointmentDate = LocalDate.parse(appointment.getDate());
                String dayOfWeek = appointmentDate.getDayOfWeek().toString();
                TableTimetable timetable = timetableRepository.findById(storeId).orElse(null);

                if (timetable != null) {
                    TableDay dayPlan = timetable.getDayPlan(dayOfWeek);

                    if (dayPlan != null) {
                        List<TableTimeSlot> workingHours = dayPlan.getWorkingHours();
                        List<TableAppointment> appointments = StreamSupport.stream(appointmentRepository.findByDate(appointment.getDate()).spliterator(), false)
                                .collect(Collectors.toList());

                        List<TableTimeSlot> bookedTimeSlots = appointments.stream()
                                .filter(appt -> appt.getStatus().equals("CONFIRMED"))
                                .map(TableAppointment::getTimeSlot)
                                .collect(Collectors.toList());

                        // Remove the canceled appointment's time slot from the booked time slots
                        bookedTimeSlots.remove(appointment.getTimeSlot());

                        List<TableTimeSlot> availableTimeSlots = workingHours.stream()
                                .filter(slot -> !bookedTimeSlots.contains(slot))
                                .collect(Collectors.toList());

                        // Update the day plan's working hours with the updated available time slots
                        dayPlan.setWorkingHours(availableTimeSlots);
                        timetableRepository.save(timetable);
                        System.out.println("Updated available time slots for the day plan");

                        ObjectMapper mapper = new ObjectMapper();
                        String cancellationJson = mapper.writeValueAsString(cancellation);

                        return ResponseEntity.ok()
                                .body(new ApiResult<>("true", "Appointment canceled: Ref -> " + id + ", cancellation -> " + cancellationJson));
                    } else {
                        System.out.println("No day plan found for the appointment's day");
                        return new ResponseEntity<>(new ApiResult<>("false", "No day plan found for the appointment's day"), HttpStatus.NOT_FOUND);
                    }
                } else {
                    System.out.println("Timetable not found for the store");
                    return new ResponseEntity<>(new ApiResult<>("false", "Timetable not found for the store"), HttpStatus.NOT_FOUND);
                }
            } else {
                System.out.println("Appointment not found");
                return new ResponseEntity<>(new ApiResult<>("false", "Appointment not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(method = RequestMethod.GET, path = "/api/appointment/slots", produces = "application/json")
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


    //TODO - get Appointment details by ID

    @RequestMapping(method = RequestMethod.PUT, path = "/api/appointment/update", produces = "application/json")
    public ResponseEntity<?> updateAppointment(
            @RequestBody UpdateAppointmentRequest request
    ) {
        try {
            String id = request.getId();
            UUID uuid = UUID.fromString(id);
            Optional<TableAppointment> optAppointment = appointmentRepository.findById(uuid);
            if (optAppointment.isPresent()) {

                String date = request.getDate(); //check validity

                LocalDate parsedDate = LocalDate.parse(date);

                if (DateUtils.isValidForAppointment(parsedDate)) {

                    List<TableTimetable> timetables = new ArrayList<>();
                    timetableRepository.findAll().forEach(timetables::add);

                    if (timetables.isEmpty()) {
                        //no booking because there's no weekly plan
                        return new ResponseEntity<>(new ApiResult<>(
                                "false", "No weekly plan found"
                        ), HttpStatus.NOT_FOUND);
                    } else {

                        String day = parsedDate.getDayOfWeek().toString();

                        TableTimetable timetable = timetables.get(0);
                        TableDay dayPlan = timetable.getDayPlan(day);

                        if (dayPlan == null) {
                            return new ResponseEntity<>(new ApiResult<>(
                                    "false", "No plan found in selected day."
                            ), HttpStatus.NOT_FOUND);
                        } else {
                            List<TableAppointment> appointments = new ArrayList<>();
                            appointmentRepository.findByDate(date).forEach(appointments::add);

                            //TODO make sure not only by date, but also (visitorId, storeId, etc.)
                            Map<TableTimeSlot, TableAppointment> appointmentMap = new LinkedHashMap<>();
                            for (TableAppointment appointment : appointments) {
                                appointmentMap.put(appointment.getTimeSlot(), appointment);
                            }

                            List<TableTimeSlot> availableTimeSlots = new ArrayList<>();

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

                            LocalTime startTime = request.getStart(); //check validity
                            LocalTime endTime = request.getEnd(); //check validity

                            TableTimeSlot timeSlot = new TableTimeSlot();
                            timeSlot.setStart(startTime);
                            timeSlot.setEnd(endTime);

                            if (availableTimeSlots.contains(timeSlot)) {
                                TableAppointment savedAppointment = optAppointment.get();

                                savedAppointment.setDate(date);
                                savedAppointment.setTimeSlot(timeSlot);

                                if (request.getStoreId() != null) {
                                    savedAppointment.setStoreId(request.getStoreId());
                                }

                                appointmentRepository.save(savedAppointment);

                                Notification notification = Notification.create(
                                        "Appointment Rescheduled", "Appointment: " + id + ", changed to " + date + ", " + timeSlot
                                );
                                notificationService.sendNotification(notification);

                                return new ResponseEntity<>(new ApiResult<>(
                                        "true", "Appointment updated."
                                ), HttpStatus.OK);
                            } else {
                                //error cannot book appointment - slot taken
                                return new ResponseEntity<>(new ApiResult<>(
                                        "false", "Cannot book appointment, please choose another time slot."
                                ), HttpStatus.FORBIDDEN);
                            }
                        }
                    }
                } else {
                    return new ResponseEntity<>(new ApiResult<>(
                            "false", "Invalid Date for appointment"
                    ), HttpStatus.BAD_REQUEST);
                }

            } else {
                return new ResponseEntity<>(
                        new ApiResult<>("false", "Appointment not found!")
                        , HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }



}
