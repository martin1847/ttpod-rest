package test.java7;

import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class Temp extends Benchmark {
    //    public static void main(String[] args) throws Exception {
//        Runner.main(Temp.class, args);
//    }
    @Test
    public void testBench() {
        CaliperMain.main(Temp.class, new String[]{});
    }

    public void timeReflect(int reps) throws Exception {
        Method m = K.class.getMethod("exec", Object.class);

        K k = new K();

        for (int i = 0; i < reps; i++) {
            m.invoke(k, i);
        }
    }

    public void timeHandle(int reps) throws Throwable {
        MethodHandle mh = MethodHandles.lookup().findVirtual(K.class,
                "exec", MethodType.methodType(void.class, Object.class));

        K k = new K();

        for (int i = 0; i < reps; i++) {
            mh.invokeExact(k, (Object) i);
        }
    }

    public void timeDirect(int reps) throws Exception {
        Method m = K.class.getMethod("exec", Object.class);

        K k = new K();

        for (int i = 0; i < reps; i++) {
            k.exec(i);
        }
    }

    public void timeIface(int reps) throws Exception {
        M m = new K();

        for (int i = 0; i < reps; i++) {
            m.exec(i);
        }
    }

    public void timeStatic(int reps) throws Exception {
        for (int i = 0; i < reps; i++) {
            K.execS(i);
        }
    }
}
