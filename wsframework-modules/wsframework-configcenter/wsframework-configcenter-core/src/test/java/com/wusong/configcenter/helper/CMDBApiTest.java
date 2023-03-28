package com.wusong.configcenter.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author p14
 */
class CMDBApiTest {

    @Test
    void getAllConfig() {
        CMDBApi cmdbApi=new CMDBApi("ws-default-salt","infra-dev","infra","http://localhost:8080");
        System.out.println("x "+cmdbApi.getAllConfig("ws-cmdb-service","dev"));
        System.out.println("y "+cmdbApi.getAllConfig("0","dev"));
        System.out.println("y "+cmdbApi.getAllConfig("configcenter-sample","dev"));
    }
}