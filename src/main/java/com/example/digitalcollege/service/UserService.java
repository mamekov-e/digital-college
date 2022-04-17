package com.example.digitalcollege.service;

import com.example.digitalcollege.dto.UserDto;
import com.example.digitalcollege.exceptions.UserExistException;
import com.example.digitalcollege.model.ERole;
import com.example.digitalcollege.model.User;
import com.example.digitalcollege.payload.request.SignupRequest;
import com.example.digitalcollege.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest userSignUp) {
        User user = new User();

        user.setFirstName(userSignUp.getFirstName());
        user.setLastName(userSignUp.getLastName());
        user.setMiddleName(userSignUp.getMiddleName());
        user.setPhoneNumber(userSignUp.getPhoneNumber());
        user.setEmail(userSignUp.getEmail());
        user.setPassword(passwordEncoder.encode(userSignUp.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        try {
            LOG.info("User saved {}", userSignUp.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration {}", e.getMessage());
            throw new UserExistException("The user " + user.getEmail() + " already exists");
        }
    }

    public User updateUser(UserDto userDto, Principal principal) {
        User user = getUserByPrincipal(principal);

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMiddleName(userDto.getMiddleName());
        user.setIIN(userDto.getIIN());
        user.setDob(userDto.getDob());

        LOG.info("User info updated {}", user.getEmail());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByCreatedDateAsc();
    }

    public User getUserByIIN(String IIN, Principal principal) {
        User user = getUserByPrincipal(principal);
        return userRepository.findUserByIIN(user.getIIN())
                .orElseThrow(() -> new UsernameNotFoundException("User IIN not found: " + IIN));
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String email = principal.getName();

        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User email not found: " + email));
    }

    public User getUserById(long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User id not found: " + userId));
    }
}
