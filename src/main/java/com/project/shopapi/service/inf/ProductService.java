package com.project.shopapi.service.inf;

import com.project.shopapi.entity.Product;
import com.project.shopapi.entity.User;
import com.project.shopapi.model.request.CreateProductReq;
import com.project.shopapi.model.request.UpdateSizeCountReq;

import java.util.List;

public interface ProductService {
    Product save(Product product);
    void create(CreateProductReq req, User user);
    void delete(int productId);
    Product getById(int productId);
    List<Product> getAllProduct(int pageNo, int pageSize, String sortBy);
    List<Product> getAllProductByRangePrice(float startPrice, float endPrice, int pageNo, int pageSize,
                                                   String sortBy);
    List<Product> searchProductByName(String keyword, int pageNo,
                                             int pageSize, String sortBy);
    List<Product> getAllProductByCategory(int categoryId, int pageNo,
                                                 int pageSize, String sortBy);
    List<Product> getTop12Product();
    List<Product> getListProductByUser(int userId);
    List<Product> getListProductByCategoryOfShop(int catId, int shopId);
    void increaseCountBuyProduct(int productId, int quantity);
    void reduceQuantityProduct(int productId, int number);
    List<Product> getListNewProduct();
    int countAllProduct();
    int countAllProductOfCategory(int categoryId);
    void updateSizeCount(UpdateSizeCountReq req);
    Product update(CreateProductReq createProductReq, int productId);
}
