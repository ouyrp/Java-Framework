package com.wusong.uc.common.exception;

public class SdkNoneInitException extends RuntimeException{

    public SdkNoneInitException(){
        super("uc-sdk未初始化");
    }

    public SdkNoneInitException(String message){
        super(message);
    }
}
