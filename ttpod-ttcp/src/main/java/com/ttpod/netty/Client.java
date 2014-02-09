package com.ttpod.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;

import java.net.SocketAddress;

/**
 * date: 14-1-7 下午4:42
 *
 * @author: yangyang.cong@ttpod.com
 */
public class Client {
    EventLoopGroup workerGroup;

    Channel channel;

    public Client(SocketAddress socketAddress, ChannelHandler channelHandler) throws InterruptedException {
        this(socketAddress,true, channelHandler);
    }

    public Client(SocketAddress socketAddress, boolean NIO, ChannelHandler channelHandler) throws InterruptedException {
        this.workerGroup = NIO ? new NioEventLoopGroup() : new OioEventLoopGroup();
        Bootstrap b = new Bootstrap(); // (1)
        b.group(workerGroup); // (2)
        b.channel( NIO ?NioSocketChannel.class : OioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(channelHandler);
        // Start the client.
        ChannelFuture f = b.connect(socketAddress).sync(); // (5)
        // Wait until the connection is closed.
        this.channel = f.channel();
    }

    public void close() throws InterruptedException {
//        channel.closeFuture().sync();
        channel.unsafe().closeForcibly();
        workerGroup.shutdownGracefully();
    }

    public Channel getChannel() {
        return channel;
    }


}
