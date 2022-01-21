package com.jelleglebbeek.pcparts.auth.model;

import com.jelleglebbeek.pcparts.user.entities.User;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserLoginResult {

    @NonNull
    private String token;
    @NonNull
    private User user;

}
