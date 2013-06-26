package com.ttpod.rest.web;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * date: 13-2-22 下午1:43
 *
 * @author: yangyang.cong@ttpod.com
 */
public class StaticSpring implements ServletContextListener {


//    public static final ScheduledExecutorService EXE = Executors.newScheduledThreadPool(
//            Math.max(3,Runtime.getRuntime().availableProcessors()/3),
//            new CustomizableThreadFactory("StaticSpring.EXE")
//    );


    static final ExecutorService EXE =
            new ThreadPoolExecutor(Math.max(4,Runtime.getRuntime().availableProcessors()), 160,
                    60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    new CustomizableThreadFactory("StaticSpring.EXE")) ;

    private static ApplicationContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        context = WebApplicationContextUtils.getRequiredWebApplicationContext(
                sce.getServletContext()
        );


//        WebSupport.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        EXE.shutdownNow();
    }


    public static ApplicationContext getContext(){

        if( null == context){
            throw new RuntimeException("context NOT INIT..");
        }

        return context;
    }

    public static Object get(String beanName){
        return getContext().getBean(beanName);
    }

    public static <T>T get(Class<T> clssName){
        return getContext().getBean(clssName);
    }

    public static void execute(Runnable run){
        EXE.execute(run);
    }
}
