package org.tangze.work.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/16 0016.
 * 类描述
 * 版本
 */
public class MyCollection extends DataSupport implements Serializable {


    /**
     * 自增id
     */

    private int id;

    /**
     * 我的收藏id
     */
    private int myCollection_id;

    /**
     * 用户id
     */
    private int userId;


    /**
     * 产品Id
     */
    private int productId;


    public MyCollection() {
    }


    public int getMyCollection_id() {
        return myCollection_id;
    }

    public void setMyCollection_id(int myCollection_id) {
        this.myCollection_id = myCollection_id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
