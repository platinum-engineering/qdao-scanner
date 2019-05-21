package io.qdao.scanner.configs;

import io.qdao.scanner.components.SocketConnector;
import io.qdao.scanner.dto.UserPrincipal;
import io.qdao.scanner.handler.AssignPrincipalHandshakeHandler;
import io.qdao.scanner.utils.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SocketConnector connector;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(Topics.SUBSCRIBE_QUEUE, Topics.SUBSCRIBE_USER_REPLY);
        config.setApplicationDestinationPrefixes(Topics.SUBSCRIBE_QUEUE);
        config.setUserDestinationPrefix(Topics.SUBSCRIBE_USER_PREFIX);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint(Topics.ENDPOINT_CONNECT, Topics.ENDPOINT_REGISTER)
                .setHandshakeHandler(new AssignPrincipalHandshakeHandler())
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        final Principal principal = event.getUser();
        if (principal == null) {
            return;
        }
        if (principal instanceof UserPrincipal) {
            connector.subscribe(principal.getName());
        }
    }

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        final Principal principal = event.getUser();
        if (principal == null) {
            return;
        }
        if (principal instanceof UserPrincipal) {
            connector.connect(principal.getName());
        }
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        final Principal principal = event.getUser();
        if (principal == null) {
            return;
        }
        if (principal instanceof UserPrincipal) {
            connector.disconnect(principal.getName());
        }
    }
}
