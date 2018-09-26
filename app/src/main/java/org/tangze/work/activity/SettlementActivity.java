package org.tangze.work.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.pingplusplus.android.Pingpp;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.adapter.SettlementAdapter;
import org.tangze.work.callback.OnKeyboardsChangeListener;
import org.tangze.work.callback.PayCallBack;
import org.tangze.work.constant.ConstAddress;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.constant.ConstPay;
import org.tangze.work.entity.Address;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.temp.ProductOrder;
import org.tangze.work.entity.ShoppingCar;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.RandomCodeUtil;
import org.tangze.work.utils.StringUtils;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.layout.InputMethodLayout;
import org.tangze.work.widget.popwindow.PayPopWindow;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 结算activity
 */
public class SettlementActivity extends BaseActivity implements OnClickListener, OnKeyboardsChangeListener, PayCallBack {

    /**
     * title
     */
    private ImageView iv_settlement_back;
    private TextView tv_settlement_title;
    private ImageView iv_common_search;

    /**
     * 上部默认地址
     */

    private LinearLayout ll_consignee_and_phone;//联系人和电话布局
    private TextView tv_reciver_name_in_settlement;
    private TextView tv_reciver_number_in_settlement;
    private TextView tv_reciever_address_detai_in_settlement;
    private LinearLayout ll_address_in_settlement; //地址所在布局
    private TextView tv_text_address_for_set; //“设置收货地址”的textView


    /**
     * scrollView和最外面的布局
     */

    private ScrollView sv_settlement;
    private InputMethodLayout iml;


    /**
     * 中间listView
     */
    private SettlementAdapter settlementAdapter;
    private ListView lv_product_to_order;


    /**
     * listView下面的总数
     */
    private TextView tv_product_count;
    /**
     * 下部订单备注
     */
    private EditText et_order_remark;

    /**
     * 底部提交
     */

    private TextView tv_total_all_price;
    private LinearLayout ll_deliver_order_in_settlement;


    /**
     * 相关数据
     */

    private Intent intent;
    private Bundle bundle;
    private ProductOrder productOrder;


    /**
     * 测试支付
     */

    private Button btn_1;
    private Button btn_2;

    /**
     * 相关广播
     *
     * @param savedInstanceState
     */
    private AddressEditSuccessBackReceiver addressEditSuccessBackReceiver; //address设置完成修改后

    private PayPopWindow payPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        initData();
    }


    private void initData() {


        //注册广播

        registerBroadCast();//address设置完成修改后

        /**
         * 1.设置地址
         * 2.设置product产品列表
         * 3.设置一共多少商品
         * 4.设置合计
         */

        /**
         * 1.设置地址：
         */
        setDefultAddressToUi();
        /**
         * 2.设置product产品列表
         * 调整界面
         */
        setProductListToUi();

        /**
         * 3.设置一共买了多少商品
         */
        setBuyCountToUi();
        /**
         * 4.显示合计价格（即总计价格）
         */
        setTotalPriceToUi();

    }

    /**
     * 设置默认地址到UI
     */
    private Address setDefultAddressToUi() {

        List<Address> addresses = DataSupport.where("isdefault = ?", ConstAddress.IS_DEFUALT_ADDR).find(Address.class);
        Address address = null;
        if (null != addresses && addresses.size() > 0) {
            ShowUIByHasDefaultAddress();
            address = addresses.get(0);
            tv_reciver_name_in_settlement.setText(address.getConsignee());
            tv_reciver_number_in_settlement.setText(address.getTelNum());
            tv_reciever_address_detai_in_settlement.setText(address.getAddressDetail());
            productOrder.setAddressDefault(address);
        } else {

            ShowUIByNoAddress();

        }
//        productOrder.setAddressDefault(address);
        return address;
    }


    /**
     * 有默认地址时，顶部UI变更
     */
    private void ShowUIByHasDefaultAddress() {

        ll_consignee_and_phone.setVisibility(View.VISIBLE);
        tv_reciever_address_detai_in_settlement.setVisibility(View.VISIBLE);
        tv_text_address_for_set.setVisibility(View.GONE);

    }

    /**
     * 没有默认地址或没有地址时，ui变更
     */

    private void ShowUIByNoAddress() {

        ll_consignee_and_phone.setVisibility(View.GONE);
        tv_reciever_address_detai_in_settlement.setVisibility(View.GONE);
        tv_text_address_for_set.setVisibility(View.VISIBLE);
    }


    /**
     * 设置product产品列表
     */
    private void setProductListToUi() {

        //wen订单提交新修改
        List<Product> product_transmit = productOrder.getProducts_transmit();

        if (null != product_transmit && product_transmit.size() > 0) {

            Map<Integer, Integer> map = productOrder.getMap();

            settlementAdapter.setDatas(product_transmit, map);

        }
        adajustListView();


    }

    /**
     * 设置一共要购买了多少商品
     */
    private void setBuyCountToUi() {


        //wen订单提交新修改
        int count = 0;

        List<Product> product_transmit = productOrder.getProducts_transmit();

        Map<Integer, Integer> map = productOrder.getMap();

        if (null != product_transmit && product_transmit.size() > 0) {

            for (int i = 0; i < product_transmit.size(); i++) {

                count += map.get(product_transmit.get(i).getProduct_id());

            }

        }

        tv_product_count.setText(String.valueOf(count));
    }


    /**
     * 设置购买总价到界面ui
     */

    private void setTotalPriceToUi() {


        //wen订单提交新修改
        float totalPrice = 0;
        List<Product> product_transmit = productOrder.getProducts_transmit();
        Map<Integer, Integer> map = productOrder.getMap();

        for (int i = 0; i < product_transmit.size(); i++) {

            Product product = product_transmit.get(i);

            int num = map.get(product.getProduct_id()); //得到对应商品的购买数量


            float singleLocalPrice = Float.valueOf(product.getLocalPrice());

            totalPrice += num * singleLocalPrice;

        }
        productOrder.setTotalprice(String.valueOf(totalPrice));
        tv_total_all_price.setText(String.valueOf(totalPrice));

    }

    /**
     * 调整界面冲突
     */
    private void adajustListView() {

        setListViewHeightBasedOnChildren(lv_product_to_order); //ListView适应ScrollView

        //解决进入界面后，srollview自动滑动到下部的listView，listView上面的数据不再顶部
        ll_address_in_settlement.setFocusable(true);
        ll_address_in_settlement.setFocusableInTouchMode(true);
        ll_address_in_settlement.requestFocus();
    }


    @Override
    protected void initViews() {


        /**
         * 测试
         */
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);

        /**
         * title
         */
        iv_settlement_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_settlement_title = (TextView) findViewById(R.id.tv_common_title);
        tv_settlement_title.setText(getString(R.string.settlement));
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_search.setVisibility(View.GONE);
        /**
         * 上部默认地址
         */
        ll_consignee_and_phone = (LinearLayout) findViewById(R.id.ll_consignee_and_phone);
        tv_reciver_name_in_settlement = (TextView) findViewById(R.id.tv_reciver_name_in_settlement);
        tv_reciver_number_in_settlement = (TextView) findViewById(R.id.tv_reciver_number_in_settlement);
        tv_reciever_address_detai_in_settlement = (TextView) findViewById(R.id.tv_reciever_address_detai_in_settlement);
        ll_address_in_settlement = (LinearLayout) findViewById(R.id.ll_address_in_settlement);
        tv_text_address_for_set = (TextView) findViewById(R.id.tv_text_address_for_set);


        /**
         * scrollView和最外面的布局
         */

        sv_settlement = (ScrollView) findViewById(R.id.sv_settlement);
        iml = (InputMethodLayout) findViewById(R.id.iml);

        /**
         * 中间listView
         */
        lv_product_to_order = (ListView) findViewById(R.id.lv_product_to_order);
        settlementAdapter = new SettlementAdapter(this);
        lv_product_to_order.setAdapter(settlementAdapter);


        tv_product_count = (TextView) findViewById(R.id.tv_product_count_in_footView);


        /**
         * 底部订单备注
         */
        et_order_remark = (EditText) findViewById(R.id.et_order_remark);

        /**
         * 底部提交
         */
        tv_total_all_price = (TextView) findViewById(R.id.tv_total_all_price);
//        tv_total_transport_price = (TextView) findViewById(R.id.tv_total_transport_price);
        ll_deliver_order_in_settlement = (LinearLayout) findViewById(R.id.ll_deliver_order_in_settlement);

    }

    @Override
    protected void initListener() {


        /**
         * 测试
         */
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);


        /**
         * title
         */
        iv_settlement_back.setOnClickListener(this);


        /**
         * 进入地址设置的点击事件
         */
        ll_address_in_settlement.setOnClickListener(this);


        /**
         * 底部提交
         */
        ll_deliver_order_in_settlement.setOnClickListener(this);


        /**
         * 点击设置键盘显示
         */

        iml.setOnKeyboardsChangeListener(this);


    }


    /**
     * 软件盘数据该改变的回掉接口
     *
     * @param state
     */

    @Override
    public void onKeyBoardStateChange(int state) {
        switch (state) {
            case InputMethodLayout.KEYBOARD_STATE_HIDE:

                break;
            case InputMethodLayout.KEYBOARD_STATE_SHOW:


                srollToTargetWhenKeyboardShow();

                break;

            default:
                break;
        }
    }


    /**
     * 显示自动滑动
     */
    private void srollToTargetWhenKeyboardShow() {

        sv_settlement.post(new MyRunnable(this));


    }


    class MyRunnable implements Runnable {


        private WeakReference<Context> weakReference;

        public MyRunnable(Context context) {

            weakReference = new WeakReference<Context>(context);


        }

        @Override
        public void run() {

            if (weakReference.get() == null) {


                return;
            }

            int count = sv_settlement.getChildCount();

            int height = 0;

            for (int i = 0; i < count; i++) {

                View view = sv_settlement.getChildAt(i);

                int h = view.getMeasuredHeight();

                height += h;
            }

            Log.i("quanbugaodu", height + " 当前线程id：" + Thread.currentThread().getId());

            sv_settlement.scrollTo(0, height);
        }
    }


    @Override
    protected void processIntent() {
        intent = this.getIntent();
        if (null != intent) {

            bundle = intent.getExtras();

            if (null != bundle) {

                productOrder = (ProductOrder) bundle.getSerializable(ConstIntent.BundleKEY.PRODUCT_ORDER_TO_SETTLEMENT);


                List<Product> pros = productOrder.getProducts_transmit();

                for (int i = 0; i < pros.size(); i++) {

                    Log.i("shopcarProId", "购物车中接收到的商品对象列表product_id：" + pros.get(i).getProduct_id());
                }
            }
        }
    }


    private void testGetCharge(int type) {

        Map<String, Object> map = new HashMap<>();


        if (type == 1) {


            /**
             * 限制规则：
             * alipay : 1-64 位，  支付宝支付
             * wx : 2-32 位，     微信支付
             * bfb : 1-20 位，     百度钱包
             * upacp : 8-40 位，银联支付
             * yeepay_wap :1-50 位， 易宝手机网页支付
             * jdpay_wap :1-30 位，京东手机网页支付
             * qpay :1-30 位，QQ 钱包
             * cmb_wallet :10 位纯数字字符串。   招行一网通
             * 注：除  cmb_wallet 外的其他渠道推荐使用 8-20 位，要求数字或字母，不允许特殊字符)。
             */

            map.put("order_no", "a515s1dfdssfssasd"); //生成一个随机字符串，注意要参看API 有的支付渠道对字符串的长度有限制
            map.put("amount", 100);
            map.put("app", "app_Sa1GGSCW5q9GqXT4");
            map.put("channel", "alipay");
            map.put("currency", "cny");
            map.put("client_ip", "127.0.0.1");
            map.put("subject", "支付宝测试商品标题");
            map.put("body", "支付宝测试商品描述");


        } else {


            map.put("order_no", "a515s1dfsdsssss");
            map.put("amount", 100);
            map.put("app", "app_Sa1GGSCW5q9GqXT4");
            map.put("channel", "wx");
            map.put("currency", "cny");
            map.put("client_ip", "127.0.0.1");

            map.put("subject", "微信测试商品标题");
            map.put("body", "微信测试商品描述");


        }


        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.GET_PAY_CHARGE, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                ToastUtil.showMsg(SettlementActivity.this, jsonArray.toString());


                String serverback = jsonArray.toString();


                JSONObject jsonObject = null;
                JSONArray jsonArray1 = null;
                try {
                    jsonArray1 = new JSONArray(serverback);

                    jsonObject = (JSONObject) jsonArray1.get(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String chageBACK = jsonObject.toString();

                Log.i("zhifucharge", chageBACK);


                Pingpp.createPayment(SettlementActivity.this, chageBACK);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {


                ToastUtil.showMsg(SettlementActivity.this, resultCode + " " + resultMessage);

            }

            @Override
            public void _onError(String error) {


                ToastUtil.showMsg(SettlementActivity.this, error);

            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
            /* 处理返回值
             * "success" - 支付成功
             * "fail"    - 支付失败
             * "cancel"  - 取消支付
             * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
             */

                if(result.equals("success")){

                    deliverTheOrderToServer();

                }


                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息


                Log.i("zhifujieguo", "pay_result:" + result + "错误描述：" + errorMsg + " 错误描述：" + extraMsg);


            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            /**
             * 测试
             */

            case R.id.btn_1:

                testGetCharge(1);
                break;

            case R.id.btn_2:
                testGetCharge(2);
                break;


            case R.id.iv_common_back:
                this.finish();
                break;


            case R.id.ll_address_in_settlement:

                //address设置完成修改前
//                goToAddessPage();

                //address设置完成修改后
                gotAddressSetPage();

                break;


            case R.id.ll_deliver_order_in_settlement:


//                deliverTheOrderToServer();


                showPayPopWindow();
                //2017223需要还原
//                testZip();
                break;

        }
    }


    /**
     * popowindwo中点击支付的回调函数
     *
     * @param payChannel
     */

    @Override
    public void payByChannel(String payChannel) {


        Map<String, Object> map = new HashMap<>();


        /**
         * 限制规则：
         * alipay : 1-64 位，  支付宝支付 要求数字或字母
         * wx : 2-32 位，     微信支付
         * bfb : 1-20 位，     百度钱包
         * upacp : 8-40 位，银联支付
         * yeepay_wap :1-50 位， 易宝手机网页支付
         * jdpay_wap :1-30 位，京东手机网页支付
         * qpay :1-30 位，QQ 钱包
         * cmb_wallet :10 位纯数字字符串。   招行一网通
         * 注：除  cmb_wallet 外的其他渠道推荐使用 8-20 位，要求数字或字母，不允许特殊字符)。
         */


        ProductOrder finalOrder = getFinalOrder();
        Float toltalPrice = Float.valueOf(finalOrder.getTotalprice());

        map.put(ConstPay.KEY.ORDER_NO, RandomCodeUtil.creatNumAndLetter(25)); //生成一个随机字符串，注意要参看API 有的支付渠道对字符串的长度有限制
        map.put(ConstPay.KEY.AMOUNT, 100 * toltalPrice);
        map.put(ConstPay.KEY.APP, ConstPay.VALUE.APP_SIGN);
        map.put(ConstPay.KEY.CHANNEL, payChannel);
        map.put(ConstPay.KEY.CURRENCY, ConstPay.VALUE.CURRENCY);
        map.put(ConstPay.KEY.SUBJECT, ConstPay.VALUE.COMMON_SUBJECT);
        map.put(ConstPay.KEY.CLIENT_IP, StringUtils.getIpAddressString());


        String proName = getProductNameAndCount(finalOrder);


        Log.i("productName"," 组合名称："+proName);


        map.put(ConstPay.KEY.BODY, proName);


        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.GET_PAY_CHARGE, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {




                String chargeBack = HttpReturnParse.getInstance().parseChargeBack(jsonArray);


                Pingpp.createPayment(SettlementActivity.this, chargeBack);


//                ToastUtil.showMsg(SettlementActivity.this, jsonArray.toString());
//
//
//                String serverback = jsonArray.toString();
//
//
//                JSONObject jsonObject = null;
//                JSONArray jsonArray1 = null;
//                try {
//                    jsonArray1 = new JSONArray(serverback);
//
//                    jsonObject = (JSONObject) jsonArray1.get(0);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                ssssssssssssssss
//
//                String chageBACK = jsonObject.toString();
//
//
//
//
//                Pingpp.createPayment(SettlementActivity.this, chageBACK);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {


                ToastUtil.showMsg(SettlementActivity.this, resultCode + " " + resultMessage);

            }

            @Override
            public void _onError(String error) {


                ToastUtil.showMsg(SettlementActivity.this, error);

            }
        });


    }


    /**
     * 提交购买产品时候，获取产品的名称作为产品描述
     *
     * @param productOrder
     * @return
     */
    private String getProductNameAndCount(ProductOrder productOrder) {

        String name = "";

        List<Product> list = productOrder.getProducts_transmit();

        Map map = productOrder.getMap();

        if (null != list && list.size() > 0) {

            if (list.size() == 1) {

                Product product = list.get(0);

                name = product.getProductName() +ConstPay.COUNT_TO_PAY+map.get(product.getProduct_id());

                return name;

            } else {


                for (int i = 0; i < list.size(); i++) {

                    Product product = list.get(i);

                    String nameTemp = product.getProductName()+ConstPay.COUNT_TO_PAY+map.get(product.getProduct_id());


                    if (i == list.size() - 1) {

                        name = name + nameTemp;

                    } else {
                        name = name + nameTemp + ConstBase.COMMA;

                    }


                }


            }


        }


        return name;


    }


    /**
     * 显示支付的dialog
     */
    private void showPayPopWindow() {




        if (!hasDefultAddressOrNot()) {

            ToastUtil.showResMsg(this, R.string.address_First);
            return;

        }



        hideKeyboard(this, et_order_remark);

        if (null == payPopWindow) {

            payPopWindow = new PayPopWindow(this);
            payPopWindow.setPayCallBack(this);
            payPopWindow.showAtLocation(ll_deliver_order_in_settlement,
                    Gravity.BOTTOM, 0, 0);

        } else {

            payPopWindow.setPayCallBack(this);
            payPopWindow.showAtLocation(ll_deliver_order_in_settlement,
                    Gravity.BOTTOM, 0, 0);
        }

    }


    /**
     * 判断软键盘是否弹出
     */
    private void hideKeyboard(Context context, View v) {


        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);

        boolean result = imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


        Log.i("ruanjianpan", result + "");
    }


    private void testZip() {


        String s = "{\n" +
                "  \"id\": \"ch_DCOWjTaTePyT1GiX1GKWLaL8\",\n" +
                "  \"object\": \"charge\",\n" +
                "  \"created\": 1487844542,\n" +
                "  \"livemode\": true,\n" +
                "  \"paid\": false,\n" +
                "  \"refunded\": false,\n" +
                "  \"app\": \"app_K4SuvHrvjX90mLCy\",\n" +
                "  \"channel\": \"alipay\",\n" +
                "  \"order_no\": \"1000001910363083\",\n" +
                "  \"client_ip\": \"119.23.140.53\",\n" +
                "  \"amount\": 240,\n" +
                "  \"amount_settle\": 240,\n" +
                "  \"currency\": \"cny\",\n" +
                "  \"subject\": \"Your Subject\",\n" +
                "  \"body\": \"Your Body\",\n" +
                "  \"time_paid\": null,\n" +
                "  \"time_expire\": 1487930942,\n" +
                "  \"time_settle\": null,\n" +
                "  \"transaction_no\": null,\n" +
                "  \"refunds\": {\n" +
                "    \"object\": \"list\",\n" +
                "    \"url\": \"/v1/charges/ch_DCOWjTaTePyT1GiX1GKWLaL8/refunds\",\n" +
                "    \"has_more\": false,\n" +
                "    \"data\": []\n" +
                "  },\n" +
                "  \"amount_refunded\": 0,\n" +
                "  \"failure_code\": null,\n" +
                "  \"failure_msg\": null,\n" +
                "  \"metadata\": {},\n" +
                "  \"credential\": {\n" +
                "    \"object\": \"credential\",\n" +
                "    \"alipay\": {\n" +
                "      \"orderInfo\": \"service=\\\"mobile.securitypay.pay\\\"&_input_charset=\\\"utf-8\\\"&notify_url=\\\"https%3A%2F%2Fnotify.pingxx.com%2Fnotify%2Fcharges%2Fch_DCOWjTaTePyT1GiX1GKWLaL8\\\"&partner=\\\"2088911081676225\\\"&out_trade_no=\\\"1000001910363083\\\"&subject=\\\"Your Subject\\\"&body=\\\"Your Body ch_DCOWjTaTePyT1GiX1GKWLaL8\\\"&total_fee=\\\"2.40\\\"&payment_type=\\\"1\\\"&seller_id=\\\"2088911081676225\\\"&it_b_pay=\\\"2017-02-24 18:09:02\\\"&sign=\\\"gMAyOkH%2FMxgiTynL1HrEpH8T8S1Vn4Z9eT9MEZ5JkcZnBj9u2Zt6UW68PNnVIeZ46Dqa0tWNsyIhernSWb9BgCb6pOsKCa4LMU7ZndenEXyw2jlBDLxoe19yN6i0SQeeyNW7HPWWlTVV520byEUhkNPz0HnQbSo%2Bh1sE6nAOqyM%3D\\\"&sign_type=\\\"RSA\\\"\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"extra\": {},\n" +
                "  \"description\": null\n" +
                "}";
        Pingpp.createPayment(this, s);


    }

    /**
     * 进入到地址设置界面
     * //address设置完成修改前
     */

    private void goToAddessPage() {


        Intent intent = new Intent(this, AddressActivity.class);

        Bundle bundle = new Bundle();

        bundle.putInt(ConstIntent.BundleKEY.SETTMENT_TO_ADDRESS, ConstIntent.BundleValue.SETTMENT_TO_ADDRESS);


        intent.putExtras(bundle);

        startActivityForResult(intent, ConstIntent.RequestCode.SETTLEMENT_TO_ADDRESS);


    }


    //address设置完成修改后
    private void gotAddressSetPage() {


        Intent intent = new Intent(this, AddressActivity.class);

        Bundle bundle = new Bundle();

        bundle.putInt(ConstIntent.BundleKEY.SETTMENT_TO_ADDRESS, ConstIntent.BundleValue.SETTMENT_TO_ADDRESS);


        intent.putExtras(bundle);


        openActivity(AddressActivity.class, bundle);

    }


    //address设置完成修改前
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == ConstIntent.ResponseCode.SET_ADDRESS_SUCCESS_TO_SETTLMENT) {
//
//            setDefultAddressToUi();
//
//        }
//
//
//    }


    /**
     * 结算界面设置中，设置地址完成后返回的广播
     * //address设置完成修改后
     */
    private void registerBroadCast() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstIntent.IntentAction.ADDRESS_SUCCESS_SET_BACK_SETTLEMENT);

        addressEditSuccessBackReceiver = new AddressEditSuccessBackReceiver();

        this.registerReceiver(addressEditSuccessBackReceiver, filter);

    }


    private class AddressEditSuccessBackReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConstIntent.IntentAction.ADDRESS_SUCCESS_SET_BACK_SETTLEMENT.equals(action)) {

                /**
                 * 地址更新
                 */
                setDefultAddressToUi();


            }
        }
    }


    /**
     * 判是否设置默认地址
     *
     * @return
     */
    private boolean hasDefultAddressOrNot() {

        boolean result = false;

        List<Address> addresses = DataSupport.where("isdefault = ?", ConstAddress.IS_DEFUALT_ADDR).find(Address.class);

        if (null != addresses && addresses.size() > 0) {

            result = true;

        }

        return result;

    }

    private void deliverTheOrderToServer() {


        if (!hasDefultAddressOrNot()) {

            ToastUtil.showResMsg(this, R.string.address_First);
            return;

        }


        ProductOrder finalOrder = getFinalOrder();

        /**
         * 发起网络提交订单
         */

        Map map = ParaUtils.getDeliverOrder(finalOrder);


        Log.i("confirmOrder", map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.DELIVER_ORDER, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                boolean result = HttpReturnParse.getInstance().parseDeliverOrderBack(jsonArray);


                if (result) {

                    /**
                     * 订单提交成功
                     * 1.删除购物车内提交的数据列表
                     * 2.进入到成功提交界面
                     */

                    delProductInShppingCar();

                    SettlementActivity.this.openActivityAndFinishItself(DeliverOrderSuccessActivity.class, null);


                } else {

                    ToastUtil.showResMsg(SettlementActivity.this, R.string.order_commit_fail);
                }

                Log.i(HttpConst.SERVER_BACK, "===提交订单返回成功==" + jsonArray.toString());


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                ToastUtil.showMsg(getApplicationContext(), resultCode + " " + resultMessage);
                Log.i(HttpConst.SERVER_BACK, "===提交订单返回失败==" + resultCode + " " + resultMessage);


            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(getApplicationContext(), error);
                Log.i(HttpConst.SERVER_BACK, "===提交订单返回失败==" + error);
            }
        });

    }


    /**
     * 订单提交成功后，删除掉购物车中批量提交的数据
     * 删除掉购物车中的数据
     */
    private void delProductInShppingCar() {


        //wen订单提交新修改
        ProductOrder p_OrderDeliverEd = getFinalOrder();
        int userId = p_OrderDeliverEd.getUser_id();
        List<Product> products_transmit = p_OrderDeliverEd.getProducts_transmit();

        /**
         * 执行删除操作
         */

        for (int i = 0; i < products_transmit.size(); i++) {


            int pro_id = products_transmit.get(i).getProduct_id();

            DataSupport.deleteAll(ShoppingCar.class, "user_id = ? and productId = ?", String.valueOf(userId), String.valueOf(pro_id));
            DataSupport.deleteAll(Product.class, "product_id = ?", String.valueOf(pro_id));

            Log.i("shopcarProId", "被删掉的shopCar中的product_id：" + pro_id);
        }

        Intent intent = new Intent();
        intent.setAction(ConstIntent.IntentAction.ORDER_SUCCESS_UPDAT_SHOPCAR);
        sendBroadcast(intent);


    }


    private ProductOrder getFinalOrder() {


        //wen订单提交新修改
        /**
         * 1.地址相关信息整理（收货地址、联系电话、收货人）
         * 2.remark备注相关
         * 3.用户id
         * 4.上家用户id
         * 5.content；
         */


        /**
         * remark备注相关
         */
        String remarkTemp = et_order_remark.getText().toString();
        if (TextUtils.isEmpty(remarkTemp)) {
            remarkTemp = "";
        }
        productOrder.setRemarks(remarkTemp);

        /**
         * content 这里面需要根据priductid的Size()，来动态生成多个随机17位jsonKey
         */
        List<String> jsonKeys = new ArrayList<>();


        for (int i = 0; i < productOrder.getProducts_transmit().size(); i++) {

            jsonKeys.add(RandomCodeUtil.createCode());

        }
        productOrder.setPrd_json_key(jsonKeys);


        return productOrder;

    }


    /**
     * 解决listView嵌套与ScrollView中的异常问题
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        SettlementAdapter listAdapter = (SettlementAdapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (null != addressEditSuccessBackReceiver) {

            this.unregisterReceiver(addressEditSuccessBackReceiver);
        }


        if (null != payPopWindow) {

            payPopWindow.dismiss();
            payPopWindow = null;

        }


    }


}
