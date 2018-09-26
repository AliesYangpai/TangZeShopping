package org.tangze.work.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/19 0019.
 * 类描述  分类表 一级分类
 * 版本
 */
public class Classify extends DataSupport implements Serializable{


  //自增Id
    private int id;

    //    1.分类id
    private int classify_id;

//    2.分类名称
    private String classfiyName;


//    3.上级分类Id
    private int topId;  //0代表该分类是一级分类


//    4.分类缩略图
    private String thumbnail;


//    5.分类创建时间
    private String createTime;


    public Classify() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassify_id() {
        return classify_id;
    }

    public void setClassify_id(int classify_id) {
        this.classify_id = classify_id;
    }

    public String getClassfiyName() {
        return classfiyName;
    }

    public void setClassfiyName(String classfiyName) {
        this.classfiyName = classfiyName;
    }

    public int getTopId() {
        return topId;
    }

    public void setTopId(int topId) {
        this.topId = topId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
