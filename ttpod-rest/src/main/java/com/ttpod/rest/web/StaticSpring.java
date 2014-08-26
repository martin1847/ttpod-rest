package com.ttpod.rest.web;

import com.ttpod.rest.AppProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.beans.Introspector;
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
            new ThreadPoolExecutor(Math.max(Integer.parseInt(AppProperties.get("StaticSpring.thread.min","8"))
                    ,Runtime.getRuntime().availableProcessors()), 512,
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


    /**
     * Convert the first character of ClassName from upper case to lower case, then get from Spring .
     * <br>
     *     Thus "FooBah" becomes "fooBah" and "X" becomes "x", but "URL" stays as "URL".
     */
    public static <T>T camelGet(Class<T> className){
        return (T) getContext().getBean(Introspector.decapitalize(className.getSimpleName()));
    }


    /**
     * @see #camelGet(Class)
     */
    @Deprecated public static Object get(String beanName){
        return getContext().getBean(beanName);
    }

    public static <T>T get(Class<T> clssName){
        return getContext().getBean(clssName);
    }

    public static void execute(Runnable run){
        EXE.execute(run);
    }

}
