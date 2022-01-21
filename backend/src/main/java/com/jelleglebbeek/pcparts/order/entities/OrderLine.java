package com.jelleglebbeek.pcparts.order.entities;

import com.jelleglebbeek.pcparts.product.entities.Product;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter @Setter
public class OrderLine {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Order order;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Product product;

    private int amount;

}
