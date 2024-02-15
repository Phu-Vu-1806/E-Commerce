package com.project.shopapi.controller;

import com.project.shopapi.entity.Category;
import com.project.shopapi.entity.Product;
import com.project.shopapi.entity.ProductSize;
import com.project.shopapi.entity.User;
import com.project.shopapi.model.dto.ProductDto;
import com.project.shopapi.model.request.CreateProductReq;
import com.project.shopapi.model.request.UpdateSizeCountReq;
import com.project.shopapi.repository.UserRepository;
import com.project.shopapi.service.impl.CategoryServiceImpl;
import com.project.shopapi.service.impl.ProductServiceImpl;
import com.project.shopapi.service.impl.UserServiceImpl;
import com.project.shopapi.utils.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable(name = "id") Integer id) {
        Product product = productService.getById(id);
        ProductDto productDto = new ProductDto( product.getName(), product.getDescription(), product.getDetail(), product.getPrice());
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> deleteProduct(@PathVariable(name = "id") Integer id) {

        productService.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProductDto>> getAllProduct(@RequestParam(defaultValue = "0") int pageNo,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestParam(defaultValue = "id") String sortBy) {

        List<Product> products = productService.getAllProduct(pageNo, pageSize, sortBy);
        List<ProductDto> dtos = products.stream().map(Product::toDto).collect(Collectors.toList());

        return new ResponseEntity<List<ProductDto>>(dtos, HttpStatus.OK);
    }

    @GetMapping("/getByUser/{userId}")
    public ResponseEntity<?> getListProduct(@PathVariable(name = "userId") int userId){
        List<Product> products = productService.getListProductByUser(userId);
        List<ProductDto> collect = products.stream().map(Product::toDto).collect(Collectors.toList());
        return new ResponseEntity<List<ProductDto>>(collect, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateProductReq req) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Auth: "+auth);

        String username = auth.getName();

        User user = userService.findByUsername(username);

        productService.create(req, user);
        return new ResponseEntity<MessageResponse>(new MessageResponse("Sucesss"), HttpStatus.OK);

    }

    @GetMapping("/search/priceRange")
    public ResponseEntity<List<ProductDto>> getProductByPriceRange(
            @RequestParam(name = "startPrice", defaultValue = "0") float startPrice,
            @RequestParam("endPrice") float endPrice,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {

        List<Product> products = productService.getAllProductByRangePrice(startPrice,
                endPrice, pageNo, pageSize,
                sortBy);
        List<ProductDto> dtos = products.stream().map(Product::toDto).collect(Collectors.toList());

        return new ResponseEntity<List<ProductDto>>(dtos, HttpStatus.OK);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<ProductDto>> getProductByName(@RequestParam("productName") String productName,

                                                             @RequestParam(defaultValue = "0") int pageNo,
                                                             @RequestParam(defaultValue = "10") int pageSize,
                                                             @RequestParam(defaultValue = "id") String sortBy) {

        List<Product> products = productService.searchProductByName(productName,
                pageNo, pageSize, sortBy);
        List<ProductDto> dtos = products.stream().map(Product::toDto).collect(Collectors.toList());

        return new ResponseEntity<List<ProductDto>>(dtos, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductByCategory(@PathVariable("categoryId") int categoryId,
                                                                 @RequestParam(defaultValue = "0") int pageNo,
                                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                                 @RequestParam(defaultValue = "id") String sortBy) {

        List<Product> products = productService.getAllProductByCategory(categoryId,
                pageNo, pageSize, sortBy);
        List<ProductDto> dtos = products.stream().map(Product::toDto).collect(Collectors.toList());

        return new ResponseEntity<List<ProductDto>>(dtos, HttpStatus.OK);
    }

    @GetMapping("/top12")
    public ResponseEntity<List<ProductDto>> getTop12Product() {

        List<Product> products = productService.getTop12Product();
        List<ProductDto> dtos = products.stream().map(Product::toDto).collect(Collectors.toList());

        return new ResponseEntity<List<ProductDto>>(dtos, HttpStatus.OK);
    }

    @GetMapping("/newProduct")
    public ResponseEntity<List<ProductDto>> getListNewProduct() {

        List<Product> products = productService.getListNewProduct();
        List<ProductDto> dtos = products.stream().map(Product::toDto).collect(Collectors.toList());

        return new ResponseEntity<List<ProductDto>>(dtos, HttpStatus.OK);
    }

    @GetMapping("/countAllProduct")
    public ResponseEntity<?> countAllProduct() {

        Integer amount = productService.countAllProduct();

        return new ResponseEntity<Integer>(amount, HttpStatus.OK);

    }

    @GetMapping("/countAllProductOfCategory/{categoryId}")
    public ResponseEntity<?> countAllProductOfCategory(@PathVariable("categoryId") int categoryId) {

        Integer amount = productService.countAllProductOfCategory(categoryId);

        return new ResponseEntity<Integer>(amount, HttpStatus.OK);

    }

    @PutMapping("/update-size-count")
    public ResponseEntity<?> updateSizeCount(@RequestBody UpdateSizeCountReq req) {
        productService.updateSizeCount(req);

        return ResponseEntity.ok("Cập nhật thành công");
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody CreateProductReq createProductReq,
                                           @PathVariable(name = "productId") int productId){
        Product product = productService.update(createProductReq, productId);
        return ResponseEntity.ok("Success");
    }
}
