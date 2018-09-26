package org.tangze.work.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/19 0019.
 * 类描述  商品实体类
 * 版本
 */
public class Product extends DataSupport implements Serializable {





    //    1.自增Id
    private int id;

    //    1.商品_Id
    private int product_id;


    //    7.商品分类id
    private int classify_id;   //如果产品上级是二级分类，则这个id是二级分类的id，如果产品只属于一级分类id则这个字段对应一级分类id


    //  userId //用户id

//    private int user_id;

    //    2.商品名称
    private String productName;


    private String spellAll;//全拼


    private String spellSimple;//简拼


//    3.市场售价

    private String originalPrice; //市场售价


    //5.本店售价
    private String localPrice;//本店售价



    //8.库存量

    private int stock;//


    //9.缩略图

    private String thumnail;//产品缩略语


    //10.是否上架

    private int isShelves;//是否上架


    //11.产品描述
    private String productDescribe;//产品描述


    //12.首页推荐

    private String isRecommand;//首页推荐


    //13.新品
    private String isNew;//新品


    //15.是否热卖产品

    private String isHot;//


    //16.精品
    private String isBoutique;


    //17.是否免运费

    private String freeCarriage;

    //    6.销量
    private int salesVolume;


    //    7.商品描述
    private String productDetial;

//    8.图片id【集合】

    private List<Integer> productPicIds = new ArrayList<>();


    //    9.是否收藏
    private String isCollect;

    //    10.商品运费
    private String carriage;


    /**
     * ****************************配合添加的字段***********************************************
     */
    private String picture; //产品图片


    private String time ; //时间

//    11.字母索引首字母




    private int headerId; //仅仅作为header的分组显示，无其他意义

    public Product() {
    }


    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }


    public int getClassify_id() {
        return classify_id;
    }

    public void setClassify_id(int classify_id) {
        this.classify_id = classify_id;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getProductDetial() {
        return productDetial;
    }

    public void setProductDetial(String productDetial) {
        this.productDetial = productDetial;
    }

    public List<Integer> getProductPicIds() {
        return productPicIds;
    }

    public void setProductPicIds(List<Integer> productPicIds) {
        this.productPicIds = productPicIds;
    }

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public String getCarriage() {
        return carriage;
    }

    public void setCarriage(String carriage) {
        this.carriage = carriage;
    }


    public String getLocalPrice() {
        return localPrice;
    }

    public void setLocalPrice(String localPrice) {
        this.localPrice = localPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public int getIsShelves() {
        return isShelves;
    }

    public void setIsShelves(int isShelves) {
        this.isShelves = isShelves;
    }

    public String getProductDescribe() {
        return productDescribe;
    }

    public void setProductDescribe(String productDescribe) {
        this.productDescribe = productDescribe;
    }

    public String getIsRecommand() {
        return isRecommand;
    }

    public void setIsRecommand(String isRecommand) {
        this.isRecommand = isRecommand;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }



    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }


    public String getIsBoutique() {
        return isBoutique;
    }

    public void setIsBoutique(String isBoutique) {
        this.isBoutique = isBoutique;
    }

    public String getFreeCarriage() {
        return freeCarriage;
    }

    public void setFreeCarriage(String freeCarriage) {
        this.freeCarriage = freeCarriage;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

//    public int getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(int user_id) {
//        this.user_id = user_id;
//    }


    public String getSpellAll() {
        return spellAll;
    }

    public void setSpellAll(String spellAll) {
        this.spellAll = spellAll;
    }

    public String getSpellSimple() {
        return spellSimple;
    }

    public void setSpellSimple(String spellSimple) {
        this.spellSimple = spellSimple;
    }
}
