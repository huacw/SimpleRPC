package net.sea.simple.rpc.server.annotation;

import net.sea.simple.rpc.constants.CommonConstants;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * rpc服务注解
 *
 * @author sea
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface RPCService {
    /**
     * 服务版本
     *
     * @return
     */
    public String version() default CommonConstants.DEFAULT_SERVICE_VERSION;

    /**
     * 发布的类型
     *
     * @return
     */
    public Class<?>[] publishClasses();

    /**
     * 发布的方法
     *
     * @return
     */
    public String[] publishMethods() default {};
}
