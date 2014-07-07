package com.example.web

import com.mongodb.DB
import com.mongodb.DBObject
import com.ttpod.rest.anno.Rest
import com.ttpod.rest.persistent.KGS
import com.ttpod.rest.web.Crud
import com.ttpod.rest.web.StaticSpring

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import static com.ttpod.rest.common.doc.MongoKey.ALL_FIELD
import static com.ttpod.rest.common.util.WebUtils.$$
import static com.ttpod.rest.groovy.CrudClosures.*

/**
 * date: 14-7-7 14:47
 * @author: yangyang.cong@ttpod.com
 */
@Rest
class CrudController extends Base{


    @Resource
    DB mainDb
    @Resource
    KGS kgs

    /**
     * 生成增删改查代码，对应后端管理接口
     */
//    @Delegate Crud crud = new Crud(((DB)StaticSpring.get("adminDb")).getCollection('test'),true,
//        [_id:{kgs.nextId("test.namespace")},name:Str,order:Int,status:Ne0,timestamp:Timestamp],
//        new Crud.QueryCondition(){
//            public DBObject sortby(HttpServletRequest req) {
//                $$("order",-1)
//            }
//    })

    /**
     * 前端分页查询
     * /crud/page?page=1&size=10
     */
    def page(HttpServletRequest req){
        Crud.list(req, mainDb.getCollection('test'),$$("status",true),ALL_FIELD,$$("order",-1))
    }


}
