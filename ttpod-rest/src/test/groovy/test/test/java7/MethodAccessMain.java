package test.test.java7;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MethodAccessMain {
 
    private static void withReflectionArgs() {
        Method meth = MethodAccessExampleWithArgs.makeMethod();
 
        MethodAccessExampleWithArgs mh0 =
            new MethodAccessExampleWithArgs(0);
        MethodAccessExampleWithArgs mh1 =
            new MethodAccessExampleWithArgs(1);
 
        try {
            System.out.println("Invocation using Reflection");
            meth.invoke(mh0, 5, "Jabba the Hutt");
            meth.invoke(mh1, 7, "Boba Fett");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
 
    private static void withMhArgs() {
        MethodHandle mh = MethodAccessExampleWithArgs.makeMh(void.class,"bar");
        MethodHandle mhMap = MethodAccessExampleWithArgs.makeMh(Map.class,"map");

        MethodAccessExampleWithArgs mh0 =
            new MethodAccessExampleWithArgs(0);
        MethodAccessExampleWithArgs mh1 =
            new MethodAccessExampleWithArgs(1);
 
        try {
            System.out.println("Invocation using Method Handle");
            mh.invokeExact(mh0, 42, "R2D2");
            mh.invokeExact(mh1, 43, "C3PO");
            System.out.println(
                    (Map)mhMap.invokeExact(mh0,43, "mapMsg")
            );
//            System.out.println( //WrongMethodTypeException: (ILjava/lang/String;)Ljava/util/Map;
//            //// cannot be called as (Ltest/test/java7/MethodAccessExampleWithArgs;ILjava/lang/String;)Ljava/lang/Object;
//                    // or use invoke
//                    mhMap.invokeExact(mh0,43, "mapMsg")
//            );

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public  void testMain() {
        withReflectionArgs();
        withMhArgs();
    }
}