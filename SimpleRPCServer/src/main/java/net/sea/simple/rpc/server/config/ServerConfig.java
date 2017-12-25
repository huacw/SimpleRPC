package net.sea.simple.rpc.server.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 服务器配置
 * 
 * @author sea
 *
 */
@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.NONE)
public class ServerConfig {
	@XmlAttribute
	private String serviceName;
	@XmlAttribute
	private String serviceIp;
	@XmlAttribute
	private int port;
	@XmlAttribute
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
