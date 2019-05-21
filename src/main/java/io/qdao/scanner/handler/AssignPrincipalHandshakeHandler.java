package io.qdao.scanner.handler;


import io.qdao.scanner.dto.AppUserDetails;
import io.qdao.scanner.dto.UserPrincipal;
import io.qdao.scanner.utils.SecurityUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class AssignPrincipalHandshakeHandler extends DefaultHandshakeHandler {

    private static final String ATTR_PRINCIPAL = "__principal__";

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final Long uid;

        if (!attributes.containsKey(ATTR_PRINCIPAL)) {
            final AppUserDetails details = SecurityUtil.getCurrentUserDetails();
            if (details != null) {
                uid = details.getUid();
                attributes.put(ATTR_PRINCIPAL, uid);
            } else {
                uid = null;
            }
        } else {
            uid = (long) attributes.get(ATTR_PRINCIPAL);
        }
        if (uid != null) {
            return new UserPrincipal(uid);
        } else {
            return null;
        }
    }
}
