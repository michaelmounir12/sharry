package com.example.app.sharry.service;

import com.example.app.sharry.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return a default authority or empty list, depending on your future features
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // Implement your logic for account expiration
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Implement your logic for account locking
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Implement your logic for credential expiration
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Implement your logic for account enabling
        return true;
    }
}