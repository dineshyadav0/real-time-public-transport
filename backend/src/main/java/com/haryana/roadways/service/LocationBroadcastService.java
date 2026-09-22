package com.haryana.roadways.service;

import com.haryana.roadways.dto.LocationUpdate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class LocationBroadcastService {

    private final SimpMessagingTemplate messagingTemplate;

    public LocationBroadcastService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastLocation(LocationUpdate locationUpdate) {

        String destination = "/topic/trips/" + locationUpdate.getTripId();

        messagingTemplate.convertAndSend(
                destination,
                locationUpdate
        );
    }
}
