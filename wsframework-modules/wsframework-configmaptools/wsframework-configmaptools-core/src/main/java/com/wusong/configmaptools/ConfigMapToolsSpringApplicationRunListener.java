package com.wusong.configmaptools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import java.util.*;

public class ConfigMapToolsSpringApplicationRunListener implements SpringApplicationRunListener {
    private static final Logger log = LoggerFactory.getLogger(ConfigMapToolsSpringApplicationRunListener.class);
    private static final String SPRINGBOOT_APPLICATION_NAME = "spring.application.name";
    private static final String SPRINGBOOT_PROFILES_ACTIVE = "spring.profiles.active";
    private static final String SPRINGBOOT_CONFIGMAP_DISABLE = "wsframework.tools.configmap.enable";
    private static final String SPRINGBOOT_CONFIGMAP_AUTOWIRED = "wsframework.tools.configmap.autowired";
    private static final String SPRINGBOOT_CONFIGMAP_LEVEL = "wsframework.tools.configmap.level";
    private static final String SPRINGBOOT_CONFIGMAP_APPNAME = "wsframework.tools.configmap.appName";
    private ConfigMapToolsApi configMapToolsApi;

    private ObjectMapper objectMapper = new ObjectMapper();

    public ConfigMapToolsSpringApplicationRunListener(SpringApplication application, String[] args) {
    }

    public void environmentPrepared(ConfigurableEnvironment env) {
        String enable = env.getProperty(SPRINGBOOT_CONFIGMAP_DISABLE);
        if (enable == null || enable.equals("true")) {
            String active = getProfilesActive(env);
            String applicationName = getApplicationName(env);
            String autowired = env.getProperty(SPRINGBOOT_CONFIGMAP_AUTOWIRED);
            String level = env.getProperty(SPRINGBOOT_CONFIGMAP_LEVEL);
            Map<String, String> extraData = new HashMap<>();
            extraData.put("autowired", autowired);
            try {
                configMapToolsApi = new ConfigMapToolsApi(null, active, applicationName, extraData);
                Properties configMapData = new Properties();
                configMapToolsApi.getConfigmapEnvData().forEach((key, value) -> {
                    configMapData.put(key, value);
                });
                PropertySource propertySource = new PropertiesPropertySource("configMapTools", configMapData);
                if (level == null || "".equals(level) || level.equals("last")) {
                    env.getPropertySources().addBefore("CMDB", propertySource);
                } else if (level.equals("first")) {
                    env.getPropertySources().addFirst(propertySource);
                } else if (env.getPropertySources().stream().filter(d -> d.getName().equals(level)).findAny().orElse(null) != null) {
                    env.getPropertySources().addBefore(level, propertySource);
                } else {
                    env.getPropertySources().addBefore("CMDB", propertySource);
                }


                //自动组装配置
                String configForSpringStr = configMapToolsApi.getConfigForSpringStr();
                if (autowired == null || autowired.equalsIgnoreCase("true")) {
                    String projectName = env.getProperty("PROJECT");
                    if (projectName == null) {
                        log.error(" get configmap,not PROJECT env");
                    } else {
                        if (configForSpringStr != null) {
                            //替换项目名的占位符{{}}
                            Map<String, String> replaceKeys = new HashMap<>();
                            replaceKeys.put("projectUpper", projectName.replace("-", "").replace("_", "").toUpperCase(Locale.ROOT));
                            configForSpringStr = new StrSubstitutor(replaceKeys, "{{", "}}").replace(configForSpringStr);
                            List<ConfigMap2SpringMappingDto> configMap2SpringMappingDtoList = objectMapper.readValue(configForSpringStr, new TypeReference<List<ConfigMap2SpringMappingDto>>() {
                            });

                            configMap2SpringMappingDtoList.stream().forEach(d -> {
                                //不为空则处理
                                if (Arrays.stream(d.getEnvKeys().split(",")).filter(k -> env.getProperty(k) != null).findAny().orElse(null) != null) {
                                    configMapData.put(d.getSpringKey(), d.getSpringValue());
                                }
                            });
                        }
                    }
                }
            } catch (Exception e) {
                log.error("ConfigMapToolsSpringApplicationRunListener init error ", e);
            }
        } else {
            log.info("ConfigMapToolsSpringApplicationRunListener disabled ");
        }
    }

    private String getProfilesActive(ConfigurableEnvironment env) {
        String active = env.getProperty(SPRINGBOOT_PROFILES_ACTIVE, "");
        if (StringUtils.hasText(active)) {
            return active.split(",")[0];
        } else {
            throw new IllegalArgumentException("为了确保配置正确。请设置 " + SPRINGBOOT_PROFILES_ACTIVE);
        }
    }

    private String getApplicationName(ConfigurableEnvironment env) {
        String applicationName = env.getProperty(SPRINGBOOT_CONFIGMAP_APPNAME);
        if (!StringUtils.hasText(applicationName)) {
            applicationName = env.getProperty(SPRINGBOOT_APPLICATION_NAME);
        }
        if (!StringUtils.hasText(applicationName)) {
            throw new IllegalArgumentException("spring.application.name must be config");
        }
        return applicationName;
    }


    public void contextLoaded(ConfigurableApplicationContext context) {
    }

    public void contextPrepared(ConfigurableApplicationContext context) {
    }

    public void finished(ConfigurableApplicationContext context, Throwable exception) {
    }

    public void starting() {
    }

    public void failed(ConfigurableApplicationContext context, Throwable exception) {
    }
}
