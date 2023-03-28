package com.wusong.jobcenter;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
public class XxlJobConfig {
    private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);
    //    @Autowired
//    private ApplicationProperties applicationProperties;
    @Value("${spring.application.name}")
    String applicationName;

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.executor.appname:}")
    private String appName;

    @Value("${xxl.job.executor.ip:}")
    private String ip;

    @Value("${xxl.job.executor.port:9999}")
    private int port;

    @Value("${xxl.job.accessToken:}")
    private String accessToken;

    @Value("${xxl.job.executor.logpath:./logs}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays:4}")
    private int logRetentionDays;

    @Autowired(required = false)
    private InetUtils inetUtils;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() throws UnknownHostException {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        String name = StringUtils.hasLength(appName) ? appName : applicationName;
        xxlJobSpringExecutor.setAppname(name);
        if (StringUtils.hasText(ip)) {
            xxlJobSpringExecutor.setIp(ip);
        } else if (inetUtils != null) {
            xxlJobSpringExecutor.setIp(inetUtils.findFirstNonLoopbackAddress().getHostAddress());
        }
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        if (StringUtils.hasText(logPath)) {
            xxlJobSpringExecutor.setLogPath(logPath);
        }
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        logger.info(">>>>>>>>>>> xxl-job config init. appName {} adminAddresses {}", name, adminAddresses);
        return xxlJobSpringExecutor;
    }
}