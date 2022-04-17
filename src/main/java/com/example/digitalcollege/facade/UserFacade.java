package com.example.digitalcollege.facade;

import com.example.digitalcollege.dto.UserDto;
import com.example.digitalcollege.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMiddleName(user.getMiddleName());
        userDto.setIIN(user.getIIN());
        userDto.setDob(user.getDob());

        return userDto;
    }
}
