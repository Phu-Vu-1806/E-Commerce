package com.project.shopapi.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderInfoDto {
    private int id;
    private long totalPrice;
    private int size;
    private String productName;
    private long productPrice;
}
