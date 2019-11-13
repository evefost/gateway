package com.eve.hystrix.extend;

public class HystrixException extends RuntimeException{

    public HystrixException(String message){
        super(message);
    }
}
