package org.tangze.work.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/16 0016.
 * 类描述  信息展示对象
 * 版本
 */
public class ShowInfo extends DataSupport implements Serializable {

    /**
     * 自增id
     */
    private int id;


    private String showinfo_Id;

    private String title;

    private String time;

    private String thumbnail;


    public ShowInfo() {
    }

    public String getShowinfo_Id() {
        return showinfo_Id;
    }

    public void setShowinfo_Id(String showinfo_Id) {
        this.showinfo_Id = showinfo_Id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
