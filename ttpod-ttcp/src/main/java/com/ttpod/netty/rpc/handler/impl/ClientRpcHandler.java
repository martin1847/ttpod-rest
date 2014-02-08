package com.ttpod.netty.rpc.handler.impl;

import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;
import com.ttpod.netty.rpc.handler.ClientRpcStub;
import com.ttpod.netty.rpc.handler.OutstandingContainer;
import com.ttpod.netty.rpc.handler.ResponseObserver;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * date: 14-2-7 下午1:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class ClientRpcHandler extends SimpleChannelInboundHandler<ResponseBean> implements ClientRpcStub {
    {
        System.out.println(
                "new ClientRpcHandler :" + this
        );
    }
    // Stateful properties
    private volatile Channel channel;
    private static final Logger logger = LoggerFactory.getLogger(ClientRpcHandler.class);

    // TODO beanckmark with RingBuffer . https://github.com/LMAX-Exchange/disruptor/wiki/Getting-Started
    private static final OutstandingContainer outstandings = OutstandingContainer.ARRAY_0xFFFF;

    protected void messageReceived(ChannelHandlerContext ctx, ResponseBean msg) throws Exception {
        ResponseObserver observer = outstandings.remove(msg.getReqId());
        if(null != observer){
            observer.onSuccess(msg);
        }else{
            logger.error("Unknown ResponseBean with id : {}", msg.getReqId());
        }
    }

    @Deprecated
    protected void channelRead0(ChannelHandlerContext ctx, ResponseBean msg) throws Exception{
        messageReceived(ctx,msg);
    }

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Unexpected exception from downstream.", cause);
        ctx.close();
    }



    @Override
    public void rpc(final RequestBean req, ResponseObserver observer) {
        if(null !=  outstandings.put(req.reqId = OutstandingContainer.ID.next() ,observer)){
            logger.warn("rpc req id Conflict : {}" ,req);
        }
        channel.writeAndFlush(req);
        // TODO !future.isSuccess() clean Or Use Disruptor ( Ring Buffer ) Or just waring with put above?
//        .addListener( new ChannelFutureListener() {
//            public void operationComplete(ChannelFuture future) throws Exception {
//                if(!future.isSuccess()){
//                    outstandings.remove(req.reqId);
//                }
//            }
//        });
    }

    @Override
    public ResponseBean rpc(RequestBean req) {
        BlockingResponseObserver done = new BlockingResponseObserver();
        rpc(req,done);
        synchronized (done) {
            while (done.response == null) {
                try {
                    done.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
        return done.response;
    }

    @Override
    public ResponseBean rpc(RequestBean req, int timeOutMills) throws TimeoutException{
        ResponseFuture future = new ResponseFuture();
        rpc(req,future);
        try {
            return future.get(timeOutMills, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(" rpc future Error -> ",e);
            throw new RuntimeException("future Got Error . ",e);
        }
    }
}
