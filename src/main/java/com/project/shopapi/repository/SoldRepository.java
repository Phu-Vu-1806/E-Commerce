package com.project.shopapi.repository;

import com.project.shopapi.entity.Product;
import com.project.shopapi.entity.Sold;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SoldRepository extends CrudRepository<Sold, Integer> {
    Sold findByProduct(Product product);
}
