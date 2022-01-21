package com.jelleglebbeek.pcparts.user.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jelleglebbeek.pcparts.order.entities.Order;
import com.jelleglebbeek.pcparts.user.Role;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;

    @JsonIgnore()
    private String password;

    private String email;

    private String firstName;
    private String middleName;
    private String lastName;

    @JsonIgnore()
    @Column(length = 512)
    private String privateKey;
    @JsonIgnore()
    @Column(length = 512)
    private String publicKey;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders;
}
