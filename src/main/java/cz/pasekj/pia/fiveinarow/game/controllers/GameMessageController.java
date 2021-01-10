package cz.pasekj.pia.fiveinarow.game.controllers;

import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.GameRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserInGameRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.game.GameMessage;
import cz.pasekj.pia.fiveinarow.game.PlayerColor;
import cz.pasekj.pia.fiveinarow.game.services.EndGameService;
import cz.pasekj.pia.fiveinarow.game.services.InGameHandlerService;
import cz.pasekj.pia.fiveinarow.game.services.NewGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

/**
 * Handler of WebSocket messages associated with gameplay
 */
@Controller
@RequiredArgsConstructor
public class GameMessageController {
    /** UserEntity DAO */
    private final UserRepository userRepository;
    /** Messaging service for sending user-directed messages */
    private final SimpMessagingTemplate simpMessagingTemplate;
    /** InGameHandler service */
    private final InGameHandlerService inGameHandlerService;
    /** EndGame service */
    private final EndGameService endGameService;
    /** NewGame service */
    private final NewGameService newGameService;

    /**
     * Handle incoming messages, resolves actions that must be done and send responses to
     * correct users. It changes the game state via game services
     * @param msg received message
     * @param user user who sent the message
     * @param sessionId session ID of the user who sent the message
     * @throws Exception N/A
     */
    @MessageMapping("/secured/game")
    public void handleGameMessage(@Payload GameMessage msg,
                                  Principal user,
                                  @Header("simpSessionId") String sessionId) throws Exception {
        String from = user.getName();
        UserEntity fromUser = userRepository.findByUsername(from);
        String fromEmail = fromUser.getEmail();

        // Handle pre-game messages
        if (handlePreGame(msg, from)) return;

        // Handle in game messages
        String gameID = inGameHandlerService.getInGame(fromEmail);
        if(gameID == null) return;

        String toEmail = inGameHandlerService.getCompetitor(gameID, fromEmail);
        UserEntity userTo = userRepository.findByEmail(toEmail);
        if(userTo == null) return;

        PlayerColor playerColor = fromEmail.equals(inGameHandlerService.getWhitePlayerEmail(gameID)) ? PlayerColor.WHITE : PlayerColor.BLACK;

        // handle in game messages
        switch (msg.getAction()) {
            case MOVE:
                if(inGameHandlerService.performMove(gameID, fromEmail, msg.getX(), msg.getY())) {
                    GameMessage outputMessage = new GameMessage(
                            GameMessage.GameMessageAction.COUNTER_MOVE,
                            msg.getPlayerColor(),
                            msg.getX(),
                            msg.getY());
                    simpMessagingTemplate.convertAndSendToUser(userTo.getUsername(), "/secured/notification/queue/specific-user", outputMessage);
                    PlayerColor winner = inGameHandlerService.getWin(gameID);
                    String to = userTo.getUsername();
                    if (winner != null) {
                        handleWin(gameID, from, to, playerColor, winner);
                    }
                }
                break;
            case CONNECT:
                GameMessage outputMessage = new GameMessage(
                        GameMessage.GameMessageAction.CONNECT_DATA,
                        userTo.getUsername(),
                        playerColor,
                        inGameHandlerService.getPlayerOnMove(gameID),
                        inGameHandlerService.getBoard(gameID)
                );
                simpMessagingTemplate.convertAndSendToUser(from, "/secured/notification/queue/specific-user", outputMessage);
                break;
            case MESSAGE:
                msg.setMessage(HtmlUtils.htmlEscape(msg.getMessage()));
                simpMessagingTemplate.convertAndSendToUser(userTo.getUsername(), "/secured/notification/queue/specific-user", msg);
                break;
            case SURRENDER:
                inGameHandlerService.surrender(gameID, fromEmail);
                PlayerColor winner = inGameHandlerService.getWin(gameID);
                handleWin(gameID, from, userTo.getUsername(), playerColor, winner);
                break;
        }
    }

    /**
     * Handle pre-game action messages, that are used for creating a game
     * @param msg received message
     * @param from username of the message sender
     * @return true = pre-game handled, false = pre-game not handled
     */
    private boolean handlePreGame(GameMessage msg, String from) {
        if(msg.getAction() == GameMessage.GameMessageAction.START
        || msg.getAction() == GameMessage.GameMessageAction.ACCEPT) {
            String to = msg.getOpponent();
            String toEmail = userRepository.findByUsername(to).getEmail();

            switch (msg.getAction()) {
                case START:
                    if (inGameHandlerService.getInGame(toEmail) != null) return true;

                    GameMessage outputMessage = new GameMessage(
                            GameMessage.GameMessageAction.START,
                            from,
                            msg.getX(),
                            msg.getY()
                    );
                    simpMessagingTemplate.convertAndSendToUser(to, "/secured/notification/queue/specific-user", outputMessage);
                    break;
                case ACCEPT:
                    newGameService.createGame(from, to, msg.getX(), msg.getY());
                    GameMessage acceptMessage = new GameMessage(
                            GameMessage.GameMessageAction.ACCEPT,
                            from,
                            msg.getX(),
                            msg.getY()
                    );
                    simpMessagingTemplate.convertAndSendToUser(to, "/secured/notification/queue/specific-user", acceptMessage);
                    break;
            }

            return true;
        }
        return false;
    }

    /**
     * Handle situation when a players wins
     * @param gameID ID of the corresponding game
     * @param from username of the player on move
     * @param to username of the opponent
     * @param currentPlayerColor current player color
     * @param winnerColor color of the winner
     */
    private void handleWin(String gameID, String from, String to, PlayerColor currentPlayerColor, PlayerColor winnerColor) {
        endGameService.finishGame(gameID);
        GameMessage winMessage = new GameMessage(GameMessage.GameMessageAction.WIN);
        GameMessage loseMessage = new GameMessage(GameMessage.GameMessageAction.LOSE);
        if(winnerColor == currentPlayerColor) {
            simpMessagingTemplate.convertAndSendToUser(from, "/secured/notification/queue/specific-user", winMessage);
            simpMessagingTemplate.convertAndSendToUser(to, "/secured/notification/queue/specific-user", loseMessage);
        } else {
            simpMessagingTemplate.convertAndSendToUser(from, "/secured/notification/queue/specific-user", loseMessage);
            simpMessagingTemplate.convertAndSendToUser(to, "/secured/notification/queue/specific-user", winMessage);
        }
    }

}
