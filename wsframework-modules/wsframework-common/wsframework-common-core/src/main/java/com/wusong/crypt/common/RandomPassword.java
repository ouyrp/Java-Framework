package com.wusong.crypt.common;

import static com.wusong.crypt.common.AESEncryptor.randomBytes;

/**
 * @author p14
 */
public class RandomPassword {

    /**
     * 生成 length 长度的字节数组
     * @param length
     * @return  16进制。字符串长度是 length 的两倍。
     */
    public static String randomHex(int length) {
        return new String(Hex.encode(randomBytes(length)));
    }

    public static String randomHex() {
        return randomHex(32);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(randomHex());
        }
    }
}
