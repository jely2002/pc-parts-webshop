package com.jelleglebbeek.pcparts.product.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jelleglebbeek.pcparts.product.entities.properties.PropertyType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter @Setter
public class Category {

    @Id
    private String name;

    private String description;

    @JsonManagedReference("category")
    @OneToMany(mappedBy = "category")
    private Set<PropertyType> propertyTypes;

}
