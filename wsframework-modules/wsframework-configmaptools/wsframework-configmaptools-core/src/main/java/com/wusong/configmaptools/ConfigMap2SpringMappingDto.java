package com.wusong.configmaptools;

import java.io.Serializable;

public class ConfigMap2SpringMappingDto implements Serializable {
    private String springKey;
    private String springValue;
    private String envKeys;

    public String getSpringKey() {
        return springKey;
    }

    public void setSpringKey(String springKey) {
        this.springKey = springKey;
    }

    public String getSpringValue() {
        return springValue;
    }

    public void setSpringValue(String springValue) {
        this.springValue = springValue;
    }

    public String getEnvKeys() {
        return envKeys;
    }

    public void setEnvKeys(String envKeys) {
        this.envKeys = envKeys;
    }
}
