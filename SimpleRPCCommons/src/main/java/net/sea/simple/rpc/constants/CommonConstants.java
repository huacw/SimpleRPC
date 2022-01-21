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
    String DEFAULT_SERVICE_VERSION = "1.0";
    /**
     * 默认最大连接数
     */
    int DEFAULT_MAX_CONNECTIONS = 1000;
    /**
     * jboss-marshalling版本号
     */
    int JBOSS_MARSHALLING_VERSION = 5;
    /**
     * sessionId长度
     */
    int SESSION_ID_LENGTH = 32;
    /**
     * 服务的默认权重
     */
    double DEFAULT_SERVICE_WEIGHT = 1.0;
    /**
     * 默认的字符集(字符串类型)
     */
    String DEFAULT_CHARSET_STR = "UTF-8";
    /**
     * 默认的字符集
     */
    Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_STR);
    /**
     * 默认的心跳超时时间（3000ms）
     */
    long DEFAULT_HEART_TIMEOUT = 3000;
    /**
     * 默认的session超时时间（3000ms）
     */
    int DEFAULT_SESSION_TIMEOUT = 60000;
    /**
     * 默认的注册中心类型
     */
    String DEFAULT_REGISTER_CENTER_TYPE = "zk";
    /**
     * 默认的连接超时时间（3000ms）
     */
    int DEFAULT_CONNECTION_TIMEOUT = 3000;
    /**
     * 默认的获取连接超时时间（500ms）
     */
    int DEFAULT_FETCH_CONNECTION_TIMEOUT = 5000;
    /**
     * 默认RPC客户端的连接超时时间（5s）
     */
    int DEFAULT_CLIENT_CONNECTION_TIMEOUT = 5;
    /**
     * 默认RPC客户端最大连接数
     */
    int DEFAULT_CLIENT_MAX_CONNECTION_COUNT = 1000;
    /**
     * 请求消息类型
     */
    byte REQUEST_MESSAGE_TYPE = 1;
    /**
     * 响应消息类型
     */
    byte RESPONSE_MESSAGE_TYPE = 2;
    /**
     * 系统正常返回
     */
    int SUCCESS_CODE = 100;
    /**
     * 请求优先级（正常）
     */
    byte REQUEST_NORMAL_PRIORITY = 5;
    /**
     * 请求优先级（最大）
     */
    byte REQUEST_MAX_PRIORITY = 10;
    /**
     * 请求优先级（最小）
     */
    byte REQUEST_MIN_PRIORITY = 1;
    /**
     * 最大报文长度(10M)
     */
    int MAX_MESSAGE_LENGTH = 10 * 1024 * 1024;
    /**
     * 客户端连接的最大空闲时间，单位毫秒
     */
    long MAX_CLIENT_IDLE_TIME = 1000 * 60 * 30;

    /**
     * 错误码
     */
    interface ErrorCode {
        /**
         * 系统异常
         */
        int ERR_SYSTEM = 500;
        /**
         * 业务默认异常
         */
        int ERR_BIZ_DEFAULT = 501;
        /**
         * 业务默认运行时异常
         */
        int ERR_BIZ_RUNTIME_DEFAULT = 502;
        /**
         * 请求数据包体格式异常
         */
        int ERR_REQUEST_BODY_STYLE = 503;
        /**
         * 未知的服务异常
         */
        int ERR_UNKNOWN_SERVICE = 504;
        /**
         * 未知的方法异常
         */
        int ERR_UNKNOWN_METHOD = 505;
        /**
         * 未知的方法异常
         */
        int ERR_NO_METHOD = 506;
    }
}
