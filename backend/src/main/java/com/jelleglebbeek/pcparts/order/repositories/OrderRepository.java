package com.jelleglebbeek.pcparts.order.repositories;

import com.jelleglebbeek.pcparts.order.entities.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {
}
