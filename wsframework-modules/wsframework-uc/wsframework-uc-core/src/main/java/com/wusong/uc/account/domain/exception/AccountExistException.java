package com.wusong.uc.account.domain.exception;

public class AccountExistException extends Exception{

    public AccountExistException(){
        super("注册时账号已存在");
    }
}
