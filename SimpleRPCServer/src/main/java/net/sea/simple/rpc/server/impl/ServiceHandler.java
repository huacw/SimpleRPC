package net.sea.simple.rpc.server.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sea.simple.rpc.data.request.RPCRequest;

/**
 * 服务处理handler
 *
 * @author chengwu.hua
 * @Date 2018/4/23 16:34
 * @Version 1.0
 */
public class ServiceHandler extends SimpleChannelInboundHandler<RPCRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest msg) throws Exception {

    }
}
