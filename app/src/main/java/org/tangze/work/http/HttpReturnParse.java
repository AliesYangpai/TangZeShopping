package org.tangze.work.http;

import android.content.ContentValues;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.tangze.work.constant.ConstAddress;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstCollection;
import org.tangze.work.constant.ConstSp;
import org.tangze.work.entity.Address;
import org.tangze.work.entity.Classify;
import org.tangze.work.entity.ClassifySecond;
import org.tangze.work.entity.DeliverOrder;
import org.tangze.work.entity.MyCollection;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.ShowInfo;
import org.tangze.work.entity.User;
import org.tangze.work.entity.temp.ProductServerBackOrder;
import org.tangze.work.entity.temp.ShowInfoDetial;
import org.tangze.work.entity.temp.TopMyStock;
import org.tangze.work.utils.CharacterParserUtil;
import org.tangze.work.utils.SpUtil;
import org.tangze.work.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2016/10/11 0011.
 * 类描述  数据返回的解析的单例类
 * <p/>
 * 版本
 */
public class HttpReturnParse {

    private static HttpReturnParse mInstance;

    private static ReentrantLock mLock = new ReentrantLock();

    public static HttpReturnParse getInstance() {

        try {

            mLock.lock();

            if (mInstance == null) {

                mInstance = new HttpReturnParse();
            }
            return mInstance;

        } finally {

            mLock.unlock();
        }

    }


    public HttpReturnParse() {
    }


    /**
     * 将 gson的jsonArray转化成org的jsonObject
     *
     * @param jsonArray
     * @return
     */
    private JSONObject getTranslateToJsonObject(JsonArray jsonArray) {


        String serverback = jsonArray.toString();


        JSONObject jsonObject = null;
        JSONArray jsonArray1 = null;


        try {
            jsonArray1 = new JSONArray(serverback);

            jsonObject = (JSONObject) jsonArray1.get(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;

    }


    /**
     * 将 gson的jsonArray转化成org的jsonArray 返回的jsonArray长度大于1时候
     *
     * @param jsonArray
     * @return
     */
    private JSONArray getTranslateToJsonArray(JsonArray jsonArray) {


        String serverback = jsonArray.toString();


        JSONArray jsonArray1 = null;

        try {
            jsonArray1 = new JSONArray(serverback);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonArray1;

    }


    /**
     * 登陆返回解析,并保存到用户表
     */
    public boolean parseLoginBackAndSaveUser(JsonArray jsonArray) {

        boolean result = false;


        JSONObject jsonObject = getTranslateToJsonObject(jsonArray);


        try {
            int userId = jsonObject.getInt("UID");
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            int top_id = jsonObject.getInt("top_id");     //用户上家Id
            String name = jsonObject.getString("name");
            String tel = jsonObject.getString("tel");
            String email = jsonObject.getString("email");
            int grade = jsonObject.getInt("grade");    //用户等级
            String uimg = jsonObject.getString("uimg");
            String area = jsonObject.getString("area"); //用户所在地区
            String code = jsonObject.getString("code"); //用户注册码
            int open = jsonObject.getInt("open");      //是否启动
            String regTime = jsonObject.getString("time"); //注册时间
            String user_qq = jsonObject.getString("qq");

            User user = new User();
            user.setUser_id(userId);
            user.setUserName(username);
            user.setPassWord(password);
            user.setTopId(top_id);
            user.setNickName(name);
            user.setTelNum(tel);
            user.setEmail(email);
            user.setGrade(grade);
            user.setHeadPic(uimg);
            user.setArea(area);
            user.setRegCode(code);
            user.setOpen(open);
            user.setRegTime(regTime);
            user.setUser_qq(StringUtils.excludeNull(user_qq));

            User userfinded = DataSupport.findFirst(User.class);

            if (userfinded != null) {

                DataSupport.delete(User.class, userfinded.getId());
                result = user.save();
            } else {

                result = user.save();

            }


            if (result) {
                SpUtil.getInstance().saveBooleanTosp(ConstSp.SP_KEY_IS_LOGIN_OR_NOT, ConstSp.SP_VALUE.IS_LOGIN);
                SpUtil.getInstance().saveIntToSp(ConstSp.SP_KEY_USER_TYPE, grade);

            } else {
                Log.i("litepal", "用户登陆返回保存失败");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }


    /**
     * 首页banner返回解析
     */

    public List<String> parseBanner(JsonArray jsonArray) {


        List<String> list = new ArrayList<>();
        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);

        for (int i = 0; i < jsonArrayBack.length(); i++) {


            try {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);
                String url = jsonObject.getString("img");
                list.add(url);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return list;
    }



    /**
     * 模糊查询的数据返回
     * @param jsonArray
     */
        public void parseSearchFuzzBackSave(JsonArray jsonArray) {

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);


        List<Product> list = new ArrayList<>();
        for (int i = 0; i < jsonArrayBack.length(); i++) {

            try {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);


                Product product = new Product();
                product.setProduct_id(jsonObject.getInt("sid")); //商品id
                product.setClassify_id(jsonObject.getInt("cid")); //商品分类id
                product.setProductName(jsonObject.getString("name"));//商品名称
                product.setLocalPrice(jsonObject.getString("price")); //本店价格
                product.setOriginalPrice(jsonObject.getString("original")); //市场价
                product.setStock(jsonObject.getInt("stock"));  //库存
                product.setSalesVolume(jsonObject.getInt("sold")); //销量
                product.setThumnail(jsonObject.getString("thumbnail")); //缩略图
                product.setIsShelves(jsonObject.getInt("shelves")); //是否上架
                product.setPicture(jsonObject.getString("picture")); //产品大图片
                product.setProductDescribe(StringUtils.excludeNull(jsonObject.getString("describe"))); //产品描述
                product.setIsRecommand(StringUtils.excludeNull(jsonObject.getString("isrecommand"))); //是否首页推荐
                product.setIsNew(StringUtils.excludeNull(jsonObject.getString("isnew"))); //是否新品
                product.setIsHot(StringUtils.excludeNull(jsonObject.getString("ishot"))); //是否热卖
                product.setIsBoutique(StringUtils.excludeNull(jsonObject.getString("isboutique"))); //是否精品
                product.setFreeCarriage(StringUtils.excludeNull(jsonObject.getString("free_shipping"))); //是否免运费
                product.setTime(StringUtils.excludeNull(jsonObject.getString("time"))); //时间
                product.setIsCollect(jsonObject.getString("isCollect")); //是否收藏


                String allSpellWithSpace = CharacterParserUtil.getInstance().getSelling(jsonObject.getString("name"));
                String spellAll = allSpellWithSpace.replace(ConstBase.STRING_SPACE, ConstBase.STRING_COLON);
                String spellSimple = CharacterParserUtil.getInstance().getSimpleSpellPinYin(allSpellWithSpace);

                product.setSpellAll(spellAll);
                product.setSpellSimple(spellSimple);

                Log.i("pinyin", "字母全拼："+spellAll+" 简拼："+spellSimple);

                list.add(product);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        List<Product> products = DataSupport.findAll(Product.class);
        if (products != null && products.size() > 0) {

            DataSupport.deleteAll(Product.class);
            DataSupport.saveAll(list);
        } else {

            DataSupport.saveAll(list);

        }

    }






    /**
     * 首页推荐接口解析
     * //wen订单提交新修改
     */
    public List<Product> parseRecommandBackSave(JsonArray jsonArray) {

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);


        List<Product> list = new ArrayList<>();
        for (int i = 0; i < jsonArrayBack.length(); i++) {

            try {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);


                Product product = new Product();
                product.setProduct_id(jsonObject.getInt("sid")); //商品id
                product.setClassify_id(jsonObject.getInt("cid")); //商品分类id
                product.setProductName(jsonObject.getString("name"));//商品名称
                product.setLocalPrice(jsonObject.getString("price")); //本店价格
                product.setOriginalPrice(StringUtils.excludeNull(jsonObject.getString("original"))); //市场价
                product.setStock(jsonObject.getInt("stock"));  //库存
                product.setSalesVolume(jsonObject.getInt("sold")); //销量
                product.setThumnail(jsonObject.getString("thumbnail")); //缩略图
                product.setIsShelves(jsonObject.getInt("shelves")); //是否上架
                product.setPicture(jsonObject.getString("picture")); //产品大图片
                product.setProductDescribe(StringUtils.excludeNull(jsonObject.getString("describe"))); //产品描述
                product.setIsRecommand(StringUtils.excludeNull(jsonObject.getString("isrecommand"))); //是否首页推荐
                product.setIsNew(StringUtils.excludeNull(jsonObject.getString("isnew"))); //是否新品
                product.setIsHot(StringUtils.excludeNull(jsonObject.getString("ishot"))); //是否热卖
                product.setIsBoutique(StringUtils.excludeNull(jsonObject.getString("isboutique"))); //是否精品
                product.setFreeCarriage(StringUtils.excludeNull(jsonObject.getString("free_shipping"))); //是否免运费
                product.setTime(StringUtils.excludeNull(jsonObject.getString("time"))); //时间
                product.setIsCollect(jsonObject.getString("isCollect")); //是否收藏


                String allSpellWithSpace = CharacterParserUtil.getInstance().getSelling(jsonObject.getString("name"));
                String spellAll = allSpellWithSpace.replace(ConstBase.STRING_SPACE, ConstBase.STRING_COLON);
                String spellSimple = CharacterParserUtil.getInstance().getSimpleSpellPinYin(allSpellWithSpace);

                product.setSpellAll(spellAll);
                product.setSpellSimple(spellSimple);

                Log.i("pinyin", "字母全拼：" + spellAll + " 简拼：" + spellSimple);

                list.add(product);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return list;

    }

    /**
     * 上级用户我的库存解析
     */

    public List<TopMyStock> parseMyStock(JsonArray jsonArray) {

        List<TopMyStock> topMyStocks = new ArrayList<>();
        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);

        for (int i = 0; i < jsonArrayBack.length(); i++) {


            try {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);
//                private String stockId; //真正的库存id
//
//                private String classify_id;//分类id
//
//                private String user_id;// 库存产品id
//
//                private String stockName;
//
//                private String localPrice;
//
//                private String stockCount;//库存量
//
//                private String thumbnail;//缩略图

                String stockId = jsonObject.getString("sid");
                String classify_id = jsonObject.getString("cid");
                String stockName = jsonObject.getString("name");
                String localPrice = jsonObject.getString("price");
                String stockCount = jsonObject.getString("stock");
                String thumbnail = jsonObject.getString("thumbnail");
                String orignalPrice = jsonObject.getString("original");
                TopMyStock topMyStock = new TopMyStock();

                topMyStock.setStockId(StringUtils.excludeNull(stockId));
                topMyStock.setClassify_id(StringUtils.excludeNull(classify_id));
                topMyStock.setStockName(StringUtils.excludeNull(stockName));
                topMyStock.setLocalPrice(StringUtils.excludeNull(localPrice));
                topMyStock.setStockCount(StringUtils.excludeNull(stockCount));
                topMyStock.setThumbnail(StringUtils.excludeNull(thumbnail));
                topMyStock.setOriginal_price(StringUtils.excludeNull(orignalPrice));

                topMyStocks.add(topMyStock);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return topMyStocks;
    }


    /**
     * 获取产品1级分类数据解析
     */
    public void parseClassifyBackSave(JsonArray jsonArray) {

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);

        List<Classify> list = new ArrayList<>();

        for (int i = 0; i < jsonArrayBack.length(); i++) {

            try {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);

                Classify classify = new Classify();
                classify.setClassify_id(jsonObject.getInt("cid"));
                classify.setTopId(jsonObject.getInt("tip"));
                classify.setClassfiyName(jsonObject.getString("name"));
                classify.setThumbnail(jsonObject.getString("thumbnail"));
                classify.setCreateTime(jsonObject.getString("time"));


                list.add(classify);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        List<Classify> classifies = DataSupport.findAll(Classify.class);
        if (classifies != null && classifies.size() > 0) {

            DataSupport.deleteAll(Classify.class);
            DataSupport.saveAll(list);
        } else {

            DataSupport.saveAll(list);

        }


    }


    /**
     * 获取产品2级分类数据解析
     */
    public void parseSecondClassifyBackSave(JsonArray jsonArray) {

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);

        List<ClassifySecond> list = new ArrayList<>();

        for (int i = 0; i < jsonArrayBack.length(); i++) {

            try {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);

                ClassifySecond classifySecond = new ClassifySecond();
                classifySecond.setClassifySecond_id(jsonObject.getInt("cid"));
                classifySecond.setTopId(jsonObject.getInt("tip"));
                classifySecond.setClassfiyName(jsonObject.getString("name"));
                classifySecond.setThumbnail(jsonObject.getString("thumbnail"));
                classifySecond.setCreateTime(jsonObject.getString("time"));


                list.add(classifySecond);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        List<ClassifySecond> classifiesSeconds = DataSupport.findAll(ClassifySecond.class);
        if (classifiesSeconds != null && classifiesSeconds.size() > 0) {

            DataSupport.deleteAll(ClassifySecond.class);
            DataSupport.saveAll(list);
        } else {

            DataSupport.saveAll(list);

        }


    }

    /**
     * 获取指定分类下面的所有信息
     */

    public void parseProductByClassify(JsonArray jsonArray) {
        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);

        List<Product> list = new ArrayList<>();
        for (int i = 0; i < jsonArrayBack.length(); i++) {

            try {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);

                Product product = new Product();
                product.setProduct_id(jsonObject.getInt("sid")); //商品id
                product.setClassify_id(jsonObject.getInt("cid")); //商品分类id
                product.setProductName(jsonObject.getString("name"));//商品名称
                product.setLocalPrice(jsonObject.getString("price"));
                product.setOriginalPrice(jsonObject.getString("original"));
                product.setStock(jsonObject.getInt("stock"));
                product.setSalesVolume(jsonObject.getInt("sold")); //销量
                product.setThumnail(StringUtils.excludeNull(jsonObject.getString("thumbnail")));
                product.setIsShelves(jsonObject.getInt("shelves"));
                product.setPicture(jsonObject.getString("picture"));
                product.setProductDescribe(StringUtils.excludeNull(jsonObject.getString("describe")));
                product.setIsRecommand(StringUtils.excludeNull(jsonObject.getString("isrecommand")));
                product.setIsNew(StringUtils.excludeNull(jsonObject.getString("isnew")));
                product.setIsHot(StringUtils.excludeNull(jsonObject.getString("ishot")));
                product.setIsBoutique(StringUtils.excludeNull(jsonObject.getString("isboutique")));
                product.setFreeCarriage(StringUtils.excludeNull(jsonObject.getString("free_shipping")));
                product.setTime(StringUtils.excludeNull(jsonObject.getString("time")));
//                product.setUser_id(jsonObject.getInt("UID"));
                product.setIsCollect(jsonObject.getString("isCollect"));


                String allSpellWithSpace = CharacterParserUtil.getInstance().getSelling(jsonObject.getString("name"));
                String spellAll = allSpellWithSpace.replace(ConstBase.STRING_SPACE, ConstBase.STRING_COLON);
                String spellSimple = CharacterParserUtil.getInstance().getSimpleSpellPinYin(allSpellWithSpace);

                product.setSpellAll(spellAll);
                product.setSpellSimple(spellSimple);

                Log.i("pinyin", "字母全拼：" + spellAll + " 简拼：" + spellSimple);


                list.add(product);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        List<Product> products = DataSupport.findAll(Product.class);
        if (products != null && products.size() > 0) {

            DataSupport.deleteAll(Product.class);
            DataSupport.saveAll(list);
        } else {

            DataSupport.saveAll(list);

        }
    }


    /**
     * 用户编辑返回解析,并保存到用户表
     *
     * @param jsonArray
     * @param userEdit
     * @return 影响的行数
     */
    public boolean parseUserEditBackAndSaveUser(JsonArray jsonArray, User userEdit) {

        boolean result = false;


        JSONObject jsonObject = getTranslateToJsonObject(jsonArray);


        try {
            int userId = jsonObject.getInt("id");


            List<User> users = DataSupport.where("user_id = ?", String.valueOf(userId)).find(User.class);

            User userFind = users.get(0);


            userFind.setNickName(userEdit.getNickName());

            userFind.setTelNum(userEdit.getTelNum());
            userFind.setEmail(userEdit.getEmail());
            userFind.setPassWord(userEdit.getPassWord());
            userFind.setRegCode(userEdit.getRegCode());
            userFind.setUser_qq(userEdit.getUser_qq());


            int affectRow = 0;

            affectRow = userEdit.updateAll("user_id = ?", String.valueOf(userId));


            if (affectRow > 0) {

                result = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }


    /**
     * 解析地址返回
     */

    public boolean parseAddressBack(JsonArray jsonArray) {

        boolean result = false;

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);


        if (jsonArrayBack.length() != 0) {

            List<Address> list = new ArrayList<>();
            for (int i = 0; i < jsonArrayBack.length(); i++) {

                try {
                    JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);

                    int addressID = jsonObject.getInt("aid");
                    int user_id = jsonObject.getInt("UID");
                    String consignee = jsonObject.getString("consignee");
                    String area = jsonObject.getString("address");
                    String addressDetail = jsonObject.getString("detailed");
                    String telNum = jsonObject.getString("tel");
                    String isDefult = jsonObject.getString("isdefault");
                    Address address = new Address();
                    address.setAddress_Id(addressID);
                    address.setConsignee(consignee);
                    address.setArea(area);
                    address.setAddressDetail(addressDetail);
                    address.setTelNum(telNum);
                    address.setUser_id(user_id);
                    address.setIsdefault(isDefult);

                    list.add(address);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            List<Address> addresses = DataSupport.findAll(Address.class);
            if (addresses != null && addresses.size() > 0) {

                DataSupport.deleteAll(Address.class);
                DataSupport.saveAll(list);
            } else {

                DataSupport.saveAll(list);

            }

            result = true;

        } else {

            result = false;

        }
        return result;
    }


    /**
     * 设置默认地址的解析返回
     *
     * @return
     */
    public boolean parseSettingDefaultAddess(JsonArray jsonArray) {
        boolean result = false;

        JSONObject jsonObject = getTranslateToJsonObject(jsonArray);


        try {
            int address_id = jsonObject.getInt("id");

            List<Address> addresses = DataSupport.where("address_Id = ?", String.valueOf(address_id)).find(Address.class);


            Address address = addresses.get(0);

            address.setIsdefault(ConstAddress.NOT_DEFUALT_ADDR);

            int affectRow = 0;

            affectRow = address.updateAll("isdefault = ?", ConstAddress.IS_DEFUALT_ADDR);  //将所有isDefault为true的都更新为false

            if (affectRow > 0) {


                address.setIsdefault(ConstAddress.IS_DEFUALT_ADDR);

                address.update(address.getId()); //这里面直接使用getId，就是直接更新当前的address

                result = true;

            } else {


                result = false;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }


    /**
     * 添加新地址的返回解析
     *
     * @param jsonArray
     * @return
     */
    public int parseAddNewAddressBackAndSave(JsonArray jsonArray) {

        int id = -1;
        boolean result = false;
        JSONObject jsonObject = getTranslateToJsonObject(jsonArray);

        try {
            int new_addressId = jsonObject.getInt("aid");
            int user_id = jsonObject.getInt("UID");
            String consignee = jsonObject.getString("consignee");
            String area = jsonObject.getString("address");
            String detailed = jsonObject.getString("detailed");
            String tel = jsonObject.getString("tel");
            String isDefualt = jsonObject.getString("isdefault");


            if (isDefualt.equals(ConstAddress.NOT_DEFUALT_ADDR)) {


                Address address = new Address();
                address.setAddress_Id(new_addressId);
                address.setUser_id(user_id);
                address.setConsignee(consignee);
                address.setArea(area);
                address.setTelNum(tel);
                address.setAddressDetail(detailed);
                address.setIsdefault(isDefualt);

                result = address.save();

                if (result) {

                    id = new_addressId;
                }

            } else {


                List<Address> addresses = DataSupport.findAll(Address.class);


                /**
                 * 1.先判断是否添加了地址
                 * 如果不加此判断，在更新的时候会有异常
                 * 这个实际上就是 是否持久化的一个判断
                 */
                if (addresses != null && addresses.size() > 0) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isdefault", ConstAddress.NOT_DEFUALT_ADDR);
                    int tempAffect = DataSupport.updateAll(Address.class, contentValues, "isdefault = ?", ConstAddress.IS_DEFUALT_ADDR);

                    if (tempAffect > 0) {


                        Address addressNew = new Address();
                        addressNew.setAddress_Id(new_addressId);
                        addressNew.setUser_id(user_id);
                        addressNew.setConsignee(consignee);
                        addressNew.setArea(area);
                        addressNew.setTelNum(tel);
                        addressNew.setAddressDetail(detailed);
                        addressNew.setIsdefault(isDefualt);
                        result = addressNew.save();

                        if (result) {
                            id = new_addressId;
                        }
                    }


                } else {
                    /**
                     * 如果还没有添加地址，则这里直接进行保存
                     */

                    Address addressNew = new Address();
                    addressNew.setAddress_Id(new_addressId);
                    addressNew.setUser_id(user_id);
                    addressNew.setConsignee(consignee);
                    addressNew.setArea(area);
                    addressNew.setTelNum(tel);
                    addressNew.setAddressDetail(detailed);
                    addressNew.setIsdefault(isDefualt);
                    result = addressNew.save();
                    if (result) {
                        id = new_addressId;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return id;
    }


    /**
     * 编辑地址返回
     */
    public boolean parseEditAddessBackAndSave(JsonArray jsonArray, String consignee, String area, String detailed, String tel_num) {
        boolean result = false;

        JSONObject jsonObject = getTranslateToJsonObject(jsonArray);


        try {
            int address_id = jsonObject.getInt("id");

            List<Address> addresses = DataSupport.where("address_Id = ?", String.valueOf(address_id)).find(Address.class);


            Address address = addresses.get(0);
            address.setConsignee(consignee);
            address.setArea(area);
            address.setAddressDetail(detailed);
            address.setTelNum(tel_num);
            int affectRow = address.update(address.getId());
            if (affectRow == 1) {

                result = true;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }


    /**
     * 获取上级用户信息返回的解析
     * 目前只需要top_qq,后续有变化再解析
     *
     * @param jsonArray
     * @return
     */
    public boolean parseTopUserBackAndSave(JsonArray jsonArray) {

        boolean result = false;

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);


        try {


            if (jsonArrayBack != null && jsonArrayBack.length() > 0) {


                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(0);

                String top_qq = jsonObject.getString("qq");

                SpUtil.getInstance().saveStringToSp(ConstSp.SP_KEY_TOP_QQ, top_qq);


                result = true;
            } else {

                result = false;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


//                "UID":"20",
//                "username":"666",
//                "password":null,
//                "top_id":"20",
//                "name":"One",
//                "tel":"12324242",
//                "email":"212@127.com",
//                "grade":"1",
//                "uimg":null,
//                "area":"\u6d59\u6c5f",
//                "code":"12345",
//                "open":"1",
//                "qq":"12345",
//                "time":"1475906902"


        return result;
    }


    /**
     * 我的收藏返回
     *
     * @param jsonArray
     */
    public List<Product> parseMyCollectionBack(JsonArray jsonArray) {

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);

        List<Product> list = new ArrayList<>();

        List<MyCollection> myCollections = new ArrayList<>();
        for (int i = 0; i < jsonArrayBack.length(); i++) {

            try {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);

                int mycollectionId = jsonObject.getInt("id");
                int productId = jsonObject.getInt("sid");
                int userId = jsonObject.getInt("UID");
                String thumbnail = jsonObject.getString("thumbnail");
                String productName = jsonObject.getString("name");
                String localPrice = jsonObject.getString("price");
                String originalPrice = jsonObject.getString("original");




                Product product = new Product();
                product.setProduct_id(productId);
                product.setProductName(productName);
                product.setLocalPrice(localPrice);
                product.setOriginalPrice(originalPrice);
                product.setThumnail(thumbnail);


                list.add(product);

                MyCollection myCollection = new MyCollection();
                myCollection.setMyCollection_id(mycollectionId);
                myCollection.setProductId(productId);
                myCollection.setUserId(userId);
                myCollections.add(myCollection);





            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        List<MyCollection> myCollectionList = DataSupport.findAll(MyCollection.class);
        if (myCollectionList != null && myCollectionList.size() > 0) {

            DataSupport.deleteAll(MyCollection.class);
            DataSupport.saveAll(myCollections);
        } else {
            DataSupport.saveAll(myCollections);
        }


        return list;
    }


    /**
     * 成功收藏的返回解析
     *
     * @param jsonArray
     * @param productId 使用这个参数进行MyCollection表的反查
     */
    public boolean parseAddCollectBack(JsonArray jsonArray, int productId, int userId) {


        boolean result = false;

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);

        for (int i = 0; i < jsonArrayBack.length(); i++) {


            try {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);

                int collect_id = jsonObject.getInt("id");  //收藏的id

                /**
                 * 1.添加一个记录收藏到收藏表
                 * 2.更新当前product表中的收藏状态
                 */

                MyCollection myCollection = new MyCollection();

                myCollection.setMyCollection_id(collect_id);
                myCollection.setProductId(productId);
                myCollection.setUserId(userId);

                result = myCollection.save();



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return result;
    }


    /**
     * 成功取消收藏的返回解析
     *
     * @param jsonArray
     * @param productId 使用这个参数进行MyCollection表的反查
     */
    public boolean parseCancelCollectBack(JsonArray jsonArray, int productId, int userId) {

        boolean result = false;

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);

        for (int i = 0; i < jsonArrayBack.length(); i++) {


            try {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);

                int collect_id = jsonObject.getInt("id");  //收藏的id

               int affect = DataSupport.deleteAll(MyCollection.class, "myCollection_id = ?", String.valueOf(collect_id));

               if(affect > 0) {

                   result = true;

               }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return result;

    }


    /**
     * 解析单个更新的产品
     * //wen订单提交cut
     */

//    public int parseSingleProduct(JsonArray jsonArray) {
//
//
//        int rowAffect = 0;
//
//        JSONObject jsonObject = getTranslateToJsonObject(jsonArray);
//
//
//        try {
//
//            int prodcutId = jsonObject.getInt("sid");
//
//
//            List<Product> products = DataSupport.where("product_id = ?", String.valueOf(prodcutId)).find(Product.class);
//
//            if (null != products && products.size() > 0) {
//
//                Product product = products.get(0);
//                product.setProduct_id(jsonObject.getInt("sid")); //商品id
//                product.setClassify_id(jsonObject.getInt("cid")); //商品分类id
//                product.setProductName(jsonObject.getString("name"));//商品名称
//                product.setLocalPrice(jsonObject.getString("price"));
//                product.setOriginalPrice(jsonObject.getString("original"));
//                product.setStock(jsonObject.getInt("stock"));
//                product.setSalesVolume(jsonObject.getInt("sold")); //销量
//                product.setThumnail(jsonObject.getString("thumbnail"));
//                product.setIsShelves(jsonObject.getInt("shelves"));
//                product.setPicture(jsonObject.getString("picture"));
//                product.setProductDescribe(StringUtils.excludeNull(jsonObject.getString("describe")));
//                product.setIsRecommand(StringUtils.excludeNull(jsonObject.getString("isrecommand")));
//                product.setIsNew(StringUtils.excludeNull(jsonObject.getString("isnew")));
//                product.setIsHot(StringUtils.excludeNull(jsonObject.getString("ishot")));
//                product.setIsBoutique(StringUtils.excludeNull(jsonObject.getString("isboutique")));
//                product.setFreeCarriage(StringUtils.excludeNull(jsonObject.getString("free_shipping")));
//                product.setTime(StringUtils.excludeNull(jsonObject.getString("time")));
////                product.setUser_id(jsonObject.getInt("UID"));
//                product.setIsCollect(jsonObject.getString("isCollect"));
//
//                String allSpellWithSpace = CharacterParserUtil.getInstance().getSelling(jsonObject.getString("name"));
//                String spellAll = allSpellWithSpace.replace(ConstBase.STRING_SPACE, ConstBase.STRING_COLON);
//                String spellSimple = CharacterParserUtil.getInstance().getSimpleSpellPinYin(allSpellWithSpace);
//
//                product.setSpellAll(spellAll);
//                product.setSpellSimple(spellSimple);
//
//                Log.i("pinyin", "字母全拼：" + spellAll + " 简拼：" + spellSimple);
//
//
//                rowAffect = product.update(product.getId());
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        return rowAffect;
//    }


    /**
     * 解析单个更新的产品
     * //wen订单提交新修改
     */

    public Product parseSingleProduct(JsonArray jsonArray) {




        JSONObject jsonObject = getTranslateToJsonObject(jsonArray);
        Product product = new Product();

        try {

            product.setProduct_id(jsonObject.getInt("sid")); //商品id
            product.setClassify_id(jsonObject.getInt("cid")); //商品分类id
            product.setProductName(jsonObject.getString("name"));//商品名称
            product.setLocalPrice(jsonObject.getString("price"));
            product.setOriginalPrice(jsonObject.getString("original"));
            product.setStock(jsonObject.getInt("stock"));
            product.setSalesVolume(jsonObject.getInt("sold")); //销量
            product.setThumnail(jsonObject.getString("thumbnail"));
            product.setIsShelves(jsonObject.getInt("shelves"));
            product.setPicture(jsonObject.getString("picture"));
            product.setProductDescribe(StringUtils.excludeNull(jsonObject.getString("describe")));
            product.setIsRecommand(StringUtils.excludeNull(jsonObject.getString("isrecommand")));
            product.setIsNew(StringUtils.excludeNull(jsonObject.getString("isnew")));
            product.setIsHot(StringUtils.excludeNull(jsonObject.getString("ishot")));
            product.setIsBoutique(StringUtils.excludeNull(jsonObject.getString("isboutique")));
            product.setFreeCarriage(StringUtils.excludeNull(jsonObject.getString("free_shipping")));
            product.setTime(StringUtils.excludeNull(jsonObject.getString("time")));
//                product.setUser_id(jsonObject.getInt("UID"));
            product.setIsCollect(jsonObject.getString("isCollect"));

            String allSpellWithSpace = CharacterParserUtil.getInstance().getSelling(jsonObject.getString("name"));
            String spellAll = allSpellWithSpace.replace(ConstBase.STRING_SPACE, ConstBase.STRING_COLON);
            String spellSimple = CharacterParserUtil.getInstance().getSimpleSpellPinYin(allSpellWithSpace);

            product.setSpellAll(spellAll);
            product.setSpellSimple(spellSimple);

            Log.i("pinyin", "字母全拼：" + spellAll + " 简拼：" + spellSimple);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return product;
    }

    /**
     * 成功提交订单的返回解析
     *
     * @param jsonArray
     * @param
     */
    public boolean parseDeliverOrderBack(JsonArray jsonArray) {


        boolean result = false;

        JSONObject jsonObject = getTranslateToJsonObject(jsonArray);

        try {
            String orderId = jsonObject.getString("id");

            DeliverOrder deliverOrder = new DeliverOrder(orderId);

            result = deliverOrder.save();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }

    /**
     * 获取所有订单的返回
     */

    public List<ProductServerBackOrder> parseAllOrdersBack(JsonArray jsonArray) {


        List<ProductServerBackOrder> list = new ArrayList<>();

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);


        for (int i = 0; i < jsonArrayBack.length(); i++) {

            try {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);
                ProductServerBackOrder productServerBackOrder = new ProductServerBackOrder();

                productServerBackOrder.setId(jsonObject.getInt("oid"));//订单id

                productServerBackOrder.setOrder_num(jsonObject.getString("number")); //订单编号
//                private String content;//组合数据;
                productServerBackOrder.setContent(jsonObject.getString("content"));
//                private String totalprice;//订单总价
                productServerBackOrder.setTotalprice(jsonObject.getString("totalprice"));
//                private String freight;//订单运费
                productServerBackOrder.setFreight(StringUtils.excludeNull(jsonObject.getString("freight")));
//                private String express;//快递公司
                productServerBackOrder.setExpress(StringUtils.excludeNull(jsonObject.getString("express")));
//                private String time;//下单时间
                productServerBackOrder.setTime(StringUtils.excludeNull(jsonObject.getString("time")));
//                private int state;//订单状态
                productServerBackOrder.setState(jsonObject.getInt("state"));
//                private String rmarks;//备注信息
                productServerBackOrder.setRmarks(StringUtils.excludeNull(jsonObject.getString("remarks")));
//                private int order_user_id;//用户id
                productServerBackOrder.setOrder_user_id(jsonObject.getString("UID"));
//                private String consignee; //收货人
                productServerBackOrder.setConsignee(jsonObject.getString("consignee"));
//                private String tel;//联系方式
                productServerBackOrder.setTel(jsonObject.getString("tel"));
//                private String address;//详细送货地址
                productServerBackOrder.setAddress(jsonObject.getString("address"));


                list.add(0, productServerBackOrder); //将订单定在第一个

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return list;
    }

    /**
     * 成功提交订单的返回解析
     * //订单需要修改本地保存，
     *
     * @param jsonArray
     * @param
     */
    public boolean parseAllOrdersInMainAndSave(JsonArray jsonArray) {


        boolean result = false;

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);

        List<DeliverOrder> list = new ArrayList<>();
        try {

            for (int i = 0; i < jsonArrayBack.length(); i++) {


                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);

                String order_id = jsonObject.getString("oid");
                int order_user_id = jsonObject.getInt("UID");

                DeliverOrder deliverOrder = new DeliverOrder();
                deliverOrder.setOrder_id(order_id);
                deliverOrder.setOrder_user_id(order_user_id);
                list.add(deliverOrder);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<DeliverOrder> deliverOrders = DataSupport.findAll(DeliverOrder.class);
        if (deliverOrders != null && deliverOrders.size() > 0) {

            DataSupport.deleteAll(DeliverOrder.class);
            DataSupport.saveAll(list);
        } else {

            DataSupport.saveAll(list);

        }


        return result;
    }


    /**
     * 解析信息发布列表信息返回
     */

    public boolean parseInfoList(JsonArray jsonArray) {


        boolean result = false;

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);

        List<ShowInfo> list = new ArrayList<>();


        try {

            for (int i = 0; i < jsonArrayBack.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArrayBack.get(i);

                ShowInfo showInfo = new ShowInfo();
                showInfo.setShowinfo_Id(jsonObject.getString("id"));

                showInfo.setTitle(StringUtils.excludeNull(jsonObject.getString("title")));
                showInfo.setTime(jsonObject.getString("time"));
                showInfo.setThumbnail(StringUtils.excludeNull(jsonObject.getString("thumbnail")));
                list.add(showInfo);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        List<ShowInfo> showInfos = DataSupport.findAll(ShowInfo.class);
        if (showInfos != null && showInfos.size() > 0) {

            DataSupport.deleteAll(ShowInfo.class);
            DataSupport.saveAll(list);
            result = true;
        } else {

            DataSupport.saveAll(list);
            result = true;
        }


        return result;
    }


    public ShowInfoDetial parseInfoDetail(JsonArray jsonArray) {

        ShowInfoDetial showInfoDetial = new ShowInfoDetial();

        JSONArray jsonArrayBack = getTranslateToJsonArray(jsonArray);


        try {


            JSONObject jsonObject = (JSONObject) jsonArrayBack.get(0);

            showInfoDetial.setInfo_detial_id(jsonObject.getString("id"));
            showInfoDetial.setTitle(jsonObject.getString("title"));
            showInfoDetial.setAuthor(jsonObject.getString("author"));
            showInfoDetial.setTime(jsonObject.getString("time"));
            showInfoDetial.setContent(jsonObject.getString("content"));
            showInfoDetial.setSource(jsonObject.getString("source"));
            showInfoDetial.setBelong(jsonObject.getString("belong"));


            showInfoDetial.setSort(StringUtils.excludeNull(jsonObject.getString("sort")));
            showInfoDetial.setType(jsonObject.getString("type"));
            showInfoDetial.setThumbnail(StringUtils.excludeNull(jsonObject.getString("thumbnail")));
            showInfoDetial.setIssue(StringUtils.excludeNull(jsonObject.getString("issue")));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return showInfoDetial;

    }


    /**
     * 解析Server返回的chargeBack
     * @param jsonArray
     * @return
     */
    public String parseChargeBack(JsonArray jsonArray) {

        String back = "";

       JSONObject jsonObject =  getTranslateToJsonObject(jsonArray);

        return jsonObject.toString();

    }

}
