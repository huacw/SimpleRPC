package net.sea.simple.rpc.server.meta;

import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.utils.SpringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 执行方法
     *
     * @param methodName
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object invoke(String methodName, Object... args) throws InvocationTargetException, IllegalAccessException {
        if (methodMeta == null) {
            throw new RPCServerRuntimeException(CommonConstants.ErrorCode.ERR_NO_METHOD, String.format("服务【%s】发布PRPC方法", serviceName));
        }
        List<Class<?>> params = new ArrayList<>();
        for (Object arg : args) {
            params.add(arg.getClass());
        }
        Method method = methodMeta.getMethod(methodName, params.toArray(new Class[]{}));
        if (method == null) {
            throw new RPCServerRuntimeException(CommonConstants.ErrorCode.ERR_UNKNOWN_METHOD,
                                                String.format("服务【%s】未发布方法【%s】", serviceName, methodName));
        }
        return method.invoke(SpringUtils.getBean(serviceName), args);
    }

    @Override
    public String toString() {
        return new StringBuilder("ServiceMeta{serviceName='").append(serviceName).append("'").append(", methodMeta=").append(methodMeta).append("}")
                .toString();
    }
}
