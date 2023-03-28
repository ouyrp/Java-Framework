package com.wusong.crypt.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wusong.crypt.common.SecretKeyAuthenticator;
import com.wusong.web.dto.ApiResult;
import com.wusong.crypt.common.AuthConstants;
import com.wusong.crypt.common.Signature;
import com.wusong.crypt.common.SecretKeyStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author p14
 */
@Slf4j
public class SignatureHandlerInterceptor implements HandlerInterceptor {
    private ObjectMapper objectMapper;
    private SecretKeyStore secretKeyStore;
    private SecretKeyAuthenticator secretKeyAuthenticator;

    public SignatureHandlerInterceptor(ObjectMapper objectMapper, SecretKeyStore secretKeyStore) {
        this(objectMapper,secretKeyStore,null);
    }

    public SignatureHandlerInterceptor(ObjectMapper objectMapper, SecretKeyStore secretKeyStore, SecretKeyAuthenticator secretKeyAuthenticator) {
        this.objectMapper = objectMapper;
        this.secretKeyStore = secretKeyStore;
        this.secretKeyAuthenticator = secretKeyAuthenticator==null?new SecretKeyAuthenticator.PermitAll(): secretKeyAuthenticator;
    }

    /**
     * 请求过期时间默认5s，防止重放攻击
     */
    private int requestExpireMillis=5_000;

    public int getRequestExpireMillis() {
        return requestExpireMillis;
    }

    public void setRequestExpireMillis(int requestExpireMillis) {
        this.requestExpireMillis = requestExpireMillis;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!isValidRequest(request,response)){
            return false;
        }
        String accessKey=request.getHeader(AuthConstants.HEADER_AK);
        String requestTime=request.getHeader(AuthConstants.HEADER_TIME);
        String requestSign=request.getHeader(AuthConstants.HEADER_SIGN);
        String requestSignBody=request.getHeader(AuthConstants.HEADER_SIGN_BODY);
        String secretKey=secretKeyStore.getSecretKey(accessKey);
        if(!secretKeyAuthenticator.hasPermit(accessKey,request)){
            writeResponse(response,HttpStatus.FORBIDDEN.value(), "鉴权失败");
            return false;
        }
        String signData;
        if(requestSignBody!=null){
            signData= String.format("%s%s%s%s%s%s",requestTime,
                    request.getMethod(), request.getRequestURI(),request.getQueryString()==null?"":"?"+request.getQueryString(), requestTime,((SignatureRequestWrapper) request).getBody());
        }else {
            signData= String.format("%s%s%s%s%s", requestTime,
                    request.getMethod(), request.getRequestURI(),request.getQueryString()==null?"":"?"+request.getQueryString(), requestTime);
        }
        String sign = Signature.hmacSHA1(secretKey,signData);
        if(!Objects.equals(sign,requestSign)){
            log.info("验签失败 [{}] {}",request.getRequestURI(),accessKey);
            log.debug("signData {}",signData);
            log.debug("sign {} requestSign {}",sign,requestSign);
            writeResponse(response,HttpStatus.UNAUTHORIZED.value(), "签名错误");
            return false;
        }
        OffsetDateTime time =null;
        try{
            time=OffsetDateTime.parse(requestTime,AuthConstants.TIME_FORMATTER);
        }catch (Throwable t){
            log.info("request time 格式错误 {} {}",requestTime,t.getLocalizedMessage());
            return false;
        }
        if(OffsetDateTime.now().isAfter(time.plus(requestExpireMillis, ChronoUnit.MILLIS))){
            log.warn("[{}] {} 请求过期 requestTime={} time={}",request.getRequestURI(),accessKey,requestTime,time);
            writeResponse(response,HttpStatus.BAD_REQUEST.value(), "请求已过期");
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 判断你请求是否合法
     * @param request
     * @param response
     * @return
     */
    private boolean isValidRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessKey=request.getHeader(AuthConstants.HEADER_AK);
        List<String> keys = Arrays.asList(AuthConstants.HEADER_AK, AuthConstants.HEADER_TIME, AuthConstants.HEADER_SIGN);
        for (String key:keys) {
            if(request.getHeader(key)==null){
                log.info("[{}] {} 缺少参数 header {}",request.getRequestURI(),accessKey,key);
                writeResponse(response,HttpStatus.UNAUTHORIZED.value(), "缺少参数 "+key);
                return false;
            }
        }
        return true;
    }

    private void writeResponse(HttpServletResponse response,int status,String message) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);
        try(PrintWriter writer = response.getWriter()){
            ApiResult<Void> result=ApiResult.error(""+status, message);
            writer.write(objectMapper.writeValueAsString(result));
        }
    }
}
