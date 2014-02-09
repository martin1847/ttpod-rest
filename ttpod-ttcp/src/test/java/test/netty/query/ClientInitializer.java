package test.netty.query;

import com.ttpod.netty.rpc.client.DefaultClientHandler;
import com.ttpod.netty.rpc.codec.RequestEncoder;
import com.ttpod.netty.rpc.codec.ResponseDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * date: 14-2-9 下午7:55
 *
 * @author: yangyang.cong@ttpod.com
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    final ChannelHandler requestEncoder = new RequestEncoder();
    final ChannelHandler responseDecoder = new ResponseDecoder();

    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
        p.addLast("responseDecoder", responseDecoder);
        p.addLast("requestEncoder", requestEncoder);
        p.addLast(new DefaultClientHandler());
    }

//
//    private ClientInitializer(){}
//
//    public final static ClientInitializer INSTANCE = new ClientInitializer();
}
