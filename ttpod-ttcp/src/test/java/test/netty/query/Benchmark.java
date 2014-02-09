package test.netty.query;

import com.ttpod.netty.rpc.RequestBean;
import com.ttpod.netty.rpc.client.ClientHandler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

class Benchmark implements Runnable{
    String name;
    ClientHandler handler;
    ExecutorService exe;
    int reqCount;

    Benchmark(String name, ClientHandler handler, ExecutorService exe, int reqCount) {
        this.name = name;
        this.handler = handler;
        this.exe = exe;
        this.reqCount = reqCount;
    }

    public void run() {
        final CountDownLatch latch = new CountDownLatch(reqCount);
        for (int i = reqCount; i > 0; i--) {
            exe.execute(new Runnable() {
                public void run() {
                    String q = Thread.currentThread().getName();
                    RequestBean req = new RequestBean(RequestBean.QueryServie.SONG, (short) 1, (short) 50, q);
                    rpcCall(req);
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    protected void rpcCall(RequestBean req){
        handler.rpc(req);
    }

    public int costMills(){
        long b = System.currentTimeMillis();
        run();
        int cost = (int) (System.currentTimeMillis() - b);
        System.out.println(name +"  cost : " + cost + " ms, rps : " +( reqCount * 1000  / cost));

        return cost;
    }


    static void VS(Benchmark a,Benchmark b,int TIMES){
        int aTotal = 0;
        int bTotal = 0;
        for (int j = TIMES + 5 ; j > 0; j--) {// Hot for 5 times.
            int costa = a.costMills();
            int costb = b.costMills();
            if (j <= TIMES) {
                aTotal += costa;
                bTotal += costb;
            }
        }
        System.out.println("===========================END========================");
        int cost = aTotal / TIMES;
        System.out.println(a.name + "  cov ,cost : " + cost + " ms, rps : " + a.reqCount * 1000  / cost);
        cost = bTotal / TIMES;
        System.out.println(b.name + "  cov ,cost : " + cost + " ms, rps : " + b.reqCount * 1000  / cost);
    }
}