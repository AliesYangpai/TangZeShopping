package org.tangze.work.utils;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.temp.ProductOrder;
import org.tangze.work.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/10 0010.
 * 类描述
 * 版本
 */
public class ParaUtils {


    /**
     * 注册参数
     *
     * @param username
     * @param password
     * @param name
     * @param code
     * @param tel
     * @param email
     * @return
     */
    public static Map getRegisterParam(String username, String password, String name, String code, String tel, String email, String qq) {

        Map<String, Object> map = new HashMap<>();

        map.put("username", username);
        map.put("password", password);
        map.put("name", name);
        map.put("code", code);
        map.put("tel", tel);
        map.put("email", email);
        map.put("qq", qq);

        return map;

    }

    /**
     * 登陆参数
     *
     * @param username
     * @param password
     * @return
     */
    public static Map getLoginParam(String username, String password) {

        Map<String, Object> map = new HashMap<>();

        map.put("username", username);
        map.put("password", password);

        return map;

    }

    /**
     * 获取首页推荐 所传递的参数
     *
     * @param UID userid
     * @return
     */
    public static Map getRecommandProductInfo(int UID) {

        Map<String, Object> map = new HashMap<>();
        map.put("UID", UID);
        return map;
    }


    /**
     * 获取单个1级分类下的所有二级分类：
     */

    public static Map getAllSecondClassifiesByOneTop(int cid) {

        Map<String, Object> map = new HashMap<>();
        map.put("cid", cid);
        return map;
    }


    /**
     * 获取当前用的下的所有商品
     */

    public static Map getAllProduct(int userId) {

        Map<String, Integer> map = new HashMap<>();

        map.put("UID", userId);

        return map;
    }


    /**
     * 获取当前用的分页加载
     */

    public static Map getPageProduct(int userId,int page) {

        Map<String, Integer> map = new HashMap<>();

        map.put("UID", userId);
        map.put("page",page);
        return map;
    }




    /**
     * 获取当前用的分页加载，并应用与首页中间四项：新品推荐、热卖、精品推荐、首页推荐
     * @param userId   用户id
     * @param param    新品推荐、热卖、精品推荐、首页推荐相关key
     * @param value   新品推荐、热卖、精品推荐、首页推荐相关value
     * @return
     */
    public static Map getPageProductToMain4(int userId,String param,int value,int page) {

        Map<String, Integer> map = new HashMap<>();

        map.put("UID", userId);
        map.put(param,value);
        map.put("page",page);
        return map;
    }



    public static Map getFuzzySearch(int userId,String fuzzyValue) {

        Map<String, Object> map = new HashMap<>();

        map.put("UID", userId);
        map.put("search",fuzzyValue);
        return map;

    }

    /**
     * 上级用户获取我的库存
     */
    public static Map getMyStockInTop(int userId) {

        Map<String, Integer> map = new HashMap<>();

        map.put("UID", userId);

        return map;
    }


    /**
     *  上级用户获取我的库存
     * @param userId 上级用户id
     * @param page  分页数量
     * @param tag   区分上拉加载和下拉差量更新
     * @return
     */
    public static Map getMyStockInTop(int userId,int page,int tag) {

        Map<String, Integer> map = new HashMap<>();

        map.put("UID", userId);
        map.put("page",page);
        map.put("tag",tag);

        return map;
    }


    /**
     * 编辑用户信息参数
     */

    public static Map getUserInfoParam(User userEdit) {
        Map<String, Object> map = new HashMap<>();

        map.put("UID", userEdit.getUser_id());
        map.put("username", userEdit.getUserName());
        map.put("password", userEdit.getPassWord());
        map.put("name", userEdit.getNickName());
        map.put("code", userEdit.getRegCode());
        map.put("email", userEdit.getEmail());
        map.put("tel", userEdit.getTelNum());
        map.put("qq", userEdit.getUser_qq());
        return map;

    }


    /**
     * 获取当前用户地址的请求参数
     */

    public static Map getUserIdToGetAddressList(int userId) {

        Map<String, Integer> map = new HashMap<>();

        map.put("UID", userId);

        return map;
    }


    /**
     * 设置默认地址传递的参数
     */
    public static Map getDefaultAddresId(int address_id) {

        Map<String, Integer> map = new HashMap<>();

        map.put("aid", address_id);

        return map;
    }


    /**
     * 添加新的用户发货地址
     */

    public static Map addNewUserAddress(int UID, String consignee, String area, String detialed, String tel, String defaultcheck) {

        Map<String, Object> map = new HashMap<>();

        map.put("UID", UID);
        map.put("consignee", consignee);
        map.put("address", area);
        map.put("detailed", detialed);
        map.put("tel", tel);
        map.put("default", defaultcheck);

        return map;
    }

    /**
     * 编辑地址
     */
    public static Map editUserAddress(int address_id, String consignee, String area, String detialed, String tel) {

        Map<String, Object> map = new HashMap<>();

        map.put("aid", address_id);
        map.put("consignee", consignee);
        map.put("address", area);
        map.put("detailed", detialed);
        map.put("tel", tel);
        return map;
    }


    /**
     * 删除地址
     */
    public static Map delUserAddress(int address_id) {

        Map<String, Object> map = new HashMap<>();

        map.put("aid", address_id);
        return map;
    }

    /**
     * 获取上级用户信息，其中包含上级用户的qq
     *
     * @param UID 上一级的用户id
     * @return
     */
    public static Map getTopInfo(int UID) {

        Map<String, Object> map = new HashMap<>();

        map.put("UID", UID);

        return map;

    }


    /**
     * 获取所有收藏所需要的参数
     */

    public static Map getMyCollections(int UID) {

        Map<String, Object> map = new HashMap<>();

        map.put("UID", UID);

        return map;

    }


    /**
     * 添加收藏、取消收藏的请求参数
     *
     * @param sid
     * @param UID
     * @return
     */
    public static Map getAddOrCancelCollect(int sid, int UID) {

        Map<String, Object> map = new HashMap<>();

        map.put("sid", sid);
        map.put("UID", UID);

        return map;

    }


    /**
     * 执行添加收藏
     * @param sid
     * @param UID
     * @param name
     * @param price
     * @param original
     * @param thumbnail
     * @return
     */
    public static Map getAddCollect(int sid,int UID,String name,String price,String original,String thumbnail) {

        Map<String, Object> map = new HashMap<>();

        map.put("sid", sid);
        map.put("UID", UID);
        map.put("name", name);
        map.put("price", price);
        map.put("original", original);
        map.put("thumbnail",thumbnail);
        return map;
    }


    /**
     * 获取二级分类下的所有产品数据
     */

    public static Map getAllProductBySecondClassIds(int userId,List<Integer> ids) {

        Map<String,Object> map = new HashMap<>();


        JSONObject jsonObject = new JSONObject();

        for(int i = 0 ; i < ids.size();i++) {


            try {
                int id = ids.get(i);

//                jsonObject.put(String.valueOf(id),id);
                jsonObject.put(String.valueOf(id),String.valueOf(id));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        map.put("cid",jsonObject);
        map.put("UID",userId);


        return map;


    }









    /**
     * 获取单个产品的最新数据
     *
     * @param sid 产品id
     * @param UID 用户id
     * @return
     */
    public static Map getSingleProduct(int UID,int sid) {

        Map<String, Object> map = new HashMap<>();

        map.put("UID", UID);
        map.put("sid", sid);

        return map;

    }


    /**
     * ******************************订单相关参数**********************************
     * 格式如下：
     */

//    {
//        "content":{
//        "ae255762":{
//                    "sid":120,
//                    "name":"香蕉",
//                    "num":60,
//                    "price":12,
//        },
//        "9e72ce61":{
//                    "sid":100,
//                    "name":"苹果",
//                    "num":40,
//                    "price":12,
//        }
//    },
//        "tip":"5",
//            "freight":"魅族手机",
//            "express":"1299",
//            "UID":"5",
//            "courier_number":"500",
//            "totalprice":"20",
//            "consignee":"没图",
//            "tel":"1",
//            "address":"展示图",
//            "remarks":"22"
//    }
    /**
     * 提交一个订单
     *
     * @param productOrder 最终要提交的订单order
     * @return
     */
    public static Map getDeliverOrder(ProductOrder productOrder) {


//wen订单提交新修改
        Map<String, Object> map = new HashMap<>();

        String remarks = productOrder.getRemarks();
        String address = productOrder.getAddressDefault().getAddressDetail();
        String tel = productOrder.getAddressDefault().getTelNum();
        String consignee = productOrder.getAddressDefault().getConsignee();
        String totalprice = productOrder.getTotalprice();
        int uid = productOrder.getUser_id();
        int tid = productOrder.getTop_id();

        map.put("freight", "");  //订单运费  【暂不实现】
        map.put("express", "");//快递公司 【暂不实现】
        map.put("courier_number", "");//快递单号 【暂不实现】


        map.put("remarks", remarks);//准备信息
        map.put("address", address);//收货地址
        map.put("tel", tel);//电话号码
        map.put("consignee", consignee);//收货人
        map.put("totalprice", totalprice);//总价
        map.put("UID", uid);//用户id
        map.put("tid", tid);//快递单号


        List<String> jsonKeys = productOrder.getPrd_json_key();
        List<Product> product_transmit = productOrder.getProducts_transmit();
        JSONObject jsonObjectParent = new JSONObject();
        for (int j = 0; j < jsonKeys.size(); j++) {


            JSONObject jsonObjectChild = new JSONObject();

            Product product = product_transmit.get(j);

            try {
                jsonObjectChild.put("sid", String.valueOf(product.getProduct_id()));   //产品id
                jsonObjectChild.put("name",product.getProductName());                   //产品名称
                jsonObjectChild.put("num",productOrder.getMap().get(product.getProduct_id())); //产品购买数量
                jsonObjectChild.put("price",product.getLocalPrice());
                jsonObjectChild.put("thumbnail",product.getThumnail());
                jsonObjectChild.put("original",product.getOriginalPrice());
                jsonObjectParent.put(jsonKeys.get(j),jsonObjectChild);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        map.put("content", jsonObjectParent.toString());//快递单号


        return map;

    }

    /**
     * 查询（获取）所有订单的参数
     */

    public static Map getAllOrderInfo(int UID,int state) {

        Map<String, Object> map = new HashMap<>();

        if(0 == state) {
            map.put("UID", UID);
        }else {

            JSONArray jsonArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("state",state);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
            map.put("where",jsonArray.toString());
            map.put("UID",UID);
        }
        return map;
    }





    /**
     * 查询（获取）所有订单的参数
     * //OrderState修改后
     * @param UID    用户id
     * @param state  订单状态
     * @param page   分页数量
     * @param tag    标记是下拉刷新还是上拉加载
     * @return
     */
    public static Map getAllOrderInfo(int UID,int state,int page,int tag) {

        Map<String, Object> map = new HashMap<>();

        if(0 == state) {
            map.put("UID", UID);
        }else {

            JSONArray jsonArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("state",state);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
            map.put("where",jsonArray.toString());
            map.put("UID",UID);
            map.put("page",page);
            map.put("tag",tag);
        }
        return map;
    }


    /**
     * 获取下级用户发起订单操作的参数
     */

    public static Map operateByDownUser(int order_id,String user_id) {

        Map<String, Object> map = new HashMap<>();

        map.put("oid", order_id);
        map.put("UID", user_id);

        return map;
    }



    /**
     * 获取上级用户收到的下级用户提供的订单
     * @param UID   上级用户的id
     * @param state 订单状态
     * @return
     * 注意，这里需要指定一个top的参数，如果有值，则查询的是该用户收到的订单，值任意
     * 这里我指定为99；
     */

    public static Map getAllOrderInfoInTop(int UID,int state) {

        Map<String, Object> map = new HashMap<>();

        if(0 == state) {
            map.put("UID", UID);
        }else {

            JSONArray jsonArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("state",state);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);


            map.put("UID", UID);
            map.put("top", ConstBase.TOP_VALUE);
            map.put("where",jsonArray.toString());


        }

        return map;
    }


    /**
     *  获取上级用户收到的下级用户提供的订单
     * @param UID  上级用户的id
     * @param state 订单状态
     * @param page 分页数量
     * @param tag  判断是 上拉加载更多还是下拉刷新
     * @return
     * 注意，这里需要指定一个top的参数，如果有值，则查询的是该用户收到的订单，值任意
     * 这里我指定为99；
     * //配货上下修改后
     */

    public static Map getAllOrderInfoInTop(int UID,int state,int page,int tag) {

        Map<String, Object> map = new HashMap<>();

        if(0 == state) {
            map.put("UID", UID);
        }else {

            JSONArray jsonArray = new JSONArray();

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("state",state);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);


            map.put("UID", UID);
            map.put("top", ConstBase.TOP_VALUE);
            map.put("where",jsonArray.toString());
            map.put("page",page);
            map.put("tag",tag);

        }

        return map;
    }




    /**
     * 通过信息展示Id获取信息展示detail的参数配置
     * @param id
     * @return
     */
    public static Map getShowInfoDetailById(String id) {

        Map<String, Object> map = new HashMap<>();


            map.put("id",id);




        return map;
    }
}
