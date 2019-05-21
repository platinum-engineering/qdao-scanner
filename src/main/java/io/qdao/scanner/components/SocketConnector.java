package io.qdao.scanner.components;

import io.qdao.scanner.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SocketConnector {

    @Autowired
    private SimpMessagingTemplate template;

    private Set<Long> currentUserConnected = new HashSet<>();

    private String userConnected(User user) {
        if (user == null) {
            return null;
        }
        if (currentUserConnected.contains(user.getUid())) {
            return String.valueOf(user.getUid());
        }
        return null;
    }

    public void connect(String token) {
        if (token == null) {
            return;
        }
        final Long uid = Long.parseLong(token);
        currentUserConnected.add(uid);
    }

    public void disconnect(String token) {
        if (token == null) {
            return;
        }
        final Long uid = Long.parseLong(token);
        currentUserConnected.remove(uid);
    }

    public void subscribe(String token) {
        if (token == null) {
            return;
        }

        final Long uid = Long.parseLong(token);
        currentUserConnected.add(uid);
    }

    public void notifyUser(User user, String topic, Object message) {
        final String token = userConnected(user);
        if (token == null) {
            return;
        }

        template.convertAndSendToUser(token, topic, message);
    }

    public void notifyAuthenticatedUser(String topic, Object message) {
        for (Long uid : currentUserConnected) {
            final String token = String.valueOf(uid);
            if (token == null) { continue; }
            template.convertAndSendToUser(token, topic, message);
        }
    }

    public void notifyAll(String path, Object message) {
        template.convertAndSend(path, message);
    }


}
