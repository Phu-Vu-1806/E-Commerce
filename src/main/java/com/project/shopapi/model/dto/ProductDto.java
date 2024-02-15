package com.project.shopapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ProductDto {
    private String name;
    private String description;
    private String detail;
    private float price;
}
