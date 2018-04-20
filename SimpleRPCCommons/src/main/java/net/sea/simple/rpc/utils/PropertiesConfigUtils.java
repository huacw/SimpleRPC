package net.sea.simple.rpc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

/**
 * 属性配置文件工具类
 * 
 * @author sea
 *
 */
public class PropertiesConfigUtils {
	private static final String DEFAULT_PROPERTY_FILE_URI = "classpath:config/app-config.properties";
	private volatile static Properties DEFAULT_PROP;

	static {
		DEFAULT_PROP = loadPropertiesFile(DEFAULT_PROPERTY_FILE_URI);
	}

	/**
	 * 获取默认属性配置文件
	 * 
	 * @return 属性对象
	 */
	public static Properties getCustomProperty(String fileName) {
		return loadPropertiesFile(fileName);
	}

	/**
	 * 加载配置文件
	 * 
	 * @param fileName
	 * @return
	 */
	private static Properties loadPropertiesFile(String fileName) {
		Properties prop = new Properties();
		try {
			File file = ResourceUtils.getFile(fileName);
			prop.load(new FileInputStream(file));
		} catch (IOException e) {
			throw new RPCServerRuntimeException(e);
		}
		return prop;
	}

	/**
	 * 获取默认属性配置中的属性
	 * 
	 * @param key
	 *            属性键
	 * @return 属性值
	 */
	public static String getDefaultProperty(String key) {
		return DEFAULT_PROP.getProperty(key);
	}

	/**
	 * 获取默认属性配置中的属性
	 * 
	 * @param key
	 *            属性键
	 * @param defaultValue
	 *            默认值
	 * @return 属性值
	 */
	public static String getDefaultProperty(String key, String defaultValue) {
		return StringUtils.isBlank(DEFAULT_PROP.getProperty(key)) ? defaultValue : DEFAULT_PROP.getProperty(key);
	}

	public static void main(String[] args) {
		System.out.println(getDefaultProperty("key", "ddd"));
	}
}
