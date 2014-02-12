package test.netty.query;

import com.ttpod.netty.Client;
import com.ttpod.netty.rpc.InnerBindUtil;
import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;
import com.ttpod.netty.rpc.client.ClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientInitializer;
import com.ttpod.netty.rpc.client.OutstandingContainer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * date: 14-1-28 下午2:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class QueryClient {
    public static void main(String[] args) throws Exception {
        Client client = new Client(
                new InetSocketAddress("127.0.0.1", 6666), new DefaultClientInitializer());
        // Read commands from the stdin.
        final ClientHandler handler = client.getChannel().pipeline().get(DefaultClientHandler.class);
        final int THREADS = OutstandingContainer.UNSIGN_SHORT_OVER_FLOW;
        ExecutorService exe = Executors.newFixedThreadPool(Math.min(1024,THREADS));
        exe.execute(new Benchmark("assertThreadSafe",handler,exe,THREADS){
            protected void rpcCall(RequestBean req) {
                ResponseBean msg =  handler.rpc(req);
                if (!msg.toString().contains(req.getData())) {
                    System.err.println(req.getData()+ "\t" + InnerBindUtil.id(req) + "\t" + msg);
                }
            }
        });
        System.out.println("Pls Input a  word ..");
//      final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (; ; ) {
            String line = in.readLine();
            RequestBean req = new RequestBean(RequestBean.QueryServie.SONG, (short) 1, (short) 50, line);
            ResponseBean res = handler.rpc(req);
            System.out.println(line + "  ->  rpc["+ InnerBindUtil.id(req) +"] -> " +res );
            if ("bye".equals(line)) {
                client.close();
                break;
            }
        }
        exe.shutdown();

    }
}
