package com.wusong.crypt.common;

/**
 * @author p14
 */
public interface SecretKeyAuthenticator {
    /**
     * 判断 ak 是否有target的访问权限
     * @param ak
     * @param target
     * @return
     */
    boolean hasPermit(String ak,Object target);
    class PermitAll implements SecretKeyAuthenticator{

        @Override
        public boolean hasPermit(String ak, Object target) {
            return true;
        }
    }
}
