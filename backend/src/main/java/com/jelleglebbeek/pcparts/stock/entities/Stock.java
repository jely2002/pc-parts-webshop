package com.jelleglebbeek.pcparts.stock.entities;

import com.jelleglebbeek.pcparts.product.entities.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
public class Stock {

    @Id
    @Column(name = "product_id")
    private UUID productId;

    @PrimaryKeyJoinColumn(name = "product_id", referencedColumnName = "id")
    @OneToOne(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private Product product;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Restock> restocks;

    private int amount;

}
