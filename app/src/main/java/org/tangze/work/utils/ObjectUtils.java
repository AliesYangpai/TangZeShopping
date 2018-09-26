package org.tangze.work.utils;



import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tangze.work.entity.temp.ProductChildServerBack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/23 0023.
 * 类描述
 * 版本
 */
public class ObjectUtils {


    public static List<ProductChildServerBack> getListData(String str) {

        List<ProductChildServerBack> list = new ArrayList<>();

        try {

            //得到所有的keys

                JSONObject jsonObject = new JSONObject(str);


                Iterator keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();

                    Log.i("randomKey", "key:" + key);

                    JSONObject jsonChild = jsonObject.getJSONObject(key);


                    String product_id = jsonChild.getString("sid");
                    String buyCount = jsonChild.getString("num");
                    String price = jsonChild.getString("price");
                    String product_name = jsonChild.getString("name");
                    String original_price = jsonChild.getString("original");
                    String thumail = jsonChild.getString("thumbnail");

                    ProductChildServerBack productChildServerBack = new ProductChildServerBack();
                    productChildServerBack.setProduct_id(product_id);
                    productChildServerBack.setBuyCount(buyCount);
                    productChildServerBack.setLocal_price(price);
                    productChildServerBack.setProduct_name(product_name);
                    productChildServerBack.setOriginal_price(original_price);
                    productChildServerBack.setThumnail(thumail);
                    list.add(productChildServerBack);

                    Log.i("randomKey", "key:" + key+" 对应的值:"+"产品id："+product_id+" 产品单价价格："+price+" 产品数量："+buyCount+" 产品名称"+product_name);

                }





        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }

}
