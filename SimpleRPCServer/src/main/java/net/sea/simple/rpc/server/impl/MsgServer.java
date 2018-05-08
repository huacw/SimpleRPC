package net.sea.simple.rpc.server.impl;

import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.server.config.ServerConfig;
import net.sea.simple.rpc.server.enumeration.ServiceType;

/**
 * 异步消息类服务器
 * 
 * @author sea
 *
 */
public class MsgServer extends AbstractServer {

	@Override
	protected void addListener(ServerConfig config, RegCallback callback) throws RPCServerException {

	}

	@Override
	protected String getServiceType() {
		return ServiceType.asynMsg.name();
	}

}
