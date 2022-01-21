package com.jelleglebbeek.pcparts.product.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jelleglebbeek.pcparts.product.entities.conditions.Condition;
import com.jelleglebbeek.pcparts.product.entities.properties.Property;
import com.jelleglebbeek.pcparts.stock.entities.Stock;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    private String name;
    private String description;
    private String externalUrl;

    private Double price;
    private Boolean hasStock;
    private String imageUrl;

    @JsonManagedReference("properties")
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Property> properties;

    @JsonManagedReference("conditions")
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Condition> conditions;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Category category;

    @OneToOne(cascade = CascadeType.DETACH)
    private Stock stock;

}
