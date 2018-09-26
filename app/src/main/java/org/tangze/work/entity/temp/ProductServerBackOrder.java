package org.tangze.work.entity.temp;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/22 0022.
 * 类描述 已经提交的订单，不保存到数据库，用于帮助显示，订单界面中Server返回的信息
 * 版本
 */
public class ProductServerBackOrder implements Serializable{

    private int id ;//订单id 真正的id

    private String order_num; //单个订单编号

    private String content;//组合数据;

    private String totalprice;//订单总价

    private String freight;//订单运费

    private String express;//快递公司

    private String time;//下单时间

    private int state;//订单状态

    private String rmarks;//备注信息

    private String order_user_id;//下单用户id

    private String consignee; //收货人

    private String tel;//联系方式

    private String address;//详细送货地址




    public ProductServerBackOrder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRmarks() {
        return rmarks;
    }

    public void setRmarks(String rmarks) {
        this.rmarks = rmarks;
    }

    public String getOrder_user_id() {
        return order_user_id;
    }

    public void setOrder_user_id(String order_user_id) {
        this.order_user_id = order_user_id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
