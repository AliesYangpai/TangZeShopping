package org.tangze.work.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/24 0024.
 * 类描述     搜索历史表
 * 版本
 */
public class SearchHistory extends DataSupport implements Serializable {


    //自增Id
    private int id;



    //    1.商品分类id

    private String search_text;


    public SearchHistory() {
    }

    public String getSearch_text() {
        return search_text;
    }

    public void setSearch_text(String search_text) {
        this.search_text = search_text;
    }
}
