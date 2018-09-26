package org.tangze.work.Test;

/**
 * Created by Administrator on 2016/10/22 0022.
 * 类描述
 * 版本
 */
public class TestOrderChild {


    private int id;

    private String thumnail; //头像

    private String name;//名称


    private String local_price;//现价

    private String original_price;//原价

    private String buyCount;//购买数量


    public TestOrderChild() {
    }


    public TestOrderChild(String thumnail, String name, String local_price, String original_price, String buyCount) {
        this.thumnail = thumnail;
        this.name = name;
        this.local_price = local_price;
        this.original_price = original_price;
        this.buyCount = buyCount;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocal_price() {
        return local_price;
    }

    public void setLocal_price(String local_price) {
        this.local_price = local_price;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }
}
