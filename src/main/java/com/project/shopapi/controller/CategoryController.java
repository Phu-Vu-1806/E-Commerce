package com.project.shopapi.controller;

import com.project.shopapi.model.dto.CategoryDto;
import com.project.shopapi.service.impl.CategoryServiceImpl;
import com.project.shopapi.utils.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto){
        categoryService.saveCategory(categoryDto);
        return new ResponseEntity<MessageResponse>(new MessageResponse("Sucesss"), HttpStatus.OK);
    }
}
