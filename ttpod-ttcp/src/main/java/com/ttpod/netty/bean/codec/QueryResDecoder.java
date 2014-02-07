package com.ttpod.netty.bean.codec;

import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.ttpod.netty.bean.QueryRes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * date: 14-2-7 下午12:00
 *
 * @author: yangyang.cong@ttpod.com
 */
@ChannelHandler.Sharable
public class QueryResDecoder extends MessageToMessageDecoder<ByteBuf> {

    static final Schema<QueryRes> schema =  RuntimeSchema.getSchema(QueryRes.class);
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
        // deser
        QueryRes pojo = new QueryRes();
        ProtostuffIOUtil.mergeFrom(array, offset, length, pojo, schema);
        out.add(pojo);
    }
}