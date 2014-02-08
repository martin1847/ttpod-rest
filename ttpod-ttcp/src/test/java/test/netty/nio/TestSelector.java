package test.netty.nio;

import java.nio.channels.Selector;
import java.lang.RuntimeException;
import java.lang.Thread;

public class TestSelector {
    private static final int MAXSIZE = 5;

    public static final void main(String argc[]) {
        Selector[] sels = new Selector[MAXSIZE];

        try {
            for (int i = 0; i < MAXSIZE; ++i) {
                sels[i] = Selector.open();
                //sels[i].close();
            }
            Thread.sleep(60000);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}