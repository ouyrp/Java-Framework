package com.wusong.uc.account.mapper;

import com.wusong.uc.account.domain.AccountBo;
import com.wusong.uc.account.domain.AccountRegisterBo;
import com.wusong.uc.account.domain.LoginAccountBo;
import com.wusong.uc.account.domain.enums.AccountStatusEnum;
import com.wusong.uc.account.domain.enums.AccountTypeEnum;
import com.wusong.uc.common.SdkConfigs;
import com.wusong.uc.common.enums.SdkConfigKeyEnum;
import com.wusong.uc.profile.domain.ProfileBo;
import com.wusong.uc.profile.domain.enums.ProfileSystemEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.maven.shared.utils.StringUtils;

import java.util.*;

/**
 * javadoc AccountMapper
 * <p>
 *     账号数据结构mapper
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/28 9:58 AM
 * @version 1.0.0
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMapper {

    /**
     * javadoc buildRegisterParams
     * @apiNote 账号注册参数构建
     *
     * @param bo 注册信息
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author weng xiaoyong
     * @date 2022/3/14 10:16 AM
     **/
    public static Map<String, Object> buildRegisterParams(AccountRegisterBo bo){
        final String registerSource = SdkConfigs.loadConfig(SdkConfigKeyEnum.REGISTER_SOURCE);
        final Map<String, Object> params = new HashMap<>();
        params.put("accountId", bo.getAccountId());
        params.put("account", bo.getAccount());
        if(Objects.nonNull(bo.getAccountType())){
            params.put("accountType", bo.getAccountType().getType());
        }
        params.put("identifyingCode", bo.getVerifyCode());
        params.put("registerSource", registerSource);
        params.put("userSystem", ProfileSystemEnum.UC.getSystem());
        params.put("nickname", bo.getNickname());
        if(Objects.nonNull(bo.getGender())){
            params.put("gender", bo.getGender().getGender());
        }
        params.put("photo", bo.getPhotoUrl());
        params.put("provinceCode", bo.getProvinceCode());
        params.put("cityCode", bo.getCityCode());
        params.put("address", bo.getAddress());
        params.put("phone", bo.getPhone());
        params.put("email", bo.getEmail());
        params.put("password", bo.getPassword());
        params.put("endpoint", bo.getEndpoint().getEndpoint());
        params.put("serviceName", SdkConfigs.loadConfig(SdkConfigKeyEnum.SERVICE_NAME));
        return params;
    }

    public static AccountBo map2bo(Map<String, Object> map){
        if(Objects.isNull(map) || map.isEmpty()){
            return null;
        }
        final String name;
        final String nickName = (String)map.get("nickName");
        if(StringUtils.isNotEmpty(nickName)){
            name = nickName;
        }else{
            name = (String) map.get("name");
        }

        final ProfileBo profile = new ProfileBo()
                .setProfileId((String) map.get("userId"))
                .setAccountId((String) map.get("userId"))
                .setNickname(name)
                .setGender((Integer) map.get("gender"))
                .setPhoto((String) map.get("photo"))
                .setProvinceCode((String) map.get("provinceCode"))
                .setCityCode((String) map.get("cityCode"))
                .setAddress((String) map.get("address"))
                .setPhone((String) map.get("phone"))
                .setEmail((String) map.get("email"))
                .setCts((String) map.get("cts"))
                .setUts((String) map.get("uts"))
                ;
        final List<LoginAccountBo> accounts = new ArrayList<>();
        if(Objects.nonNull(profile.getPhone()) && !profile.getPhone().isEmpty()){
            accounts.add(
                    new LoginAccountBo()
                            .setAccountName(profile.getPhone())
                            .setAccountType(AccountTypeEnum.PHONE.getType())
                            .setAccountStatus(AccountStatusEnum.NORMAL.getStatus())
            );
        }else if(Objects.nonNull(profile.getEmail()) && !profile.getEmail().isEmpty()){
            accounts.add(
                    new LoginAccountBo()
                            .setAccountName(profile.getEmail())
                            .setAccountType(AccountTypeEnum.EMAIL.getType())
                            .setAccountStatus(AccountStatusEnum.NORMAL.getStatus())
            );
        }
        return new AccountBo()
                .setAccountId((String)map.get("userId"))
                .setAccounts(accounts)
                // 现在所有账号都是正常使用状态
                .setStatus(AccountStatusEnum.NORMAL.getStatus())
                .setRegisterTime((String) map.get("registerDate"))
                .setProductLine((String) map.get("productLine"))
                .setRegisterEndpoint((String)map.get("registerEndpoint"))
                .setLastLoginTime((String) map.get("lastLoginTime"))
                .setProfile(profile)
                ;
    }
}
