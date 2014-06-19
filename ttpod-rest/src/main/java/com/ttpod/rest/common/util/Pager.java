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
public class Pager<ListOrObj> implements Serializable{

    private static final long serialVersionUID = 181494579783120364L;

    int currentPage;
    int size;
    int pages;

    long rows;
    ListOrObj data;

    public Pager(){

    }
    public Pager(long countRows, int currentPage, int size)
    {
        this.rows = countRows;
        this.size = size;
        this.pages = (int)((countRows + size - 1) / size);
        this.currentPage = (currentPage < 1 ? 1 : currentPage > this.pages ? this.pages : currentPage);
    }

    public Pager(ListOrObj data,long countRows, int currentPage, int size)
    {
        //this(countRows, ( startRow/ (size = size < 1 ? 1 : size) +1), (size = size < 1 ? 1 : size) );

        this(countRows, currentPage, size);
        this.data = data;
    }

    public long getRows()
    {
        return this.rows;
    }

    public ListOrObj getData()
    {
        return this.data;
    }

    public void setData(ListOrObj data) {
        this.data = data;
    }

    public int getCurrentPage()
    {
        return this.currentPage;
    }

    public int getPages()
    {
        return this.pages;
    }

    public int getSize()
    {
        return this.size;
    }

    public void setRows(long rows)
    {
        this.rows = rows;
    }

    /**
     * 返回一个空页对象
     * @return
     */
    public static Pager empty(){
        return new Pager(0,0,100);
    }
}
