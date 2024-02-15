package com.project.shopapi.payload.request;

import com.project.shopapi.annotation.ValidImage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class UploadImg {
    @NotNull
    @ValidImage
    private MultipartFile image;
}
