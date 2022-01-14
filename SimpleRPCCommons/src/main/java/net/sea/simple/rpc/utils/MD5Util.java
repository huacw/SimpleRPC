package net.sea.simple.rpc.utils;


import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Util {
    /**
     * 加密方法
     *
     * @param inputText
     * @return
     */
    public static String encrypt(String inputText) {
        if (StringUtils.isBlank(inputText)) {
            return "";
        }
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(inputText.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return fillZero(new BigInteger(1, md.digest()).toString(16));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 前缀补0
     *
     * @param md5Str
     * @return
     */
    private static String fillZero(String md5Str) {
        if (StringUtils.isBlank(md5Str)) {
            return md5Str;
        }
        int length = md5Str.length();
        int zeroCount = 32 - length;
        if (zeroCount == 0) {
            return md5Str;
        }
        StringBuffer zeroPrefix = new StringBuffer();
        for (int i = 0; i < zeroCount; i++) {
            zeroPrefix.append("0");
        }
        return String.format("%s%s", md5Str, zeroPrefix);
    }
}
