package com.jelleglebbeek.pcparts.order.entities;

public enum PaymentProvider {
    PAYPAL("PAYPAL"),
    IDEAL("IDEAL"),
    CREDITCARD("CREDITCARD");

    public final String value;

    PaymentProvider(String value) {
        this.value = value;
    }
}
