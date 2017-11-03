package net.sea.simple.rpc.server.exception;

/**
 * 不支持服务器类型异常
 * 
 * @author sea
 *
 */
public class UnsupportedServerTypeException extends Exception {
	private static final long serialVersionUID = -815996030508665789L;

	public UnsupportedServerTypeException() {
		super("不支持服务器类型");
	}
}
