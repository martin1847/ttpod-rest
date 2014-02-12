package com.ttpod.netty.rpc.server;

import com.ttpod.netty.rpc.codec.RequestDecoder;
import com.ttpod.netty.rpc.codec.ResponseEncoder;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * date: 14-2-12 下午4:26
 *
 * @author: yangyang.cong@ttpod.com
 */
public class DefaultServerInitializer extends ChannelInitializer<SocketChannel> {

    final ChannelHandler frameEncoder = new ProtobufVarint32LengthFieldPrepender();
    final ChannelHandler responseEncoder = new ResponseEncoder();
    final EventLoopGroup serverGroup = new NioEventLoopGroup(
//                0, Executors.newCachedThreadPool()
    );

    final DefaultServerHandler serverHandler;

    public DefaultServerInitializer(DefaultServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast("decoder", new RequestDecoder());

        p.addLast("frameEncoder",frameEncoder );
        p.addLast("responseEncoder", responseEncoder);

        p.addLast(serverGroup,"serverHandler", serverHandler);
    }
}
