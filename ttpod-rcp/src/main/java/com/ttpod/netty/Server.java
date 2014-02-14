package com.ttpod.netty;

import com.ttpod.netty.rpc.pool.GroupManager;
import com.ttpod.netty.util.IpAddress;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * date: 14-1-7 下午4:20
 *
 * @author: yangyang.cong@ttpod.com
 */
public class Server {


    int port;
    EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    Channel channel;
    ChannelHandler channelHandler;
    GroupManager groupManager;
    public Server(ChannelHandler channelHandler) {
        this(channelHandler,8080,null);
    }
    public Server(ChannelHandler channelHandler, int port) {
        this(channelHandler,port,null);
    }

    public Server(ChannelHandler channelHandler, int port,GroupManager groupManager) {
        this.channelHandler = channelHandler;
        this.port = port;
        this.groupManager = groupManager;

        start();
    }


    private void start(){

        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)use NioServerSocketChannel instantiate a new Channel to accept incoming connections.
                    .option(ChannelOption.SO_BACKLOG, 128) //  option() is for the NioServerSocketChannel that accepts incoming connections
                    .childHandler(channelHandler)//(4)
                    .childOption(ChannelOption.TCP_NODELAY, true)  // (5)childOption() is for the Channels accepted by the parent ServerChannel
                    .childOption(ChannelOption.SO_KEEPALIVE, true);// which is NioServerSocketChannel in this case.

            // Bind and start to accept incoming connections.
            channel = b.bind(port).sync().channel(); // (7)

            System.out.println("Starting server at "+ IpAddress.eth0IpOrHostName() +":"+port);

            if(groupManager != null){
                groupManager.join(IpAddress.eth0IpOrHostName() +":"+port,null);
                System.out.println("server joined : "+ groupManager.name());
            }

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void shutdown(){
        channel.close();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }



}
