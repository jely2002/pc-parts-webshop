package com.jelleglebbeek.pcparts.user.entities;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateUserRequest {
    @NotEmpty
    String email;
    @NotEmpty
    String password;
    @NotEmpty
    String firstName;
    String middleName;
    String lastName;
}
