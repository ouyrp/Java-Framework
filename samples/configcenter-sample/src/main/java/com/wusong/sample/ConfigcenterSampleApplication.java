package com.wusong.sample;

import com.wusong.configcenter.helper.CMDBApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;

/**
 * @author p14
 */
@SpringBootApplication
@Slf4j
public class ConfigcenterSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigcenterSampleApplication.class, args);
    }

    @Value("${demo.user.name:}")
    private String userNameFromApollo;
    @Autowired
    private Environment environment;

    @PostConstruct
    public void init(){
        log.info("========== 应用自己的配置 ========== ");
        log.info("userNameFromApollo = {}",userNameFromApollo);
        log.info("========== 应用自己的配置 END ========== ");

        log.info("========== Spring 能识别的配置（框架植入） ========== ");
        Arrays.asList("spring.datasource.url",
                "spring.datasource.username",
                "spring.datasource.password",
                "xxl.job.admin.addresses",
                "spring.redis.host",
                "spring.redis.port",
                "spring.redis.password",
                "spring.elasticsearch.rest.uris",
                "spring.elasticsearch.rest.username",
                "spring.elasticsearch.rest.password",
                "spring.kafka.bootstrapServers",
                "spring.data.mongodb.host",
                "spring.data.mongodb.port",
                "spring.data.mongodb.database",
                "spring.data.mongodb.username",
                "spring.data.mongodb.password",
                "apollo.meta"
                ).forEach(k->{
            log.info("{} = {}",k,environment.getProperty(k));
        });
        log.info("========== Spring 能识别的配置（框架植入）END ========== ");

        log.info("========== CMDB 返回的配置（不要用这些！！！，这里只是展示） ========== ");
        Map<String, String> config = CMDBApi.getInstance().getAllConfig(environment.getProperty("spring.application.name"), environment.getProperty("spring.profiles.active"));
        config.forEach((k,v)->{
            log.info("CMDB==> {}={}",k,v);
        });
        log.info("========== CMDB 返回的配置（不要用这些！！！，这里只是展示）END ========== ");

    }
}
