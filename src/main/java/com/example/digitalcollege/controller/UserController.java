package com.example.digitalcollege.controller;

import com.example.digitalcollege.dto.UserDto;
import com.example.digitalcollege.facade.UserFacade;
import com.example.digitalcollege.model.User;
import com.example.digitalcollege.service.UserService;
import com.example.digitalcollege.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping("/get-current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDto userDto = userFacade.userToUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable("userId") String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDto userDto = userFacade.userToUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtoList = userService.getAllUsers()
                .stream()
                .map(userFacade::userToUserDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDto userDto,
                                              BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User user = userService.updateUser(userDto, principal);
        UserDto userUpdated = userFacade.userToUserDto(user);

        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }
}
