package com.example.digitalcollege.controller;

import com.example.digitalcollege.payload.request.LoginRequest;
import com.example.digitalcollege.payload.request.SignupRequest;
import com.example.digitalcollege.payload.response.JwtResponse;
import com.example.digitalcollege.payload.response.MessageResponse;
import com.example.digitalcollege.security.SecurityConstants;
import com.example.digitalcollege.security.jwt.JwtProvider;
import com.example.digitalcollege.service.UserService;
import com.example.digitalcollege.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ResponseErrorValidation errorValidation;
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser (@Valid @RequestBody SignupRequest signupRequest,
                                                BindingResult bindingResult) {
        ResponseEntity<Object> errors = errorValidation.mapValidationService(bindingResult);

        if (!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUser(signupRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser (@Valid @RequestBody LoginRequest loginRequest,
                                                    BindingResult bindingResult) {
        ResponseEntity<Object> errors = errorValidation.mapValidationService(bindingResult);

        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = SecurityConstants.TOKEN_PREFIX + jwtProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtResponse(true, jwt));
    }
}
