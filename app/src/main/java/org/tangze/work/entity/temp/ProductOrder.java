package org.tangze.work.entity.temp;



import org.tangze.work.entity.Address;
import org.tangze.work.entity.Product;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/18 0018.
 * 类描述  该类不保存到数据库，用于进入结算界面时，传递的order信息
 * 版本
 */
public class ProductOrder implements Serializable {


    /**
     * 传递前要设置的数据【创建订单时，如果是显示订单，则不存在数据设置先后】
     */

    private List<Product> products_transmit;//商品列表  wen订单提交新修改


    private int top_id ;//用户上家id

    private int user_id; //用户id

    private Map<Integer,Integer> map;//购买的总数量   key：商品id, value:商品数量

    private String order_number; //订单编号 当显示订单的时候使用这个字段



    /**
     * 到结算界面后需要进行的计算【创建订单时】
     */
    private String totalprice;//订单总价

    private String remarks; //订单备注信息

    private Address addressDefault; //用户默认地址；

    private List<String> prd_json_key;


    public ProductOrder() {
    }


    public int getTop_id() {
        return top_id;
    }

    public void setTop_id(int top_id) {
        this.top_id = top_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Integer> map) {
        this.map = map;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Address getAddressDefault() {
        return addressDefault;
    }

    public void setAddressDefault(Address addressDefault) {
        this.addressDefault = addressDefault;
    }


    public List<String> getPrd_json_key() {
        return prd_json_key;
    }

    public void setPrd_json_key(List<String> prd_json_key) {
        this.prd_json_key = prd_json_key;
    }


    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }


    //wen订单提交新修改
    public List<Product> getProducts_transmit() {
        return products_transmit;
    }
    //wen订单提交新修改
    public void setProducts_transmit(List<Product> products_transmit) {
        this.products_transmit = products_transmit;
    }
}
