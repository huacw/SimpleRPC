package net.sea.simple.rpc.register.center.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.register.center.IRegister;
import net.sea.simple.rpc.register.center.cache.IServiceRouterCache;
import net.sea.simple.rpc.register.center.nacos.config.NacosRegisterCenterConfig;
import net.sea.simple.rpc.register.center.nacos.constants.NacosConstants;
import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.utils.JsonUtils;
import net.sea.simple.rpc.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: nacos的注册器
 * @Author: hcw
 * @Date: 2021/1/19 10:42
 */
public class NacosRegister implements IRegister {
    private Logger logger = Logger.getLogger(getClass());
    private NacosRegisterCenterConfig nacosRegisterCenterConfig;
    private NamingService namingService;
    private IServiceRouterCache serviceRouterCache;

    public NacosRegister(NacosRegisterCenterConfig nacosRegisterCenterConfig) {
        this.nacosRegisterCenterConfig = nacosRegisterCenterConfig;
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        logger.info("注册中心类型为：nacos");
        checkConfig(nacosRegisterCenterConfig);
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.NAMESPACE, nacosRegisterCenterConfig.getNamespace());
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosRegisterCenterConfig.getServerAddresses());
        if (nacosRegisterCenterConfig.isNeedAuth()) {
            properties.setProperty(PropertyKeyConst.USERNAME, nacosRegisterCenterConfig.getUsername());
            properties.setProperty(PropertyKeyConst.PASSWORD, nacosRegisterCenterConfig.getPassword());
        }
        try {
            namingService = NacosFactory.createNamingService(properties);
        } catch (NacosException e) {
            logger.error("初始化nacos注册中心异常", e);
            throw new RPCServerRuntimeException(e);
        }
    }

    /**
     * 检查配置
     *
     * @param config
     */
    private void checkConfig(NacosRegisterCenterConfig config) {
        if (config == null || StringUtils.isBlank(config.getServerAddresses())) {
            throw new RPCServerRuntimeException(String.format("【\n%s\n】配置缺失",
                    "register.center.config.nacos.serverAddresses或\nregister:\n\tcenter:\n\t\tconfig:\n\t\t\tnacos:\n\t\t\t\tserverAddresses或"));
        }
    }

    @Override
    public String getRegisterName() {
        return NacosConstants.REGISTER_CENTER_TYPE;
    }

    @Override
    public String getRGSign() {
        return MD5Util.encrypt(nacosRegisterCenterConfig.getServerAddresses());
    }

    @Override
    public boolean addNode(ServiceInfo service) {

        Instance instance = new Instance();
        String serviceName = service.getServiceName();
        String host = service.getHost();
        int port = service.getPort();
        instance.setInstanceId(String.format("%s-%s-%s", serviceName, host, port));
        instance.setEnabled(true);
        instance.setHealthy(true);
        String version = service.getVersion();
        String regServiceName = getRegServiceName(service);
        instance.setServiceName(regServiceName);
        instance.addMetadata("serverType", service.getServiceType());
        instance.addMetadata("serverVersion", version);
        instance.setIp(host);
        instance.setPort(port);
        instance.setWeight(1.0);
        try {
            namingService.registerInstance(regServiceName, instance);
            logger.info(String.format("注册服务【%s】版本【%s】成功，注册的服务信息：%s", serviceName, version, JsonUtils.toJson(service)));
            return true;
        } catch (NacosException e) {
            logger.error(String.format("注册服务【%s】异常", serviceName), e);
            return false;
        }
    }

    /**
     * 获取注册的实际服务名称
     *
     * @param service
     * @return
     */
    private String getRegServiceName(ServiceInfo service) {
        String version = service.getVersion();
        String serviceName = service.getServiceName();
        return String.format("%s_%s", serviceName, version);
    }

    @Override
    public boolean removeNode(ServiceInfo service) {
        String serviceName = service.getServiceName();
        String host = service.getHost();
        int port = service.getPort();
        try {
            namingService.deregisterInstance(getRegServiceName(service), host, port);
            logger.info(String.format("注销服务【%s】成功，注销的服务信息：%s", serviceName, JsonUtils.toJson(service)));
            return true;
        } catch (NacosException e) {
            logger.error(String.format("注销服务【%s】异常", serviceName), e);
            return false;
        }
    }

    @Override
    public boolean reconnect(ServiceInfo service) {
        return addNode(service);
    }

    @Override
    public ServiceInfo findNode(ServiceInfo service) {
        String serviceName = service.getServiceName();
        try {
            Instance instance = namingService.selectOneHealthyInstance(getRegServiceName(service));
            ServiceInfo serviceInfo = buildServiceInfo(instance);
            return serviceInfo;
        } catch (NacosException e) {
            logger.error(String.format("获取服务【%s】异常", serviceName), e);
            throw new RPCServerRuntimeException(e);
        }
    }

    @Override
    public boolean hasNode(ServiceInfo service) {
        return hasNextServiceNode(getRegServiceName(service));
    }

    @Override
    public boolean hasNextServiceNode(String serviceName) {
        try {
            List<Instance> allInstances = namingService.getAllInstances(serviceName, false);
            allInstances = allInstances.stream().filter(Instance::isHealthy).collect(Collectors.toList());
            return !CollectionUtils.isEmpty(allInstances);
        } catch (NacosException e) {
            logger.error(String.format("获取所有可用服务【%s】异常", serviceName), e);
            return false;
        }
    }

    @Override
    public List<ServiceInfo> findAllAvailableServices() {
        List<ServiceInfo> serviceInfoList = new ArrayList<>();
        try {
            int pageIndex = 1;
            int pageCount;
            int pageSize = 100;
            do {
                ListView<String> listView = namingService.getServicesOfServer(pageIndex, pageSize);
                pageCount = (listView.getCount() - 1) / pageSize + 1;
                pageIndex++;
                List<String> serviceNames = listView.getData();
                for (String serviceName : serviceNames) {
                    namingService.getAllInstances(serviceName, false).forEach(instance -> serviceInfoList.add(buildServiceInfo(instance)));
                }
            } while (pageIndex <= pageCount);
        } catch (NacosException e) {
            logger.error("获取所有注册服务异常", e);
        }
        return serviceInfoList;
    }

    /**
     * 构建注册服务信息
     *
     * @param instance
     * @return
     */
    private ServiceInfo buildServiceInfo(Instance instance) {
        String serviceName = instance.getServiceName();
        String[] tmpArray = serviceName.split("_");
        ServiceInfo serviceInfo = new ServiceInfo(tmpArray[0]);
        serviceInfo.setHost(instance.getIp());
        serviceInfo.setPort(instance.getPort());
        Map<String, String> metadata = instance.getMetadata();
        serviceInfo.setServiceType(metadata.get("serverType"));
        serviceInfo.setVersion(tmpArray.length < 2 || StringUtils.isBlank(tmpArray[1]) ? CommonConstants.DEFAULT_SERVICE_VERSION : tmpArray[1]);
        return serviceInfo;
    }
}
