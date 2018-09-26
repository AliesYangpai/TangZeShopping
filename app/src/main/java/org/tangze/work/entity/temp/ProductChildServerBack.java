package org.tangze.work.entity.temp;

/**
 * Created by Administrator on 2016/10/23 0023.
 * 类描述
 * 版本
 * 订单中每一个产品对象的实体类
 */
public class ProductChildServerBack {

    private String thumnail; //头像

    private String product_id; //产品id;

    private String product_name;//产品名称

    private String local_price;//现价

    private String original_price;//原价

    private String buyCount;//购买数量


    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getLocal_price() {
        return local_price;
    }

    public void setLocal_price(String local_price) {
        this.local_price = local_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }
}
