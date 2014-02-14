package test.netty.time.main;

import com.ttpod.netty.Server;
import test.netty.time.server.TimeServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * date: 14-1-7 下午4:47
 *
 * @author: yangyang.cong@ttpod.com
 */
public class TimeServer {

    public static void main(String[] args) {
        new Server(new ChannelInitializer<SocketChannel>() {// (4)

            final TimeServerHandler SHARED_HANDER =  new TimeServerHandler();
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("handler", SHARED_HANDER);
//                          pipeline.addLast("handler", new EchoServerHandler());
//                            pipeline.addLast("encoder", new StringEncoder());
            }
        });
    }
}
