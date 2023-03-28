package com.wusong.uc.common.module.pcs;

import com.wusong.uc.common.IApi;
import com.wusong.web.dto.ApiResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Map;

/**
 * javadoc ProductConfigApi
 * <p>
 *     产品配置管理api
 *     pcs for product center service
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/3 12:17 PM
 * @version 1.0.0
 **/
public interface ProductConfigApi extends IApi {

    /**
     * javadoc loadConfig
     * @apiNote
     *
     * @param productId 产品id
     * @return retrofit2.Call<com.wusong.web.dto.ApiResult<java.util.Map<java.lang.String,java.lang.Object>>>
     * @author weng xiaoyong
     * @date 2022/3/3 12:21 PM
     **/
    @GET(value = "/pcs/product/config")
    Call<ApiResult<Map<String, Object>>> loadConfig(@Query(value = "productId") String productId);
}
