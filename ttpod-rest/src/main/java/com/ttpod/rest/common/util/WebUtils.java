package com.ttpod.rest.common.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.ttpod.rest.AppProperties;
import com.ttpod.rest.common.doc.ParamKey;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * date: 13-5-15 上午10:04
 *
 * @author: yangyang.cong@ttpod.com
 */
public class WebUtils extends org.springframework.web.util.WebUtils{

    /**
     * 从 请求中获取 page 参数，第几页.
     */
    public static int  getPage(HttpServletRequest request){
        int page = ServletRequestUtils.getIntParameter(
                request, ParamKey.In.page, ParamKey.In.PAGE_DEFAULT
        );

        if(page <= 0){
            page = ParamKey.In.PAGE_DEFAULT;
        }

        return page;
    }

    /**
     * 从 请求中获取 size 参数，页大小.
     */
    public static int  getPageSize(HttpServletRequest request){
        int size = ServletRequestUtils.getIntParameter(
                request,ParamKey.In.size,ParamKey.In.SIZE_DEFAULT
        );

        if(size <= 0){
            size = ParamKey.In.SIZE_DEFAULT;
        }else if(size >= ParamKey.In.SIZE_MAX_ALLOW){
            size = ParamKey.In.SIZE_MAX_ALLOW;
        }

        return size;
    }


    //
    public static Map normalOutPager(Pager pager){
        return normalOutModel(pager.getRows(), pager.getPages(), pager.getData());
    }


    public static Map normalOutModel(long count,int allPage,Object data){

        Map map = new HashMap();
        map.put(ParamKey.Out.code, ParamKey.Out.SUCCESS);
        if(count>=0){
            map.put(ParamKey.Out.rows,count);
        }
        if(allPage>=0){
            map.put(ParamKey.Out.pages,allPage);
        }
        map.put(ParamKey.Out.data,data);

        return map;

    }

//    /**
//     * 数据填充到 request 中
//     * @param request
//     * @param map
//     */
//    public static void populate(HttpServletRequest request,Map<String,Object> map){
//
//    }




    public static Date getEtime(HttpServletRequest request){
        return getTime(request,"etime");
    }

    public static Date getStime(HttpServletRequest request){
        return getTime(request,"stime");
    }
    static final String DFMT = "yyyy-MM-dd HH:mm:ss";
    private static Date getTime(HttpServletRequest request,String key)  {
        String str = request.getParameter(key);
        if(null != str && str.length() >  0 ){
            try {
                return new SimpleDateFormat(DFMT).parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static QueryBuilder fillTimeBetween(HttpServletRequest req){
        QueryBuilder query = QueryBuilder.start();
        Date stime = getStime(req);
        Date etime = getEtime(req);
        if (stime !=null || etime !=null){
            query.and("timestamp");
            if(stime != null){
                query.greaterThanEquals(stime.getTime());
            }
            if (etime != null){
                query.lessThan(etime.getTime());
            }
        }
        return query;
    }



    static final Set<String> nocount_table = new HashSet<String>();
    static {
        String[] str = AppProperties.get("api.nocount_table","").split(",");
        for(String table : str){
            nocount_table.add(table);
        }
    }

    public static Pager<List<DBObject>> mongoPager(DBCollection table, DBObject query, DBObject field, DBObject sort, int page, int pageSize) {
        return new Pager<List<DBObject>>(
                table.find(query, field).sort(sort).skip((page - 1) * pageSize).limit(pageSize).toArray()
                ,nocount_table.contains(table.getName()) ? -1:table.count(query), page, pageSize);
    }


    public static BasicDBObject $$(String key,Object value){
        return new BasicDBObject(key,value);
    }

    public static BasicDBObject $$(Map map){
        return new BasicDBObject(map);
    }


}
