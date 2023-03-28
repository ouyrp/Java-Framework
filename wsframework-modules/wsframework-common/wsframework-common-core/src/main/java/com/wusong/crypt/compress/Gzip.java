package com.wusong.crypt.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author p14
 */
public class Gzip {

    public static byte[] gzip(byte[] data) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(bos);
            gzipOutputStream.write(data);
            gzipOutputStream.finish();
            gzipOutputStream.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] unGzip(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            GZIPInputStream gzipInputStream = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num = -1;
            while ((num = gzipInputStream.read(buf, 0, buf.length)) != -1) {
                bos.write(buf, 0, num);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
