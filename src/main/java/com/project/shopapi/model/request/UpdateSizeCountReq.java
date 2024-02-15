package com.project.shopapi.model.request;

import lombok.Data;

@Data
public class UpdateSizeCountReq {
    private int sizeId;
    private int size;
    private int count;
}
