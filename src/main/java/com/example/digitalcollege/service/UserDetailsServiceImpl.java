package com.example.digitalcollege.service;

import com.example.digitalcollege.model.User;
import com.example.digitalcollege.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User email not found" + email));

        return UserDetailsImpl.build(user);
    }

    public UserDetails loadUserById(Long userId) {
        User user = userRepository.findUserById(userId)
                .orElse(null);

        assert user != null;
        return UserDetailsImpl.build(user);
    }
}
