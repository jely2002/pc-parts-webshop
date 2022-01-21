package com.jelleglebbeek.pcparts.product.entities.properties;

public enum DataType {
    STRING("STRING"),
    INT("INT"),
    DOUBLE("DOUBLE");

    public final String value;

    DataType(String value) {
        this.value = value;
    }
}
