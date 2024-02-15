package com.project.shopapi.repository;

import com.project.shopapi.entity.Category;
import org.springframework.data.repository.CrudRepository;


public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Category findByName(String name);
    boolean existsByName(String name);
}
