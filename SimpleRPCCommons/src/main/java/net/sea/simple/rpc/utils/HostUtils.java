package net.sea.simple.rpc.utils;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author chengwu.hua
 * @Date 2018/5/7 13:32
 * @Version 1.0
 */
public class HostUtils {
    protected static Logger logger = Logger.getLogger(HostUtils.class);

    private HostUtils() {
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    public static String getLocalIP() {
        List<String> result = new ArrayList<>();
        Enumeration<NetworkInterface> netInterfaces;
        String defaultIP = "127.0.0.1";
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1) {
                        result.add(ip.getHostAddress());
                    }
                }
            }
//            return CollectionUtils.isEmpty(result) ? defaultIP : result.get(0);
            return "192.168.4.118";
        } catch (Exception e) {
            logger.error("获取本地IP异常", e);
            return defaultIP;
        }
    }
}
