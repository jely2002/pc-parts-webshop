package com.jelleglebbeek.pcparts.stock.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter @Setter
public class Restock {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Stock stock;

    private int amount;

    @Enumerated(EnumType.STRING)
    private RestockState state;

    private Date deliveryDate;

}
