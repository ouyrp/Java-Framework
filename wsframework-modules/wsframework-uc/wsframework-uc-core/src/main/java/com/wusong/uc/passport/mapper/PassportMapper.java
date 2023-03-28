package com.wusong.uc.passport.mapper;

import com.galaxy.ws.spec.common.core.datetime.DateTimes;
import com.wusong.uc.passport.domain.BusinessLoginRequestBo;
import com.wusong.uc.passport.domain.LoginRequestBo;
import com.wusong.uc.passport.domain.TokenPayloadBo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * javadoc PassportMapper
 * <p>
 *     passport 相关数据mapper
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/2 3:18 PM
 * @version 1.0.0
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings(value = "Duplicates")
public class PassportMapper {

    public static Map<String, Object> loginBo2map(LoginRequestBo bo){
        final Map<String, Object> params = new HashMap<>();
        params.put("account", bo.getAccount());
        if(Objects.nonNull(bo.getAccountType())){
            params.put("accountType", bo.getAccountType().getType());
        }
        params.put("systemCode", bo.getSystemCode().getCode());
        params.put("identifyingCode", bo.getVerifyCode());
        params.put("password", bo.getPassword());
        params.put("endpoint", bo.getEndpoint().getEndpoint());
        params.put("deviceId", bo.getDeviceId());
        params.put("expire", bo.getExpire());
        return params;
    }

    public static Map<String, Object> businessLoginBo2map(BusinessLoginRequestBo bo){
        final Map<String, Object> params = new HashMap<>();
        params.put("account", bo.getAccount());
        if(Objects.nonNull(bo.getAccountType())){
            params.put("accountType", bo.getAccountType().getType());
        }
        params.put("merchantId", bo.getMerchantId());
        params.put("accountId", bo.getAccountId());
        params.put("systemCode", bo.getSystemCode().getCode());
        params.put("identifyingCode", bo.getVerifyCode());
        params.put("password", bo.getPassword());
        params.put("endpoint", bo.getEndpoint().getEndpoint());
        params.put("deviceId", bo.getDeviceId());
        params.put("expire", bo.getExpire());
        return params;
    }

    public static TokenPayloadBo map2TokenBo(Map<String, Object> map){

        final TokenPayloadBo bo = new TokenPayloadBo()
                .setTokenId((String) map.get("tokenId"))
                .setAccountId((String) map.get("accountId"))
                .setAccountName((String) map.get("accountName"))
                .setAccountType((Integer) map.get("accountType"))
                .setProfileId((String) map.get("profileId"))
                .setSystemCode((String) map.get("systemCode"))
                .setDeviceId((String) map.get("deviceId"))
                .setMerchantId((String) map.get("merchantId"))
                .setExtraParam((String) map.get("extraParam"))
                .setCts((String) map.get("cts"))
                .setUts((String) map.get("uts"))
                .setEts((String) map.get("ets"))
                .setAvailable(1)
                ;
        final Integer tokenStatus = (Integer) map.get("tokenStatus");
        if(Objects.isNull(tokenStatus) || tokenStatus > 0){
            bo.setAvailable(0);
        }else {
            if(DateTimes.toDatetime(bo.getEts()).isBefore(LocalDateTime.now())){
                bo.setAvailable(1);
            }
        }
        return bo;
    }
}
