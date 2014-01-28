package com.ttpod.netty.bean;

/**
 * date: 14-1-28 上午11:31
 *
 * @author: yangyang.cong@ttpod.com
 */
public class QueryReq {


    byte service =0;
    byte page =1 ;
    byte size =50;
    String q = "TTPOD";

    public QueryReq(){}
    public QueryReq(byte service, byte page, byte size, String q) {
        this.service = service;
        this.page = page;
        this.size = size;
        this.q = q;
    }

    public enum QueryServie{
        SONG,SINGER,ALBUM;

        public byte flag(){
            return (byte) ordinal();
        }
    }


    public byte getService() {
        return service;
    }

    public void setService(byte service) {
        this.service = service;
    }

    public byte getPage() {
        return page;
    }

    public void setPage(byte page) {
        this.page = page;
    }

    public byte getSize() {
        return size;
    }

    public void setSize(byte size) {
        this.size = size;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    @Override
    public String toString() {
        return "QueryReq{" +
                "service=" + service +
                ", page=" + page +
                ", size=" + size +
                ", q='" + q + '\'' +
                '}';
    }
}
