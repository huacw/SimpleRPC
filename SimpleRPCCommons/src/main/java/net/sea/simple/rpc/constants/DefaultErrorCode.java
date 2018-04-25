package net.sea.simple.rpc.constants;

/**
 * 系统默认错误码
 *
 * @author chengwu.hua
 * @Date 2018/4/25 14:03
 * @Version 1.0
 */
public enum DefaultErrorCode {
    SystemException(CommonConstants.ErrorCode.ERR_SYSTEM, "系统异常"),
    RpcException(CommonConstants.ErrorCode.ERR_BIZ_DEFAULT, "业务默认异常"),
    RpcRuntimeException(CommonConstants.ErrorCode.ERR_BIZ_RUNTIME_DEFAULT, "业务默认运行时异常");

    private int errCode;
    private String message;

    DefaultErrorCode(int errCode, String message) {
        this.errCode = errCode;
        this.message = message;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
