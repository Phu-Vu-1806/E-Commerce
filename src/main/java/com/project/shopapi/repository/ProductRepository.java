package com.project.shopapi.repository;

import com.project.shopapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    @Query(value = "SELECT * FROM products WHERE price >= ?1 AND price <= ?2", nativeQuery = true)
    Page<Product> getAllProductByRangePrice(float startPrice, float endPrice, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE name LIKE %?1%", nativeQuery = true)
    Page<Product> searchProductByName(String keyword, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.category_id = ?1", nativeQuery = true)
    Page<Product> getProductByCategory(int categoryId, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE active = 1 ORDER BY count_buy DESC LIMIT 12", nativeQuery =  true)
    List<Product> getTop12Product();

    @Query(value = "SELECT * FROM products AS p INNER JOIN users AS u ON p.created_by = u.id WHERE p.active = 1 AND u.id = ?1", nativeQuery = true)
    List<Product> getListProductByUser(Integer userId);

    @Query(value = "SELECT * FROM products AS p INNER JOIN users AS u ON p.created_buy = u.id"
            + "INNER JOIN categories AS c ON p.category_id = c.id WHERE p.active = 1 AND u.id = ?2 AND c.id = ?1", nativeQuery =  true)
    List<Product> getListProductByCategoryOfShop(Integer catId, Integer shopId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Product p SET p.count_buy = p.count_buy + ?2 WHERE p.id = ?1",nativeQuery = true)
    void increaseCountBuyProduct(Integer productId, Integer quantity);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Product p SET p.quantity = p.quantity - ?2 WHERE p.id = ?1", nativeQuery = true)
    void reduceQuantityProduct(Integer productId, Integer number);

    @Query(value = "SELECT * FROM products WHERE active = 1 ORDER BY created_at DESC LIMIT 5", nativeQuery = true)
    List<Product> getListNewProduct();

    @Query(value = "SELECT COUNT(*) FROM products WHERE active = 1", nativeQuery = true)
    int countAllProduct();

    @Query(value = "SELECT COUNT(*) FROM products AS p INNER JOIN categories AS c ON p.category_id = c.id WHERE c.id = ?1 ",nativeQuery = true)
    int countAllProductOfCategory(Integer categoryId);
}
