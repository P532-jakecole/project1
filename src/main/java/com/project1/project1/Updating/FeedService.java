package com.project1.project1.Updating;

import com.project1.project1.Feed.FeedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class FeedService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendUpdate(FeedObject object) {
        messagingTemplate.convertAndSend(
                "/feed/" + object.getName(),
                object
        );
    }
}
