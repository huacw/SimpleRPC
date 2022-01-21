package net.sea.simple.rpc.utils;

/**
 * @Description: crc生成工具
 * @Author: hcw
 * @Date: 2022/1/21 9:51
 */
public class CRCCodeUtil {
    /**
     * 计算CRC16校验码
     *
     * @param bytes
     * @return
     */
    public static Integer getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return CRC;
    }
}
