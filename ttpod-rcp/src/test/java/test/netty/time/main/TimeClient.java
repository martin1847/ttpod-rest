package test.netty.time.main;

import com.ttpod.netty.Client;
import test.netty.time.client.TimeClientHandler;
import test.netty.time.client.TimeDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * date: 14-1-7 下午4:41
 *
 * @author: yangyang.cong@ttpod.com
 */
public class TimeClient {

    public static void main(String[] args) throws Exception{

        new Client(new InetSocketAddress("127.0.0.1", 8080),
            new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
                }
        });

    }

}
