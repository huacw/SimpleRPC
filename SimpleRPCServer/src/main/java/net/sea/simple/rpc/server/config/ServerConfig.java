package net.sea.simple.rpc.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.beans.ConstructorProperties;

/**
 * 服务器配置
 * 
 * @author sea
 *
 */
@ConfigurationProperties(prefix = "rpc.server.config")
@Component
public class ServerConfig {
	private String serviceName;
	private String serviceIp;
	private int port;
	private boolean opened;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceIp() {
		return serviceIp;
	}

	public void setServiceIp(String serviceIp) {
		this.serviceIp = serviceIp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}
}
