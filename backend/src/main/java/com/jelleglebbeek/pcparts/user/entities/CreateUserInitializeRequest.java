package com.jelleglebbeek.pcparts.user.entities;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateUserInitializeRequest {
    @NotEmpty
    String email;
}
