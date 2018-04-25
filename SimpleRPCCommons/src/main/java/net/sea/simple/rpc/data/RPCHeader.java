package net.sea.simple.rpc.data;

import java.util.Map;

/**
 * RPC消息头
 *
 * @author sea
 */
public class RPCHeader {
    private int crcCode = 0x2468bdf;
    private int length;// 消息长度
    private String sessionId;// 会话ID
    private int statusCode;// 状态码，0-成功
    private byte type;// 消息类型，1-请求，2-响应
    private byte priority;// 消息优先级
    private String localHost;// 本地地址
    private String remoteHost;// 远程地址
    private String version;// 服务版本号
    private Map<String, String> exProperties;// 扩展属性

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, String> getExProperties() {
        return exProperties;
    }

    public void setExProperties(Map<String, String> exProperties) {
        this.exProperties = exProperties;
    }

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String localHost) {
        this.localHost = localHost;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
