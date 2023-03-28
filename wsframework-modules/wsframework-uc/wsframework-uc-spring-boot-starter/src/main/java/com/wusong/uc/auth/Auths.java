package com.wusong.uc.auth;



import com.wusong.uc.common.enums.AuthorizationCodeEnum;
import com.wusong.uc.common.exception.AuthorizationException;
import com.wusong.uc.passport.domain.TokenPayloadBo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * javadoc Auths
 * <p>
 * current user holder by thread-local
 * I think user-id in header will be better
 *  注意 注意 注意: 为了使开发流程, 本util下的所有方法对外抛出的都是unchecked exception, 目的就是为了不用显式try...catch...处理
 *  但是 但是 但是 该 unchecked exception (AuthorizationException) 需要结合业务全局处理
 *  比如: 全局捕获该异常 然后返回业务能识别的body, 本异常内部包含一个 业务识别码code, 一个 业务错误消息 message
 * <p>
 *
 * @author weng xiaoyong
 * @version 1.0.0
 * @see AuthorizationException
 * @date 2021/4/30 3:06 PM
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Auths {
    private static final ThreadLocal<TokenPayloadBo> TOKEN_REPOSITORY = new InheritableThreadLocal<>();

    /**
     * javadoc setToken
     * @apiNote 设置token payload
     *
     * @param bo token payload
     * @author weng xiaoyong
     * @date 2022/3/18 3:20 PM
     **/
    public static void setToken(TokenPayloadBo bo){
        TOKEN_REPOSITORY.set(bo);
    }


    /**
     * javadoc getToken
     * @apiNote 获取当前请求授权信息
     *
     * @return com.wusong.uc.passport.domain.TokenPayloadBo
     * @author weng xiaoyong
     * @date 2022/3/16 5:10 PM
     **/
    public static TokenPayloadBo getToken(){
        final TokenPayloadBo payload = TOKEN_REPOSITORY.get();
        if(Objects.nonNull(payload)){
            return payload;
        }
        throw new AuthorizationException(AuthorizationCodeEnum.AUTH_NOT_EXIST);
    }

    /**
     * javadoc removeToken
     * @apiNote 移除授权信息
     *
     * @author weng xiaoyong
     * @date 2022/3/16 5:10 PM
     **/
    public static void removeToken(){
        TOKEN_REPOSITORY.remove();
    }
}
