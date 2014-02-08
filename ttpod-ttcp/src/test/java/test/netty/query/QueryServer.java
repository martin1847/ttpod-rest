package test.netty.query;

import com.ttpod.netty.Server;
import com.ttpod.netty.rpc.codec.RequestDecoder;
import com.ttpod.netty.rpc.codec.ResponseEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.util.Version;

/**
 * date: 14-1-28 下午1:11
 *
 * @author: yangyang.cong@ttpod.com
 */
public class QueryServer {
    public static void main(String[] args) {



        final ChannelHandler frameEncoder = new ProtobufVarint32LengthFieldPrepender();
        final ChannelHandler responseEncoder = new ResponseEncoder();
        final EventLoopGroup searchGroup = new NioEventLoopGroup(
//                0, Executors.newCachedThreadPool()
        );

        System.out.println(
                Version.identify()
        );

        final  ChannelHandler serverHandler = new QueryServerHandler();
        new Server(new ChannelInitializer<SocketChannel>() {// (4)

            //            final QueryReqDecoder decoder =  ;
//            final QueryReqEncoder encoder =  ;
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();

                p.addLast("decoder", new RequestDecoder());

                p.addLast("frameEncoder",frameEncoder );
                p.addLast("responseEncoder", responseEncoder);

                p.addLast(searchGroup,"serverHandler", serverHandler);
            }
        },6666);

        searchGroup.shutdownGracefully();
    }
}
