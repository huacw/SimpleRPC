package net.sea.simple.rpc.server.exception;

import net.sea.simple.rpc.exception.RPCServerException;

/**
 * 不支持服务器类型异常
 * 
 * @author sea
 *
 */
public class UnsupportedServerTypeException extends RPCServerException {
	private static final long serialVersionUID = -815996030508665789L;

	public UnsupportedServerTypeException() {
		super("不支持服务器类型");
	}
}
