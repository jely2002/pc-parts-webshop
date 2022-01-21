package com.jelleglebbeek.pcparts.auth.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserInitializeLogin {

    @NotEmpty
    String email;

}
