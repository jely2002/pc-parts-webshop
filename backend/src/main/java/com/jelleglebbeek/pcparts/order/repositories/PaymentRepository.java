package com.jelleglebbeek.pcparts.order.repositories;

import com.jelleglebbeek.pcparts.order.entities.Payment;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PaymentRepository extends CrudRepository<Payment, UUID> {
}
