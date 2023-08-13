package com.sm.qms.controllers.store;

import com.sm.qms.model.database.TableStore;
import com.sm.qms.model.entity.Notification;
import com.sm.qms.model.response.ApiResult;
import com.sm.qms.repository.StoreRepository;
import com.sm.qms.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/superAdmin")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private NotificationService notificationService;







}
