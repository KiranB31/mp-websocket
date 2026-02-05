package com.example.websocket.service;

import com.example.websocket.model.UserActivity;
import com.example.websocket.repository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ActivityService {

    @Autowired
    private UserActivityRepository activityRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void logAndBroadcastActivity(UserActivity activity) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        UserActivity existing = activityRepository.findByUsername(activity.getUsername())
                .map(a -> {
                    a.setAction(activity.getAction());
                    a.setDetails(activity.getDetails());
                    a.setTimestamp(timestamp);
                    return a;
                })
                .orElseGet(() -> {
                    activity.setTimestamp(timestamp);
                    return activity;
                });

        // Save (either new or updated)
        activityRepository.save(existing);

        // Broadcast the updated activity
        messagingTemplate.convertAndSend("/topic/admin/activity", existing);
    }

    public java.util.List<UserActivity> getAllActivities() {
        return activityRepository.findAll();
    }
}
