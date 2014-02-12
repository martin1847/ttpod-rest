package com.ttpod.netty.rpc.client;

import com.ttpod.netty.rpc.codec.RequestEncoder;
import com.ttpod.netty.rpc.codec.ResponseDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * DefaultClientInitializer
 *
 * date: 14-2-12 下午4:24
 *
 * @author: yangyang.cong@ttpod.com
 */
public class DefaultClientInitializer extends ChannelInitializer<SocketChannel> {

    final ChannelHandler requestEncoder = new RequestEncoder();
    final ChannelHandler responseDecoder = new ResponseDecoder();

    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
        p.addLast("responseDecoder", responseDecoder);
        p.addLast("requestEncoder", requestEncoder);
        initClientHandler(p);
    }

    protected void  initClientHandler(ChannelPipeline p){
        p.addLast(new DefaultClientHandler());
    }
}
