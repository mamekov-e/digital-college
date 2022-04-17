package com.example.digitalcollege.dto;

import com.example.digitalcollege.model.School;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class UserDto {
    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String middleName;
    private String IIN;
    private Date dob;
    private String phoneNumber;
    private School schoolInfo;
}
