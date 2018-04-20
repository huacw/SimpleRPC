package net.sea.simple.rpc.server.impl;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.sea.simple.rpc.data.codec.RPCMessageDecoder;
import net.sea.simple.rpc.data.codec.RPCMessageEncoder;

/**
 * RPC服务的服务器端
 * 
 * @author sea
 *
 */
public class RPCServer {

	/**
	 * 启动rpc服务器
	 * 
	 * @param host
	 * @param port
	 * @throws Exception
	 */
	public void start(String host, int port) throws Exception {
		// 配置服务端的NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();

			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws IOException {
							ch.pipeline().addLast(new RPCMessageDecoder(1024 * 1024, 4, 4));
							ch.pipeline().addLast(new RPCMessageEncoder());
							ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
							// ch.pipeline().addLast("HeartBeatHandler", new
							// HeartBeatRespHandler());
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
		} finally {
			// 优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
