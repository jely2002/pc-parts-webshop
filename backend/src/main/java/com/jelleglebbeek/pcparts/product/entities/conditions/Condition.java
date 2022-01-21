package com.jelleglebbeek.pcparts.product.entities.conditions;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jelleglebbeek.pcparts.product.entities.Product;
import com.jelleglebbeek.pcparts.product.entities.properties.Property;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity(name="\"condition\"")
@Getter @Setter
public class Condition {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Property property;

    @JsonBackReference("conditions")
    @ManyToOne(cascade = CascadeType.DETACH)
    private Product product;

    @Enumerated(EnumType.STRING)
    private Comparator comparator;

    private String value;

    private String errorMessage;

    private String description;

    private boolean skipIfPropertyNotExist;

}
