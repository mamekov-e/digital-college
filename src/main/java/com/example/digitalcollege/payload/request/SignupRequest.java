package com.example.digitalcollege.payload.request;

import com.example.digitalcollege.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignupRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Middle name is required")
    private String middleName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Email(message = "Input type email")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6)
    private String password;
}
