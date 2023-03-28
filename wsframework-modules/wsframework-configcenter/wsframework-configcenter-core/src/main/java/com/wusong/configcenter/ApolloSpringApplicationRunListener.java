package com.wusong.configcenter;

import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.foundation.internals.provider.DefaultApplicationProvider;
import com.wusong.configcenter.helper.CMDBApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.Set;

@Order(value = 1)
@Slf4j
public class ApolloSpringApplicationRunListener implements SpringApplicationRunListener {

    private static final String APOLLO_APP_ID_KEY = "app.id";
    private static final String APOLLO_ENV_KEY = "env";
    private static final String APOLLO_BOOTSTRAP_ENABLE_KEY = "apollo.bootstrap.enabled";
    private static final String SPRINGBOOT_APPLICATION_NAME = "spring.application.name";
    private static final String SPRINGBOOT_PROFILES_ACTIVE = "spring.profiles.active";
    private static final String CONFIGCENTER_INFRA_NAMESPACE = "WS.INFRA";
    private static final String CONFIGCENTER_CMDB_NAMESPACE="WS.CMDB.JAVA";

    private static final String APOLLO_CLUSTER_KEY = "apollo.cluster";
    private static final String APOLLO_META_KEY = "apollo.meta";

    public ApolloSpringApplicationRunListener(SpringApplication application, String[] args) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment env) {
        Properties props = new Properties();
        props.put(APOLLO_BOOTSTRAP_ENABLE_KEY, true);
        System.setProperty("spring.banner.location", "classpath:META-INF/banner.txt");
        env.getPropertySources().addFirst(new PropertiesPropertySource("apolloConfig", props));
        // 初始化环境
        this.initApolloEnv(env);
        // 初始化cluster
        this.initApolloCluster(env);
        // 初始化appId
        this.initApolloAppId(env);
        // 初始化 CMDB 配置
        this.initCMDBConfig(env);
        // 初始化架构提供的默认配置
        this.initInfraConfig(env);
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }

    private void initApolloAppId(ConfigurableEnvironment env) {
        String applicationName = env.getProperty(SPRINGBOOT_APPLICATION_NAME);
        String apolloAppId = env.getProperty(APOLLO_APP_ID_KEY);
        if (ObjectUtils.isEmpty(apolloAppId)) {
            if (!ObjectUtils.isEmpty(applicationName)) {
                log.info("{}={}.", APOLLO_APP_ID_KEY, applicationName);
                System.setProperty(APOLLO_APP_ID_KEY, applicationName);
            } else {
                throw new IllegalArgumentException(
                        "Config center must config app.id in " + DefaultApplicationProvider.APP_PROPERTIES_CLASSPATH);
            }
        } else {
            log.info("{} is {}. But you don`t need do this. {} is preferred", APOLLO_APP_ID_KEY, apolloAppId, SPRINGBOOT_APPLICATION_NAME);
            System.setProperty(APOLLO_APP_ID_KEY, apolloAppId);
        }
    }

    private String getActiveProfile(ConfigurableEnvironment env){
        return env.getProperty(SPRINGBOOT_PROFILES_ACTIVE,"");
    }
    private String getApplicationName(ConfigurableEnvironment env){
        return env.getProperty(SPRINGBOOT_APPLICATION_NAME,"");
    }

    private void initApolloEnv(ConfigurableEnvironment env) {
        String active = getActiveProfile(env);
        if(StringUtils.hasText(active)){
            System.setProperty(APOLLO_ENV_KEY, getFirstProfile(active));
        }else {
            throw new IllegalArgumentException("为了确保配置正确。请设置 "+SPRINGBOOT_PROFILES_ACTIVE);
        }
    }

    private String getFirstProfile(String active){
        return active.split(",")[0];
    }

    private void initApolloCluster(ConfigurableEnvironment env) {
        String cluster = env.getProperty(APOLLO_CLUSTER_KEY);
        if (!ObjectUtils.isEmpty(cluster)) {
            System.setProperty(APOLLO_CLUSTER_KEY, cluster);
        }
    }

    private Properties infraProperties=new Properties();

    private void initInfraConfig(ConfigurableEnvironment env) {
        com.ctrip.framework.apollo.Config apolloConfig = ConfigService.getConfig(CONFIGCENTER_INFRA_NAMESPACE);
        Set<String> propertyNames = apolloConfig.getPropertyNames();
        if (propertyNames != null && propertyNames.size() > 0) {
            infraProperties = new Properties();
            for (String propertyName : propertyNames) {
                infraProperties.setProperty(propertyName, apolloConfig.getProperty(propertyName, null));
            }
            EnumerablePropertySource enumerablePropertySource =
                    new PropertiesPropertySource(CONFIGCENTER_INFRA_NAMESPACE, infraProperties);
            env.getPropertySources().addLast(enumerablePropertySource);
        }
    }


    private void addCMDBFrameworkWrapperConfig(ConfigurableEnvironment env) {
        com.ctrip.framework.apollo.Config apolloConfig = ConfigService.getConfig(CONFIGCENTER_CMDB_NAMESPACE);
        Set<String> propertyNames = apolloConfig.getPropertyNames();
        if (propertyNames != null && propertyNames.size() > 0) {
            Properties pro = new Properties();
            for (String propertyName : propertyNames) {
                pro.setProperty(propertyName, apolloConfig.getProperty(propertyName, null));
            }
            EnumerablePropertySource enumerablePropertySource =
                    new PropertiesPropertySource(CONFIGCENTER_CMDB_NAMESPACE, pro);
            env.getPropertySources().addLast(enumerablePropertySource);
        }
    }

    private void initCMDBConfig(ConfigurableEnvironment env) {
        if("false".equals(env.getProperty("wsframework.cmdb.enable"))) {
            log.warn("cmdb is not enabled");
        }else {
            String salt = env.getProperty("wsframework.cmdb.service.salt", "ws-default-salt");
            String ak = env.getProperty("wsframework.cmdb.service.ak", getDefaultAk(env));
            // dev 环境默认sk为infra，其它环境需要部署时指定
            String sk = env.getProperty("wsframework.cmdb.service.sk", env.getProperty("CMDB_SK", "infra"));
            String cmdbUrl = env.getProperty("wsframework.cmdb.service.url",env.getProperty("CMDB_URL", "https://wusongcmdbservice.wusong.com"));
            CMDBApi cmdbApi = new CMDBApi(salt, ak, sk, cmdbUrl);
            CMDBApi.setInstance(cmdbApi);
            resetApolloMeta(env,cmdbApi);
            addCmdbConfig(env,cmdbApi);
            addCMDBFrameworkWrapperConfig(env);
        }
    }
    private String getDefaultAk(ConfigurableEnvironment env){
        if(StringUtils.hasText(getActiveProfile(env))){
            return "infra-"+getFirstProfile(getActiveProfile(env));
        }else {
            return "infra";
        }
    }

    private void resetApolloMeta(ConfigurableEnvironment env,CMDBApi cmdbApi) {
        String configServer=cmdbApi.getConfigServer(getApplicationName(env),getActiveProfile(env));
        if(env.containsProperty(APOLLO_META_KEY)){
            log.error("{} 已设置。CMDB 指定地址[{}]不生效。",APOLLO_META_KEY,configServer);
        }else {
            System.setProperty(APOLLO_META_KEY,configServer);
        }
        log.info("{}={}",APOLLO_META_KEY,env.getProperty(APOLLO_META_KEY));
    }

    public void addCmdbConfig(ConfigurableEnvironment env,CMDBApi cmdbApi){
        Properties propertes = new Properties();
        propertes.putAll(cmdbApi.getAllConfig(getApplicationName(env), getActiveProfile(env)));
        EnumerablePropertySource enumerablePropertySource =
                new PropertiesPropertySource("CMDB", propertes);
        env.getPropertySources().addLast(enumerablePropertySource);

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void starting() {
        // 保留方法，为了兼容历史版本
    }

}
