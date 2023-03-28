package com.wusong.uc.handler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ITokenHandler {

    /**
     * javadoc readToken
     * @apiNote 从请求中读取token
     *
     * @param request http 请求
     * @param response http 相应
     * @return java.lang.String
     * @author weng xiaoyong
     * @date 2022/3/17 10:02 AM
     **/
    String readToken(HttpServletRequest request, HttpServletResponse response);

    /**
     * javadoc writeToken
     * @apiNote 向http-resp写token
     *
     * @param response http 相应
     * @param expire 有效期 单位秒
     * @param token 授权
     * @author weng xiaoyong
     * @date 2022/3/17 10:03 AM
     **/
    void writeToken(HttpServletResponse response, int expire, String token);
}
