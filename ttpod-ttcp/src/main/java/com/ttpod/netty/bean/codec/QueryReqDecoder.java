package com.ttpod.netty.bean.codec;

import com.ttpod.netty.bean.QueryReq;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * date: 14-1-28 上午11:40
 *
 * @author: yangyang.cong@ttpod.com
 */
//@ChannelHandler.Sharable
public class QueryReqDecoder extends ByteToMessageDecoder {
    static final int BYTE  = 1;
    static final int INT_BYTE  = 4 * BYTE;
    static final Charset UTF8 = Charset.forName("UTF8");
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // Wait until the length prefix is available.
        if (in.readableBytes() < INT_BYTE) {
            return;
        }

        in.markReaderIndex();
//
////        // Check the magic number.
////        int magicNumber = in.readUnsignedByte();
////        if (magicNumber != 'F') {
////            in.resetReaderIndex();
////            throw new CorruptedFrameException(
////                    "Invalid magic number: " + magicNumber);
////        }
//
//        // Wait until the whole data is available.
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte service =  in.readByte();
        byte page = in.readByte();
        byte size = in.readByte();
        String q = in.toString(in.readerIndex(),in.readableBytes(),UTF8);
        QueryReq req = new QueryReq(service,page,size,q);
        out.add(req);
//        System.out.println("[QueryReqDecoder end] : "+req);

    }
}
