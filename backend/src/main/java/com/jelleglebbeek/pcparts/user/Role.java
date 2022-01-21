package com.jelleglebbeek.pcparts.user;

public enum Role {
    CUSTOMER("customer"),
    ADMIN("admin");

    public final String value;

    Role(String value) {
        this.value = value;
    }
}
