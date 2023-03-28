package com.wusong.uc.person;

import com.wusong.infrastructure.realperson.api.common.bo.*;
import com.wusong.uc.common.BaseManagerImpl;
import com.wusong.uc.common.SdkConfigs;
import com.wusong.uc.common.module.person.PersonApi;
import com.wusong.uc.common.module.realperson.RealPersonApi;
import com.wusong.uc.person.domain.PersonBo;
import com.wusong.uc.person.domain.PersonCreateOrUpdateBo;
import com.wusong.uc.person.domain.enums.AuthorizationTypeEnum;
import com.wusong.uc.person.domain.enums.IdTypeEnum;
import com.wusong.web.dto.ApiResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonManagerImpl extends BaseManagerImpl implements IPersonManager {

    @Override
    public ApiResult<IdCardOcrSdkInitResponse> getIdCardOcrSdkInitParams(String userId, String businessKey) {
        return execute(SdkConfigs.api(RealPersonApi.class).getIdCardOcrSdkInitParams(IdCardOcrSdkInitRequest.builder()
                .appId(SdkConfigs.getAppId())
                .userId(userId)
                .businessKey(businessKey)
                .build()));
    }

    @Override
    public ApiResult<UploadIdCardInfoForH5Response> uploadIdCardInfoForH5(UploadIdCardInfoForH5Request params) {
        params.setAppId(SdkConfigs.getAppId());
        ApiResult<Boolean> checkExists = idCardUsedByOther(params.getUserId(), params.getName(), params.getIdNo());
        if (!checkExists.successful()) {
            return ApiResult.error(checkExists.getCode(), checkExists.getMessage());
        }
        if (checkExists.getData()) {
            return ApiResult.error("-1", "身份信息已被其他用户认证");
        }
        return execute(SdkConfigs.api(RealPersonApi.class).uploadIdCardInfoForH5(params));
    }

    @Override
    public ApiResult<VerificationResultCheckForH5Response> verificationResultCheckForH5(VerificationResultCheckForH5Request params) {
        params.setAppId(SdkConfigs.getAppId());
        //先核验是否认证
        ApiResult<VerificationResultCheckForH5Response> result = execute(SdkConfigs.api(RealPersonApi.class).verificationResultCheckForH5(params));
        //未核验成功过则通知person
        if (!result.successful()) {
            return result;
        }
        //通知uc认证结果
        VerificationResultCheckForH5Response.CardInfo cardInfo = result.getData().getCardInfo();
        PersonCreateOrUpdateBo personCreateOrUpdateBo = new PersonCreateOrUpdateBo();
        personCreateOrUpdateBo.setAccountId(result.getData().getUserId());
        personCreateOrUpdateBo.setAuthTaskId(result.getData().getFlowNo());
        personCreateOrUpdateBo.setAuthTypes(Stream.of(AuthorizationTypeEnum.TWO_FACTOR_ID_NAME.getType(), AuthorizationTypeEnum.FACE.getType()).collect(Collectors.toList()));
        personCreateOrUpdateBo.setName(cardInfo.getName());
        personCreateOrUpdateBo.setIdCardNo(cardInfo.getIdNo());
        personCreateOrUpdateBo.setIdCardType(cardInfo.getIdType());
        //通知person 修改认证信息
        ApiResult<PersonBo> restResult = execute(SdkConfigs.api(PersonApi.class).createOrUpdate(personCreateOrUpdateBo));
        if (restResult.successful()) {
            return result;
        } else {
            return ApiResult.error(100, restResult.getMessage());
        }
    }

    @Override
    public ApiResult<PersonBo> verifyCheck(String userId) {
        ApiResult<PersonBo> result = execute(SdkConfigs.api(PersonApi.class).findByAccountId(userId));
        if (result.successful()) {
            if (result.getData() == null) {
                result.setData(new PersonBo());
            }
        }
        return result;
    }

    @Override
    public ApiResult<Boolean> idCardUsedByOther(String userId, String name, String idNo) {
        ApiResult<List<String>> result = execute(SdkConfigs.api(PersonApi.class).listAuthAccountIdsByIdNumber(idNo, IdTypeEnum.ID_CARD.getType()));
        if (result.successful()) {
            return ApiResult.ok(result.getData() != null && result.getData().stream().filter(d -> d.equals(userId)).findAny().orElse(null) != null);
        }
        return ApiResult.error(result.getCode(), result.getMessage());
    }

    @Override
    public ApiResult<Boolean> idCardVerification(String userId, String name, String idNo) {
        ApiResult<Boolean> checkOtherResult = idCardUsedByOther(userId, name, idNo);
        if (!checkOtherResult.successful()) {
            return ApiResult.error(-1, checkOtherResult.getMessage());
        }
        if (checkOtherResult.getData()) {
            return ApiResult.error(-2, "已被其他用户认证过");
        }
        return execute(SdkConfigs.api(RealPersonApi.class).verificationNameAndIdNo(VerificationNameAndIdNoRequest.builder()
                .appId(SdkConfigs.getAppId())
                .userId(userId)
                .name(name)
                .idNo(idNo)
                .build()));

    }
}
