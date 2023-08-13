package com.sm.qms.controllers.feedback;

import com.sm.qms.model.constants.AppConstants;
import com.sm.qms.model.database.TableFeedback;
import com.sm.qms.model.database.TableVisitor;
import com.sm.qms.model.entity.Notification;
import com.sm.qms.model.entity.FeedbackRequest;
import com.sm.qms.model.response.ApiResult;
import com.sm.qms.repository.FeedbackRepository;
import com.sm.qms.repository.VisitorRepository;
import com.sm.qms.service.NotificationService;
import com.sm.qms.utilities.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private NotificationService notificationService;

    @RequestMapping(method = RequestMethod.POST, path = "/api/feedback/create", produces = "application/json")
    public ResponseEntity<ApiResult> createFeedback(@RequestBody FeedbackRequest request) {
        try {

            String visitorId = request.getVisitorId(); //check valid visitorID
            String appointmentId = request.getAppointmentId(); //check valid appointmentId

            //

            Optional<TableVisitor> visitor = visitorRepository.findById(visitorId);
            if (visitor.isPresent()) {

                UUID uuid = UUID.randomUUID();
                Long dateMillis = DateUtils.getTimestamp();

                float rating = request.getRating();
                String message = request.getMessage();

                UUID appointmentUUID = UUID.fromString(appointmentId);

                TableFeedback feedback = new TableFeedback();
                feedback.setId(uuid);
                feedback.setVisitorId(visitorId);
                feedback.setAppointmentID(appointmentUUID);
                feedback.setRating(rating);
                feedback.setMessage(message);
                feedback.setTimestamp(dateMillis);

                feedbackRepository.save(feedback);

                //thank you note to visitor email

                //TODO - new email to admin

//                String html = "<h4>Thanks for Rating Us!</h4>" +
//                        "<p>" +
//                        "<strong>Your Visitor ID</strong>: " + visitorId + "<br />" +
//                        "<strong>Rating</strong>: " + +rating + " ⭐️ given!" + "<br />" +
//                        "<strong>Message</strong>: " + message +
//                        "</p>";
                String html = "Thanks for Rating Us!" +
                        "\n\n" +
                        "Your Visitor ID: " + visitorId + "\n" +
                        "Rating: " + +rating + " ⭐️ given!" + "\n" +
                        "Message: " + message;
//
//                String html2 = "Congratulations for Rating Us!\n\n"+
//                        "Visitor ID: " + visitorId + "\n" +
//                        "Rating: " +rating + " ⭐️ given!" + "\n" +
//                        "Message: " + message;

                notificationService.sendNotification(Notification.create(
                        "Feedback Sent for Appointment: " + appointmentId, html, AppConstants.EMAIL_TO
                ));

                return new ResponseEntity<>(new ApiResult(
                        "true", "Feedback sent successfully!"
                ), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(new ApiResult(
                        "false", "Invalid Visitor ID request"
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/feedback/list", produces = "application/json")
    public ResponseEntity<List<TableFeedback>> getFeedbacks() {
        try {
            List<TableFeedback> feedbacks = new ArrayList<>();
            feedbackRepository.findAll().forEach(feedbacks::add);
            return new ResponseEntity<>(feedbacks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
