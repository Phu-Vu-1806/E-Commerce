package com.project.shopapi.repository;

import com.project.shopapi.entity.ProductSize;
import org.springframework.data.repository.CrudRepository;

public interface ProductSizeRepository extends CrudRepository<ProductSize, Integer> {
    ProductSize findBySize(int size);
    boolean existsBySize(int size);
}
