package com.ttpod.netty;

import com.ttpod.netty.rpc.pool.CloseableChannelFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;

/**
 * date: 14-1-7 下午4:42
 *
 * @author: yangyang.cong@ttpod.com
 */
public class Client implements CloseableChannelFactory {
    EventLoopGroup workerGroup;

    public Client(SocketAddress socketAddress, ChannelHandler channelHandler) throws InterruptedException {
        this(socketAddress,true, channelHandler);
    }

    final Bootstrap b;
    final SocketAddress socketAddress;
    final ChannelGroup clientGroup;
    public Client(SocketAddress socketAddress, boolean NIO, ChannelHandler channelHandler){
        this.socketAddress = socketAddress;
        this.workerGroup = NIO ? new NioEventLoopGroup() : new OioEventLoopGroup();
        b = new Bootstrap(); // (1)
        b.group(workerGroup); // (2)
        b.channel( NIO ?NioSocketChannel.class : OioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(channelHandler);
        this.clientGroup = new DefaultChannelGroup("clientGroup",GlobalEventExecutor.INSTANCE);
    }

    public Channel connect(){

        Channel channel = b.connect(socketAddress).syncUninterruptibly().channel();
        clientGroup.add(channel);
        // Start the client.
        return channel; // (5)
    }

    @Override
    public Channel newChannel() {
        return connect();
    }

    @Override
    public void shutdown(){
//        channel.closeFuture().sync();
//        channel.unsafe().closeForcibly();
        clientGroup.disconnect().awaitUninterruptibly();
        workerGroup.shutdownGracefully();
    }


}
