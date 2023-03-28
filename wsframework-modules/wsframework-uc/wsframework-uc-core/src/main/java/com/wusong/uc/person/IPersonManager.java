package com.wusong.uc.person;

import com.wusong.infrastructure.realperson.api.common.bo.*;
import com.wusong.uc.common.IComponent;
import com.wusong.uc.common.annos.UcSdkFuture;
import com.wusong.uc.person.domain.PersonBo;
import com.wusong.web.dto.ApiResult;

@UcSdkFuture
public interface IPersonManager extends IComponent {

    static IPersonManager instance() {
        return new PersonManagerImpl();
    }


    /**
     * 获取身份证ocr识别初始化参数
     *
     * @param userId      用户userId
     * @param businessKey 用于标识业务编号方便后期统计或者关联
     * @return
     */
    ApiResult<IdCardOcrSdkInitResponse> getIdCardOcrSdkInitParams(String userId, String businessKey);

    /**
     * 获取身份证ocr识别初始化参数
     *
     * @param params
     * @return
     */
    ApiResult<UploadIdCardInfoForH5Response> uploadIdCardInfoForH5(UploadIdCardInfoForH5Request params);

    /**
     * 活体检测完成后的跳转参数校验 并返回认证结果
     *
     * @param params
     * @return
     */
    ApiResult<VerificationResultCheckForH5Response> verificationResultCheckForH5(VerificationResultCheckForH5Request params);

    /**
     * 校验用户是否已经核验
     *
     * @param userId
     * @return
     */
    ApiResult<PersonBo> verifyCheck(String userId);


    /**
     * 二要素(姓名+身份证号) 核验是否被其他人使用
     *
     * @param name
     * @param idNo
     * @return
     */
    ApiResult<Boolean> idCardUsedByOther(String userId, String name, String idNo);


    ApiResult<Boolean> idCardVerification(String userId, String name, String idNo);
}
