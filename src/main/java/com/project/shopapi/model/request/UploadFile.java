package com.project.shopapi.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFile {
    private String name;
    private String url;
    private String type;
    private long size;
}
