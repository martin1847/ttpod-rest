package test.netty.time.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Although the first solution has resolved the problem with the TIME client,
 * the modified handler does not look that clean.
 * Imagine a more complicated protocol which is composed of multiple fields such as a variable length field.
 * Your ChannelHandler implementation will become unmaintainable very quickly.
 *
 * you could split TimeClientHandler into two handlers:
 *
 *      TimeDecoder which deals with the fragmentation issue, and
 *      the initial simple version of TimeClientHandler.
 *
 *
 * io.netty.example.factorial for a binary protocol,
 *
 * 处理基于流的传输协议

 套接字缓冲区的小警告
 在基于流的传输协议里就像TCP/IP, 收到的数据会存储到套接字缓冲区. 不幸的是, 基于流传输的缓冲区不是一个数据包队列而是一个字节队列. 这意味着, 即使你发送了两条消息作为两条独立的数据包, 操作系统也不会像两条消息一样处理他们而是为一串字节. 所以, 不保证你读到的正是你远程节点写的. 例如, 让我们假设TCP/IP协议栈的操作系统已经收到三个数据包:

 +-----+-----+-----+
 | ABC | DEF | GHI |
 +-----+-----+-----+
 因为基于流协议的一般性质, 在你的程序里有很高的机会会将以下面这种零散的形式读到他们:

 +----+-------+---+---+
 | AB | CDEFG | H | I |
 +----+-------+---+---+
 因此, 收到的部分, 无论是服务端或者客户端, 应该整理零散的收到的数据到一个或多个有意义的框(frames)通过程序逻辑可以容易的理解. 在上面的例子, 收到的数据应该像下面这样被装框:

 +-----+-----+-----+
 | ABC | DEF | GHI |
 +-----+-----+-----+

 *
 */
public class TimeDecoder extends ByteToMessageDecoder { // (1)

    @Override
    //calls the decode() method with an internally maintained cumulative buffer whenever new data is received.
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
        if (in.readableBytes() < 4) {
            return; // (3)add nothing to out where there is not enough data in the cumulative buffer.
        }
        
        out.add(in.readBytes(4)); // (4)adds an object to out, it means the decoder decoded a message successfully
        // then discard the read part of the cumulative buffer
    }


    /**
     * TimeDecoder extends ReplayingDecoder<VoidEnum> {
            @Override
            protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, VoidEnum state) {
                out.add(in.readBytes(4));
            }
      }
     */
}