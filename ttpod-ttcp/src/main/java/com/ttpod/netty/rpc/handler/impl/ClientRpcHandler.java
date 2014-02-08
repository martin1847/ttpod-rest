package com.ttpod.netty.rpc.handler.impl;

import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;
import com.ttpod.netty.rpc.handler.ClientRpcStub;
import com.ttpod.netty.rpc.handler.ResponseObserver;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * date: 14-2-7 下午1:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class ClientRpcHandler extends SimpleChannelInboundHandler<ResponseBean> implements ClientRpcStub {
    {
        System.out.println(
                "new QueryClientHandler :" + this
        );
    }
    // Stateful properties
    private volatile Channel channel;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ClientRpcHandler.class);
    private static final ConcurrentHashMap<Short,ResponseObserver> map = new ConcurrentHashMap<>(128);

    protected void messageReceived(ChannelHandlerContext ctx, ResponseBean msg) throws Exception {
        map.remove(msg.getReqId()).onSuccess(msg);
    }

    @Deprecated
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBean msg) throws Exception{
        messageReceived(ctx,msg);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Unexpected exception from downstream.", cause);
        ctx.close();
    }

    @Override
    public void rpc(RequestBean req, ResponseObserver observer) {
        if(null !=  map.put(req.reqId,observer)){
            logger.warn("rpc req id Conflict : {}" ,req);
        }
        channel.writeAndFlush(req);
    }

    @Override
    public ResponseBean rpc(RequestBean req) {
        return rpc(req,2000);
    }

    @Override
    public ResponseBean rpc(RequestBean req, int timeOutMills) {
        ResponseFuture future = new ResponseFuture();
        rpc(req,future);
        try {
            return future.get(timeOutMills, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("future Got Error . ",e);
        }
    }
}
