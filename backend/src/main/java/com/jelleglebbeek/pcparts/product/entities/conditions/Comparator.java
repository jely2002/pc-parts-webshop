package com.jelleglebbeek.pcparts.product.entities.conditions;

public enum Comparator {
    EQUALS("EQUALS"),
    SMALLER("SMALLER"),
    SMALLER_OR_EQUAL("SMALLER_OR_EQUAL"),
    BIGGER("BIGGER"),
    BIGGER_OR_EQUAL("BIGGER_OR_EQUAL"),
    IS("IS");

    public final String value;

    Comparator(String value) {
        this.value = value;
    }
}
