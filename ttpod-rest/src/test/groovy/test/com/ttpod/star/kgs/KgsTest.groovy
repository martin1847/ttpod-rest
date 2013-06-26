package test.com.ttpod.star.kgs

import com.ttpod.rest.persistent.KGS
import org.junit.Test
import test.BaseTest

import javax.annotation.Resource
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier

import static org.junit.Assert.assertEquals

/**
 * date: 13-3-11 下午6:06
 * @author: yangyang.cong@ttpod.com
 */
class KgsTest extends BaseTest{

    @Resource
    KGS seqKGS;

    @Test
    public void testMulitThread(){

        int count = 60;



        final CyclicBarrier cb=new CyclicBarrier(count);
        final CountDownLatch cd=new CountDownLatch(count);

        final List<Set<Long>> totalIds = new ArrayList<Set<Long>>(count);

        final int ID_NEED = 500;

        for(int i=0 ; i< count; i++){
            new Thread(){

                Set<Long> ids;
                {
                    ids = new HashSet<Long>(ID_NEED);
                    totalIds.add(ids);
                }


                public void run(){
                    try {
                        cb.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for(int j=0;j< ID_NEED;j++){
                        ids.add(seqKGS.nextId());
//                     if(!ids.add(id)){
//                         System.out.println(Thread.currentThread().getName() + "\t"+ id);
//                     }
                    }
                    cd.countDown();

                }
            }.start();
        }


        try {
            cd.await();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Set<Long> totals = new HashSet<Long>(ID_NEED * count);

        for(Set<Long> set : totalIds){
            totals.addAll(set);
        }

        assertEquals(totals.size() ,ID_NEED * count);

    }

    @Test
    void testOne(){
        println seqKGS.nextId()
    }

}
