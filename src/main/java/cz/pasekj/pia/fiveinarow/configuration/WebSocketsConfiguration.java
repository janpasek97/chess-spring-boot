package cz.pasekj.pia.fiveinarow.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketsConfiguration implements WebSocketMessageBrokerConfigurer {

    /** MessageBroker config */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/secured/notification/queue/specific-user");
        registry.setApplicationDestinationPrefixes("/app-ws");
        registry.setUserDestinationPrefix("/secured/notification");
    }

    /** Registration of 2 application websockets endpoints */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/secured/friends");
        registry.addEndpoint("/secured/friends").withSockJS();
        registry.addEndpoint("/secured/game");
        registry.addEndpoint("/secured/game").withSockJS();
    }
}
