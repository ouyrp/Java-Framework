package com.wusong.uc.config;


import com.wusong.uc.account.IAccountManager;
import com.wusong.uc.common.SdkConfigs;
import com.wusong.uc.common.enums.EnvEnum;
import com.wusong.uc.handler.DefaultTokenHandler;
import com.wusong.uc.handler.ITokenHandler;
import com.wusong.uc.passport.IPassportManager;
import com.wusong.uc.person.IPersonManager;
import com.wusong.uc.profile.IProfileManager;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.Locale;

@EnableConfigurationProperties(value = { UcSdkConfiguration.class})
public class UcSdkAutoConfiguration implements WebMvcConfigurer {

    @Setter(onMethod_ = @Autowired)
    private UcSdkConfiguration configuration;

    @Value(value = "${spring.profiles.active:DEV}")
    private String env;


    @PostConstruct
    public void init(){
        final String upperEnv = env.toUpperCase(Locale.ROOT);
        final EnvEnum envEnum;
        switch (upperEnv) {
            case "LOCAL":
            case "DEV":
                envEnum = EnvEnum.DEV;
                break;
            case "TEST":
                envEnum = EnvEnum.TEST;
                break;
            case "PROD":
            case "STAGING":
            case "MASTER":
                envEnum = EnvEnum.PROD;
                break;
            default:
                throw new RuntimeException("[" + this.env + "] 无法匹配 UC-SDK-STARTER 启动环境");
        }
        SdkConfigs.init(envEnum, configuration.getAppId());
    }

    /**
     * javadoc accountManager
     * @apiNote 账户相关组件
     *          starter 自己不用, 只是暴露出来给有需要的应用直接使用
     *
     * @return com.wusong.uc.account.IAccountManager
     * @author weng xiaoyong
     * @date 2022/3/18 2:56 PM
     **/
    @Bean(value = "accountManager")
    public IAccountManager accountManager(){
        return SdkConfigs.component(IAccountManager.class);
    }

    /**
     * javadoc personManager
     * @apiNote 自然人相关组件
     *          starter 自己不用, 只是暴露出来给有需要的应用直接使用
     *
     * @return com.wusong.uc.person.IPersonManager
     * @author weng xiaoyong
     * @date 2022/3/18 2:56 PM
     **/
    @Bean(value = "personManager")
    public IPersonManager personManager(){
        return SdkConfigs.component(IPersonManager.class);
    }

    /**
     * javadoc profileManager
     * @apiNote 账户画像相关组件
     *          starter 自己不用, 只是暴露出来给有需要的应用直接使用
     *
     * @return com.wusong.uc.profile.IProfileManager
     * @author weng xiaoyong
     * @date 2022/3/18 2:56 PM
     **/
    @Bean(value = "profileManager")
    public IProfileManager profileManager(){
        return SdkConfigs.component(IProfileManager.class);
    }

    /**
     * javadoc passportManager
     * @apiNote 认证相关组件
     *          starter 自己不用, 只是暴露出来给有需要的应用直接使用
     *
     * @return com.wusong.uc.passport.IPassportManager
     * @author weng xiaoyong
     * @date 2022/3/18 2:56 PM
     **/
    @Bean(value = "passportManager")
    public IPassportManager passportManager(){
        return SdkConfigs.component(IPassportManager.class);
    }

    /**
     * javadoc tokenHandler
     * @apiNote token处理器
     *          starter 自己不用, 只是暴露出来给有需要的应用直接使用
     *
     * @return com.wusong.uc.handler.ITokenHandler
     * @author weng xiaoyong
     * @date 2022/3/18 3:02 PM
     **/
    @Bean(value = "tokenHandler")
    @ConditionalOnMissingBean(ITokenHandler.class)
    public ITokenHandler tokenHandler(){
        return new DefaultTokenHandler();
    }

    /**
     * javadoc ucMvcConfig
     * @apiNote mvc
     *
     * @return com.wusong.uc.config.UcMvcConfig
     * @author weng xiaoyong
     * @date 2022/3/18 3:48 PM
     **/
    @Bean
    public UcMvcConfig ucMvcConfig(){
        return new UcMvcConfig();
    }
}
