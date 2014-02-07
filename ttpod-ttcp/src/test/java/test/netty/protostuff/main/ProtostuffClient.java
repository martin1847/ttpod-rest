package test.netty.protostuff.main;

import com.ttpod.netty.Client;
import com.ttpod.netty.bean.codec.QueryReqDecoder;
import com.ttpod.netty.bean.codec.QueryReqEncoder;
import com.ttpod.netty.Pojo;
import test.netty.protostuff.codec.ProtostuffRuntimeDecoder;
import test.netty.protostuff.codec.ProtostuffRuntimeEncoder;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.net.InetSocketAddress;

/**
 * date: 14-2-6 下午9:17
 *
 * @author: yangyang.cong@ttpod.com
 */
public class ProtostuffClient {
    public static void main(String[] args) {
        final QueryReqDecoder decoder =  new QueryReqDecoder();
        final QueryReqEncoder encoder =  new QueryReqEncoder();

//        byte flag = 1;
////                                ctx.flush();
//                                final ChannelFuture f = ctx.writeAndFlush(new QueryReq(flag,flag,flag,"周记录"));
        new Client(new InetSocketAddress("127.0.0.1", 8080),
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                        p.addLast("protobufDecoder", new ProtostuffRuntimeDecoder());

                        p.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                        p.addLast("protobufEncoder", new ProtostuffRuntimeEncoder());

                        p.addLast("handler", new SimpleChannelInboundHandler<Pojo>() {
                            @Override
                            protected void messageReceived(ChannelHandlerContext ctx, Pojo msg) throws Exception {
                                System.out.println(" ProtostuffClient Revice  :" + msg);
                            }
                        });
                    }
                });

    }
}
