package com.wusong.uc.common.module.realperson;

import com.wusong.infrastructure.realperson.api.common.bo.*;
import com.wusong.uc.common.IApi;
import com.wusong.web.dto.ApiResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RealPersonApi extends IApi {


    /**
     * 获取身份证ocr识别初始化参数
     *
     * @param params
     * @return
     */
    @POST(value = "/api/personVerification/idCardOcrSdkInit")
    Call<ApiResult<IdCardOcrSdkInitResponse>> getIdCardOcrSdkInitParams(@Body IdCardOcrSdkInitRequest params);

    /**
     * 上报身份证识别信息，并返回唤起活体检测的参数
     *
     * @param params
     * @return
     */
    @POST("/api/personVerification/uploadIdCardInfoForH5")
    Call<ApiResult<UploadIdCardInfoForH5Response>> uploadIdCardInfoForH5(@Body UploadIdCardInfoForH5Request params);

    /**
     * 活体检测完成后的跳转参数校验 并返回认证结果
     *
     * @param params
     * @return
     */
    @POST("/api/personVerification/verificationResultCheckForH5")
    Call<ApiResult<VerificationResultCheckForH5Response>> verificationResultCheckForH5(@Body VerificationResultCheckForH5Request params);

    /**
     * 二要素（姓名+身份证号核验）
     *
     * @param params
     * @return
     */
    @POST("/api/personVerification/verificationNameAndIdNo")
    Call<ApiResult<Boolean>> verificationNameAndIdNo(@Body VerificationNameAndIdNoRequest params);
}
