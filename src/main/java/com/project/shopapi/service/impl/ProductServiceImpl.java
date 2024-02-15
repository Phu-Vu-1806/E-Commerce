package com.project.shopapi.service.impl;

import com.github.slugify.Slugify;
import com.project.shopapi.entity.*;
import com.project.shopapi.exception.BadRequestException;
import com.project.shopapi.model.request.CreateProductReq;
import com.project.shopapi.model.request.UpdateSizeCountReq;
import com.project.shopapi.repository.ProductRepository;
import com.project.shopapi.repository.ProductSizeRepository;
import com.project.shopapi.repository.SoldRepository;
import com.project.shopapi.service.inf.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.project.shopapi.utils.Constant.SIZE_VN;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final CategoryServiceImpl categoryService;
    private final SoldRepository soldRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void create(CreateProductReq req, User user) {
        Category category = categoryService.getById(req.getCategoryId());
        Slugify slg = new Slugify();
        String slug = slg.slugify(req.getName());
        List<ProductSize> sizes = new ArrayList<>();
        ProductSize ps = productSizeRepository.findBySize(req.getSize());

        sizes.add(ps);

        Product product = Product.builder()
                .name(req.getName())
                .description(req.getDescription())
                .detail(req.getDetail())
                .price(req.getPrice())
                .slug(slug)
//                .created_at(new Date())
                .count_buy(0)
                .category(category)
//                .user(user)
                .sizes(sizes)
                .build();

        save(product);

        Sold sold = new Sold(0, product);
        soldRepository.save(sold);
    }

    @Override
    public void delete(int productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Product getById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public List<Product> getAllProduct(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
//        tạo trang để hiển thị cho 1 số lượng phần tử hiển nhất định
        Page<Product> pageResult = productRepository.findAll(pageable);

        if(pageResult.hasContent()){
            List<Product> products = pageResult.getContent();
            return pageResult.getContent();
        }else {
            return new ArrayList<Product>();
        }
    }

    @Override
    public List<Product> getAllProductByRangePrice(float startPrice, float endPrice, int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Product> result = productRepository.getAllProductByRangePrice(startPrice, endPrice, pageable);

        if (result.hasContent()) {
            return result.getContent();
        } else {
            return new ArrayList<Product>();
        }
    }

    @Override
    public List<Product> searchProductByName(String keyword, int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> result = productRepository.searchProductByName(keyword,
                pageable);

        if (result.hasContent()) {
            return result.getContent();
        } else {
            return new ArrayList<Product>();
        }
    }

    @Override
    public List<Product> getAllProductByCategory(int categoryId, int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> result = productRepository.getProductByCategory(categoryId,
                pageable);

        if (result.hasContent()) {
            return result.getContent();
        } else {
            return new ArrayList<Product>();
        }
    }

    @Override
    public List<Product> getTop12Product() {
        return productRepository.getTop12Product();
    }

    @Override
    public List<Product> getListProductByUser(int userId) {
        return productRepository.getListProductByUser(userId);
    }

    @Override
    public List<Product> getListProductByCategoryOfShop(int catId, int shopId) {
        return productRepository.getListProductByCategoryOfShop(catId, shopId);
    }

    @Override
    public void increaseCountBuyProduct(int productId, int quantity) {
        productRepository.increaseCountBuyProduct(productId, quantity);
    }

    @Override
    public void reduceQuantityProduct(int productId, int number) {
        productRepository.reduceQuantityProduct(productId, number);

    }

    @Override
    public List<Product> getListNewProduct() {
        return productRepository.getListNewProduct();
    }

    @Override
    public int countAllProduct() {
        return productRepository.countAllProduct();
    }

    @Override
    public int countAllProductOfCategory(int categoryId) {
        return productRepository.countAllProductOfCategory(categoryId);
    }

    @Override
    public void updateSizeCount(UpdateSizeCountReq req) {
        boolean isValid = false;

        for (int size : SIZE_VN) {

            if (size == req.getSize()) {
                isValid = true;
            }

        }

        if (isValid == false) {
            throw new BadRequestException("Size khong hop le");
        }

        ProductSize ps = productSizeRepository.findBySize(req.getSize());

        if (ps == null) {
            ProductSize productSize = ProductSize.builder()
                    .size(req.getSize())
                    .quantity(req.getCount())
                    .build();

            productSizeRepository.save(productSize);
        } else {
            ps.setQuantity(req.getCount());
            productSizeRepository.save(ps);
        }

    }

    @Override
    public Product update(CreateProductReq createProductReq, int productId) {
        Product product = productRepository.findById(productId).get();
        if(product.equals(null)){
            throw new RuntimeException("Not found any product");
        }
        Category category = categoryService.getById(productId);
        List<ProductSize> sizes = new ArrayList<>();
        ProductSize ps = productSizeRepository.findBySize(createProductReq.getSize());
        sizes.add(ps);

        product.setName(createProductReq.getName());
        product.setDescription(createProductReq.getDescription());
        product.setDetail(createProductReq.getDetail());
        product.setPrice(createProductReq.getPrice());
        product.setCategory(category);
        product.setSizes(sizes);

        productRepository.save(product);

        return product;
    }
}
