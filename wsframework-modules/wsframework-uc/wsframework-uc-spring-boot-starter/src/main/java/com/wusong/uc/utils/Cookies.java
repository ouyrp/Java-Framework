package com.wusong.uc.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.maven.surefire.shared.compress.utils.Lists;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * javadoc Cookies
 * <p>
 *     操作cookie工具类
 * <p>
 * @author weng xiaoyong
 * @date 2022/3/16 7:11 PM
 * @version 1.0.0
 **/
@SuppressWarnings(value = "unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cookies {

    public static List<String> list(String key){
        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(Objects.isNull(attributes)) {
            return Lists.newArrayList();
        }
        return list(key, attributes.getRequest());
    }

    public static List<String> list(String key, HttpServletRequest request){
        final List<String> values = Lists.newArrayList();
        if(Objects.isNull(key) || key.isEmpty()){
            return values;
        }
        final Cookie[] cookies = request.getCookies();
        if(Objects.nonNull(cookies) && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(key.equals(cookie.getName())){
                    final String value = cookie.getValue();
                    if(Objects.nonNull(value)){
                        values.add(cookie.getValue());
                    }
                }
            }
        }
        return values;
    }

    public static String findFirst(String key){
        final List<String> values = list(key);
        if(values.isEmpty()){
            return null;
        }
        return values.get(0);
    }

    public static String findFirst(String key, HttpServletRequest request){
        final List<String> values = list(key, request);
        if(values.isEmpty()){
            return null;
        }
        return values.get(0);
    }

    public static String findLast(String key){
        final List<String> values = list(key);
        if(values.isEmpty()){
            return null;
        }
        return values.get(values.size() - 1);
    }

    public static String findLast(String key, HttpServletRequest request){
        final List<String> values = list(key, request);
        if(values.isEmpty()){
            return null;
        }
        return values.get(values.size() - 1);
    }
}
