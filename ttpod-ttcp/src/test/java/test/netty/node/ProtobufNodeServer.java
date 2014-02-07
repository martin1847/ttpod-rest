package test.netty.node;

import com.ttpod.netty.Server;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

import java.util.List;

/**
 * date: 14-2-6 下午4:13
 *
 * @author: yangyang.cong@ttpod.com
 */
public class ProtobufNodeServer {
    public static void main(String[] args) {

        new Server(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
//                ch.pipeline().addLast(new ReadTimeoutHandler(10));  //如果10秒钟都没有新的数据读取，那么自动关闭
//                ch.pipeline().addLast(new WriteTimeoutHandler(1));  //写的1秒钟超时


                /**
                 *protocol 编码介绍
                 * http://www.cppblog.com/true/archive/2009/09/11/95873.html
                 * https://developers.google.com/protocol-buffers/docs/encoding?hl=zh-CN
                 *
                 * http://www.searchtb.com/2010/11/protocol-buffers%E7%9A%84%E5%BA%94%E7%94%A8%E4%B8%8E%E5%88%86%E6%9E%90.html
                 *
                 * http://code.google.com/p/protobuf/wiki/ThirdPartyAddOns?spm=0.0.0.0.wbHcyG
                 *
                 * https://github.com/square/wire
                 *
                 */
                p.addLast(new ProtobufVarint32FrameDecoder());
                p.addLast(new MessageToMessageDecoder<ByteBuf>() {
                    @Override
                    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
                        final byte[] array;
                        final int offset;
                        final int length = msg.readableBytes();
                        if (msg.hasArray()) {
                            array = msg.array();
                            offset = msg.arrayOffset() + msg.readerIndex();
                        } else {
                            array = new byte[length];
                            msg.getBytes(msg.readerIndex(), array, 0, length);
                            offset = 0;
                        }
                        out.add(new String(array, offset, length));
                    }
                });
                p.addLast(new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
                        System.out.println(
                                "Recevied :" + msg
                        );
                    }
                });
            }
        });
    }
}
