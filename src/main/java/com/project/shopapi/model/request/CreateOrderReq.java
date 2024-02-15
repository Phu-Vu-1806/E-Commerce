package com.project.shopapi.model.request;

import lombok.Data;

@Data
public class CreateOrderReq {

    private int productId;
    private int size;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private long totalPrice;
    private int quantity;
    private long productPrice;
}
