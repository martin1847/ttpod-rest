package com.ttpod.netty.bean.main;

import com.ttpod.netty.Client;
import com.ttpod.netty.bean.QueryReq;
import com.ttpod.netty.bean.codec.QueryReqDecoder;
import com.ttpod.netty.bean.codec.QueryReqEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
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
        final QueryReqEncoder encoder =  new QueryReqEncoder();
        new Client(new InetSocketAddress("127.0.0.1", 8080),
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        //new QueryReqDecoder(),new QueryReqEncoder(),
                        ch.pipeline().addLast( new QueryReqDecoder(),encoder,
                                new SimpleChannelInboundHandler<QueryReq>(){
                                    protected void messageReceived(ChannelHandlerContext ctx, QueryReq msg) throws Exception {
                                        System.out.println(
                                                "Client Recevied :  " + msg
                                        );
                                    }
                                });
                    }
                });

    }
}
