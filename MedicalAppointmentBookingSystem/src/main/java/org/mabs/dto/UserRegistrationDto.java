package org.mabs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    private String fullName;
    private String email;
    private String password;
}
