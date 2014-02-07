package com.ttpod.netty.bean.main;

import com.ttpod.netty.Server;
import com.ttpod.netty.bean.QueryReq;
import com.ttpod.netty.bean.codec.QueryReqDecoder;
import com.ttpod.netty.bean.codec.QueryReqEncoder;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;

/**
 * date: 14-1-28 下午1:11
 *
 * @author: yangyang.cong@ttpod.com
 */
public class QueryServer {
    public static void main(String[] args) {


        final QueryReqEncoder encoder = new QueryReqEncoder();
        new Server(new ChannelInitializer<SocketChannel>() {// (4)

            //            final QueryReqDecoder decoder =  ;
//            final QueryReqEncoder encoder =  ;
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("decoder", new QueryReqDecoder());
                pipeline.addLast("encoder", encoder);
                pipeline.addLast("hanlder", new ChannelHandlerAdapter() {
                    public void channelActive(final ChannelHandlerContext ctx) { // (1)
                        final ChannelFuture f = ctx.writeAndFlush(
                                new QueryReq(QueryReq.QueryServie.SONG, (short) 10, (short) 200, "test 2014"));
                        f.addListener(ChannelFutureListener.CLOSE);
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        cause.printStackTrace();
                        ctx.close();
                    }
                });

            }
        });
    }
}
