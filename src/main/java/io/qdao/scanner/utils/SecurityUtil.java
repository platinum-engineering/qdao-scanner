package io.qdao.scanner.utils;

import io.qdao.scanner.dto.AppUserDetails;
import io.qdao.scanner.models.User;
import io.qdao.scanner.types.Role;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public final class SecurityUtil {

    private SecurityUtil() {    }

    public static User getCurrentUser() {
        final AppUserDetails details = getCurrentUserDetails();
        if (details != null) {
            return details.getUser();
        }
        return null;
    }

    public static AppUserDetails getCurrentUserDetails() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AppUserDetails) {
            return (AppUserDetails) authentication.getPrincipal();
        }
        return null;
    }

    public static boolean isAdministrator(User user) {
        final List<Role> roles = user.getRoles();
        return roles.contains(Role.ROLE_MANAGER) || roles.contains(Role.ROLE_DEVELOPER);
    }

    public static Authentication getCurrentAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return authentication;
    }
}
