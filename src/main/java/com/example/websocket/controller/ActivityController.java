package com.example.websocket.controller;

import com.example.websocket.model.UserActivity;
import com.example.websocket.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @MessageMapping("/activity.track")
    public void trackActivity(@Payload UserActivity activity) {
        activityService.logAndBroadcastActivity(activity);
    }

    @GetMapping("/api/activity")
    public List<UserActivity> getAllActivities() {
        return activityService.getAllActivities();
    }
}
