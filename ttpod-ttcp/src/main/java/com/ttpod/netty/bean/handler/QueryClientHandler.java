package com.ttpod.netty.bean.handler;

import com.ttpod.netty.bean.QueryReq;
import com.ttpod.netty.bean.QueryRes;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * date: 14-2-7 下午1:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class QueryClientHandler extends SimpleChannelInboundHandler<QueryRes> {

    {
        System.out.println(
                "new QueryClientHandler :" + this
        );
    }

    // Stateful properties
    private volatile Channel channel;


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QueryClientHandler.class);


    private static final ConcurrentHashMap<Short,QueryResFuture> map = new ConcurrentHashMap<>(1024);

    protected void messageReceived(ChannelHandlerContext ctx, QueryRes msg) throws Exception {

        map.get(msg.getReqId()).set(msg);
//        System.out.println(
//                Thread.currentThread().getName() +" GOT MSG : " + msg
//        );
//        if (null == callback) {
//            answer.add(msg);
//        } else {
//            callback.dealWith(msg);
//        }
    }

    protected void channelRead0(ChannelHandlerContext ctx, QueryRes msg) throws Exception{
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

    public void doSearchWithCallBack(QueryReq req,QueryResCallback callback) {
        channel.writeAndFlush(req);
    }
    public QueryRes doSearch(QueryReq req) {
        QueryResFuture future = new QueryResFuture();
        map.put(req.reqId,future);
        channel.writeAndFlush(req);
        try {
            return future.get(2000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("future Got Error . ",e);
        }
    }

    public interface QueryResCallback{
        void dealWith(QueryRes msg) throws Exception;
    }
}
