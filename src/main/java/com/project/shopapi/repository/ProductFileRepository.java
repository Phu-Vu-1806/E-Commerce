package com.project.shopapi.repository;

import com.project.shopapi.entity.ProductFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProductFileRepository extends JpaRepository<ProductFile, String> {
}
