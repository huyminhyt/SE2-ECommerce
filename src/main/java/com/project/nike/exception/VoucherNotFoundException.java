package com.project.nike.exception;

public class VoucherNotFoundException extends RuntimeException{
    public VoucherNotFoundException(String message){
        super(message);
    }
}
