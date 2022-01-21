package com.jelleglebbeek.pcparts.order.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter @Setter
public class Payment {

    @Id
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    boolean completed;

    @Enumerated(EnumType.STRING)
    PaymentProvider paymentProvider;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Order order;
}
