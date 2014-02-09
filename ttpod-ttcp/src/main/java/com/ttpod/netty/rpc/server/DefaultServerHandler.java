package com.ttpod.netty.rpc.server;

import com.ttpod.netty.rpc.InnerBindUtil;
import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * date: 14-2-9 下午1:11
 *
 * @author: yangyang.cong@ttpod.com
 */
@ChannelHandler.Sharable
public class DefaultServerHandler extends SimpleChannelInboundHandler<RequestBean>  implements ServerHandler{
    protected void messageReceived(ChannelHandlerContext ctx, RequestBean msg) throws Exception {
        short reqId = InnerBindUtil.id(msg);
        ResponseBean data = handleRequest(msg);
        InnerBindUtil.bind(data,reqId);
//        data.setReqId(msg._reqId);
//        data.setCode(1);
//        data.setPages(10);
//        data.setRows(2000);
//        data.setData(Arrays.asList(new Pojo(q, 100), new Pojo("OK", 10)));
        ChannelFuture future = ctx.writeAndFlush(data);
        //  Close the connection if the client has sent 'bye'.
//        if ("bye".equals(q)) {
//            future.addListener(ChannelFutureListener.CLOSE);
//        }
    }

    protected void channelRead0(ChannelHandlerContext ctx, RequestBean msg) throws Exception {
        messageReceived(ctx,msg);
    }

    ServerProcessor[] processors;
    public void setProcessors(ServerProcessor[] processors) {
        this.processors = processors;
    }


    public ResponseBean handleRequest(RequestBean request) throws Exception{
        return processors[request.getService()].handle(request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught ... ");
        super.exceptionCaught(ctx, cause);
    }
}
