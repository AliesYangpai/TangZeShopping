package org.tangze.work.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/18 0018.
 * 类描述   真正的order表，此表需要保存数据库，当成功添加一个订单，此表会加一条订单号的记录
 * 版本
 */
public class DeliverOrder extends DataSupport implements Serializable{

    private int id; //自增id

    private String order_id; //服务器返回的订单id

    private int order_user_id; //提交订单的用户id

    public DeliverOrder() {
    }


    public DeliverOrder(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getOrder_user_id() {
        return order_user_id;
    }

    public void setOrder_user_id(int order_user_id) {
        this.order_user_id = order_user_id;
    }
}
