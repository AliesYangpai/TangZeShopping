package org.tangze.work.entity.temp;

import org.tangze.work.entity.Address;
import org.tangze.work.entity.Product;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25 0025.
 * 类描述 用于查看订单时候的实体类
 * 版本
 */
public class ProductOrderShow {

    private List<Product> products;

    private String freight;//运费

    private String express;//快递公司

    private int user_id; //用户id

    private String courier_number;//购买总数

    private String totalPrice;//总价

    private String consignee;//收货人

    private String tel;//联系电话

    private Address address;//联系地址对象

    private String remark;//订单备注


    public ProductOrderShow() {
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCourier_number() {
        return courier_number;
    }

    public void setCourier_number(String courier_number) {
        this.courier_number = courier_number;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
