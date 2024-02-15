package com.project.shopapi.service.impl;

import com.project.shopapi.entity.Product;
import com.project.shopapi.entity.ProductFile;
import com.project.shopapi.entity.User;
import com.project.shopapi.repository.ProductFileRepository;
import com.project.shopapi.repository.ProductRepository;
import com.project.shopapi.service.inf.ProductFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ProductFileServiceImpl implements ProductFileService {

    @Autowired
    private ProductFileRepository productFileRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductFile save(MultipartFile file, User user, int productId) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        Product product = productRepository.findById(productId).get();
        ProductFile file1 = new ProductFile(fileName, file.getContentType(), file.getBytes(),product, userService.findByUsername(user.getUsername()));
        return productFileRepository.save(file1);
    }

    @Override
    public ProductFile getFile(String id) {
        return productFileRepository.findById(id).get();
    }

    @Override
    public List<ProductFile> getAllFile() {
        return productFileRepository.findAll();
    }
}
