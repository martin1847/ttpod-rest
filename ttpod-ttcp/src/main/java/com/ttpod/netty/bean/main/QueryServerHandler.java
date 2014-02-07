package com.ttpod.netty.bean.main;

import com.ttpod.netty.bean.QueryReq;
import io.netty.channel.*;

/**
 * date: 14-2-7 上午11:11
 *
 * @author: yangyang.cong@ttpod.com
 */
@ChannelHandler.Sharable
public class QueryServerHandler extends SimpleChannelInboundHandler<QueryReq> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, QueryReq msg) throws Exception {

        String q = msg.getQ();
        msg.setQ("Server GOT  q: "+q);
        // We do not need to write a ChannelBuffer here.
        // We know the encoder inserted at TelnetPipelineFactory will do the conversion.
        ChannelFuture future = ctx.writeAndFlush(msg);

        //  Close the connection if the client has sent 'bye'.
        if ("bye".equals(q)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
//    }
}
