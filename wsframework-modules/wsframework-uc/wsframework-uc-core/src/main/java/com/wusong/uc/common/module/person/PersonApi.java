package com.wusong.uc.common.module.person;

import com.wusong.uc.common.IApi;
import com.wusong.uc.person.domain.PersonBo;
import com.wusong.uc.person.domain.PersonCreateOrUpdateBo;
import com.wusong.web.dto.ApiResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface PersonApi extends IApi {


    /**
     * 根据userId查询客户信息
     *
     * @param accountId
     * @return
     */
    @GET(value = "/infra/uc/account/person/by-account-id")
    Call<ApiResult<PersonBo>> findByAccountId(@Query(value = "accountId") String accountId);


    /**
     * 创建/更新 自然人信息
     *
     * @param bo
     * @return
     */
    @POST(value = "/infra/uc/account/person/create-or-update")
    Call<ApiResult<PersonBo>> createOrUpdate(@Body PersonCreateOrUpdateBo bo);

    /**
     * javadoc listAuthAccountIdsByIdNumber
     *
     * @param idNumber 身份证件号
     * @param idType   身份证件类型
     * @return com.wusong.web.dto.ApiResult<java.util.List < java.lang.String>>
     * @apiNote 查询该身份下的认证账户id列表
     * @author weng xiaoyong
     * @date 2022/4/20 18:14
     **/
    @GET(value = "/infra/uc/account/person/account/ids")
    Call<ApiResult<List<String>>> listAuthAccountIdsByIdNumber(@Query(value = "idNumber") String idNumber,
                                                               @Query(value = "idType") String idType);
}
