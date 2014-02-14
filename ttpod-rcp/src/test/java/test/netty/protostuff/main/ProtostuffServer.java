package test.netty.protostuff.main;

import com.ttpod.netty.Server;
import com.ttpod.netty.Pojo;
import test.netty.protostuff.codec.ProtostuffRuntimeDecoder;
import test.netty.protostuff.codec.ProtostuffRuntimeEncoder;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * date: 14-2-6 下午7:56
 *
 * @author: yangyang.cong@ttpod.com
 */
public class ProtostuffServer {
    public static void main(String[] args) {

        new Server(new ChannelInitializer<SocketChannel>() {// (4)

            //            final QueryReqDecoder decoder =  ;
//            final QueryReqEncoder encoder =  ;
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                p.addLast("protobufDecoder", new ProtostuffRuntimeDecoder());

                p.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                p.addLast("protobufEncoder", new ProtostuffRuntimeEncoder());

//                p.addLast("handler", new WorldClockServerHandler());
                p.addLast("hanlder", new SimpleChannelInboundHandler<Pojo>(){

                    protected void messageReceived(ChannelHandlerContext ctx, Pojo msg) throws Exception {
                        System.out.println(
                                "Recived :" + msg
                        );
                        ctx.write( msg );
                    }

                public void channelActive(final ChannelHandlerContext ctx) throws Exception{ // (1)
                    final ChannelFuture f = ctx.writeAndFlush(new Pojo("Hello,channelActive",12));
                    f.addListener(ChannelFutureListener.CLOSE);
                    super.channelActive(ctx);
                }

                    protected void channelRead0(ChannelHandlerContext ctx, Pojo msg) throws Exception {
                     messageReceived(ctx, msg);
                    }
                });

            }
        });
    }
}
