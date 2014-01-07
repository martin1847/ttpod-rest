package com.ttpod.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;

/**
 * date: 14-1-7 下午4:42
 *
 * @author: yangyang.cong@ttpod.com
 */
public class Client {

    SocketAddress socketAddress;

    ChannelHandler channelHandler;

    public Client(SocketAddress socketAddress, ChannelHandler channelHandler) {
        this.socketAddress = socketAddress;
        this.channelHandler = channelHandler;
        connect();
    }

    void connect() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(channelHandler);

            // Start the client.
            ChannelFuture f = b.connect(socketAddress).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
