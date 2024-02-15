package com.project.shopapi.exception;

public class ExistCategory extends RuntimeException{
    public ExistCategory(String message) {
        super(message);
    }
}
