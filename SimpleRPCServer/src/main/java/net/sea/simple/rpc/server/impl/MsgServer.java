package net.sea.simple.rpc.server.impl;

import net.sea.simple.rpc.server.enumeration.ServiceType;

/**
 * 异步消息类服务器
 * 
 * @author sea
 *
 */
public class MsgServer extends AbstractServer {

	@Override
	protected String getServiceType() {
		return ServiceType.asynMsg.name();
	}

}
