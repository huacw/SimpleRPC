package net.sea.simple.rpc.utils;

import com.github.zkclient.ZkClient;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.server.ServiceInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * zookeeper工具类
 *
 * @author sea
 * @Date 2018/4/25 15:39
 * @Version 1.0
 */
public class ZKUtils {
    /**
     * 服务节点是否存在
     *
     * @param service
     * @return
     */
    public static boolean hasNode(ZkClient client, ServiceInfo service) {
        String serviceName = service.getServiceName();
        if (StringUtils.isBlank(serviceName)) {
            return false;
        }
        String host = service.getHost();
        if (StringUtils.isBlank(host)) {
            return client.exists(getServiceNameNode(service));
        }
        return client.exists(getServiceNameNode(service).concat("/").concat(host));
    }

    /**
     * 获取服务节点地址
     *
     * @param service
     * @return
     */
    public static String getServiceNameNode(ServiceInfo service) {
        return String.format(CommonConstants.ROOT_PATH.concat(service.getServiceName()), service.getVersion());
    }
}
