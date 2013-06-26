package test.test.java7;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodAccessExampleWithArgs {
    private final int i;
 
    public MethodAccessExampleWithArgs(int i_) {
        i = i_;
    }
 
       private void bar(int j, String msg) {
        System.out.println("Private Method \'bar\' successfully accessed : "
                + i +", "+ j +" : "+ msg +"!");
    }

    private Map map(int j, String msg) {
        Map obj = new HashMap();
        obj.put("j",j);
        obj.put("msg",msg);
        return obj;
    }


    // Using Reflection
    public static Method makeMethod() {
        Method meth = null;
 
        try {
            Class<?>[] argTypes =
                new Class[] { int.class, String.class };
 
            meth = MethodAccessExampleWithArgs.class.
                            getDeclaredMethod("bar", argTypes);
 
            meth.setAccessible(true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
 
        return meth;
    }
 
    // Using method handles
    public static MethodHandle makeMh(Class t,String name) {
        MethodHandle mh;
 
        MethodType desc = MethodType.methodType(t, int.class, String.class);
 
        try {
            mh = MethodHandles.lookup().findVirtual(MethodAccessExampleWithArgs.class, name, desc);
            System.out.println("mh="+mh);
        } catch (Exception e) {
            throw (AssertionError)new AssertionError().initCause(e);
        }
        return mh;
      }
  }