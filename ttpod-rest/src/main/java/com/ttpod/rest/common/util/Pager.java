package com.ttpod.rest.common.util;

import groovy.transform.CompileStatic;

import java.io.Serializable;

/**
 *
 * 分页对象
 *
 *
 * date: 12-8-21 上午10:32
 *
 * @author: yangyang.cong@ttpod.com
 */
@CompileStatic
public class Pager implements Serializable{

    private static final long serialVersionUID = 181494579783120364L;

    long count;
    int currentPage;
    int pageSize;
    int allPage;
    Object data;

    public Pager(){

    }
    public Pager(long countRows, int currentPage, int pageSize)
    {
        this.count = countRows;
        this.pageSize = pageSize;
        this.allPage = (int)((countRows + pageSize - 1) / pageSize);
        this.currentPage = (currentPage < 1 ? 1 : currentPage > this.allPage ? this.allPage : currentPage);
    }

    public Pager(Object data,long countRows, int currentPage, int pageSize)
    {
        //this(countRows, ( startRow/ (pageSize = pageSize < 1 ? 1 : pageSize) +1), (pageSize = pageSize < 1 ? 1 : pageSize) );

        this(countRows, currentPage, pageSize);
        this.data = data;
    }

    public long getCount()
    {
        return this.count;
    }

    public Object getData()
    {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCurrentPage()
    {
        return this.currentPage;
    }

    public int getAllPage()
    {
        return this.allPage;
    }

    public int getPageSize()
    {
        return this.pageSize;
    }

    public void setCount(long count)
    {
        this.count = count;
    }

    /**
     * 返回一个空页对象
     * @return
     */
    public static Pager empty(){
        return new Pager(0,0,100);
    }
}
