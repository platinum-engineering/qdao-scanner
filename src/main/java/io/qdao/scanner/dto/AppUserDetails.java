package io.qdao.scanner.dto;

import io.qdao.scanner.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AppUserDetails implements UserDetails {

    private final Collection<GrantedAuthority> authorities;
    private final Long uid;
    private final String password;
    private final String email;
    private final String token;
    private final User user;


    public AppUserDetails(Collection<GrantedAuthority> authorities, User user, String token) {
        this.authorities = authorities;
        this.uid = user.getUid();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.user = user;
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
