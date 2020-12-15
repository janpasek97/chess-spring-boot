package cz.pasekj.pia.fiveinarow.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/secured/friends")
    public void handleFriendsMessage(@Payload FriendsMessage msg,
                                     Principal user,
                                     @Header("simpSessionId") String sessionId) throws Exception {

        FriendsMessage outputMessage = new FriendsMessage(msg.getFrom(), msg.getTo(), msg.getAction());
        simpMessagingTemplate.convertAndSendToUser(msg.getTo(), "/secured/notification/queue/specific-user", outputMessage);
    }

}
