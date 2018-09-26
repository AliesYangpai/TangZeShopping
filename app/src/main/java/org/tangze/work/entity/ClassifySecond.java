package org.tangze.work.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/12 0012.
 * 类描述   二级分类
 * 版本
 */
public class ClassifySecond extends DataSupport implements Serializable {

    //    1.分类id
    private int id;



    //    2.分类_id
    private int classifySecond_id;

    //    2.分类名称
    private String classfiyName;


    //    3.上级分类Id
    private int topId;  //0代表该分类是一级分类


    //    4.分类缩略图
    private String thumbnail;


    //    5.分类创建时间
    private String createTime;


    public ClassifySecond() {
    }


    public int getClassifySecond_id() {
        return classifySecond_id;
    }

    public void setClassifySecond_id(int classifySecond_id) {
        this.classifySecond_id = classifySecond_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
