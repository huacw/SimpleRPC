package net.sea.simple.rpc.data.response;

import net.sea.simple.rpc.data.RPCBody;

/**
 * RPC响应消息体
 *
 * @author sea
 */
public class RPCResponseBody extends RPCBody {
    private Object result;// 返回对象
    private String errClassName;// 异常类名

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getErrClassName() {
        return errClassName;
    }

    public void setErrClassName(String errClassName) {
        this.errClassName = errClassName;
    }
}
