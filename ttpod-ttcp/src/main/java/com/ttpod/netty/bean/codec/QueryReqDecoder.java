package com.ttpod.netty.bean.codec;

import com.ttpod.netty.bean.QueryReq;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * date: 14-1-28 上午11:40
 *
 * @author: yangyang.cong@ttpod.com
 */
//@ChannelHandler.Sharable
public class QueryReqDecoder extends ByteToMessageDecoder {
    static final int MAGIC_BYTE  = 1;
    static final int LENGTH_BYTE  = 2;

    static final int HEADER_BYTE   = MAGIC_BYTE + LENGTH_BYTE ;
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() <= HEADER_BYTE) {// Wait until the length prefix is available.
            return;
        }
        in.markReaderIndex();
        if (        in.readUnsignedByte() != QueryReqEncoder.MAGIC
              ||    in.readUnsignedShort() > in.readableBytes()  // Wait until the whole data is available.
         ) {
            in.resetReaderIndex();
            return;
        }
        byte service =  in.readByte();
        short page = in.readUnsignedByte();
        short size = in.readUnsignedByte();
        String q = in.toString(in.readerIndex(),in.readableBytes(), CharsetUtil.UTF_8);
        QueryReq req = new QueryReq();
        req.setService(service);
        req.setPage(page);
        req.setSize(size);
        req.setQ(q);
        out.add(req);
    }
}
