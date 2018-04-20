package net.sea.simple.rpc.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * RPC服务器异常
 * 
 * @author sea
 *
 */
public class RPCServerException extends Exception {
	private static final long serialVersionUID = -3609659874586025638L;
	private int errorCode;
	private String message;

	public RPCServerException() {
		super();
	}

	public RPCServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RPCServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public RPCServerException(String message) {
		super(message);
	}

	public RPCServerException(Throwable cause) {
		super(cause);
	}

	public RPCServerException(int errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}

	public RPCServerException(int errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String getMessage() {
		return StringUtils.isBlank(message) ? super.getMessage() : message;
	}

}
