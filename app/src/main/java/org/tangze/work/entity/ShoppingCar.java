package org.tangze.work.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/17 0017.
 * 类描述  /购物车，这个只进行本地保存
 * 版本
 */
public class ShoppingCar  extends  DataSupport implements Serializable {


    private int id; //自增id

    private int user_id ;

    private int productId;

    private int buyCount;



    public ShoppingCar() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

}
