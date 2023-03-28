package com.wusong.uc.person.domain;

import com.galaxy.ws.spec.common.core.param.Option;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * javadoc PersonQueryBo
 * <p>
 *     人的信息查询bo
 * <p>
 * @author weng xiaoyong
 * @date 2022/2/28 2:28 PM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class PersonQueryBo {

    /**
     * 个人id
     **/
    @Option
    private String personId;

    /**
     * 账号id
     **/
    @Option
    private String accountId;

    /**
     * 账号类型
     **/
    @Option
    private Integer accountType;

}
