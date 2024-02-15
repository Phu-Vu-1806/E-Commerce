package com.project.shopapi.service.inf;

import com.project.shopapi.entity.ProductFile;
import com.project.shopapi.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductFileService {
    ProductFile save(MultipartFile file, User user, int productId) throws IOException;
    ProductFile getFile(String id);
    List<ProductFile> getAllFile();
}
