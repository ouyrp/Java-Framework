package com.wusong.uc.profile.mapper;

import com.wusong.uc.common.SdkConfigs;
import com.wusong.uc.common.enums.SdkConfigKeyEnum;
import com.wusong.uc.profile.domain.ProfileBo;
import com.wusong.uc.profile.domain.ProfileUpdateBo;
import com.wusong.uc.profile.domain.enums.ProfileSystemEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.maven.shared.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileMapper {

    public static Map<String, Object> buildUpdateParams(ProfileUpdateBo bo){
        final Map<String, Object> params = new HashMap<>();
        params.put("userId", bo.getProfileId());
        params.put("nickname", bo.getNickname());
        if(Objects.nonNull(bo.getGender())){
            params.put("gender", bo.getGender().getGender());
        }
        params.put("photo", bo.getPhoto());
        params.put("provinceCode", bo.getProvinceCode());
        params.put("cityCode", bo.getCityCode());
        params.put("address", bo.getAddress());
        params.put("serviceName", SdkConfigs.loadConfig(SdkConfigKeyEnum.SERVICE_NAME));
        return params;
    }

    public static ProfileBo buildUserBo(Map<String, Object> map){
        final String name;
        final String nickName = (String)map.get("nickName");
        if(StringUtils.isNotEmpty(nickName)){
            name = nickName;
        }else{
            name = (String) map.get("name");
        }
        return new ProfileBo()
                .setProfileId((String) map.get("userId"))
                .setAccountId((String) map.get("userId"))
                .setNickname(name)
                .setSystemCode(ProfileSystemEnum.UC.getSystem())
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
    }
}
