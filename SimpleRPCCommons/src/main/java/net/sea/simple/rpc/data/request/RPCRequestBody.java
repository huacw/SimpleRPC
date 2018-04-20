package net.sea.simple.rpc.data.request;

import java.util.List;

import net.sea.simple.rpc.data.RPCBody;

/**
 * RPC请求消息体
 * 
 * @author sea
 *
 */
public class RPCRequestBody extends RPCBody {
	private String serviceName;// 服务类名
	private String method;// 方法名
	private List<Object> args;// 参数列表

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<Object> getArgs() {
		return args;
	}

	public void setArgs(List<Object> args) {
		this.args = args;
	}

}
