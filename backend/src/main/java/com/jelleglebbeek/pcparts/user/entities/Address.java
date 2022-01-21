package com.jelleglebbeek.pcparts.user.entities;

import com.jelleglebbeek.pcparts.order.entities.Order;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
public class Address {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    private String addressLine;
    private String city;
    private String zip;
    private String state;

    boolean isMainAddress;

    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;

    @OneToMany(mappedBy = "shippingAddress")
    private Set<Order> shippedOrders;

    @OneToMany(mappedBy = "billingAddress")
    private Set<Order> billedOrders;
}
