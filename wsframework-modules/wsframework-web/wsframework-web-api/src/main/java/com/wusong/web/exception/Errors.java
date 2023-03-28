package com.wusong.web.exception;


/**
 *
 * @author p14
 */
public enum Errors implements Error {
    /**
     * 未知异常
     */
    SINGLE_MESSAGE("500","%s"),
    UNKNOWN("500","未知异常");

    private String code;
    private String message;

    Errors(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static DynamicError error(){
        return new DynamicError();
    }
    public static DynamicError error(String code){
        return error().setCode(code);
    }
    public static DynamicError error(String code,String message){
        return error().setCode(code).setMessage(message);
    }
    public static DynamicError error(String code,String message,Throwable cause){
        return error().setCode(code).setMessage(message).setCause(cause);
    }
    public static DynamicError error(String code,String message,Throwable cause,int httpCode){
        return error().setCode(code).setMessage(message).setCause(cause).setHttpCode(httpCode);
    }
}
