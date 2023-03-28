package com.wusong.uc.person.domain;

import com.wusong.uc.person.domain.enums.AuthorizationTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * javadoc UserBo
 * <p>
 * 用户信息
 * <p>
 *
 * @author weng xiaoyong
 * @version 1.0.0
 * @date 2022/2/25 11:26 AM
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PersonBo {

    /**
     * 自然人id
     **/
    private String personId;

    /**
     * 自然人名称
     **/
    private String name;

    /**
     * 自然人身份证件号
     **/
    private String idCardNo;

    /**
     * 自然人身份证件类型
     **/
    private String idCardType;

    /**
     * 身份证件有效期开始时间
     * yyyy-MM-dd HH:mm:ss
     **/
    private String idCardExpirationStart;

    /**
     * 身份证件有效期结束时间
     * yyyy-MM-dd HH:mm:ss
     **/
    private String idCardExpirationEnd;

    /**
     * 手机号
     **/
    private String phone;

    /**
     * 认证因子列表
     **/
    private List<PersonAuthFactorBo> authFactors;

    /**
     * 认证时间
     * yyyy-MM-dd HH:mm:ss
     **/
    private String authTime;

    /**
     * 关联的账户id
     **/
    private String accountId;

    /**
     * 是否姓名、身份证好认证
     *
     * @return
     */
    public boolean hasVerifyNameAndIdName() {
        return verifyCheck(AuthorizationTypeEnum.TWO_FACTOR_ID_NAME);
    }

    /**
     * 是否人脸认证
     *
     * @return
     */
    public boolean hasVerifyFace() {
        return verifyCheck(AuthorizationTypeEnum.FACE);
    }

    private boolean verifyCheck(AuthorizationTypeEnum authorizationType) {
        PersonAuthFactorBo nameAndIdFactor = null;
        if (authFactors != null) {
            nameAndIdFactor = authFactors.stream().filter(d -> authorizationType.getType().equals(d.getAuthType())).findAny().orElse(null);
        }
        return nameAndIdFactor != null;
    }


    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class PersonAuthFactorBo {

        /**
         * 实人id
         */
        private String personId;

        /**
         * 认证类型
         */
        private String authType;

        /**
         * 提交认证时间
         * yyyy-MM-dd HH:mm:ss
         */
        private String authTime;

        /**
         * 认证提交任务id
         */
        private String authTaskId;
    }
}
