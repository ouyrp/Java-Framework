package com.wusong.web.dto;

import com.wusong.web.exception.Error;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * @author p14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ApiResult<T> implements Serializable {
    private String code;
    private String message;
    private String timestamp;
    private T data;

    /**
     * javadoc successful
     * @apiNote 判断返回的结果请求是否被正确处理
     *
     * @return boolean
     * @author weng xiaoyong
     * @date 2022/3/3 2:24 PM
     **/
    public boolean successful(){
        return equals("200", this.code);
    }

    /**
     * javadoc successful
     * @apiNote 判断返回的结果请求是否被正确处理
     *
     * @param code 给定的code
     * @return boolean
     * @author weng xiaoyong
     * @date 2022/3/3 2:24 PM
     **/
    public boolean successful(String code){
        return equals(code, this.code);
    }

    private static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        // Step-wise comparison
        final int length = cs1.length();
        for (int i = 0; i < length; i++) {
            if (cs1.charAt(i) != cs2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static  <T>  ApiResult<T> ok(T body){
        ApiResult<T> apiResult=result();
        apiResult.setCode("200");
        apiResult.setData(body);
        return apiResult;
    }

    public static  <T>  ApiResult<T> result(){
        ApiResult<T> apiResult=new ApiResult<>();
        apiResult.setTimestamp(OffsetDateTime.now().toString());
        return apiResult;
    }

    public static  <T>  ApiResult<T> ok(){
        return ok(null);
    }

    public static  <T>  ApiResult<T> error(int status,String message){
        ApiResult<T> result=result();
        result.setCode(status+"");
        result.setMessage(message);
        return result;
    }
    public static  <T>  ApiResult<T> error(String status,String message){
        ApiResult<T> result=result();
        result.setCode(status);
        result.setMessage(message);
        return result;
    }

    @SuppressWarnings(value = "unchecked")
    public static  <T>  ApiResult<T> error(Error error){
        ApiResult<T> result=result();
        result.setCode(error.getCode());
        result.setMessage(error.getMessage());
        result.setData((T) error.getResponseBody());
        return result;
    }
}
