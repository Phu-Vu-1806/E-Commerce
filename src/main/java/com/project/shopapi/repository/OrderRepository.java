package com.project.shopapi.repository;

import com.project.shopapi.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    @Query(value = "SELECT o FROM Order o WHERE o.status = ?1 AND o.createdBy = ?2", nativeQuery = true)
    List<Order> getListOrderOfPersonByStatus(int status, Integer userId);

    @Query(value = "SELECT o FROM Order o WHERE o.id = ?1 AND o.createdBy = ?2", nativeQuery = true)
    Order getOrderDetailByUser(Integer id, Integer userId);
}
