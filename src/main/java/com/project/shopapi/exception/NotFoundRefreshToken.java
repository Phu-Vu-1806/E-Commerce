package com.project.shopapi.exception;

public class NotFoundRefreshToken extends RuntimeException{
    public NotFoundRefreshToken(String message){
        super(message);
    }
}
