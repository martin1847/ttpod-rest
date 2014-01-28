package com.ttpod.netty.bean.codec;

import com.ttpod.netty.bean.QueryReq;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * date: 14-1-28 上午11:57
 *
 * @author: yangyang.cong@ttpod.com
 */
//@ChannelHandler.Sharable
public class QueryReqEncoder extends MessageToByteEncoder<QueryReq> {

    static final int BYTE_FIELDS = 3;

    @Override
    protected void encode(ChannelHandlerContext ctx, QueryReq msg, ByteBuf out) throws Exception {

//        System.out.println("[QueryReqEncoder call]   ");
//        byte[] q = ;
//        byte[] full = new byte[q.length + BYTE_FIELDS];
        byte[] string = msg.getQ().getBytes(QueryReqDecoder.UTF8);
        out.writeInt(string.length + BYTE_FIELDS);
        out.writeByte( msg.getService());
        out.writeByte(  msg.getPage());
        out.writeByte(  msg.getSize());
        out.writeBytes(string);
//        System.arraycopy(q,0,full,q.length -1, q.length);
//        out.add(Unpooled.wrappedBuffer(full));
//        System.out.println("[QueryReqEncoder end] : "+ msg);
       // out.add(ByteBufUtil.encodeString(buf, CharBuffer.wrap(msg.getQ()),QueryReqDecoder.UTF8));
    }
}
