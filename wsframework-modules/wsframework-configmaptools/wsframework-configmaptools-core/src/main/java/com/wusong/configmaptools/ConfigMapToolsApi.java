package com.wusong.configmaptools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


public class ConfigMapToolsApi {

    private String env;
    private String appName;
    private String cmdbUrl = "https://wusongcmdbservice.wusong.com";
    private Map<String, String> configMapEnvData = new HashMap();
    private String configForSpringStr;
    private final String configMapPath = "/api/public/allConfigMap";
    private final String configMapForSpringPath = "/api/public/allConfigForSpring";


    public static final String CONFIG_MAP_ENV_DATA_KEY_APP = "APP";
    public static final String CONFIG_MAP_ENV_DATA_KEY_ENVIRONMENT = "ENVIRONMENT";
    public static final String CONFIG_MAP_ENV_DATA_KEY_GROUP = "GROUP";
    public static final String CONFIG_MAP_ENV_DATA_KEY_PROJECT = "PROJECT";

    private ObjectMapper objectMapper = new ObjectMapper();

    public ConfigMapToolsApi(String env, String appName) throws JsonProcessingException {
        this(null, env, appName, null);
    }

    public ConfigMapToolsApi(String cmdbUrl, String env, String appName, Map<String, String> extraData) throws JsonProcessingException {
        this.cmdbUrl = cmdbUrl == null ? this.cmdbUrl : cmdbUrl;
        this.env = env;
        this.appName = appName;
        Map<String, String> params = initSystemEnv();
        String timestamp = System.currentTimeMillis() + "";
        params.put("appName", appName);
        params.put("env", env);
        params.put("timestamp", timestamp);
        if (extraData != null) {
            params.putAll(extraData);
        }
        String sign = md5(appName + env + timestamp);
        params.put("sign", sign);
        String data = doPost(this.cmdbUrl + configMapPath, params);
        if (data != null) {
            Map<String, String> envData = objectMapper.readValue(data, Map.class);
            if (envData != null) {
                configMapEnvData = envData;
            }
        }
        configForSpringStr = doPost(this.cmdbUrl + configMapForSpringPath, params);
    }

    public Map<String, String> getConfigmapEnvData() {
        Map<String, String> originData = new HashMap<>();
        this.configMapEnvData.forEach((key, value) -> {
            originData.put(key, isEmpty(value) ? "" : decodeValue(value));
        });
        return originData;
    }

    public String getConfigForSpringStr() {
        return configForSpringStr;
    }

    private String decodeValue(String str) {
        byte[] a = str.getBytes(StandardCharsets.UTF_8);
        byte[] b = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = (byte) (a[i] + 1);
        }
        return new String(b);
    }

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    private Map<String, String> initSystemEnv() {
        Map<String, String> data = new HashMap();
        data.put(CONFIG_MAP_ENV_DATA_KEY_APP, System.getenv(CONFIG_MAP_ENV_DATA_KEY_APP));
        data.put(CONFIG_MAP_ENV_DATA_KEY_ENVIRONMENT, System.getenv(CONFIG_MAP_ENV_DATA_KEY_ENVIRONMENT));
        data.put(CONFIG_MAP_ENV_DATA_KEY_GROUP, System.getenv(CONFIG_MAP_ENV_DATA_KEY_GROUP));
        data.put(CONFIG_MAP_ENV_DATA_KEY_PROJECT, System.getenv(CONFIG_MAP_ENV_DATA_KEY_PROJECT));
        return data;
    }

    private String doPost(String httpUrl, Map<String, String> param) {
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(2000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(5000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/json");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer " + param.get("sign"));
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(objectMapper.writeValueAsBytes(param));
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                }
                result = sbf.toString();
            } else {
                System.err.println("ConfigMapApi connection error,responseCode:" + connection.getResponseCode());
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }

    private static String md5(String source) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            return "";
        }
        byte[] md5Bytes = md5.digest(source.getBytes(StandardCharsets.UTF_8));
        return bytes2Hex(md5Bytes);
    }

    private static String bytes2Hex(byte[] bytes) {
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            int val = ((int) bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static void main(String[] args) throws JsonProcessingException {
        ConfigMapToolsApi configMapToolsApi = new ConfigMapToolsApi("http://localhost:7070", "dev", "data-datamapservice", null);
        Object o = configMapToolsApi.getConfigmapEnvData();
    }
}
