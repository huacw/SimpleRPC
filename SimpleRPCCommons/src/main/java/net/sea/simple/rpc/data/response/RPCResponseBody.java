package net.sea.simple.rpc.data.response;

import net.sea.simple.rpc.data.RPCBody;

import java.io.Serializable;

/**
 * RPC响应消息体
 *
 * @author sea
 */
public class RPCResponseBody extends RPCBody {
    private Serializable result;// 返回对象

    public Serializable getResult() {
        return result;
    }

    public void setResult(Serializable result) {
        this.result = result;
    }
}
