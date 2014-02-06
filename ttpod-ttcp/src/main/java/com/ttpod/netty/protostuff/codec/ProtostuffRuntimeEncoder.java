package com.ttpod.netty.protostuff.codec;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;

/**
 * date: 14-2-6 下午7:48
 *
 * @author: yangyang.cong@ttpod.com
 */
@ChannelHandler.Sharable
public class ProtostuffRuntimeEncoder extends MessageToMessageEncoder<Object> {


    /**
     *
     *http://code.google.com/p/protostuff/wiki/ProtostuffRuntime
     *
     * http://code.google.com/p/protostuff/source/browse/trunk/protostuff-runtime-registry/src/test/java/com/dyuproject/protostuff/runtime/ExplicitRuntimeObjectSchemaTest.java
     *
     *
     * Performance guidelines
     As much as possible, use the concrete type when declaring a field.

     For polymorhic datasets, prefer abstract classes vs interfaces.

     Use ExplicitIdStrategy to write the type metadata as int (ser/deser will be faster and the serialized size will be smaller).
     Register your concrete classes at startup via ExplicitIdStrategy.Registry.
     For objects not known ahead of time, use IncrementalIdStrategy
     You can activate it using the system property:
     -Dprotostuff.runtime.id_strategy_factory=com.dyuproject.protostuff.runtime.IncrementalIdStrategy$Factory
     You can also use these strategies independently. E.g:
     final IncrementalIdStrategy strategy = new IncrementalIdStrategy(....);
     // use its registry if you want to pre-register classes.

     // Then when your app needs a schema, use it.
     RuntimeSchema.getSchema(clazz, strategy);

     */

    @Override
    protected void encode(
            ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {

//        ExplicitIdStrategy.Registry.
        byte[] data = null;
        Schema<Object>  schema = (Schema<Object>) RuntimeSchema.getSchema(msg.getClass());
        data = ProtostuffIOUtil.toByteArray(msg, schema, LinkedBuffer.allocate(4096));

//        schema.newMessage();
        out.add(wrappedBuffer(data));
    }
}