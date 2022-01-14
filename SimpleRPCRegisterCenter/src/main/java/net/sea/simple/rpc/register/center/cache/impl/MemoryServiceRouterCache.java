package net.sea.simple.rpc.register.center.cache.impl;

import lombok.extern.slf4j.Slf4j;
import net.sea.simple.rpc.server.ServiceInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 内存级别的路由缓存
 * @Author: hcw
 * @Date: 2022/1/14 11:18
 */
@Slf4j
public class MemoryServiceRouterCache extends AbstractServiceRouterCache {

    @Override
    protected String getCacheType() {
        return "memory";
    }

    @Override
    public void loadAvailableServices() {
        List<ServiceInfo> allAvailableServices = register.findAllAvailableServices();
        if (CollectionUtils.isEmpty(allAvailableServices)) {
            log.warn("未加载到服务列表");
        } else {
            serviceWithVersionMapper = allAvailableServices.stream().collect(Collectors.groupingBy(si -> String.format("%s_%s", si.getServiceName(), si.getVersion())));
        }
    }
}
