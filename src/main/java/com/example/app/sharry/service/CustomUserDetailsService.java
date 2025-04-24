package com.example.app.sharry.service;
import java.util.Collection;
import java.util.Optional;

import com.example.app.sharry.dao.UserRepository;
import com.example.app.sharry.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository R_user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = R_user.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomUserDetails(user);
    }

    public User getUserByUsername(String username) {
        return R_user.findByUsername(username);
    }
}
