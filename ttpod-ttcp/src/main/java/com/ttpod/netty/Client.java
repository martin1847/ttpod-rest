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
    EventLoopGroup workerGroup;

    Channel channel;
    public Client(SocketAddress socketAddress, ChannelHandler channelHandler) throws InterruptedException {
        workerGroup = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(channelHandler);

            // Start the client.
            ChannelFuture f = b.connect(socketAddress).sync(); // (5)
            // Wait until the connection is closed.
            this.channel = f.channel();

//                channel.closeFuture().sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            workerGroup.shutdownGracefully();
//        }
    }


    public void close() throws InterruptedException {
        channel.closeFuture().sync();
        workerGroup.shutdownGracefully();
    }
    public Channel getChannel() {
        return channel;
    }


}
