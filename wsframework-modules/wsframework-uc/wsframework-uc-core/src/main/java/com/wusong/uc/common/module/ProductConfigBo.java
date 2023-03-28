package com.wusong.uc.common.module;

import com.galaxy.ws.spec.common.core.param.Require;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * javadoc ProductConfigBo
 * <p>
 *     产品配置
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/3 10:31 AM
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ProductConfigBo implements Serializable {

    /**
     * 产品id
     **/
    private String productId;

    /**
     * 产品名
     */
    @Require
    private String productName;

    /**
     * 产品线
     **/
    @Require
    private String productLine;

    /**
     * 项目名
     */
    @Require
    private String projectName;

    /**
     * 应用名
     */
    @Require
    private String appName;

    /**
     * 服务名称
     */
    @Require
    private String serviceName;

    /**
     * 申请接入的基础应用名
     */
    @Require
    private String infraApp;

    /**
     * 接入ak
     */
    @Require
    private String ak;

    /**
     * 接入sk
     */
    @Require
    private String sk;

    /**
     * 备注
     */
    private String comments;

    /**
     * 创建时间
     * yyyy-MM-dd HH:mm:ss
     */
    private String cts;

    /**
     * 更新时间
     * yyyy-MM-dd HH:mm:ss
     */
    private String uts;

    public static ProductConfigBo fromMap(Map<String, Object> map){
        return new ProductConfigBo()
                .setProductId((String) map.get("productId"))
                .setProductName((String) map.get("productName"))
                .setProjectName((String) map.get("projectName"))
                .setAppName((String) map.get("appName"))
                .setServiceName((String) map.get("serviceName"))
                .setInfraApp((String) map.get("infraApp"))
                .setAk((String) map.get("ak"))
                .setSk((String) map.get("sk"))
                .setComments((String) map.get("comments"))
                .setCts((String) map.get("cts"))
                .setUts((String) map.get("uts"))
                ;
    }
}

