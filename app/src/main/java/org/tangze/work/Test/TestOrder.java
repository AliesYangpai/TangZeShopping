package org.tangze.work.Test;

/**
 * Created by Administrator on 2016/10/21 0021.
 * 类描述
 * 版本
 */
public class TestOrder {

    private String order_id;

    private String order_state;


    private int tempCount;

    public TestOrder() {
    }


    public TestOrder(String order_id, int tempCount, String order_state) {
        this.order_id = order_id;
        this.tempCount = tempCount;
        this.order_state = order_state;
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }


    public int getTempCount() {
        return tempCount;
    }

    public void setTempCount(int tempCount) {
        this.tempCount = tempCount;
    }
}
