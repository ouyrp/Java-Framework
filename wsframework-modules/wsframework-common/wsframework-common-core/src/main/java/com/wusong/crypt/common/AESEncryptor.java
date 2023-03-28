package com.wusong.crypt.common;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import static com.wusong.crypt.compress.Gzip.gzip;
import static com.wusong.crypt.compress.Gzip.unGzip;

/**
 * @author p14
 */
public class AESEncryptor {
    private static final String AES = "AES";
    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final String SECRETE_KEY_ALG = "PBKDF2WithHmacSHA1";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 96;
    private static final int KEY_ITERATION_COUNT = 1024;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final int keyLength;
    private final String salt;
    private final boolean compressGzip;

    private final Cipher encryptCipher;
    private final Cipher decryptCipher;

    public AESEncryptor(String salt) {
        this(128,salt,true);
    }

    /**
     * AESGCM 相对更安全。
     *
     * @param keyLength 必须是 128 196 256 中的一个。128已经足够安全。如果选择256，需确保JRE支持。
     * @param salt
     * @param compressGzip 对于纯文本内容，尤其是大json压缩率较高
     */
    public AESEncryptor(int keyLength, String salt,boolean compressGzip) {
        if(Objects.isNull(salt)||salt.length()<=0){
            throw new IllegalArgumentException("salt cant be null");
        }
        this.compressGzip=compressGzip;
        this.keyLength = keyLength;
        this.salt = salt;
        try {
            encryptCipher = Cipher.getInstance(AES_GCM);
            decryptCipher = Cipher.getInstance(AES_GCM);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * text 336
     * enc 272
     * @param s
     * @return
     */

    private byte[] decodeStringUTF8(String s){
        return Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8));
//        return Hex.decode(s);
    }
    private String encodeStringUTF8(byte[] bytes){
        return new String(Base64.getEncoder().encode(bytes),StandardCharsets.UTF_8);
//        return Hex.encode(bytes);
    }

    public String encryptGCM(String password, String data, String aad) {
        return encodeStringUTF8(encryptGCM(password, data.getBytes(StandardCharsets.UTF_8), aad == null ? null : aad.getBytes(StandardCharsets.UTF_8)));
    }

    public String decryptGCM(String password, String data, String aad) {
        return new String(decryptGCM(password, decodeStringUTF8(data), aad == null ? null : aad.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public String encryptGCM(String password, String data) {
        return encodeStringUTF8(encryptGCM(password, data.getBytes(StandardCharsets.UTF_8), null));
    }

    public String decryptGCM(String password, String data) {
        return new String(decryptGCM(password, decodeStringUTF8(data), null), StandardCharsets.UTF_8);
    }

    public byte[] encryptGCM(String password, byte[] data, byte[] aad) {
        return encryptGCM(getGCMKey(password), data, aad);
    }

    public byte[] decryptGCM(String password, byte[] data, byte[] aad) {
        return decryptGCM(getGCMKey(password), data, aad);
    }


    public byte[] getGCMKey(String password) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(SECRETE_KEY_ALG);
            return skf.generateSecret(new PBEKeySpec(password.toCharArray(),salt.getBytes(StandardCharsets.UTF_8), KEY_ITERATION_COUNT, keyLength)).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized byte[] encryptGCM(byte[] key, byte[] data, byte[] aad) {
        // iv每次重新生成，并放到密文头部
        byte[] iv = randomBytes(GCM_IV_LENGTH / 8);
        try {
            // 128位tag会自动追加到密文尾部
            encryptCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, AES), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            if (aad != null) {
                encryptCipher.updateAAD(aad);
            }
            byte[] cipherData = encryptCipher.doFinal(compressGzip?gzip(data):data);
            ByteBuffer byteBuffer = ByteBuffer.allocate(4+4 + iv.length + cipherData.length);
            byteBuffer.putInt(compressGzip?1:0);
            byteBuffer.putInt(iv.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherData);
            return byteBuffer.array();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized byte[] decryptGCM(byte[] key, byte[] data, byte[] aad) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        int compressFlag = byteBuffer.getInt();
        int ivLength = byteBuffer.getInt();
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);
        byte[] cipherData = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherData);
        try {
            decryptCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, AES), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            if (aad != null) {
                decryptCipher.updateAAD(aad);
            }
            return compressFlag==1?unGzip(decryptCipher.doFinal(cipherData)):decryptCipher.doFinal(cipherData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static byte[] randomBytes(int length) {
        byte[] key = new byte[length];
        SECURE_RANDOM.nextBytes(key);
        return key;
    }

}
