package test.java7;

public class K implements M {
    static long s = 0;

    long l = 0;

    @Override
    public void exec(Object o) {
        l += o.hashCode();
    }

    public static void execS(Object o) {
        s += o.hashCode();
    }
}
interface M {
    void exec(Object o);
}