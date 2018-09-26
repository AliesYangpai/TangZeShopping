package org.tangze.work.entity.temp;

import org.tangze.work.entity.Product;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29 0029.
 * 类描述    首页推荐时，用于ListView显示GirdView效果时，使用的封装类
 * 版本
 */
public class ProductPackage {

    private List<Product> productList;

    public ProductPackage() {
    }

    public ProductPackage(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
