package net.sea.simple.rpc.utils;

import java.util.Map;

/**
 * @author sea
 * @Date 2018/4/25 11:03
 * @Version 1.0
 */
public class MapUtils {
    /**
     * 判断map是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断map是否不为空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
