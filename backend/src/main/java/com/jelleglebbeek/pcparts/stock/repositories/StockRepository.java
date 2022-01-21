package com.jelleglebbeek.pcparts.stock.repositories;

import com.jelleglebbeek.pcparts.stock.entities.Stock;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface StockRepository extends CrudRepository<Stock, UUID> {
}
