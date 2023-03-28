package com.wusong.uc.person;

import com.wusong.infrastructure.realperson.api.common.bo.*;
import com.wusong.uc.common.SdkConfigs;
import com.wusong.uc.person.domain.PersonBo;
import com.wusong.web.dto.ApiResult;

public class Persons {

    /**
     * 获取身份证ocr识别初始化参数
     *
     * @param userId      业务系统用户userId 和uc一致
     * @param businessKey 用于标识业务编号方便后期统计或者关联
     * @return
     */
    public static ApiResult<IdCardOcrSdkInitResponse> getIdCardOcrSdkInitParams(String userId, String businessKey) {
        return SdkConfigs.component(IPersonManager.class).getIdCardOcrSdkInitParams(userId, businessKey);
    }


    /**
     * 上报身份证识别信息，并返回唤起活体检测的参数
     *
     * @param params
     * @return
     */
    public static ApiResult<UploadIdCardInfoForH5Response> uploadIdCardInfoForH5(UploadIdCardInfoForH5Request params) {
        return SdkConfigs.component(IPersonManager.class).uploadIdCardInfoForH5(params);
    }

    /**
     * 活体检测完成后的跳转参数校验
     *
     * @param params
     * @return
     */
    public static ApiResult<VerificationResultCheckForH5Response> verificationResultCheckForH5(VerificationResultCheckForH5Request params) {
        return SdkConfigs.component(IPersonManager.class).verificationResultCheckForH5(params);
    }

    /**
     * 根据用户Id查询是否已认证
     *
     * @param userId
     * @return
     */
    public static ApiResult<PersonBo> verifyCheck(String userId) {
        return SdkConfigs.component(IPersonManager.class).verifyCheck(userId);
    }


    /**
     * 二要素(姓名+身份证号) 核验
     *
     * @param name 姓名
     * @param idNo 身份证号
     * @return 请求状态码成功时 data=true:为已被其用户认证 data==false：未被认证
     */
    public static ApiResult<Boolean> idCardVerification(String userId, String name, String idNo) {
        return SdkConfigs.component(IPersonManager.class).idCardVerification(userId, name, idNo);
    }

}
