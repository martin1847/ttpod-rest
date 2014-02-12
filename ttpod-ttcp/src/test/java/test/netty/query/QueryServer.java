package test.netty.query;

import com.ttpod.netty.Pojo;
import com.ttpod.netty.Server;
import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;
import com.ttpod.netty.rpc.codec.RequestDecoder;
import com.ttpod.netty.rpc.codec.ResponseEncoder;
import com.ttpod.netty.rpc.server.DefaultServerHandler;
import com.ttpod.netty.rpc.server.DefaultServerInitializer;
import com.ttpod.netty.rpc.server.ServerProcessor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.util.Version;

import java.util.Arrays;

/**
 * date: 14-1-28 下午1:11
 *
 * @author: yangyang.cong@ttpod.com
 */
public class QueryServer {
    public static void main(String[] args) {


        final DefaultServerHandler serverHandler = new DefaultServerHandler();
        serverHandler.setProcessors(
                new ServerProcessor[]{
                        new ServerProcessor() {
                            public ResponseBean handle(RequestBean req) throws Exception {
                                ResponseBean data = new ResponseBean();
                                data.setCode(1);
                                data.setPages(10);
                                data.setRows(2000);
                                data.setData(Arrays.asList(new Pojo(req.getData(), 100), new Pojo("OK", 10)));
                                return data;
                            }
                        }
                }
        );
        new Server(new DefaultServerInitializer(serverHandler),6666);

    }
}
