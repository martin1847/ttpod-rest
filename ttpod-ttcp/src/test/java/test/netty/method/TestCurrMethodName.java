package test.netty.method;

/**
 * date: 14-2-8 下午2:54
 *
 * @author: yangyang.cong@ttpod.com
 */
public class TestCurrMethodName {


    static void abc(){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(methodName);
    }

    public static void main(String[] args) {
        abc();
    }

}
