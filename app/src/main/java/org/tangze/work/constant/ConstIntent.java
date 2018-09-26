package org.tangze.work.constant;

/**
 * Created by Administrator on 2016/9/22 0022.
 * 类描述  intent常量
 * 版本
 */
public class ConstIntent {

    /**
     * bundleKey
     */
    public class BundleKEY {


        public static final String ADDRESS = "address";

        public static final String ADDRESS_ENTITY = "address_entity";

        public static final String ORDER_STATE = "order_state";// 订单状态【应用于订单5状态】


        public static final String LOGOUT = "logout";//用户成功退出的key


        public static final String PRODUCT_TO_DETIAL = "product_key";

        public static final String DETIAL_TO_LOGIN = "detail_to_login";

        public static final String CLASSIFY_SECONDS_TO_PRODUCT = "classify_seconds_to_product";//点击二级分类进入到产品列表界面

        public static final String ClASSFITY_TO_SECOND_CLASSIFY = "classify_to_second_classify";//一级分类中点击进入二级分类


        public static final String PRODUCT_ORDER_TO_SETTLEMENT = "product_order_to_setttlement";

        public static final String PRODUCT_ORDER_TO_SHOW = "product_order_to_show";


        public static final String PRODUCT_ORDER_SHOW_TYPE = "product_order_to_show_type";

        public static final String PRODUCT_TO_CRITERIA_TYPE = "product_to_criteria_type";


        public static final String SETTMENT_TO_ADDRESS = "settlment_to_address"; //从结算界面进入到地址设置界面的key


        public static final String SERCH_TEXT = "search_text";//从搜索界面进入到ProductCriteriaActivity界面传递的String key


        public static final String SHOWINFO_LIST_TO_DETIAL = "showinfo_list_to_detail";
    }


    public class BundleValue {




        public static final int ADD_NEW_ADDRESS = 0;
        public static final int EDIT_ADDRESS = 1;
        public static final int LOGOUT = 1;



        /**
         * 订单6状态
         * 以后可能会修改
         */
        public static final int ORDER_NOT_SEND = 1; //未发货
        public static final int ORDER_SENDING = 2; //发货中
        public static final int ORDER_FINISH= 3; //已完成
        public static final int ORDER_BACK_GOODS_APPLY = -2;//申请退货
        public static final int ORDER_DENIED = -3; //已拒绝
        //售后相关

        public static final int ORDER_WAIT_TO_DEAL = -4; //待受理
        public static final int ORDER_DEALING = -5; //受理中
        public static final int ORDER_DEAL_FINISH = 3; //受理完成 （已完成）

        /**
         * 产品的详情界面，点击收藏，判断登陆时，跳转的回传
         */


        public static final int DETIAL_TO_LONGIN = 1;


        /**
         * 展示订单的跳转
         */
        public static final int MY_ORDER_TO_SHOW = 0;  //我的订单

        public static final int GIVE_GOODS_ORDER_TO_SHOW = 1;  //配货订单详情


        /**
         * 新品上市：1
         * 热卖商品：2
         * 精品推荐：3
         * 特价专区：4
         */
        public static final int NEW_ARRIVAL_PRODUCT = 1;  //新品上市
        public static final int HOT_PRODUCT = 2;  //热卖商品
        public static final int BOUTIQUE_ARRIVAL = 3;  //精品推荐
        public static final int MAIN_RECOMMAND = 4;  //首页推荐


        public static final int SETTMENT_TO_ADDRESS = 1; //从结算界面进入到地址设置界面的Value



        public static final int SEARCH_TO_PRO_CRITERIA = 99;//从搜索界面进入到ProductCriteriaActivity界面

    }









    /**
     * requestCode,activty跳转时，携带的请求码
     */

    public class RequestCode {


        public static final int ADD_NEW_ADDRESS = 0;   //添加新地址的请求码


        public static final int SETTLEMENT_TO_ADDRESS = 1;//从结算界面进入到地址设置界面
    }

    /**
     * responseCode,目标activty销毁时，携带的返回码
     */


    public class ResponseCode {

        public static final int LOGIN_FAIL_OR_CLICK_BACK = 1; //登陆失败或直接关闭界面的返回值


        public static final int LOGIN_SUCCESS = 0; //登陆成功的返回值


//        public static final int REGISTER_SUCCESS = 0;//  注册成功返回



        public static final int ADD_NEW_ADDRESS_SUCCESS_NOT_DEFUALT = 0; //添加地址成功，但不设置为默认地址


        public static final int ADD_NEW_ADDRESS_SUCCESS_DEFUALT = 1; //添加地址成功，设置为默认


        public static final int EDIT_ADDRESS_SUCCESS = 2;//编辑地址成功



        public static final int SET_ADDRESS_SUCCESS_TO_SETTLMENT = 5;//成功设置默认地址后的界面跳回SettlmentActity的responseCode

    }


    public class IntentAction {

        public static final String USER_EDIT_SUCCESS = "user_edit_success";


        public static final String DELIVER_ORDER_SUCCESS = "deliver_order_success";


        public static final String ORDER_SUCCESS_UPDAT_SHOPCAR = "order_success_update_shopcar";

        public static final String ADDRESS_SUCCESS_SET_BACK_SETTLEMENT = "address_success_set_back_settlement"; //结算界面中设置地址后返回的广播


    }

}
