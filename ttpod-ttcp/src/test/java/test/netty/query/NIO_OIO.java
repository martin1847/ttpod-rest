package test.netty.query;

import com.ttpod.netty.Client;
import com.ttpod.netty.rpc.client.ClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientInitializer;
import com.ttpod.netty.rpc.client.OutstandingContainer;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * date: 14-1-28 下午2:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class NIO_OIO {
    public static void main(String[] args) throws Exception {

        final int THREADS = OutstandingContainer.UNSIGN_SHORT_OVER_FLOW;
        ExecutorService exe = Executors.newFixedThreadPool(Math.min(1024, THREADS));

        Client nioClient = new Client(new InetSocketAddress("127.0.0.1", 6666),
                new DefaultClientInitializer());
        final ClientHandler NIOhandler = nioClient.getChannel().pipeline().get(DefaultClientHandler.class);
        Benchmark New = new Benchmark("New",NIOhandler,exe,THREADS);

        Client oioClient = new Client(new InetSocketAddress("127.0.0.1", 6666),false,new DefaultClientInitializer());
        final ClientHandler OIOhandler = nioClient.getChannel().pipeline().get(DefaultClientHandler.class);
        Benchmark Old = new Benchmark("Old",OIOhandler,exe,THREADS);
        Benchmark.VS(New, Old, 15);


        nioClient.close();
        oioClient.close();
        exe.shutdown();

    }
}


