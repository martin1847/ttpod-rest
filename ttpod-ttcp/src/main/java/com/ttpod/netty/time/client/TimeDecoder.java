package com.ttpod.netty.time.client;

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