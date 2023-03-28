package com.wusong.monitoring.metric.micrometer;

import io.micrometer.core.instrument.Tag;
import org.springframework.beans.factory.annotation.Value;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlatformTag {

    @Value("${spring.profiles.active:none}")
    private String activeProfiles;
    @Value("${spring.application.name:none}")
    private String applicationName;

    private List<Tag> tags;

    public PlatformTag() {
    }
    public List<Tag> getTags() {
        if (tags == null) {
            tags=new ArrayList<>();
            tags.addAll(Arrays.asList(
                    Tag.of("application.profiles.active", activeProfiles.toLowerCase()),
                    Tag.of("ip", getIp())));
            tags.addAll(Arrays.asList(Tag.of("application.name",applicationName)
                    ));
        }
        return Collections.unmodifiableList(tags);
    }

    private String getIp() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "None";
        }
    }


}
