package com.ttpod.rest.common.doc;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import groovy.transform.Immutable;

import java.util.Collections;

/**
 * date: 13-3-22 下午4:16
 *
 * @author: yangyang.cong@ttpod.com
 */
public interface MongoKey {
    String _id = "_id";
    String $inc = "$inc";
    String $gte = "$gte";
    String $gt = "$gt";
    String $lt = "$lt";
    String $lte = "$lte";

    String $set = "$set";
    String $unset = "$unset";
    String $setOnInsert = "$setOnInsert";
    String $addToSet = "$addToSet";
    String $or = "$or";
    String $not = "$not";

    String $pull = "$pull";
    String $push = "$push";
    String $in = "$in";
    String $ne = "$ne";
    String $exists = "$exists";

//    String finance_coin_count = "finance.coin_count";
//
//    String finance_log="finance_log";
//    String finance_log_id=finance_log+"."+_id;

    String timestamp ="timestamp";


    DBObject SJ_DESC = new UnmodifDBObject(new BasicDBObject(timestamp,-1));
    DBObject ID_DESC = new UnmodifDBObject(new BasicDBObject(_id,-1));
    DBObject ALL_FIELD = null;
    DBObject NO_SORT = null;
    DBObject EMPTY = new UnmodifDBObject(Collections.emptyMap());

}
