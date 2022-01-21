package com.jelleglebbeek.pcparts.stock.repositories;

import com.jelleglebbeek.pcparts.stock.entities.Restock;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RestockRepository extends CrudRepository<Restock, UUID> {
}
