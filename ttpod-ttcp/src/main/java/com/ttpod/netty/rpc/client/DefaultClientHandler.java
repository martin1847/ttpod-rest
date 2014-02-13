package com.ttpod.netty.rpc.client;

import com.ttpod.netty.rpc.InnerBindUtil;
import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;
import com.ttpod.netty.rpc.pool.ChannelPool;
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
public class DefaultClientHandler extends SimpleChannelInboundHandler<ResponseBean> implements ClientHandler {
    // Stateful properties
    private volatile Channel channel;
    private static final Logger logger = LoggerFactory.getLogger(DefaultClientHandler.class);

    private final OutstandingContainer outstandings;


    public DefaultClientHandler(){
        this(new OutstandingContainer.Array());
    }
    public DefaultClientHandler(OutstandingContainer container){
        this.outstandings = container;
    }

    protected void messageReceived(ChannelHandlerContext ctx, ResponseBean msg) throws Exception {
        ResponseObserver observer = outstandings.remove(InnerBindUtil.id(msg));
        if(null != observer){
            observer.onSuccess(msg);
        }else{
            logger.error("Unknown ResponseBean with id : {}", InnerBindUtil.id(msg));
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
        logger.warn("Unexpected exception from downstream.MayBe server is disconnted.", cause);
        //TODO reconnect use zookeeper !~
        ctx.close();
        if(null!=channelPool){
            channelPool.remove(channel);
        }
    }



    @Override
    public void rpc(final RequestBean req, ResponseObserver observer) {
        if(null !=  outstandings.put(InnerBindUtil.bind( req, outstandings.nextId()),observer)){
            logger.warn("rpc req id Conflict : {}" ,req);
        }
        channel.writeAndFlush(req);
        // !future.isSuccess() clean -> Ring Buffer ( just waring with put above )?
//        .addListener( new ChannelFutureListener() {
//            public void operationComplete(ChannelFuture future) throws Exception {
//                if(!future.isSuccess()){
//                    outstandings.remove(req._reqId);
//                }
//            }
//        });
    }

    @Override
    public ResponseBean rpc(RequestBean req) {
        ResponseObserver.Blocking done = new ResponseObserver.Blocking();
        rpc(req,done);
        return done.get();
    }

    @Override
    public ResponseBean rpc(RequestBean req, int timeOutMills) throws TimeoutException{
        ResponseObserver.Future future = new ResponseObserver.Future();
        rpc(req,future);
        try {
            return future.get(timeOutMills, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(" rpc future Error -> ",e);
            throw new RuntimeException("future Got Error . ",e);
        }
    }

    public void setChannelPool(ChannelPool channelPool) {
        this.channelPool = channelPool;
    }

    ChannelPool channelPool;
}
