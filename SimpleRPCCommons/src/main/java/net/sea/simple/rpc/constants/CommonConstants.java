package net.sea.simple.rpc.constants;

/**
 * 公用常量类
 * 
 * @author sea
 *
 */
public interface CommonConstants {
	/**
	 * 默认版本号
	 */
	public String DEFAULT_SERVICE_VERSION = "1.0";
	/**
	 * 默认最大连接数
	 */
	public int DEFAULT_MAX_CONNECTIONS = 1000;
	/**
	 * jboss-marshalling版本号
	 */
	public int JBOSS_MARSHALLING_VERSION = 5;
	/**
	 * sessionId长度
	 */
	public int SESSION_ID_LENGTH = 32;
	public String DEFAULT_CHARSET = "UTF-8";
}
