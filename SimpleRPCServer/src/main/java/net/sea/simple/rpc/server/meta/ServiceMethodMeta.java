package net.sea.simple.rpc.server.meta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.CollectionUtils;

/**
 * RPC服务方法元数据
 *
 * @author sea
 */
public class ServiceMethodMeta {
    private Map<String, List<Method>> methodMappers = new ConcurrentHashMap<>();

    /**
     * 添加方法
     *
     * @param name   方法名
     * @param method 方法对象
     */
    public void addMethod(String name, Method method) {
        List<Method> methods = methodMappers.get(name);
        if (methods == null) {
            methods = new ArrayList<>();
            methodMappers.put(name, methods);
        }
        methods.add(method);
    }

    /**
     * 删除指定方法
     *
     * @param name       方法名
     * @param methodArgs 方法参数类型
     * @return
     */
    public Method removeMethod(String name, Class<?>... methodArgs) {
        List<Method> methods = methodMappers.get(name);
        if (CollectionUtils.isEmpty(methods)) {
            return null;
        }
        Method delMethod = getMethod(name, methodArgs);
        if (delMethod != null) {
            methods.remove(delMethod);
        }
        return delMethod;
    }

    /**
     * 获取方法
     *
     * @param name       方法名
     * @param methodArgs 方法参数类型
     * @return
     */
    public Method getMethod(String name, Class<?>... methodArgs) {
        List<Method> methods = methodMappers.get(name);
        if (CollectionUtils.isEmpty(methods)) {
            return null;
        }
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != methodArgs.length) {
                continue;
            }
            boolean isSame = true;
            for (int i = 0; i < parameterTypes.length; i++) {
                try {
                    methodArgs[i].asSubclass(parameterTypes[i]);
                } catch (ClassCastException e) {
                    isSame = false;
                    break;
                }
            }
            if (isSame) {
                return method;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ServiceMethodMeta{" +
                "methodMappers=" + methodMappers +
                '}';
    }
}
