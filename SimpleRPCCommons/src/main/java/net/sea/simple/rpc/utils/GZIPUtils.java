package net.sea.simple.rpc.utils;

import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 字符串压缩与解压缩
 *
 * @author chengwu.hua
 * @Date 2018/5/7 13:32
 * @Version 1.0
 */

public class GZIPUtils {

    /**
     * 字符串压缩为GZIP字节数组
     *
     * @param str
     * @return
     */
    public static byte[] compress(String str) {
        return compress(str, CommonConstants.DEFAULT_CHARSET_STR);
    }

    /**
     * 字符串压缩为GZIP字节数组
     *
     * @param str
     * @param encoding
     * @return
     */
    public static byte[] compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out);) {
            gzip.write(str.getBytes(encoding));
            return out.toByteArray();
        } catch (IOException e) {
            throw new RPCServerRuntimeException("compress string fail", e);
        }
    }

    /**
     * GZIP解压缩
     *
     * @param bytes
     * @return
     */
    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream in = new ByteArrayInputStream(bytes);) {
            GZIPInputStream unGzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int len;
            while ((len = unGzip.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RPCServerRuntimeException("uncompress string fail", e);
        }
    }

    /**
     * 解压缩为字符串
     *
     * @param bytes
     * @return
     */
    public static String uncompressToString(byte[] bytes) {
        return uncompressToString(bytes, CommonConstants.DEFAULT_CHARSET_STR);
    }

    /**
     * 解压缩为字符串
     *
     * @param bytes
     * @param encoding
     * @return
     */
    public static String uncompressToString(byte[] bytes, String encoding) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream in = new ByteArrayInputStream(bytes);) {
            GZIPInputStream unGzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int len;
            while ((len = unGzip.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
            }
            return out.toString(encoding);
        } catch (IOException e) {
            throw new RPCServerRuntimeException("uncompressToString string fail", e);
        }
    }
}
