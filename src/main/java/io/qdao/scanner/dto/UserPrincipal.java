package io.qdao.scanner.dto;

import java.security.Principal;

public final class UserPrincipal implements Principal {

    private final Long uid;

    public UserPrincipal(Long uid) {
        this.uid = uid;
    }

    @Override
    public String getName() {
        return String.valueOf(uid);
    }

}
