package net.sea.simple.rpc.client.annotation;

import net.sea.simple.rpc.constants.CommonConstants;

import java.lang.annotation.*;

/**
 * RPC服务客户端注解
 * 
 * @author sea
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
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
//	public Class<?>[] serviceName() default {};

	/**
	 * 服务版本
	 * 
	 * @return
	 */
	public String version() default CommonConstants.DEFAULT_SERVICE_VERSION;
}
