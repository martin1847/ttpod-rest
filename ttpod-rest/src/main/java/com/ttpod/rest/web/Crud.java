package com.ttpod.rest.web;

import com.mongodb.*;
import com.mongodb.util.ObjectSerializer;
import com.ttpod.rest.common.doc.IMessageCode;
import com.ttpod.rest.common.doc.TwoTableCommit;
import com.ttpod.rest.common.util.Pager;
import com.ttpod.rest.common.util.WebUtils;
import com.ttpod.rest.web.spring.SessionInterceptor;
import groovy.lang.Closure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ttpod.rest.common.doc.MongoKey.*;

/**
 * 2013-06-03 11:30
 *
 * usage:
 *
     @Delegate Crud crud = new Crud(adminMongo.getCollection('gift_categories'),true,
        [_id:{giftKGS.nextId()},name:Str,order:Int,lucky:Eq1,status:Ne0,vip:Eq1,ratio:{it as Double}],
            new Crud.QueryCondition(){
                public DBObject sortby(HttpServletRequest req) {
                    return new BasicDBObject("order",-1);
                }
            })

 *@author : yangyang.cong@ttpod.com
 *
 */
public final class Crud {


    static final Logger log = LoggerFactory.getLogger(Crud.class);

    public final DBCollection table;

    final boolean intId ;
    private static final BasicDBObject EMPTY_QUERY = new BasicDBObject() ;
    private static final BasicDBObject SJ_DESC = new BasicDBObject(timestamp,-1);

    private final Map<String, Closure> props ;

    private  QueryCondition qc = QueryCondition.EMPTY;

    public static class QueryCondition{
        public DBObject query(HttpServletRequest req) {
            return EMPTY_QUERY;
        }
        public DBObject field(HttpServletRequest req) {
            return null;
        }

        public DBObject sortby(HttpServletRequest req) {
            return SJ_DESC;
        }

        protected boolean checkModify(Map<String,Object> data){
            return true;
        }

        public static final QueryCondition EMPTY = new QueryCondition();
    }


    public Crud(DBCollection table, Map<String, Closure> props) {
        this.table = table ;
        intId = false ;
        this.props = props ;
        lazyInit();
    }

    public Crud(DBCollection table, Map<String, Closure> props,QueryCondition qc) {
        this(table,props);
        this.qc = qc;
    }

    public Crud(DBCollection table, boolean intId, Map<String, Closure> props) {
        this.table = table;
        this.intId = intId;
        this.props = props;
        lazyInit();
    }

    public Crud(DBCollection table, boolean intId, Map<String, Closure> props,QueryCondition qc) {
        this(table,intId,props);
        this.qc = qc;
    }


    public Map list(HttpServletRequest req) {
        return list(req, table, this.qc.query(req), this.qc.field(req), this.qc.sortby(req),null);
    }


    public Map add(HttpServletRequest req) {

        Map<String,Object> map = new HashMap<String,Object>();
        for (Map.Entry<String, Closure> entry : props.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue().call(req.getParameter(key));
            if (val != null) {
                map.put(key, val);
            }
        }
        if(qc.checkModify(map) &&   table.save(new BasicDBObject(map)).getN() == 1){
            opLog(table.getName() + "_add", map);
        }

        return IMessageCode.OK;
    }


    public Object edit(HttpServletRequest req) {

        Object id = parseId(req);
        if(null == id){
            return IMessageCode.CODE0;
        }

        Map<String,Object> map = new HashMap<String,Object>();
        for (Map.Entry<String, Closure> entry : props.entrySet()) {
            String key = entry.getKey();
            if(key.equals(_id)){
                continue;
            }
            String strValue = req.getParameter(key);
            if(strValue != null){
                Object val = entry.getValue().call(strValue);
                if (val != null) {
                    map.put(key, val);
                }
            }
        }

        if(qc.checkModify(map) && map.size() > 0 && table.update(new BasicDBObject(_id,id),new BasicDBObject($set,map)).getN() == 1){
            map.put(_id,id);//just For Log
            opLog(table.getName() + "_edit", map);
        }
        return IMessageCode.OK;
    }


    public Map del(HttpServletRequest req) {
        DBObject remove = table.findAndRemove(new BasicDBObject(_id, parseId(req)));
        if (remove != null) {
            opLog(table.getName() + "_del", remove);
        }
        return IMessageCode.OK;
    }

    public Map show(HttpServletRequest req) {
        return (Map) table.findOne(parseId(req));
    }

    public Object parseId(HttpServletRequest req) {
        String id = req.getParameter(_id);
        return intId ? Integer.valueOf(id) : id;
    }

//
    private static DB adminDb;
    private static SessionInterceptor sessionInterceptor ;
    private static final String LOG_COLL_NAME = "ops";

    private static void lazyInit(){
        if( null == adminDb){
            adminDb = (DB) StaticSpring.get("adminDb");
            sessionInterceptor = StaticSpring.get(SessionInterceptor.class);
            if( ! adminDb.collectionExists(LOG_COLL_NAME) ){
                // 1 GB
                adminDb.createCollection(LOG_COLL_NAME,new BasicDBObject("capped",true).append("size",1<<30));
            }
        }
    }

    public static void opLog(String type,Object data){

        BasicDBObject obj = new BasicDBObject();
        Long tmp = System.currentTimeMillis();
        obj.put(_id,tmp);
        obj.put("type",type);
        obj.put("session",sessionInterceptor.getSession());
        obj.put("data",data);
        obj.put(timestamp,tmp);
        adminDb.getCollection(LOG_COLL_NAME).save(obj);
    }

    public static void opLog(Enum type,Object data){
        opLog(type.name(), data);
    }

    public static Map opLoglist(HttpServletRequest req,DBObject query){
        return list(req, adminDb.getCollection(LOG_COLL_NAME), query, ALL_FIELD, ID_DESC,null);
    }



    public static Map list(HttpServletRequest req, DBCollection table, DBObject query, DBObject field,DBObject sort){
        return list(req, table, query, field, sort,null);
    }

    public static Map list(HttpServletRequest req, DBCollection table, DBObject query, DBObject field,DBObject sort,Closure closureRenderList) {

        int p = WebUtils.getPage(req);
        int size = WebUtils.getPageSize(req);

        Pager pager = WebUtils.mongoPager(table, query, field, sort, p, size);
        if(closureRenderList != null ){
            List<BasicDBObject> list = (List<BasicDBObject>) pager.getData();
            if(list.size() > 0){
                closureRenderList.call(list);
            }
        }
        return WebUtils.normalOutPager(pager);

    }


    static WriteConcern writeConcern;
    static{
        try{
            writeConcern = StaticSpring.get(WriteConcern.class);
        }catch (Exception e){
            Crud.log.error("get customer writeConcern Error",e);
            writeConcern = WriteConcern.ACKNOWLEDGED;
        }
    }

    public static boolean doTwoTableCommit(BasicDBObject logWithId,TwoTableCommit commit ) {
        String log_id = (String) logWithId.get(_id);
        DBCollection mainColl  = commit.main();
        DBCollection logColl = commit.logColl();
        String name = logColl.getName();
        BasicDBObject query  = commit.queryWithId();
        if (logColl.count(new BasicDBObject(_id, log_id)) == 0)try{
            if(mainColl.update(new BasicDBObject(query).append ( name +"._id", new BasicDBObject($ne, log_id)),
                    commit.update().append($push, new BasicDBObject(name, logWithId.append(timestamp, System.currentTimeMillis()))),
                    false, false, writeConcern
            ).getN() == 1) {
                try{
                    if(commit.successCallBack()){
                        try{
                            logColl.save(logWithId, writeConcern);
                            mainColl.update(new BasicDBObject(_id,query.get(_id)),
                                    new BasicDBObject($pull, new BasicDBObject(name, new BasicDBObject(_id, log_id))),
                                    false, false, writeConcern);
                        }
                        catch (Exception e){
                            log.error("Save Logs [" +logColl.getFullName() +"] Error",e);
                        }
                        return true;
                    }
                }catch (Exception e){
                    try{commit.rollBack();}catch (Exception r){r.printStackTrace();}finally {
                        mainColl.update(new BasicDBObject(_id,query.get(_id)),
                                new BasicDBObject($pull, new BasicDBObject(name, new BasicDBObject(_id, log_id))),
                                false, false, writeConcern);
                    }
                    log.error("TwoTableCommit Error",e);
                }
            }
        }catch(Exception e){
            log.error("Main Db Error :[ " + mainColl.getFullName() ,e);
        }
        return false;
    }
}