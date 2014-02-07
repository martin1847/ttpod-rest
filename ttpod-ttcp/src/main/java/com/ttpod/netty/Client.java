package com.ttpod.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
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
        this(socketAddress,channelHandler,null);
    }

    public Client(SocketAddress socketAddress, ChannelHandler channelHandler,ChannelCallback callback) {
        this.socketAddress = socketAddress;
        this.channelHandler = channelHandler;
        connect(callback);
    }

    void connect(ChannelCallback callback) {
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
            Channel channel = f.channel();

            if(null == callback){
                channel.closeFuture().sync();
            }else{
                callback.useChannel(channel);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }


    public interface ChannelCallback{

        void useChannel( Channel channel )  throws InterruptedException;
    }

}
