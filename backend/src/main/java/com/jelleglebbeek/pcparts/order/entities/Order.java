package com.jelleglebbeek.pcparts.order.entities;

import com.jelleglebbeek.pcparts.user.entities.Address;
import com.jelleglebbeek.pcparts.user.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity(name="\"order\"")
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Address billingAddress;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Address shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderLine> orderLines;

    @OneToOne(cascade = CascadeType.DETACH)
    private Payment payment;

    //private double total = orderLines.stream().mapToDouble(OrderLine::getSubTotal).sum();

    //private double taxedTotal = total * 1.21;

}
