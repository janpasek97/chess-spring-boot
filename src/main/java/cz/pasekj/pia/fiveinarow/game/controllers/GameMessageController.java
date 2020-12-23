package cz.pasekj.pia.fiveinarow.game.controllers;

import cz.pasekj.pia.fiveinarow.game.GameMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GameMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/secured/game")
    public void handleGameMessage(@Payload GameMessage msg,
                                  Principal user,
                                  @Header("simpSessionId") String sessionId) throws Exception {

    }

}
