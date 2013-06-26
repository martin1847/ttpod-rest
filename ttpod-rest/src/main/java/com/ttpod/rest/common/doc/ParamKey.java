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

        int SIZE_DEFAULT = 6;

        int SIZE_MAX_ALLOW = 200;

        String user_id = "user_id";

    }



    interface Out{

        String code = "code";

        String msg = "msg";

        String count = "count";

        String data = "data";

        String all_page = "all_page";

        String exec = "exec";


        Integer SUCCESS = 1;

    }


}
