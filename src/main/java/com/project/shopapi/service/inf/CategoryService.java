package com.project.shopapi.service.inf;

import com.project.shopapi.entity.Category;
import com.project.shopapi.model.dto.CategoryDto;

public interface CategoryService {
    public Category getById(int id);
    public void saveCategory(CategoryDto categoryDto);
}
