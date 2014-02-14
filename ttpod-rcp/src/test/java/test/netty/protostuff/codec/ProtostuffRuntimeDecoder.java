package test.netty.protostuff.codec;

import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.ttpod.netty.Pojo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * date: 14-2-6 下午9:11
 *
 * @author: yangyang.cong@ttpod.com
 */
@ChannelHandler.Sharable
public class ProtostuffRuntimeDecoder extends MessageToMessageDecoder<ByteBuf> {
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
        Pojo pojo = new Pojo();
        Schema<Pojo> schema =  RuntimeSchema.getSchema(Pojo.class);
        ProtostuffIOUtil.mergeFrom(array,offset, length ,pojo, schema);

        out.add(pojo);
    }
}
