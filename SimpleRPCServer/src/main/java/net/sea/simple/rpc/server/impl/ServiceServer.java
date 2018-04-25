package net.sea.simple.rpc.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.sea.simple.rpc.constants.CommonConstants;
import net.sea.simple.rpc.constants.DefaultErrorCode;
import net.sea.simple.rpc.data.RPCHeader;
import net.sea.simple.rpc.data.codec.RPCMessageDecoder;
import net.sea.simple.rpc.data.codec.RPCMessageEncoder;
import net.sea.simple.rpc.data.request.RPCRequest;
import net.sea.simple.rpc.data.request.RPCRequestBody;
import net.sea.simple.rpc.data.response.RPCResponse;
import net.sea.simple.rpc.data.response.RPCResponseBody;
import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.exception.RPCServerRuntimeException;
import net.sea.simple.rpc.server.config.ServerConfig;
import net.sea.simple.rpc.server.enumeration.ServiceType;
import net.sea.simple.rpc.server.meta.ServiceMeta;
import net.sea.simple.rpc.server.utils.RPCCache;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

/**
 * 服务类服务器
 *
 * @author sea
 */
public class ServiceServer extends AbstractServer {

    @Override
    protected String getServiceType() {
        return ServiceType.service.name();
    }

    /**
     * 添加服务监听
     *
     * @param config
     * @throws RPCServerException
     */
    protected void addListener(ServerConfig config) throws RPCServerException {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        int port = config.getPort();
        String host = getLocalIP();
        config.setServiceIp(host);
        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws IOException {
                            ch.pipeline().addLast("decoder", new RPCMessageDecoder(1024 * 1024, 4, 4))
                                    .addLast("encoder", new RPCMessageEncoder())
                                    .addLast("readTimeoutHandler", new ReadTimeoutHandler(60))
                                    .addLast("serviceHandler", new ServiceHandler());
                        }
                    });

            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();
            if (StringUtils.isBlank(host)) {
                f = b.bind(port).sync();
            } else {
                f = b.bind(host, port);
            }

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RPCServerException(e);
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 服务处理handler
     *
     * @author chengwu.hua
     * @Date 2018/4/23 16:34
     * @Version 1.0
     */
    private class ServiceHandler extends SimpleChannelInboundHandler<RPCRequest> {
        private Logger logger = Logger.getLogger(getClass());

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RPCRequest request) throws Exception {
            RPCResponse response = new RPCResponse();
            response.setHeader(setResponseHeader(request.getHeader()));
            RPCRequestBody body = (RPCRequestBody) request.getBody();
            //验证请求包体
            validate(body);
            String serviceName = body.getServiceName();
            ServiceMeta service = RPCCache.newCache().findService(serviceName);
            if (service == null) {
                throw new RPCServerRuntimeException(CommonConstants.ErrorCode.ERR_UNKNOWN_SERVICE, String.format("未知的服务:%s", serviceName));
            }
            //执行服务方法
            Object result = service.invoke(body.getMethod(), body.getArgs().toArray());
            response.getHeader().setStatusCode(CommonConstants.SUCCESS_CODE);
            RPCResponseBody responseBody = new RPCResponseBody();
            responseBody.setResult(result);
            response.setResponseBody(responseBody);
            ctx.writeAndFlush(request);
        }

        /**
         * 验证请求包体格式
         *
         * @param body
         */
        private void validate(RPCRequestBody body) {
            if (body == null) {
                throw new RPCServerRuntimeException(CommonConstants.ErrorCode.ERR_REQUEST_BODY_STYLE, "RPC请求包体为空");
            }
            if (StringUtils.isBlank(body.getServiceName())) {
                throw new RPCServerRuntimeException(CommonConstants.ErrorCode.ERR_REQUEST_BODY_STYLE, "RPC请求包体服务名为空");
            }
            if (StringUtils.isBlank(body.getMethod())) {
                throw new RPCServerRuntimeException(CommonConstants.ErrorCode.ERR_REQUEST_BODY_STYLE, "RPC请求包体方法名为空");
            }
        }

        /**
         * 设置响应头信息
         *
         * @param requestHeader
         * @return
         */
        public RPCHeader setResponseHeader(RPCHeader requestHeader) {
            RPCHeader header = new RPCHeader();
            if (requestHeader == null) {
                header.setType(CommonConstants.RESPONSE_MESSAGE_TYPE);
                header.setExProperties(null);
                return header;
            }
            BeanUtils.copyProperties(requestHeader, header);
            header.setType(CommonConstants.RESPONSE_MESSAGE_TYPE);
            header.setLocalHost(requestHeader.getRemoteHost());
            header.setRemoteHost(requestHeader.getLocalHost());
            header.setExProperties(null);
            return header;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            //关闭连接
            ctx.close();
            logger.error("RPC服务异常", cause);
            //返回异信息
            RPCResponse response = new RPCResponse();
            RPCHeader header = new RPCHeader();
            if (cause instanceof RPCServerRuntimeException) {
                header.setStatusCode(((RPCServerRuntimeException) cause).getErrorCode());
            } else if (cause instanceof RPCServerException) {
                header.setStatusCode(((RPCServerException) cause).getErrorCode());
            } else {
                header.setStatusCode(DefaultErrorCode.SystemException.getErrCode());
            }
            header.setType(CommonConstants.RESPONSE_MESSAGE_TYPE);
            response.setHeader(header);
            RPCResponseBody body = new RPCResponseBody();
            body.setErrClassName(cause.getClass().getName());
            body.setResult(cause);
            response.setResponseBody(body);
            ctx.writeAndFlush(response);
        }
    }

}