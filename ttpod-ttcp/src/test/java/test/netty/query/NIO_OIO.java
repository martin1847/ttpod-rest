package test.netty.query;

import com.ttpod.netty.Client;
import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.ResponseBean;
import com.ttpod.netty.rpc.client.ClientHandler;
import com.ttpod.netty.rpc.client.DefaultClientHandler;
import com.ttpod.netty.rpc.client.OutstandingContainer;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * date: 14-1-28 下午2:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public class NIO_OIO {
    public static void main(String[] args) throws Exception {
        Client client = new Client(new InetSocketAddress("127.0.0.1", 6666),
                new ClientInitializer());
        System.out.println("Begin Loop");
        // Read commands from the stdin.
        final ClientHandler handler = client.getChannel().pipeline().get(DefaultClientHandler.class);
        final int THREADS = OutstandingContainer.UNSIGN_SHORT_OVER_FLOW;
        ExecutorService exe = Executors.newFixedThreadPool(Math.min(1024, THREADS));

        int NotifyTotal = 0;
        int FutureTotal = 0;
        int TIMES = 25;
        int HOT_TOTAL = TIMES - 5;
        for (int j = TIMES; j > 0; j--) {

            final CountDownLatch latch = new CountDownLatch(THREADS);
            long b = System.currentTimeMillis();
            for (int i = THREADS; i > 0; i--) {
                exe.execute(new Runnable() {
                    public void run() {
                        String q = Thread.currentThread().getName();
                        RequestBean req = new RequestBean(RequestBean.QueryServie.SONG, (short) 1, (short) 50, q);
                        ResponseBean msg = handler.rpc(req);
                        latch.countDown();
                    }
                });
            }
            latch.await();
            int cost = (int) (System.currentTimeMillis() - b);
            System.out.println("Notify cost :" + cost + " ms, rps : " + THREADS * 1000 / cost);
            if (j <= HOT_TOTAL) {
                NotifyTotal += cost;
            }


        }

        System.out.println("===========================END========================");
        int cost = NotifyTotal / HOT_TOTAL;
        System.out.println("Notify  cov ,cost : " + cost + " ms, rps : " + THREADS * 1000  / cost);
        cost = FutureTotal / HOT_TOTAL;
        System.out.println("Future  cov ,cost : " + cost + " ms, rps : " + THREADS * 1000  / cost);
        client.close();
        exe.shutdown();

    }
}


