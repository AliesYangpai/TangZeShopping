package org.tangze.work.entity.temp;

/**
 * Created by Administrator on 2016/10/26 0026.
 * 类描述  该类仅仅用于上级用户的库存保存
 * 版本
 */
public class TopMyStock {

    private int id; //id(如果要用于保存时，则该字段是自增id)

    private String stockId; //真正的库存id

    private String classify_id;//分类id

    private String user_id;// 库存产品id

    private String stockName;

    private String localPrice;

    private String original_price;

    private String stockCount;//库存量

    private String thumbnail;//缩略图


    public TopMyStock() {
    }


    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getClassify_id() {
        return classify_id;
    }

    public void setClassify_id(String classify_id) {
        this.classify_id = classify_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getLocalPrice() {
        return localPrice;
    }

    public void setLocalPrice(String localPrice) {
        this.localPrice = localPrice;
    }

    public String getStockCount() {
        return stockCount;
    }

    public void setStockCount(String stockCount) {
        this.stockCount = stockCount;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }
}



