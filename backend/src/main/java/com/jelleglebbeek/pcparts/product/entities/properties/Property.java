package com.jelleglebbeek.pcparts.product.entities.properties;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jelleglebbeek.pcparts.product.entities.Product;
import com.jelleglebbeek.pcparts.product.entities.conditions.Condition;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
public class Property {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    private String description;
    private Boolean highlight;

    @JsonBackReference("properties")
    @ManyToOne(cascade = CascadeType.DETACH)
    private Product product;

    @ManyToOne(cascade = CascadeType.DETACH)
    private PropertyType propertyType;

    @OneToMany(mappedBy = "property")
    private Set<Condition> conditions;

    private String value;

}
