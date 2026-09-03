package com.tracking.tracking_app.Services;

import com.tracking.tracking_app.Entities.User;
import com.tracking.tracking_app.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityConfigService implements UserDetailsService {
    private final UserRepository userRepository;

    public SecurityConfigService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }
}
