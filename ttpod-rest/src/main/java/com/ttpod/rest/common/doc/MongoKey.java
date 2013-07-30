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




    //aggregate  pipeline operators
    String $match = "$match";
    String $project = "$project";
    String $limit = "$limit";
    String $skip = "$skip";
    String $unwind = "$unwind";
    String $group = "$group";
    String $sort = "$sort";
    String $geoNear = "$geoNear";


    // geo operators
    String $within = "$within";
    String $near = "$near";
    String $nearSphere = "$nearSphere";
    String $box = "$box";
    String $center = "$center";
    String $polygon = "$polygon";
    String $centerSphere = "$centerSphere";

    String $natural = "$natural";//sort( { $natural: 1 } )documents in the order they exist on disk

    String timestamp ="timestamp";


    DBObject SJ_DESC = new UnmodifDBObject(new BasicDBObject(timestamp,-1));
    DBObject ID_DESC = new UnmodifDBObject(new BasicDBObject(_id,-1));
    DBObject ALL_FIELD = null;
    DBObject NO_SORT = null;
    DBObject EMPTY = new UnmodifDBObject(Collections.emptyMap());

}
