package net.sea.simple.rpc.client.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

import net.sea.simple.rpc.constants.CommonConstants;

/**
 * RPC服务客户端注解
 * 
 * @author sea
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface RPCClient {
	/**
	 * 服务应用名称
	 * 
	 * @return
	 */
	public String appName();

	/**
	 * 服务名称
	 * 
	 * @return
	 */
	public String serviceName() default "";

	/**
	 * 服务版本
	 * 
	 * @return
	 */
	public String version() default CommonConstants.DEFAULT_SERVICE_VERSION;
}
