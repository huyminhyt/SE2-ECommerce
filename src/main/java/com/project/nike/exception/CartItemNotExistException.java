package com.project.nike.exception;

public class CartItemNotExistException extends RuntimeException{
    public CartItemNotExistException(String msg){
        super(msg);
    }
}
