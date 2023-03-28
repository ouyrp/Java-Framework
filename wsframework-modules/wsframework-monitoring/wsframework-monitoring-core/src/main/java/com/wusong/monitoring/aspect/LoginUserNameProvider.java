package com.wusong.monitoring.aspect;

/**
 * @author p14
 */
public interface LoginUserNameProvider {
    /**
     * 获取当前登录用户的姓名
     * @return
     */
    String getLoginUserName();
}
