package net.sea.simple.rpc.server.meta;

/**
 * RPC服务元数据
 *
 * @author sea
 */
public class ServiceMeta {
    public String serviceName;// 发布的服务名称
    public ServiceMethodMeta methodMeta;// 发布的方法列表

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceMethodMeta getMethodMeta() {
        return methodMeta;
    }

    public void setMethodMeta(ServiceMethodMeta methodMeta) {
        this.methodMeta = methodMeta;
    }

    @Override
    public String toString() {
        return "ServiceMeta{" +
                "serviceName='" + serviceName + '\'' +
                ", methodMeta=" + methodMeta +
                '}';
    }
}
