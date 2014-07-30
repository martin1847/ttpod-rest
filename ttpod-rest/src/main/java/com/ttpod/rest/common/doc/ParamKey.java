package com.ttpod.rest.common.doc;

/**
 *
 * 请求中的入参与出参
 *
 * date: 12-8-21 上午9:58
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface ParamKey {

    interface In{


        String page =  System.getProperty( "rest.page" ,"page") ;

        String callback = System.getProperty( "rest.callback" ,"callback") ;

        String size = System.getProperty( "rest.size" ,"size") ;

        int PAGE_DEFAULT = 1;

        int SIZE_DEFAULT = Integer.getInteger("default.size",6);

        int SIZE_MAX_ALLOW = 200;


    }



    interface Out{

        String code = System.getProperty( "rest.code" ,"code") ;

        String data = System.getProperty( "rest.data" ,"data") ;

        /**
         * 总行数
         */
        String rows = System.getProperty( "rest.rows" ,"rows") ;

        /**
         * 总页数
         */
        String pages =  System.getProperty( "rest.pages" ,"pages") ;

        /**
         * For Debug,执行时间，毫秒
         */
        String exec = "exec";

        /**
         * For Debug，调试信息
         */
        String msg = "msg";


        Integer SUCCESS = 1;

    }


}
