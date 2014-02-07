package com.ttpod.netty.bean.main;

import com.ttpod.netty.Server;
import com.ttpod.netty.bean.codec.QueryReqDecoder;
import com.ttpod.netty.bean.codec.QueryReqEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * date: 14-1-28 下午1:11
 *
 * @author: yangyang.cong@ttpod.com
 */
public class QueryServer {
    public static void main(String[] args) {

        final  QueryReqEncoder encoder = new QueryReqEncoder();
        final  ChannelHandler handler = new QueryServerHandler();

        new Server(new ChannelInitializer<SocketChannel>() {// (4)

            //            final QueryReqDecoder decoder =  ;
//            final QueryReqEncoder encoder =  ;
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder", new QueryReqDecoder());
                pipeline.addLast("encoder", encoder);
                pipeline.addLast("hanlder", handler);
            }
        });
    }
}
