package com.sm.qms.controllers.admins;

import com.sm.qms.model.database.*;
import com.sm.qms.model.entity.Notification;
import com.sm.qms.model.entity.TimetableRequest;
import com.sm.qms.model.response.ApiResult;
import com.sm.qms.repository.AdminRepository;
import com.sm.qms.repository.StoreRepository;
import com.sm.qms.repository.SuperAdminRepository;
import com.sm.qms.repository.TimetableRepository;
import com.sm.qms.service.NotificationService;
import com.sm.qms.utilities.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;


import java.util.*;
import java.time.DayOfWeek;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/superAdmin")
public class SuperAdminController {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SuperAdminRepository superAdminRepository;
    @Autowired
    private TimetableRepository timetableRepository;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private StoreRepository storeRepository;

    @RequestMapping(method = RequestMethod.POST, path = "/create", produces = "application/json")
    public ResponseEntity<ApiResult> createSuperAdmin(@RequestBody TableSuperAdmin request) {
        try {
            String superAdminId = request.getId();
            if (superAdminRepository.existsById(superAdminId)) {
                return ResponseEntity.ok().body(new ApiResult("false", "Super admin with ID '" + superAdminId + "' already exists."));
            } else {
                superAdminRepository.save(request);
                return ResponseEntity.ok().body(new ApiResult("true", "New super admin created: " + superAdminId));
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult("false", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(method = RequestMethod.POST, path = "/admins/create", produces = "application/json")
    public ResponseEntity<ApiResult> createAdmin(@RequestBody TableAdmin request) {
        try {
            // Check if email is unique
            Optional<TableAdmin> optAdminByEmail = adminRepository.findByEmail(request.getEmail());
            if (optAdminByEmail.isPresent()) {
                return ResponseEntity.ok()
                        .body(new ApiResult("false", "Email '" + request.getEmail() + "' already exists."));
            }

            // Check if phone number is unique
            Optional<TableAdmin> optAdminByPhone = adminRepository.findByPhone(request.getPhone());
            if (optAdminByPhone.isPresent()) {
                return ResponseEntity.ok()
                        .body(new ApiResult("false", "Phone number '" + request.getPhone() + "' already exists."));
            }

            // Generate unique ID with a random suffix
            String adminId = generateUniqueAdminId();

            // Set the generated ID and status, then save the admin
            request.setId(adminId);
            request.setStatus("In_Active"); // Set status to "Active" for the new admin
            adminRepository.save(request);

            return ResponseEntity.ok()
                    .body(new ApiResult("true", "New Admin created: " + adminId));
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult("false", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    private String generateUniqueAdminId() {
        String prefix = "Ad";
        int suffixLength = 5; // Adjust the length of the random suffix as per your requirement
        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder(prefix);
        for (int i = 0; i < suffixLength; i++) {
            int randomDigit = random.nextInt(10);
            stringBuilder.append(randomDigit);
        }

        String adminId = stringBuilder.toString();

        // Ensure the ID is unique
        while (adminRepository.existsById(adminId)) {
            // Regenerate the random suffix
            stringBuilder = new StringBuilder(prefix);
            for (int i = 0; i < suffixLength; i++) {
                int randomDigit = random.nextInt(10);
                stringBuilder.append(randomDigit);
            }

            adminId = stringBuilder.toString();
        }

        return adminId;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/stores/create", produces = "application/json")
    public ResponseEntity<ApiResult> createStore(@RequestBody TableStore request) {
        try {
            // Generate a new storeId like "StoreId" + random 5-digit number
            String storeId = "StorId" + generateRandomNumber(10000, 99999);

            // Check if the generated storeId already exists in the storeRepository
            if (storeRepository.findById(storeId).isPresent()) {
                return ResponseEntity.ok().body(new ApiResult("false", "Store with storeId='" + storeId + "' already exists."));
            } else {
                // Check if the adminIds exist in TableAdmin
                List<String> adminIds = request.getAdminIds();
                for (String adminId : adminIds) {
                    Optional<TableAdmin> optAdmin = adminRepository.findById(adminId);
                    if (!optAdmin.isPresent()) {
                        return ResponseEntity.ok().body(new ApiResult("false", "Admin with id='" + adminId + "' not found."));
                    }
                }

                // Check the number of counters for the store
                int numberOfCounters = request.getNumberOfCounters();
                if (numberOfCounters <= 0 || numberOfCounters > 9) {
                    return ResponseEntity.ok().body(new ApiResult("false", "Invalid number of counters. Number of counters should be between 1 and 9."));
                }

                // Set the generated storeId to the request
                request.setStoreId(storeId);

                storeRepository.save(request);

                // Update the storeId field in TableAdmin for the corresponding admins
                for (String adminId : adminIds) {
                    TableAdmin admin = adminRepository.findById(adminId).orElse(null);
                    if (admin != null) {
                        admin.setStoreId(storeId);
                        admin.setStatus("active");
                        adminRepository.save(admin);
                    }
                }

                Notification notification = Notification.create("New Store Created", "Store ID: " + storeId);
                notificationService.sendNotification(notification);
                return ResponseEntity.ok().body(new ApiResult("true", "Store created: '" + storeId));
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult("false", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // Helper method to generate a random number between min and max (inclusive)
    private int generateRandomNumber(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    @RequestMapping(method = RequestMethod.GET, path = "stores/list", produces = "application/json")
    public ResponseEntity<List<TableStore>> getStores() {
        try {
            List<TableStore> visitors = new ArrayList<>();
            storeRepository.findAll().forEach(visitors :: add);
            return new ResponseEntity<>(visitors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/stores/{storeId}", produces = "application/json")
    public ResponseEntity<ApiResult> deleteStore(@PathVariable String storeId) {
        try {
            if (storeRepository.existsById(storeId)) {
                // Remove storeId from TableAdmin
                List<TableAdmin> admins = adminRepository.findByStoreId(storeId);
                for (TableAdmin admin : admins) {
                    admin.setStoreId(null);
                    adminRepository.save(admin);
                }

                // Delete the store
                storeRepository.deleteById(storeId);

                return ResponseEntity.ok()
                        .body(new ApiResult("true", "Store deleted: " + storeId));
            } else {
                return ResponseEntity.ok()
                        .body(new ApiResult("false", "Store with id='" + storeId + "' does not exist."));
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult(
                    "false", ex.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(method = RequestMethod.GET, path = "/admins/list", produces = "application/json")
    public ResponseEntity<ApiResult> getAdmins() {
        try {
            Iterable<TableAdmin> admins = adminRepository.findAll();
            List<TableAdmin> adminList = new ArrayList<>();
            admins.forEach(adminList :: add);
            return ResponseEntity.ok().body(new ApiResult("true", adminList));
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult("false", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(method = RequestMethod.GET, path = "/store/admins/list", produces = "application/json")
    public ResponseEntity<ApiResult> getAdminsByStoreId(@RequestParam String storeId) {
        try {
            // Retrieve admins by storeId from the repository
            List<TableAdmin> adminList = adminRepository.findByStoreId(storeId);

            if (adminList.isEmpty()) {
                // Return an error response if no admins are found for the given storeId
                return ResponseEntity.ok().body(new ApiResult("false", "No admins found for the store with storeId='" + storeId + "'."));
            }

            return ResponseEntity.ok().body(new ApiResult("true", adminList));
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult("false", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/add/admins", produces = "application/json")
    public ResponseEntity<ApiResult> assignAdminToStore(@RequestParam("storeId") String storeId, @RequestParam("adminId") String adminId) {
        try {
            Optional<TableStore> optStore = storeRepository.findById(storeId);
            Optional<TableAdmin> optAdmin = adminRepository.findById(adminId);

            if (!optStore.isPresent()) {
                return ResponseEntity.ok()
                        .body(new ApiResult("false", "Store with id='" + storeId + "' does not exist."));
            }

            if (!optAdmin.isPresent()) {
                return ResponseEntity.ok()
                        .body(new ApiResult("false", "Admin with id='" + adminId + "' does not exist."));
            }

            TableStore store = optStore.get();
            TableAdmin admin = optAdmin.get();

            if (admin.getStoreId() != null) {
                return ResponseEntity.ok()
                        .body(new ApiResult("false", "Admin with id='" + adminId + "' is already added to a store."));
            }

            // Add the condition to check numberOfCounters
            int numberOfCounters = store.getNumberOfCounters();
            if (numberOfCounters <= 0) {
                return ResponseEntity.ok()
                        .body(new ApiResult("false", "Cannot add admin to the store. The store has no counters."));
            }

            // Decrement numberOfCounters and update the store
            store.setNumberOfCounters(numberOfCounters - 1);
            storeRepository.save(store);

            // Update admin with storeId
            admin.setStoreId(storeId);
            admin.setStatus("Active"); // Set the status to "Active"
            adminRepository.save(admin);

            // Update TableStore's adminIds field
            List<String> existingAdminIds = store.getAdminIds();
            if (existingAdminIds == null) {
                store.setAdminIds(Collections.singletonList(adminId));
            } else {
                existingAdminIds.add(adminId);
                store.setAdminIds(existingAdminIds);
            }
            storeRepository.save(store);

            return ResponseEntity.ok()
                    .body(new ApiResult("true", "Admin with id='" + adminId + "' added to store with id='" + storeId + "'."));
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult(
                    "false", ex.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(method = RequestMethod.DELETE, path = "/admins/{adminId}", produces = "application/json")
    public ResponseEntity<ApiResult> deleteAdmin(@PathVariable String adminId) {
        try {
            TableAdmin admin = adminRepository.findById(adminId).orElse(null);
            if (admin != null) {
                // Update the status field to "INACTIVE"
                admin.setStatus("DELETE");
                adminRepository.save(admin);

                // Remove adminId from TableStore
                TableStore tableStore = storeRepository.findByAdminIdsContains(adminId);
                if (tableStore != null) {
                    // Remove the given adminId from the adminIds list
                    List<String> adminIds = tableStore.getAdminIds();
                    adminIds = adminIds.stream()
                            .filter(id -> !id.equals(adminId))
                            .collect(Collectors.toList());

                    // Update the adminIds field in TableStore
                    tableStore.setAdminIds(adminIds);

                    // Increase the numberOfCounters by 1
                    tableStore.setNumberOfCounters(tableStore.getNumberOfCounters() + 1);

                    storeRepository.save(tableStore);
                }

                // Clear the storeId field in TableAdmin
                admin.setStoreId(null);
                adminRepository.save(admin);

                return ResponseEntity.ok()
                        .body(new ApiResult("true", "Admin Delete with: " + adminId));
            } else {
                return ResponseEntity.ok()
                        .body(new ApiResult("false", "Admin with id='" + adminId + "' does not exist."));
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult(
                    "false", ex.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(method = RequestMethod.PUT, path = "/admins/calendarAccess", produces = "application/json")
    public ResponseEntity<ApiResult> grantCalendarAccess(@RequestParam("adminId") String adminId, @RequestParam("days") List<String> days) {
        try {
            Optional<TableAdmin> optionalAdmin = adminRepository.findById(adminId);
            if (optionalAdmin.isPresent()) {
                TableAdmin admin = optionalAdmin.get();
                admin.setCalendarAccess(days);
                adminRepository.save(admin);
                return ResponseEntity.ok()
                        .body(new ApiResult("true", "Days Calendar access granted for admin: " + adminId));
            } else {
                return ResponseEntity.ok()
                        .body(new ApiResult("false", "Admin with id='" + adminId + "' does not exist."));
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult("false", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(method = RequestMethod.POST, path = "/timetable/save", produces = "application/json")
    public ResponseEntity<?> createTimetable(@RequestBody TimetableRequest request) {
        try {
            // Get user inputs
            String storeIdParam = request.getStoreId();
            String storeId = storeIdParam == null ? "all" : storeIdParam;

            LocalTime startTime = request.getStartTime();
            LocalTime endTime = request.getEndTime();
            int timeSlotGapInMinutes = request.getTimeSlotGapInMinutes();
            LocalTime lunchStartTime = request.getLunchStartTime();
            LocalTime lunchEndTime = request.getLunchEndTime();

            // Generate time slots based on user input
            List<TableTimeSlot> timeSlots = generateTimeSlots(startTime, endTime, timeSlotGapInMinutes, lunchStartTime, lunchEndTime);

            // Fetch the weekly plan
            List<TableTimetable> weeklyPlans = new ArrayList<>();
            timetableRepository.findAll().forEach(weeklyPlans::add);

            TableTimetable timetable;
            if (weeklyPlans.isEmpty()) {
                // Create a new weekly plan if not present
                timetable = new TableTimetable();
            } else {
                // Update the existing weekly plan
                timetable = weeklyPlans.get(0);
            }

            // Apply the generated time slots to all days of the week
            TableDay day = new TableDay();
            day.setWorkingHours(timeSlots);

            timetable.setStoreId(storeId);
            timetable.setMonday(day);
            timetable.setTuesday(day);
            timetable.setWednesday(day);
            timetable.setThursday(day);
            timetable.setFriday(day);
            timetable.setSaturday(day);
            timetable.setSunday(day);
            timetable.setTimestamp(DateUtils.getTimestamp());

            // Count the number of admins for the given storeId
            TableStore store = storeRepository.findById(storeId).orElse(null);
            int numberOfAdmins = store != null ? store.getAdminIds().size() : 0;
            timetable.setNumberOfSlotsAvailable(numberOfAdmins);

            timetableRepository.save(timetable);

            Notification notification = Notification.create(
                    "Weekly Plan Saved", "More visualization to the timetable will be added..."
            );
            notificationService.sendNotification(notification);

            return ResponseEntity.ok()
                    .body(new ApiResult<>("true", "Weekly timetable saved!"));
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult<>(
                    "false", ex.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }
    }

    private List<TableTimeSlot> generateTimeSlots(LocalTime startTime, LocalTime endTime, int timeSlotGapInMinutes, LocalTime lunchStartTime, LocalTime lunchEndTime) {
        List<TableTimeSlot> timeSlots = new ArrayList<>();
        LocalTime current = startTime;

        while (current.isBefore(endTime)) {
            if (current.isBefore(lunchStartTime)) {
                // Add time slots before lunch
                LocalTime nextSlotEnd = current.plusMinutes(timeSlotGapInMinutes);
                if (nextSlotEnd.isAfter(lunchStartTime) || nextSlotEnd.equals(lunchStartTime)) {
                    nextSlotEnd = lunchStartTime;
                }
                TableTimeSlot slot = new TableTimeSlot();
                slot.setStart(current);
                slot.setEnd(nextSlotEnd);
                timeSlots.add(slot);
                current = nextSlotEnd;
            } else if (current.isAfter(lunchEndTime) || current.equals(lunchEndTime)) {
                // Add time slots after lunch
                LocalTime nextSlotEnd = current.plusMinutes(timeSlotGapInMinutes);
                if (nextSlotEnd.isAfter(endTime) || nextSlotEnd.equals(endTime)) {
                    nextSlotEnd = endTime;
                }
                TableTimeSlot slot = new TableTimeSlot();
                slot.setStart(current);
                slot.setEnd(nextSlotEnd);
                timeSlots.add(slot);
                current = nextSlotEnd;
            } else {
                // Skip lunchtime slots
                current = lunchEndTime;
            }
        }

        return timeSlots;
    }



    @RequestMapping(method = RequestMethod.POST, path = "/timetable/create", produces = "application/json")
    public ResponseEntity<ApiResult> createTimetable(@RequestBody TableTimetable request) {
        try {
            String storeId = request.getStoreId();
            if (timetableRepository.existsById(storeId)) {
                return ResponseEntity.ok().body(new ApiResult("false", "Timetable for store with ID '" + storeId + "' already exists, so first delete time table from database"));
            } else {
                timetableRepository.save(request);
                return ResponseEntity.ok().body(new ApiResult("true", "New timetable created for store: " + storeId));
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult("false", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(method = RequestMethod.DELETE, path = "/timetable/{storeId}", produces = "application/json")
    public ResponseEntity<ApiResult> deleteTimetable(@PathVariable String storeId) {
        try {
            if (timetableRepository.existsById(storeId)) {
                timetableRepository.deleteById(storeId);
                return ResponseEntity.ok()
                        .body(new ApiResult("true", "Timetable deleted for store: " + storeId));
            } else {
                return ResponseEntity.ok()
                        .body(new ApiResult("false", "Timetable for store with ID '" + storeId + "' does not exist."));
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResult(
                    "false", ex.getMessage()
            ), HttpStatus.BAD_REQUEST);
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




}
