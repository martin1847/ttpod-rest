package test.netty.query;

import com.ttpod.netty.bean.QueryReq;
import com.ttpod.netty.bean.QueryRes;
import com.ttpod.netty.Pojo;
import io.netty.channel.*;

import java.util.Arrays;

/**
 * date: 14-2-7 上午11:11
 *
 * @author: yangyang.cong@ttpod.com
 */
@ChannelHandler.Sharable
public class QueryServerHandler extends SimpleChannelInboundHandler<QueryReq> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, QueryReq msg) throws Exception {
        String q = msg.getQ();
        QueryRes  data = new QueryRes();
        data.setCode(1);
        data.setPages(10);
        data.setRows(2000);
        data.setData(Arrays.asList(new Pojo(q,100),new Pojo("OK",10)));
        ChannelFuture future = ctx.writeAndFlush(data);
        //  Close the connection if the client has sent 'bye'.
        if ("bye".equals(q)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
