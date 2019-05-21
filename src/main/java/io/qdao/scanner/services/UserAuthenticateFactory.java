package io.qdao.scanner.services;

import io.qdao.scanner.dto.AppUserDetails;
import io.qdao.scanner.models.User;
import io.qdao.scanner.types.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UserAuthenticateFactory {

    public static final String HEADER_NAME = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private JwtService jwtService;

    public AppUserDetails makeDetailsFromUser(User user) {
        if (user == null || user.getUid() == null) {
            return null;
        }
        final List<GrantedAuthority> authorities = mapToGrantedAuthorities(user.getRoles());
        final String token = jwtService.generateToken(user);
        return new AppUserDetails(authorities, user, token);
    }

    public AppUserDetails makeDetailsFromToken(String token) {
        if (token == null) {
            return null;
        }

        final User user = jwtService.parseToken(token);
        if (user == null || user.getUid() == null) {
            return null;
        }

        return makeDetailsFromUser(user);
    }

    public UsernamePasswordAuthenticationToken makeAuthentication(AppUserDetails details) {
        if (details == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(details, details.getToken(), details.getAuthorities());
    }

    private List<GrantedAuthority> mapToGrantedAuthorities(Collection<Role> roles) {
        if (roles == null) {
            return new ArrayList<>();
        }
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            final String str = role.toString();
            if (Role.contains(str)) {
                final GrantedAuthority authority = new SimpleGrantedAuthority(str);
                authorities.add(authority);
            } else {
                final String msg = String.format("Unknown user role '%s'", str);
                throw new IllegalArgumentException(msg);
            }

        }
        return authorities;
    }
}
