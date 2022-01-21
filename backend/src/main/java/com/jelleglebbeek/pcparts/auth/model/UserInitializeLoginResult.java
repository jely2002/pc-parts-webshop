package com.jelleglebbeek.pcparts.auth.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserInitializeLoginResult {

    @NonNull
    private String publicKey;

}
