package com.ttpod.netty.bean.main;

import com.ttpod.netty.Client;
import com.ttpod.netty.bean.QueryReq;
import com.ttpod.netty.bean.codec.QueryReqDecoder;
import com.ttpod.netty.bean.codec.QueryReqEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * TODO Comment here.
 * date: 14-1-28 下午2:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class QueryClient {
    public static void main(String[] args) {
        final QueryReqDecoder decoder =  new QueryReqDecoder();
        final QueryReqEncoder encoder =  new QueryReqEncoder();

//        byte flag = 1;
////                                ctx.flush();
//                                final ChannelFuture f = ctx.writeAndFlush(new QueryReq(flag,flag,flag,"周记录"));
        new Client(new InetSocketAddress("127.0.0.1", 8080),
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        //new QueryReqDecoder(),new QueryReqEncoder(),
                        ch.pipeline().addLast(decoder,encoder,new ChannelHandlerAdapter(){
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                    System.out.println(" QueryClient Revice  :" + msg);
                                    ctx.close();
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
