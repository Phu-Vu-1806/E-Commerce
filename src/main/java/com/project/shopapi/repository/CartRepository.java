package com.project.shopapi.repository;

import com.project.shopapi.entity.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CartRepository extends CrudRepository<Cart, Integer> {
    @Query(value = "SELECT * FROM carts WHERE product_id = ?1 AND user_id = ?2 ", nativeQuery = true)
    Cart findByProductAndUser(int productId, int customerId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE carts SET quantity = ?2, amount = ?4*?2 WHERE product_id = ?1 AND user_id = ?3", nativeQuery = true)
    void updateQuantity(int productId, int quantity, int customerId, float priceProduct);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM carts WHERE product_id = ?1 AND user_id = ?2", nativeQuery = true)
    void removeCart(int productId, int customerId);
}
