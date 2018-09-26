package org.tangze.work.Test;

/**
 * Created by Administrator on 2016/10/7 0007.
 * 类描述
 * 版本
 */
public class ProductTest {

    private int id;

    private String name;

    private String priceHot;

    private String price;

    private String thumnail;

    private int count;


    public ProductTest(String name, int count, String price, String priceHot) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.priceHot = priceHot;
    }


    public ProductTest(String name, String priceHot, String price, String thumnail, int count) {
        this.name = name;
        this.priceHot = priceHot;
        this.price = price;
        this.thumnail = thumnail;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceHot() {
        return priceHot;
    }

    public void setPriceHot(String priceHot) {
        this.priceHot = priceHot;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }
}
