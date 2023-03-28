package com.wusong.configcenter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

/**
 * @author wangshaoze
 */
@SpringBootTest(classes = TestApplication.class,properties = {
        "CMDB_SK=infra",
        "wsframework.cmdb.service.url=http://localhost:8080"
})
@Slf4j
public class BaseTest {

    private static final String SPRINGBOOT_APPLICATION_NAME = "spring.application.name";
    private static final String SPRINGBOOT_PROFILES_ACTIVE = "spring.profiles.active";
    private static final String CLUSTER = "apollo.cluster";

    static {
        System.setProperty(SPRINGBOOT_APPLICATION_NAME,"infra");
        System.setProperty(SPRINGBOOT_PROFILES_ACTIVE,"dev");
        System.setProperty(CLUSTER,"local");
    }

    @Autowired
    private Environment environment;

    @Test
    public void testEnv(){
        Assertions.assertEquals("bar",environment.getProperty("foo"));
        Assertions.assertEquals("false",environment.getProperty("ut.enable"));
    }

    @Test
    public void testEnvCMDB(){
//        Assertions.assertEquals("cmdb",environment.getProperty("cmdb.test.url"));
//        Assertions.assertEquals("cmdb",environment.getProperty("test.cmdb.mask.url"));
        Assertions.assertEquals("test api",environment.getProperty("this.is"));
    }

//    @Test
//    public void testEnv2(){
//        Assertions.assertEquals("local-env",environment.getProperty("test.cmdb.mask.url2"));
//    }

}
