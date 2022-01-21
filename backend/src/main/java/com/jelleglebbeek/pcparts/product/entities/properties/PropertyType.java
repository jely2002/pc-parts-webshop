package com.jelleglebbeek.pcparts.product.entities.properties;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jelleglebbeek.pcparts.product.entities.Category;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter @Setter
public class PropertyType {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DataType type;

    @JsonBackReference("category")
    @ManyToOne(cascade = CascadeType.DETACH)
    private Category category;

    private String name;

}
