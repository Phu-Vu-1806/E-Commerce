package com.project.shopapi.service.impl;

import com.project.shopapi.entity.Category;
import com.project.shopapi.exception.ExistCategory;
import com.project.shopapi.model.dto.CategoryDto;
import com.project.shopapi.repository.CategoryRepository;
import com.project.shopapi.service.inf.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getById(int id){
        return categoryRepository.findById(id).get();
    }

    @Override
    public void saveCategory(CategoryDto categoryDto) {

        Category checkCategory = categoryRepository.findByName(categoryDto.getName());
        if(checkCategory == null){
        Category category = Category.builder()
                .name(categoryDto.getName())
                .products(null)
                .build();
            categoryRepository.save(category);
        }else {
            throw new ExistCategory("Loại hàng này đã tồn tại");
        }

    }
}
