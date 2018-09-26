package org.tangze.work.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.Test.TestOrder;
import org.tangze.work.Test.TestOrderChild;
import org.tangze.work.adapter.OrderStateAdapter;
import org.tangze.work.callback.OrderAfterOperateCallBack;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.User;
import org.tangze.work.entity.temp.ProductServerBackOrder;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.http.TestHttp.TestHttpApi;
import org.tangze.work.http.TestHttp.TestHttpResult;
import org.tangze.work.http.TestHttp.TestHttpResultSubscriber;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.pullfresh.PullToRefreshLayout;
import org.tangze.work.widget.pullfresh.PullableListView;
import org.tangze.work.widget.pullfresh.PullableListViewUpAndDown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 订单状态的activty
 */
public class OrderStateActivity extends BaseActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener
        , PullToRefreshLayout.OnRefreshListener,OrderAfterOperateCallBack {


    /**
     * title
     */
    private ImageView iv_back_in_order_state;
    private TextView tv_title_in_order_state;
    private ImageView iv_common_search;


    /**
     * 中间4状态
     */
    private RadioButton rb_order_no_send;   //未发货
    private RadioButton rb_order_sending; //发货中
    private RadioButton rb_order_finish; //已完成
    private RadioButton rb_back_goods_in_down_child;//退款申请
    private RadioButton rb_denied_in_down_child; //已取消


    /**
     * 中间空白图片
     */
    private ImageView iv_empty;


    /**
     * 底部下拉刷新
     */
    private PullToRefreshLayout refresh_Layout_in_order_state;
    private PullableListViewUpAndDown plv_state_orders;
    private OrderStateAdapter orderStateAdapter; //核心adapter


    /**
     * 数据相关
     */
    private User user;

    /**
     * //OrderState修改后
     */
    private List<ProductServerBackOrder> list;
    /**
     * 待发货、已发货、已完成、退货申请、已拒绝
     * 加载更多变量和 差量下拉刷新更新变量
     * 配货上下修改后
     */
    private int wait_for_send_loadPage = 1;//待发货上拉加载更多变量page更新；
    private int wait_for_send_freshPage = 1;//待发货下拉差量刷新变量page

    private int sended_loadPage = 1;//已发货上拉加载更多变量page更新；
    private int sended_freshPage = 1;///已发货下拉差量刷新变量page

    private int finish_loadPage = 1;//已完成上拉加载更多变量page更新；
    private int finish_freshPage = 1;//已完成下拉差量刷新变量page

    private int back_goods_loadPage = 1;//退货申请上拉加载更多变量page更新；
    private int back_goods_freshPage = 1;//退货申请下拉差量刷新变量page

    private int denied_applay_loadPage = 1;//已拒绝上拉加载更多变量page更新；
    private int denied_applay_freshPage = 1;//已拒绝下拉差量刷新变量page


    /**
     * 跳转传递数据
     */
    private Intent intent;
    private Bundle bundle;
    private int currentType; //判断是从前一个界面的那个按钮跳转过来的 5 状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_state);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        user = DataSupport.findFirst(User.class);


        if(null == user) {

            return;
        }

        switch (currentType) {


            case ConstIntent.BundleValue.ORDER_NOT_SEND://未发货
                rb_order_no_send.setChecked(true);

                break;

            case ConstIntent.BundleValue.ORDER_SENDING://发货中
                rb_order_sending.setChecked(true);
                break;

            case ConstIntent.BundleValue.ORDER_FINISH://已完成
                rb_order_finish.setChecked(true);
                break;

            case ConstIntent.BundleValue.ORDER_BACK_GOODS_APPLY://申请退款
                rb_back_goods_in_down_child.setChecked(true);
                break;


            case ConstIntent.BundleValue.ORDER_DENIED://已拒绝
                rb_denied_in_down_child.setChecked(true);
                break;


        }

    }


    @Override
    protected void initViews() {
        iv_back_in_order_state = (ImageView) findViewById(R.id.iv_common_back);
        tv_title_in_order_state = (TextView) findViewById(R.id.tv_common_title);
        tv_title_in_order_state.setText(getString(R.string.my_orders));
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_search.setVisibility(View.GONE);

        /**
         * 底部下拉刷新
         */
        refresh_Layout_in_order_state = (PullToRefreshLayout) findViewById(R.id.refresh_Layout_in_order_state);
        plv_state_orders = (PullableListViewUpAndDown) findViewById(R.id.plv_state_orders);
        orderStateAdapter = new OrderStateAdapter(this);
        orderStateAdapter.setOrderAfterOperateCallBack(this);
        plv_state_orders.setAdapter(orderStateAdapter);
        /**
         * 中间5状态
         */
        rb_order_no_send = (RadioButton) findViewById(R.id.rb_order_no_send);//未发货
        rb_order_sending = (RadioButton) findViewById(R.id.rb_order_sending);//发货中
        rb_order_finish = (RadioButton) findViewById(R.id.rb_order_finish); //已完成
        rb_back_goods_in_down_child = (RadioButton) findViewById(R.id.rb_back_goods_in_down_child);//退款申请
        rb_denied_in_down_child = (RadioButton) findViewById(R.id.rb_denied_in_down_child); //已拒绝

        /**
         * 中间空白图片
         */

        iv_empty = (ImageView) findViewById(R.id.iv_empty);

    }

    @Override
    protected void initListener() {
        iv_back_in_order_state.setOnClickListener(this);


        /**
         * 底部下拉刷新
         */
        refresh_Layout_in_order_state.setOnRefreshListener(this);

        /**
         * 中间4状态
         */
        rb_order_no_send.setOnCheckedChangeListener(this);//未发货
        rb_order_sending.setOnCheckedChangeListener(this);//发货中
        rb_order_finish.setOnCheckedChangeListener(this); //已完成
        rb_back_goods_in_down_child.setOnCheckedChangeListener(this); //退款申请
        rb_denied_in_down_child.setOnCheckedChangeListener(this); //已拒绝

    }

    @Override
    protected void processIntent() {

        intent = this.getIntent();
        if (null != intent) {

            bundle = this.intent.getExtras();


            if (null != bundle) {

                this.currentType = bundle.getInt(ConstIntent.BundleKEY.ORDER_STATE);

            }

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_common_back:
                this.finish();
                break;

        }
    }




    /**
     * 显示或者隐藏空图片
     */
    private void showOrHindEmptyPic(List<ProductServerBackOrder> list) {

        if( null!=list && list.size() > 0) {

            iv_empty.setVisibility(View.GONE);
        }else {

            iv_empty.setVisibility(View.VISIBLE);

        }

    }



    /**
     * 根据状态参数从服务器获不同状态的订单取数据
     * @param url
     * @param currentType
     */
    private void getOrderDataFromServer(String url,int currentType) {

        int user_id = user.getUser_id();
        Map map = ParaUtils.getAllOrderInfo(user_id, currentType);
        Log.i(HttpConst.SERVER_BACK, "我的订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回成功OrderStateActivity中==" + jsonArray.toString());



                List<ProductServerBackOrder> list = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);


                showOrHindEmptyPic(list);

                orderStateAdapter.setList(list);

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败OrderStateActivity中==" + resultCode + " " + resultMessage);


                showOrHindEmptyPic(null);

                orderStateAdapter.setList(null);
            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(getApplicationContext(), error);


                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败OrderStateActivity中==" + error);


                showOrHindEmptyPic(null);

                orderStateAdapter.setList(null);
            }
        });

    }



    /**
     * 根据状态参数从服务器获不同状态的订单取数据
     * ////OrderState修改后
     *
     * @param url
     * @param currentType
     */
    private void getOrderDataFromServerInPage(String url, int currentType) {

        int user_id = user.getUser_id();

        int loadPageType = getFirstPageTypeByCurrentOrderType(currentType);

        Map map = ParaUtils.getAllOrderInfo(user_id, currentType, loadPageType, ConstBase.ON_FRESH);

        Log.i(HttpConst.SERVER_BACK, "我的订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单返回成功FragmentGiveGoodsOrderChild中==" + jsonArray.toString());

                list = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);

                setDataToUi();

                changePageByLoadCount(list);

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单返回失败FragmentGiveGoodsOrderChild中==" + resultCode + " " + resultMessage);

                showOrHindEmptyPic(null);

                orderStateAdapter.setList(null);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单订单返回失败FragmentGiveGoodsOrderChild中==" + error);

                ToastUtil.showMsg(OrderStateActivity.this.getApplicationContext(), error);

                showOrHindEmptyPic(null);

                orderStateAdapter.setList(null);
            }
        });

    }


    /**
     * 点击radioButton时，得到当前的loadType
     * //OrderState修改后
     *
     * @param currentType
     * @return
     */
    private int getFirstPageTypeByCurrentOrderType(int currentType) {

        int loadPageType = 0;

        switch (currentType) {

            case ConstIntent.BundleValue.ORDER_NOT_SEND://待发货

                loadPageType = wait_for_send_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_SENDING://已出货

                loadPageType = sended_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_FINISH://已完成

                loadPageType = finish_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_BACK_GOODS_APPLY://退货申请

                loadPageType = back_goods_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_DENIED://已拒绝

                loadPageType = denied_applay_loadPage;

                break;
        }

        return loadPageType;

    }


    /**
     * wen增量修改后
     * @param currentType
     * @return
     */
    private int getFristPageByCurrentOrderTypeInAddLate(int currentType) {

        int loadPageType = 0;

        switch (currentType) {

            case ConstIntent.BundleValue.ORDER_NOT_SEND://待发货

                wait_for_send_loadPage++;
                loadPageType = wait_for_send_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_SENDING://已出货
                sended_loadPage++;
                loadPageType = sended_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_FINISH://已完成
                finish_loadPage++;
                loadPageType = finish_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_BACK_GOODS_APPLY://退货申请
                back_goods_loadPage++;
                loadPageType = back_goods_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_DENIED://已拒绝
                denied_applay_loadPage++;
                loadPageType = denied_applay_loadPage;

                break;
        }

        return loadPageType;
    }


    /**
     * 将返回数据设置到界面上，并判断是否进行数据显示或隐藏
     * //OrderState修改后
     */
    private void setDataToUi() {

        if (hindEmptyPic()) {

            orderStateAdapter.setList(list);

        }

    }

    /**
     * 显示或者隐藏空图片
     * //OrderState修改后
     */
    private boolean hindEmptyPic() {

        boolean result = false;

        if (null != list && list.size() > 0) {

            iv_empty.setVisibility(View.GONE);

            result = true;

        } else {
            iv_empty.setVisibility(View.VISIBLE);

        }

        return result;

    }


    /**
     * 根据加载的数量来判断是否进行 加载page和下拉刷新page的变更
     * //OrderState修改后
     */
    private void changePageByLoadCount(List<ProductServerBackOrder> loadList) {

        if (null != loadList && loadList.size() <= ConstBase.SINGLE_LOAD_COUNT) {

            pageChangeByCurrentOrderType(currentType);

        }

    }


    /**
     * 根据当前的order类型来增加page的数量变化
     * //OrderState修改后
     *
     * @param currentType
     */
    private void pageChangeByCurrentOrderType(int currentType) {


        switch (currentType) {

            case ConstIntent.BundleValue.ORDER_NOT_SEND://待发货


                wait_for_send_freshPage = wait_for_send_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_SENDING://已出货


                sended_freshPage = sended_loadPage;
                break;

            case ConstIntent.BundleValue.ORDER_FINISH://已完成


                finish_freshPage = finish_loadPage;
                break;

            case ConstIntent.BundleValue.ORDER_BACK_GOODS_APPLY://退货申请


                back_goods_freshPage = back_goods_loadPage;
                break;

            case ConstIntent.BundleValue.ORDER_DENIED://已拒绝


                denied_applay_freshPage = denied_applay_loadPage;

                break;
        }
    }


    /**
     * 下拉刷新
     * 根据状态参数从服务器获不同状态的订单取数据
     * @param url
     * @param currentType
     */
    private void getOrderDataFromServerInFresh(String url,int currentType) {

        int user_id = user.getUser_id();
        Map map = ParaUtils.getAllOrderInfo(user_id,currentType);

        Log.i(HttpConst.SERVER_BACK, "我的订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {

                List<ProductServerBackOrder> list = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);


                showOrHindEmptyPic(list);


                orderStateAdapter.setList(list);

                refresh_Layout_in_order_state.refreshFinish(PullToRefreshLayout.SUCCEED);

                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回成功OrderStateActivity中==" + jsonArray.toString());
            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败OrderStateActivity中==" + resultCode + " " + resultMessage);
                refresh_Layout_in_order_state.refreshFinish(PullToRefreshLayout.SUCCEED);
                showOrHindEmptyPic(null);
                orderStateAdapter.setList(null);
            }

            @Override
            public void _onError(String error) {
                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败OrderStateActivity中==" + error);
                ToastUtil.showMsg(getApplicationContext(), error);
                refresh_Layout_in_order_state.refreshFinish(PullToRefreshLayout.FAIL);
                showOrHindEmptyPic(null);
                orderStateAdapter.setList(null);
            }
        });

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

            switch (buttonView.getId()) {

                case R.id.rb_order_no_send://未发货
                    currentType = ConstIntent.BundleValue.ORDER_NOT_SEND;

                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS,currentType);//OrderState修改后


                    break;
                case R.id.rb_order_sending://发货中
                    currentType = ConstIntent.BundleValue.ORDER_SENDING;
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS,currentType);//OrderState修改后


                    break;

                case R.id.rb_order_finish://已完成
                    currentType = ConstIntent.BundleValue.ORDER_FINISH;
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS,currentType);//OrderState修改后


                    break;


                case R.id.rb_back_goods_in_down_child://申请退款
                    currentType = ConstIntent.BundleValue.ORDER_BACK_GOODS_APPLY;
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS,currentType);//OrderState修改后

                    break;


                case R.id.rb_denied_in_down_child://申请退款
                    currentType = ConstIntent.BundleValue.ORDER_DENIED;
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS,currentType);//OrderState修改后
                    break;
            }
        }
    }




    /**
     * 根据当前type和来进行差量更新
     * //OrderState修改后
     */
    private void getOrderDataFromServerByFreshPage(String url, int currentType) {

        int user_id = user.getUser_id();


        int freshPageType = getFreshPageByCurrentOrderType(currentType);

        Map map = ParaUtils.getAllOrderInfo(user_id, currentType, freshPageType, ConstBase.ON_FRESH);

        Log.i(HttpConst.SERVER_BACK, "我的订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回成功OrderStateActivity中==" + jsonArray.toString());

                list = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);

                orderStateAdapter.setList(list);

                refresh_Layout_in_order_state.refreshFinish(PullToRefreshLayout.SUCCEED);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败OrderStateActivity中==" + resultCode + " " + resultMessage);

                refresh_Layout_in_order_state.refreshFinish(PullToRefreshLayout.SUCCEED);

                ToastUtil.showMsg(OrderStateActivity.this.getApplicationContext(), resultMessage);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败OrderStateActivity中==" + error);

                ToastUtil.showMsg(OrderStateActivity.this.getApplicationContext(), error);

                refresh_Layout_in_order_state.refreshFinish(PullToRefreshLayout.SUCCEED);

            }
        });
    }


    /**
     * 通过currentType来获取当前差量更新的page
     * //配货上下修改后
     * @param currentType
     */
    private int getFreshPageByCurrentOrderType(int currentType){
        int freshPageType = 0;

        switch (currentType) {

            case ConstIntent.BundleValue.ORDER_NOT_SEND://待发货

                freshPageType = wait_for_send_freshPage;

                break;

            case ConstIntent.BundleValue.ORDER_SENDING://已出货

                freshPageType = sended_freshPage;

                break;

            case ConstIntent.BundleValue.ORDER_FINISH://已完成

                freshPageType = finish_freshPage;

                break;

            case ConstIntent.BundleValue.ORDER_BACK_GOODS_APPLY://退货申请

                freshPageType = back_goods_freshPage;

                break;

            case ConstIntent.BundleValue.ORDER_DENIED://已拒绝

                freshPageType = denied_applay_freshPage;

                break;
        }

        return freshPageType;
    }




    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {




        //OrderState修改后
        getOrderDataFromServerByFreshPage(HttpConst.URL.ALL_ORDERS,currentType);

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {



//        testLoadMore();

        //OrderState修改后

        getOrderDataFromServerByLoadMore(HttpConst.URL.ALL_ORDERS, currentType);

    }


    private void testLoadMore() {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 千万别忘了告诉控件加载完毕了哦！


                refresh_Layout_in_order_state.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 3000);

    }


    /**
     * 通过加载更多方式来获取数据
     *   //OrderState修改后
     * @param url
     * @param currentType 当前的orederType类型
     *
     */
    private void getOrderDataFromServerByLoadMore(String url, int currentType) {


        int user_id = user.getUser_id();

//        int loadPageType = getFirstPageTypeByCurrentOrderType(currentType);



        int loadPageType = getFristPageByCurrentOrderTypeInAddLate(currentType);

        Map map = ParaUtils.getAllOrderInfo(user_id, currentType, loadPageType, ConstBase.ON_LOADMORE);

        Log.i(HttpConst.SERVER_BACK, "我的订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回成功OrderStateActivity中==" + jsonArray.toString());

                List<ProductServerBackOrder> loadList = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);

                setNewLoadDataToUI(loadList);

                changePageByLoadCount(loadList);

                refresh_Layout_in_order_state.loadmoreFinish(PullToRefreshLayout.SUCCEED);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败OrderStateActivity中==" + resultCode + " " + resultMessage);


                ToastUtil.showMsg(OrderStateActivity.this.getApplicationContext(), resultMessage);

                refresh_Layout_in_order_state.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败OrderStateActivity中==" + error);

                ToastUtil.showMsg(OrderStateActivity.this.getApplicationContext(), error);

                refresh_Layout_in_order_state.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });

    }


    /**
     * 设置上拉加载更多的数据到界面上
     * //配货上下修改后
     *
     * @param loadList
     */
    private void setNewLoadDataToUI(List<ProductServerBackOrder> loadList) {

        if (null != loadList && loadList.size() > 0) {

            for (int i = 0; i < loadList.size(); i++) {


                list.add(loadList.get(i));

            }
            orderStateAdapter.setList(list);
        }


    }



    /**
     * 回调函数
     */
    @Override
    public void getAllOrderByOrderStateCallBack() {


        //OrderState修改后
        getOrderDataFromServerInPageForCallBack(HttpConst.URL.ALL_ORDERS, currentType);

    }



    /**
     * 确认发货相关请求之后，的callback差量更新
     *     //OrderState修改后
     * @param url
     * @param currentType
     */
    private void getOrderDataFromServerInPageForCallBack(String url, int currentType) {

        int user_id = user.getUser_id();



        int freshPageType = getFreshPageByCurrentOrderType(currentType);

        Map map = ParaUtils.getAllOrderInfo(user_id, currentType, freshPageType, ConstBase.ON_FRESH);

        Log.i(HttpConst.SERVER_BACK, "我的订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回成功OrderStateActivity中CallBack==" + jsonArray.toString());

                list = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);

                setDataToUi();

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败OrderStateActivity中CallBack==" + resultCode + " " + resultMessage);

                ToastUtil.showMsg(OrderStateActivity.this.getApplicationContext(), resultMessage);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败OrderStateActivity中CallBack==" + error);

                ToastUtil.showMsg(OrderStateActivity.this.getApplicationContext(), error);

            }
        });


    }
}
