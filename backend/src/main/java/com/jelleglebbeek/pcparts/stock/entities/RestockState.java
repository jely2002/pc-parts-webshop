package com.jelleglebbeek.pcparts.stock.entities;

public enum RestockState {
    ORDERED("ORDERED"),
    CONFIRMED("CONFIRMED"),
    RESTOCKED("RESTOCKED");

    public final String value;

    RestockState(String value) {
        this.value = value;
    }
}
