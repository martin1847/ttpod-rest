package com.ttpod.netty.time.main;

import com.ttpod.netty.Server;
import com.ttpod.netty.time.server.TimeServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * date: 14-1-7 下午4:47
 *
 * @author: yangyang.cong@ttpod.com
 */
public class TimeServer {

    public static void main(String[] args) {
        new Server(new ChannelInitializer<SocketChannel>() {// (4)
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("handler", new TimeServerHandler());
//                          pipeline.addLast("handler", new EchoServerHandler());
//                            pipeline.addLast("encoder", new StringEncoder());
            }
        });
    }
}
