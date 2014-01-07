package com.ttpod.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * date: 14-1-7 下午4:20
 *
 * @author: yangyang.cong@ttpod.com
 */
public class Server {

    ChannelHandler channelHandler;
    int port;

    ChannelFuture channelFuture;
    public Server(ChannelHandler channelHandler) {
        this(channelHandler,8080);
    }
    public Server(ChannelHandler channelHandler, int port) {
        this.channelHandler = channelHandler;
        this.port = port;

        start();
    }


    private void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)use NioServerSocketChannel instantiate a new Channel to accept incoming connections.
                    .option(ChannelOption.SO_BACKLOG, 128) //  option() is for the NioServerSocketChannel that accepts incoming connections
                    .childHandler(channelHandler)//(4)
                    .childOption(ChannelOption.TCP_NODELAY, true)  // (5)childOption() is for the Channels accepted by the parent ServerChannel
                    .childOption(ChannelOption.SO_KEEPALIVE, true);// which is NioServerSocketChannel in this case.

            // Bind and start to accept incoming connections.
            channelFuture = b.bind(port).sync(); // (7)

            System.out.println("Starting server at 0.0.0.0:"+port);
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shut down all event loops to terminate all threads.
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


    public void close(){
        channelFuture.channel().close();
    }
}
