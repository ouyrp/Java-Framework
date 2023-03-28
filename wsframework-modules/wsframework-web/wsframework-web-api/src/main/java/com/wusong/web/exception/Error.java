package com.wusong.web.exception;

/**
 *
 * @author p14
 */
public interface Error {
    /**
     * 业务异常错误码
     * @return
     */
    String getCode();

    /**
     * 业务错误提示信息
     * @return
     */
    String getMessage();

    /**
     * http 状态码
     * @return
     */
    default int getHttpCode(){
        return 200;
    }

    /**
     * http response body
     * @return
     */
    default Object getResponseBody(){
        return null;
    }

    /**
     * 原始异常
     * @return
     */
    default Throwable getCause(){
        return null;
    }

    /**
     * 根据 message 模板格式化为新的错误信息
     * @param args
     * @return
     */
    default DynamicError format(Object ...args){
        return Errors.error(this.getCode(),String.format(this.getMessage(),args))
                .setHttpCode(getHttpCode()).setResponseBody(getResponseBody()).setCause(getCause());
    }
}
