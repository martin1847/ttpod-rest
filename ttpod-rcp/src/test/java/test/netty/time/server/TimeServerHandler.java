package test.netty.time.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 *
 * The protocol to implement in this section is the TIME protocol.
 * It is different from the previous examples in that it sends a message, which contains a 32-bit integer,
 * without receiving any requests and loses the connection once the message is sent.
 * In this example, you will learn how to construct and send a message, and to shutdown the connection on completion.
 *
 * Because we are going to ignore any received data but to send a message as soon as a connection is established,
 * we cannot use the channelRead() method this time. Instead, we should override the channelActive() method.
 *
 */
@ChannelHandler.Sharable
public class TimeServerHandler extends ChannelHandlerAdapter {

    {
        System.out.println(Thread.currentThread().getName() + "\t create " + getClass().getSimpleName() +" .");
    }

    private final AttributeKey<Boolean> auth = AttributeKey.valueOf("auth");

    public void channelActive(final ChannelHandlerContext ctx) { // (1)
        final ByteBuf time = ctx.alloc().buffer(4); // (2)
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));


        Attribute<Boolean> isAuthed =  ctx.attr(auth);
        isAuthed.set(Boolean.TRUE);

        System.out.println(isAuthed);

        final ChannelFuture f = ctx.writeAndFlush(time); // (3)A ChannelFuture represents an I/O operation which has not yet occurred.
        // It means, any requested operation might not have been performed yet because all operations are asynchronous in Netty
        /*
        For example, the following code might shutdown the connection even before a message is sent:
        Channel ch = ...;
        ch.writeAndFlush(message);
        ch.shutdown();
         */


//        f.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) {
//                assert f == future;
//                ctx.shutdown();//shutdown() also might not shutdown the connection immediately, and it returns a ChannelFuture.
//            }
//        }); // (4)

        f.addListener(ChannelFutureListener.CLOSE);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}