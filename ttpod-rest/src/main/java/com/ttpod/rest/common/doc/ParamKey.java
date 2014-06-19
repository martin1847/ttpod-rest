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


        String page = "page";

        String callback= "callback";

        String size = "size";

        int PAGE_DEFAULT = 1;

        int SIZE_DEFAULT = Integer.getInteger("default.size",6);

        int SIZE_MAX_ALLOW = 200;

        String user_id = "user_id";

    }



    interface Out{

        String code = "code";

        String data = "data";

        /**
         * 总行数
         */
        String rows = "rows";

        /**
         * 总页数
         */
        String pages = "pages";

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
