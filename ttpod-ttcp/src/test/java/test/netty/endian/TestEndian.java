package test.netty.endian;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.ByteOrder;

/**
 * date: 14-1-7 下午2:49
 * @author: yangyang.cong@ttpod.com
 */
public class TestEndian {

    @Test(timeout = 1000)
    public void a(){
        ByteBuf buf = Unpooled.buffer(4);
        buf.setInt(0, 1);
        System.out.format("%08x%n", buf.getInt(0));
        ByteBuf leBuf = buf.order(ByteOrder.LITTLE_ENDIAN);
        // 打印出 '01000000'
        System.out.format("%08x%n", leBuf.getInt(0));

        assert buf != leBuf;
        assert buf == buf.order(ByteOrder.BIG_ENDIAN);

    }
}
