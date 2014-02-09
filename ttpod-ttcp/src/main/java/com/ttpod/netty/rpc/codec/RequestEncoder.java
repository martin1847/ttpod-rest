package com.ttpod.netty.rpc.codec;

import com.ttpod.netty.rpc.RequestBean;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * date: 14-1-28 上午11:57
 *
 * @author: yangyang.cong@ttpod.com
 */
@ChannelHandler.Sharable
public class RequestEncoder extends MessageToByteEncoder<RequestBean> {


    static final int BYTE_FIELDS = 1*2 + 3 * 1;// _id + 3 byte
    public static final short MAGIC  = 0XCF;

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestBean req, ByteBuf out) throws Exception {

//        System.out.println("[QueryReqEncoder call]   ");
//        byte[] q = ;
//        byte[] full = new byte[q.length + BYTE_FIELDS];
        byte[] string = req.getData().getBytes(CharsetUtil.UTF_8);
        out.writeByte(MAGIC);
        out.writeShort(string.length + BYTE_FIELDS);
        out.writeShort( req.reqId);
        out.writeByte(  req.getService());
        out.writeByte(  req.getPage());
        out.writeByte(  req.getSize());
        out.writeBytes(string);
//        System.arraycopy(q,0,full,q.length -1, q.length);
//        out.add(Unpooled.wrappedBuffer(full));
//        System.out.println("[QueryReqEncoder end] : "+ msg);
       // out.add(ByteBufUtil.encodeString(buf, CharBuffer.wrap(msg.getData()),QueryReqDecoder.UTF8));
    }
}
