package com.wusong.uc.common.module.profile;

import com.galaxy.ws.spec.common.core.domain.RestResult;
import com.wusong.uc.common.IApi;
import com.wusong.uc.common.annos.UcSdkFakeApi;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface UcProfileApi extends IApi {

    /**
     * javadoc findUserByQuery
     * @apiNote 查找单个用户
     *
     * @param bo 查询条件
     * @return com.galaxy.ws.uc.profile.spi.domain.user.UserBo
     * @author weng xiaoyong
     * @date 2021/8/3 7:24 PM
     **/
    @POST(value = "/uc-profile-service/user/query")
    Call<RestResult<Map<String, Object>>> find(@Body Map<String, Object> bo);

    /**
     * javadoc delete
     * @apiNote 账号注销
     *
     * @param bo 注销信息
     * @return retrofit2.Call<com.galaxy.ws.spec.common.core.domain.RestResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/1 5:02 PM
     **/
    @PUT(value = "/uc-profile-service/user")
    Call<RestResult<Map<String, Object>>> delete(@Body Map<String, Object> bo);

    /**
     * javadoc sendVerifyCode
     * @apiNote 发送验证码(只用于登录、密码重置)
     *
     * @param bo 发送信息
     * @return com.galaxy.ws.spec.common.core.domain.RestResult<java.lang.Integer>
     * @author weng xiaoyong
     * @date 2022/3/1 6:34 PM
     **/
    @POST(value = "/uc-profile-service/identifying/code/send")
    Call<RestResult<Integer>> sendVerifyCode(@Body Map<String, Object> bo);

    /**
     * javadoc sendCustomIdentifyingCode
     * @apiNote 发送验证码(只用于登录、密码重置)
     *
     * @param bo 发送信息
     * @return com.galaxy.ws.spec.common.core.domain.RestResult<java.lang.Integer>
     * @author weng xiaoyong
     * @date 2022/3/1 6:34 PM
     **/
    @POST(value = "/uc-profile-service/custom/identifying/code/send")
    Call<RestResult<Integer>> sendCustomIdentifyingCode(@Body Map<String, Object> bo);

    /**
     * javadoc login
     * @apiNote 标准登录
     *
     * @param bo 登录信息
     * @return retrofit2.Call<com.galaxy.ws.spec.common.core.domain.RestResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/2 3:17 PM
     **/
    @POST(value = "/uc-profile-service/user/standard/login")
    Call<RestResult<Map<String, Object>>> login(@Body Map<String, Object> bo);

    /**
     * javadoc businessLogin
     * @apiNote 标准B端登录
     *
     * @param bo 登录信息
     * @return retrofit2.Call<com.galaxy.ws.spec.common.core.domain.RestResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/2 3:17 PM
     **/
    @POST(value = "/uc-profile-service/user/standard/business/login")
    Call<RestResult<Map<String, Object>>> businessLogin(@Body Map<String, Object> bo);

    /**
     * javadoc parseToken
     * @apiNote 令牌解析
     *
     * @param token 令牌
     * @return retrofit2.Call<com.galaxy.ws.spec.common.core.domain.RestResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/2 3:17 PM
     **/
    @GET(value = "/uc-profile-service/user/standard/token/parse")
    Call<RestResult<Map<String, Object>>> parseToken(@Query(value = "token") String token);

    /**
     * javadoc expireToken
     * @apiNote 令牌失效
     *
     * @param token 令牌
     * @return retrofit2.Call<com.galaxy.ws.spec.common.core.domain.RestResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/2 3:18 PM
     **/
    @GET(value = "/uc-profile-service/user/standard/token/expire")
    Call<RestResult<Map<String, Object>>> expireToken(@Query(value = "token") String token);

    /**
     * javadoc updatePassword
     * @apiNote 修改密码
     *
     * @param bo 密码信息
     * @return retrofit2.Call<com.galaxy.ws.spec.common.core.domain.RestResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/2 4:47 PM
     **/
    @PUT(value = "/uc-profile-service/user/standard/password/update")
    Call<RestResult<Map<String, Object>>> updatePassword(@Body Map<String, Object> bo);

    /**
     * javadoc noAuthUpdatePassword
     * @apiNote 无认证修改用户登录口令
     *
     * @param bo 更新信息
     * @return com.galaxy.ws.uc.profile.spi.domain.user.UserBo
     * @author weng xiaoyong
     * @date 2021/8/5 10:04 AM
     **/
    @PUT(value = "/uc-profile-service/user/no-auth/password")
    Call<RestResult<Map<String, Object>>> noAuthUpdatePassword(@Body Map<String, Object> bo);

    /**
     * javadoc bindingPhone
     * @apiNote 绑定用户手机号
     *
     * @param bo 绑定信息
     * @return com.galaxy.ws.uc.profile.spi.domain.user.UserBo
     * @author weng xiaoyong
     * @date 2021/8/3 8:10 PM
     **/
    @PUT(value = "/uc-profile-service/user/phone/binding")
    Call<RestResult<Map<String, Object>>> bindingPhone(@Body Map<String, Object> bo);

    /**
     * javadoc createAccount
     * @apiNote 账户注册, 尚未实现
     *          虚假的api
     *
     * @param bo 账户注册信息
     * @return retrofit2.Call<com.galaxy.ws.spec.common.core.domain.RestResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/4 11:50 AM
     **/
    @UcSdkFakeApi
    @POST(value = "/uc-profile-service/account/standard/create")
    Call<RestResult<Map<String, Object>>> createAccount(@Body Map<String, Object> bo);

    /**
     * javadoc createUser
     * @apiNote 用户注册
     *
     * @param bo 注册信息
     * @return retrofit2.Call<com.galaxy.ws.spec.common.core.domain.RestResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/4 2:21 PM
     **/
    @POST(value = "/uc-profile-service/user/standard/register")
    Call<RestResult<Map<String, Object>>> createUser(@Body Map<String, Object> bo);

    /**
     * javadoc updateUser
     * @apiNote 更新用户信息
     *
     * @param bo 用户信息
     * @return retrofit2.Call<com.galaxy.ws.spec.common.core.domain.RestResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/4 2:58 PM
     **/
    @PUT(value = "/uc-profile-service/user/standard/update")
    Call<RestResult<Map<String, Object>>> updateUser(@Body Map<String, Object> bo);

    /**
     * javadoc bindWx
     * @apiNote 绑定企微
     *
     * @param bo 信息
     * @return retrofit2.Call<com.galaxy.ws.spec.common.core.domain.RestResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/25 11:32
     **/
    @PUT(value = "/uc-profile-service/user/wx-union-id/binding")
    Call<RestResult<Map<String, Object>>> bindWx(@Body Map<String ,Object> bo);

    /**
     * javadoc bindWx
     * @apiNote 解除绑定企微
     *
     * @param bo 信息
     * @return retrofit2.Call<com.galaxy.ws.spec.common.core.domain.RestResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/25 11:32
     **/
    @PUT(value = "/uc-profile-service/user/wx-union-id/unbinding")
    Call<RestResult<Map<String, Object>>> unBindWx(@Body Map<String ,Object> bo);
}
