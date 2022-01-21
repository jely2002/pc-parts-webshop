package com.jelleglebbeek.pcparts.product.repositories;

import com.jelleglebbeek.pcparts.product.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, String> {
}
