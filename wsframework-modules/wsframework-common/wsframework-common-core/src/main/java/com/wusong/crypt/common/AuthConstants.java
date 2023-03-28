package com.wusong.crypt.common;

import java.time.format.DateTimeFormatter;

/**
 * @author p14
 */
public class AuthConstants {
    public static final String HEADER_AK ="x-ws-ak";
    public static final String HEADER_SIGN_BODY="x-ws-sign-body";
    public static final String HEADER_TIME ="x-ws-time";
    public static final String HEADER_ENV ="x-ws-env";
    public static final String HEADER_SIGN ="x-ws-sign";
    public static final DateTimeFormatter TIME_FORMATTER=DateTimeFormatter.ISO_OFFSET_DATE_TIME;
}
