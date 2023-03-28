package com.wusong.crypt.common;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author p14
 */
public class Signature {

    public static byte[] digest(byte[] data, String alg){
        try {
            MessageDigest instance = MessageDigest.getInstance(alg);
            return instance.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String digestHex(String data, String alg){
        return String.valueOf(Hex.encode(digest(data.getBytes(StandardCharsets.UTF_8),alg)));
    }

    public static String md5Hex(String data){
        return digestHex(data,"MD5");
    }

    public static String sha1Hex(String data){
        return digestHex(data,"SHA1");
    }

    public static byte[] hmac(byte[] key,byte[] data,String alg){
        try {
            Mac mac = Mac.getInstance(alg);
            mac.init(new SecretKeySpec(key,alg));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hmacHex(String key,String data,String alg){
        return String.valueOf(Hex.encode(hmac(key.getBytes(StandardCharsets.UTF_8),data.getBytes(StandardCharsets.UTF_8),alg)));
    }

    public static final String hmacMD5(String key,String data){
        return hmacHex(key,data,"HmacMD5");
    }

    public static final String hmacSHA1(String key,String data){
//        System.out.println("key "+key);
//        System.out.println("data "+data);
        return hmacHex(key,data,"HmacSHA1");
    }


}
