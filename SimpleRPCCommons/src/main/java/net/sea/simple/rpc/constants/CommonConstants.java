package net.sea.simple.rpc.constants;

import java.nio.charset.Charset;

/**
 * 公用常量类
 *
 * @author sea
 */
public interface CommonConstants {
    /**
     * 默认版本号
     */
    public String DEFAULT_SERVICE_VERSION = "1.0";
    /**
     * 默认最大连接数
     */
    public int DEFAULT_MAX_CONNECTIONS = 1000;
    /**
     * jboss-marshalling版本号
     */
    public int JBOSS_MARSHALLING_VERSION = 5;
    /**
     * sessionId长度
     */
    public int SESSION_ID_LENGTH = 32;
    /**
     * 默认的字符集(字符串类型)
     */
    public String DEFAULT_CHARSET_STR = "UTF-8";
    /**
     * 默认的字符集
     */
    public Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_STR);
    /**
     * 默认的心跳超时时间
     */
    public int DEFAULT_HEART_TIMEOUT = 3000;
    /**
     * 默认的注册中心类型
     */
    public String DEFAULT_REGISTER_CENTER_TYPE = "zk";
    /**
     * 默认的连接超时时间
     */
    public int DEFAULT_CONNECTION_TIMEOUT = 3000;
    /**
     * 默认RPC客户端的连接超时时间
     */
    public int DEFAULT_CLIENT_CONNECTION_TIMEOUT = 60;
    /**
     * 默认RPC客户端的重试次数
     */
    public int DEFAULT_CLIENT_RETRY_COUNT = 3;
    /**
     * 默认RPC客户端的重试是否开启
     */
    public boolean DEFAULT_CLIENT_RETRY_ENABLE = true;
    /**
     * RPC注册中心根节点
     */
    public String ROOT_PATH = "/rpc/services/v_%s/";
    /**
     * 请求消息类型
     */
    public byte REQUEST_MESSAGE_TYPE = 1;
    /**
     * 响应消息类型
     */
    public byte RESPONSE_MESSAGE_TYPE = 2;
    /**
     * 系统正常返回
     */
    public int SUCCESS_CODE = 100;
    /**
     * 请求优先级（正常）
     */
    public byte REQUEST_NORMAL_PRIORITY = 5;
    /**
     * 请求优先级（最大）
     */
    public byte REQUEST_MAX_PRIORITY = 10;
    /**
     * 请求优先级（最小）
     */
    public byte REQUEST_MIN_PRIORITY = 1;

    /**
     * 错误码
     */
    public interface ErrorCode {
        /**
         * 系统异常
         */
        public int ERR_SYSTEM = 500;
        /**
         * 业务默认异常
         */
        public int ERR_BIZ_DEFAULT = 501;
        /**
         * 业务默认运行时异常
         */
        public int ERR_BIZ_RUNTIME_DEFAULT = 502;
        /**
         * 请求数据包体格式异常
         */
        public int ERR_REQUEST_BODY_STYLE = 503;
        /**
         * 未知的服务异常
         */
        public int ERR_UNKNOWN_SERVICE = 504;
        /**
         * 未知的方法异常
         */
        public int ERR_UNKNOWN_METHOD = 505;
        /**
         * 未知的方法异常
         */
        public int ERR_NO_METHOD = 506;
    }
}
