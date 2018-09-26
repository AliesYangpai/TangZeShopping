package org.tangze.work.http;

/**
 * Created by Administrator on 2016/9/17 0017.
 * 类描述   网络交互的所有常量
 * 版本
 */
public class HttpConst {


    public static final String SERVER_BACK = "server_back";

    public static final String CONTENT_TYPE = "Content-Type";


    public static final String CONTENT_TYPE_VALUE = "application/json; charset=UTF-8";


//    public static final String BASE_URL = "http://www.ub-7.cn/api.php/";


    public static final String BASE_URL = "http://app.antutong.com/api.php/";






//    public static final String TEST_TOKEN_VALUE = "CImpzgpc2Cdlzm5S"; //测试token


    public static final String TEST_TOKEN_KEY_VALUE = "token/CImpzgpc2Cdlzm5S"; //测试token配合路径


    public class URL {

        /**
         * 用户注册
         */
        public static final String  USER_ADD= "user/userAdd/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 用户登陆
         */
        public static final String  USER_LOGIN= "user/login/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 首页banner图片
         */
        public static final String BANNER_LIST = "index/banner/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 获取上级用户信息接口
         * 这里包含上级用户的qq
         * 完成
         */
        public static final String TOP_USER_INFO = "user/getKf/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 首页推荐的产品
         * 11.8日和刘沛商定，不再使用用此接口，得到首页推荐
         */
        public static final String RECOMMAND_PRODUCT = "index/isrecommand/"+TEST_TOKEN_KEY_VALUE;

        /**
         * 获取分类信息 一级分类
         */

        public static final String CLASSIFIES = "shop/getClassList/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 获取分类信息 获取二级分类
         */

        public static final String CLASSIFIES_SECOND = "shop/getDownClass/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 获取产品列表（未购物）
         * 传入UID 得到产品列表
         * 传入 UID sid 则得到单个产品
         */
        public static final String ALL_PRODUCT = "shop/getGoodsList/"+TEST_TOKEN_KEY_VALUE;



        //***********************完成************//

        /**
         * 用户编辑
         */
        public static final String USER_EDIT = "user/userEdit/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 用户地址列表 (这里有问题，不确定这个是收货地址 还是发货地址)
         */

        public static final String USER_ADDRESS_LIST = "user/addressList/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 添加新的用户地址
         */


        public static final String ADD_NEW_USER_ADDRESS = "user/addressAdd/"+TEST_TOKEN_KEY_VALUE;

        /**
         * 设置用户默认地址(这里有问题，不确定这个是收货地址 还是发货地址)
         */
        public static final String SET_DEFULT_ADDRESS = "user/setDefault/"+TEST_TOKEN_KEY_VALUE;

        /**
         * 编辑用户地址
         */
        public static final String EDIT_ADDRESS = "user/addressEdit/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 删除用户地址
         */
        public static final String DEL_ADDRESS = "user/addressDelete/"+TEST_TOKEN_KEY_VALUE;

        /**
         * 获取所有的收藏列表
         */
        public static final String MY_COLLECTIONS = "shop/getCollect/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 执行取消收藏
         */
        public static final String CANCEL_COLLECT = "shop/collectDelete/"+TEST_TOKEN_KEY_VALUE;

        /**
         * 执行收藏
         */
        public static final String START_COLLECT = "shop/collectAdd/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 上级用户获取该用户库存
         */
        public static final String MY_STOCKS = "shop/getMyGoods/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 更新单个产品的接口
         * 注意 这个接口和 goodlist接口一样，只是传入参数不同会有不同结果
         * 传入UID 得到产品列表
         * 传入 UID sid 则得到单个产品
         */
        public static final String REQUEST_SINGLE_PRODUCT = "shop/getGoodsList/"+TEST_TOKEN_KEY_VALUE;



        /**
         * *************************************订单相关******************************************
         */

        /**
         * 提交订单
         */
        public static final String DELIVER_ORDER = "order/orderAdd/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 获取所有订单 【包含所有订单状态......】
         */
        public static final String ALL_ORDERS = "order/getOrder/"+TEST_TOKEN_KEY_VALUE;

        /**
         * 下级确认收货
         */

        public static final String SURE_RECEIVE = "order/isReceive/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 下级申请退货
         */

        public static final String APPLAY_FOR_RETURN_GOODS = "order/isRefund/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 下级申请售后
         */

        public static final String APPLAY_FOR_SERVICE = "order/isApply/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 上级确认发货
         */

        public static final String SURE_TO_SENDING = "order/isSend/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 上级同意退货
         */
        public static final String AGREE_TO_RETURN = "order/isReturn/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 上级拒绝退货
         */
        public static final String DENY_TO_RETURN = "order/isRefuse/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 信息发布列表
         */
        public static final String NEWS_LIST = "extended/news_list/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 信息发布详情
         */
        public static final String NEWS_CONTENT = "extended/news_content/"+TEST_TOKEN_KEY_VALUE;


        /**
         * 本服务器支付charge获取
         */


        public static final String GET_PAY_CHARGE = "payment/index/"+TEST_TOKEN_KEY_VALUE;


    }


    /**
     * 请求时的动态参数
     */
    public class Key{

        public static final String IS_NEW = "isNew";

        public static final String IS_HOT = "isHot";

        public static final String IS_BOUTIQUE = "isBoutique";

        public static final String IS_RECOMMAND = "isrecommand";

    }


    public class Value {

        public static final int MAIN_4_VALUE = 1;


//        public static final int SEARCH_DEFULT_VALUE = 1;
    }




}
