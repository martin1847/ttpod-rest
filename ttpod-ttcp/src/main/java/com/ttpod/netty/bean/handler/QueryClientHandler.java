package com.ttpod.netty.bean.handler;

import com.ttpod.netty.bean.QueryReq;
import com.ttpod.netty.bean.QueryRes;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * date: 14-2-7 下午1:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class QueryClientHandler extends SimpleChannelInboundHandler<QueryRes> {
    // Stateful properties
    private volatile Channel channel;
    private final BlockingQueue<QueryRes> answer = new LinkedBlockingQueue<QueryRes>();

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QueryClientHandler.class);
    protected void messageReceived(ChannelHandlerContext ctx, QueryRes msg) throws Exception {
        answer.add(msg);
    }

    protected void channelRead0(ChannelHandlerContext ctx, QueryRes msg) throws Exception{
        answer.add(msg);
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

    public QueryRes doSearch(QueryReq req) {
        channel.writeAndFlush(req);
        QueryRes result;
        boolean interrupted = false;
        for (;;) {
            try {
                result = answer.take();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }

        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return result;
    }

}
