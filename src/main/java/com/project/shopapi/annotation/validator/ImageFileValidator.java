package com.project.shopapi.annotation.validator;

import com.project.shopapi.annotation.ValidImage;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    @Override
    public void initialize(ValidImage constraintAnnotation) {
    }

//    @Override
//    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
//        List<String> validTypes = Arrays.asList("image/png", "image/jpg", "image/jpeg");
//        boolean isSupportedContentType = validTypes.contains(value.getContentType());
//
//        if(!isSupportedContentType){
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate("Only PNG or JPG images are allowed")
//                    .addConstraintViolation();
//        }
//        return isSupportedContentType;
//    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {

        boolean result = true;

        String contentType = multipartFile.getContentType();
        if (!isSupportedContentType(contentType)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Only PNG or JPG images are allowed.")
                    .addConstraintViolation();

            result = false;
        }

        return result;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }
}
