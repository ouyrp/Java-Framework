package com.wusong.uc.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wusong.uc.account.IAccountManager;
import com.wusong.uc.account.domain.enums.AccountRegisterSourceEnum;
import com.wusong.uc.common.enums.EnvEnum;
import com.wusong.uc.common.enums.SdkConfigKeyEnum;
import com.wusong.uc.common.exception.SdkNoneInitException;
import com.wusong.uc.common.module.ProductConfigBo;
import com.wusong.uc.common.module.pcs.ProductConfigApi;
import com.wusong.uc.common.module.person.PersonApi;
import com.wusong.uc.common.module.profile.UcProfileApi;
import com.wusong.uc.common.module.realperson.RealPersonApi;
import com.wusong.uc.passport.IPassportManager;
import com.wusong.uc.person.IPersonManager;
import com.wusong.uc.profile.IProfileManager;
import com.wusong.web.dto.ApiResult;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.*;

@SuppressWarnings(value = "unused")
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SdkConfigs {


    @Getter
    private static String appId;

    /**
     * javadoc findEnv
     *
     * @param profilesActive sb启动参数
     * @return com.wusong.uc.common.enums.EnvEnum
     * @apiNote 判断运行环境
     * @author weng xiaoyong
     * @date 2022/4/8 09:58
     **/
    public static EnvEnum findEnv(String profilesActive) {
        if (Objects.isNull(profilesActive)) {
            return null;
        }
        final String upperEnv = profilesActive.toUpperCase(Locale.ROOT);
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
            case "ONLINE":
                envEnum = EnvEnum.PROD;
                break;
            default:
                envEnum = null;
        }
        return envEnum;
    }

    private static final Map<SdkConfigKeyEnum, String> APP_CONFIGS = new EnumMap<>(SdkConfigKeyEnum.class);

    private static final Map<Class<? extends IApi>, Object> API = new HashMap<>();

    private static boolean init = false;

    private static final Map<Class<? extends IComponent>, Object> COMPONENTS = new HashMap<>();

    private static final Map<String, SdkConfig> CONFIGS = new HashMap<>();

    static {
        CONFIGS.put(
                EnvEnum.DEV.getEnv(),
                new SdkConfig()
                        .setUcProfileHost("http://platform-ucprofile.dev.svc.test.local:8080")
                        .setUcUniAuthHost("http://phoenix-uc-uniauth-service.dev.svc.test.local:8080")
                        .setPcsHost("http://infrastructure-productcenterservice.dev.svc.test.local:8080")
                        .setRealPersonHost("http://infrastructure-realpersonservice.dev.svc.test.local:8080")
                        .setUcAccountHost("http://infrastructure-ucaccountservice.dev.svc.test.local:8080")
        );
        CONFIGS.put(
                EnvEnum.TEST.getEnv(),
                new SdkConfig()
                        .setUcProfileHost("http://platform-ucprofile.test.svc.test.local:8080")
                        .setUcUniAuthHost("http://phoenix-uc-uniauth-service.test.svc.test.local:8080")
                        .setPcsHost("http://infrastructure-productcenterservice.test.svc.test.local:8080")
                        .setRealPersonHost("http://infrastructure-realpersonservice.test.svc.test.local:8080")
                        .setUcAccountHost("http://infrastructure-ucaccountservice.test.svc.test.local:8080")
        );
        CONFIGS.put(
                EnvEnum.PROD.getEnv(),
                new SdkConfig()
                        .setUcProfileHost("http://platform-ucprofile:8080")
                        .setUcUniAuthHost("http://phoenix-uc-uniauth-service:8080")
                        .setPcsHost("http://infrastructure-productcenterservice:8080")
                        .setRealPersonHost("http://infrastructure-realpersonservice:8080")
                        .setUcAccountHost("http://infrastructure-ucaccountservice:8080")
        );
    }

    private static boolean verify(String appId, EnvEnum env) {
        final ProductConfigApi productConfigApi = new Retrofit.Builder()
                .addConverterFactory(
                        JacksonConverterFactory.create(
                                new ObjectMapper()
                                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        )
                )
                .baseUrl(SdkConfigs.loadHostConfig(env).getPcsHost())
                .build()
                .create(ProductConfigApi.class);
        API.put(ProductConfigApi.class, productConfigApi);
        try {
            final ApiResult<Map<String, Object>> result = productConfigApi.loadConfig(appId).execute().body();
            if (Objects.isNull(result) || !result.successful() || Objects.isNull(result.getData())) {
                log.error("SdkConfigs.verify({}) invoke productConfigApi.loadConfig({}) failed, resp = [{}]", appId, appId, result);
                return false;
            }
            final Map<String, Object> config = result.getData();
            final ProductConfigBo bo = ProductConfigBo.fromMap(config);
            if (!"UC".equals(bo.getInfraApp())) {
                throw new SdkNoneInitException("UC-SDK AppId[" + appId + "] 无法接入UC");
            }
            APP_CONFIGS.put(SdkConfigKeyEnum.APP_ID, bo.getProductId());
            APP_CONFIGS.put(SdkConfigKeyEnum.AK, bo.getAk());
            APP_CONFIGS.put(SdkConfigKeyEnum.SK, bo.getSk());
            APP_CONFIGS.put(SdkConfigKeyEnum.SERVICE_NAME, bo.getServiceName());
            APP_CONFIGS.put(SdkConfigKeyEnum.PRODUCT_LINE, bo.getProductLine());
            APP_CONFIGS.put(SdkConfigKeyEnum.REGISTER_SOURCE, AccountRegisterSourceEnum.LC.getSource());
            APP_CONFIGS.put(SdkConfigKeyEnum.APP_NAME, bo.getAppName());
            if (log.isInfoEnabled()) {
                log.info("APP-ID[{}] INIT UC-SDK SUCCESSFULLY", appId);
            }
            return true;
        } catch (Exception ex) {
            log.error("SdkConfigs.verify({}-{}) exception: ", env, appId, ex);
            throw new RuntimeException(ex);
        }
    }

    public static synchronized void init(EnvEnum env, String appId) {
        if (!init) {
            if (!verify(appId, env)) {
                throw new SdkNoneInitException("UC-SDK 初始化失败[" + appId + "]-[" + env + "]");
            }
            final JacksonConverterFactory factory = JacksonConverterFactory.create(
                    new ObjectMapper()
                            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            );
            final UcProfileApi profileApi = new Retrofit.Builder()
                    .addConverterFactory(
                            factory
                    )
                    .baseUrl(SdkConfigs.loadHostConfig(env).getUcProfileHost())
                    .build()
                    .create(UcProfileApi.class);
            API.put(UcProfileApi.class, profileApi);
            final RealPersonApi realPersonApi = new Retrofit.Builder()
                    .addConverterFactory(factory)
                    .baseUrl(SdkConfigs.loadHostConfig(env).getRealPersonHost())
                    .build()
                    .create(RealPersonApi.class);
            API.put(RealPersonApi.class, realPersonApi);
            final PersonApi personApi = new Retrofit.Builder()
                    .addConverterFactory(factory)
                    .baseUrl(SdkConfigs.loadHostConfig(env).getUcAccountHost())
                    .build()
                    .create(PersonApi.class);
            API.put(PersonApi.class, personApi);
            COMPONENTS.put(IAccountManager.class, IAccountManager.instance());
            COMPONENTS.put(IPassportManager.class, IPassportManager.instance());
            COMPONENTS.put(IPersonManager.class, IPersonManager.instance());
            COMPONENTS.put(IProfileManager.class, IProfileManager.instance());
            SdkConfigs.appId = appId;
            init = true;
        }
    }


    /**
     * javadoc hasInit
     *
     * @return boolean
     * @apiNote 是否初始化
     * @author weng xiaoyong
     * @date 2022/3/1 11:43 AM
     **/
    public static boolean hasInit() {
        return init;
    }

    public static SdkConfig loadHostConfig(EnvEnum env) {
        return Objects.requireNonNull(CONFIGS.get(env.getEnv()));
    }

    public static String loadConfig(SdkConfigKeyEnum key) {
        if (!init) {
            throw new SdkNoneInitException();
        }
        return Objects.requireNonNull(APP_CONFIGS.get(key));
    }

    @SuppressWarnings(value = "unchecked")
    public static <T extends IComponent> T component(Class<T> clazz) {
        if (!init) {
            throw new SdkNoneInitException();
        }
        final T t = (T) COMPONENTS.get(clazz);
        return Objects.requireNonNull(t);
    }

    @SuppressWarnings(value = "unchecked")
    public static <T extends IApi> T api(Class<T> clazz) {
        if (!init) {
            throw new SdkNoneInitException();
        }
        final T t = (T) API.get(clazz);
        return Objects.requireNonNull(t);
    }

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class SdkConfig {

        private String ucProfileHost;

        private String ucUniAuthHost;

        private String pcsHost;

        private String realPersonHost;

        private String ucAccountHost;
    }

}
