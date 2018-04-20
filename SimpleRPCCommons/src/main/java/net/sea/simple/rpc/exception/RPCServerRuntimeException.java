package net.sea.simple.rpc.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * RPC服务器运行时异常
 * 
 * @author sea
 *
 */
public class RPCServerRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 8453546446925780930L;

	private int errorCode;
	private String message;

	public RPCServerRuntimeException() {
	}

	public RPCServerRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RPCServerRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RPCServerRuntimeException(String message) {
		super(message);
	}

	public RPCServerRuntimeException(Throwable cause) {
		super(cause);
	}

	public RPCServerRuntimeException(int errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}

	public RPCServerRuntimeException(int errorCode) {
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
