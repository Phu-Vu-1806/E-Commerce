package com.project.shopapi.exception;

public class ErrorVerifiedRefreshToken extends RuntimeException{
    public ErrorVerifiedRefreshToken(String message){
        super(message);
    }
}
