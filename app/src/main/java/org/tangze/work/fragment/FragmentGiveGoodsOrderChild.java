package org.tangze.work.fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.Test.TestOrder;
import org.tangze.work.adapter.OrderStateGiveGoodsAdapter;
import org.tangze.work.callback.OrderAfterOperateInTopCallBack;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.User;
import org.tangze.work.entity.temp.ProductServerBackOrder;
import org.tangze.work.entity.temp.TopMyStock;
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
 * 5状态的fragment
 */
public class FragmentGiveGoodsOrderChild extends BaseFragment implements PullToRefreshLayout.OnRefreshListener, CompoundButton.OnCheckedChangeListener
        , OrderAfterOperateInTopCallBack {

    public static final String TAG = FragmentGiveGoodsOrderChild.class.getSimpleName();

    /**
     * 订单5状态
     */
    private RadioGroup rg_5_state;//订单5状态的整体布局
    private RadioButton rb_wait_for_send;//待发货
    private RadioButton rb_sended;//已出货
    private RadioButton rb_finish;//已完成
    private RadioButton rb_back_goods;//退货处理
    private RadioButton rb_denied_applay;//售后中 【不做处理】


    /**
     * 下拉刷新布局
     */
    private PullToRefreshLayout refresh_Layout_in_give_goods;
    private PullableListViewUpAndDown lv_orders_5_state;
    private OrderStateGiveGoodsAdapter orderStateGiveGoodsAdapter;

    /**
     * 中间空白图片
     */
    private ImageView iv_empty;


    /**
     * 数据相关
     *
     * @param savedInstanceState
     */
    private int currentOrderType;
    private User user;
    private List<ProductServerBackOrder> list;//配货上下修改后
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fragmentChilld", TAG + "=============Oncreat");

    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_give_goods_order_child;
    }

    @Override
    protected void getSendData(Bundle arguments) {

    }

    @Override
    protected void initView() {


        /**
         * 订单5状态
         */

        rg_5_state = findViewById(R.id.rg_5_state);//订单5状态的整体布局
        rb_wait_for_send = findViewById(R.id.rb_wait_for_send);//待发货
        rb_sended = findViewById(R.id.rb_sended);//已出货
        rb_finish = findViewById(R.id.rb_finish);//已完成
        rb_back_goods = findViewById(R.id.rb_back_goods);//退货处理
        rb_denied_applay = findViewById(R.id.rb_denied_applay);//已拒绝


        /**
         * 下拉刷新布局
         */
        refresh_Layout_in_give_goods = findViewById(R.id.refresh_Layout_in_give_goods);
        lv_orders_5_state = findViewById(R.id.lv_orders_5_state);
        orderStateGiveGoodsAdapter = new OrderStateGiveGoodsAdapter(mActivity);
        orderStateGiveGoodsAdapter.setOrderAfterOperateInTopCallBack(this);
        lv_orders_5_state.setAdapter(orderStateGiveGoodsAdapter);


        /**
         * 中间空白图片
         */

        iv_empty = findViewById(R.id.iv_empty);
    }

    @Override
    protected void initListener() {
        rb_wait_for_send.setOnCheckedChangeListener(this);//待发货
        rb_sended.setOnCheckedChangeListener(this);//已出货
        rb_finish.setOnCheckedChangeListener(this);//已完成
        rb_back_goods.setOnCheckedChangeListener(this);//退货处理
        rb_denied_applay.setOnCheckedChangeListener(this);//已拒绝

        refresh_Layout_in_give_goods.setOnRefreshListener(this);
    }

    @Override
    protected void onLazyLoad() {
        user = DataSupport.findFirst(User.class);
        rb_wait_for_send.setChecked(true);
    }


    /**
     * 根据当前type和来进行差量更新
     * //配货上下修改后
     */
    private void getOrderDataFromServerByFreshPage(String url, int currentType) {

        int user_id = user.getUser_id();


        int freshPageType = getFreshPageByCurrentOrderType(currentType);

        Map map = ParaUtils.getAllOrderInfoInTop(user_id, currentType, freshPageType, ConstBase.ON_FRESH);

        Log.i(HttpConst.SERVER_BACK, "上级配货订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单返回成功FragmentGiveGoodsOrderChild中==" + jsonArray.toString());

                list = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);

                orderStateGiveGoodsAdapter.setList(list);

                refresh_Layout_in_give_goods.refreshFinish(PullToRefreshLayout.SUCCEED);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单返回失败FragmentGiveGoodsOrderChild中==" + resultCode + " " + resultMessage);

                refresh_Layout_in_give_goods.refreshFinish(PullToRefreshLayout.SUCCEED);

                ToastUtil.showMsg(mActivity.getApplicationContext(), resultMessage);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单订单返回失败FragmentGiveGoodsOrderChild中==" + error);

                ToastUtil.showMsg(mActivity.getApplicationContext(), error);

                refresh_Layout_in_give_goods.refreshFinish(PullToRefreshLayout.SUCCEED);

            }
        });
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//        TestStartRequestSingle();


//配货上下修改后
        getOrderDataFromServerByFreshPage(HttpConst.URL.ALL_ORDERS, currentOrderType);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {


//        testLoadMore();

        ////配货上下修改后
        getOrderDataFromServerByLoadMore(HttpConst.URL.ALL_ORDERS, currentOrderType);

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
     * 通过加载更多方式来获取数据
     *  //配货上下修改后
     * @param url
     * @param currentType 当前的orederType类型
     */
    private void getOrderDataFromServerByLoadMore(String url, int currentType) {




        int user_id = user.getUser_id();

//        int loadPageType = getFirstPageTypeByCurrentOrderType(currentType); //wen增量修改前



        int loadPageType = getFristPageByCurrentOrderTypeInAddLate(currentType); //wen增量修改后


        Map map = ParaUtils.getAllOrderInfoInTop(user_id, currentType, loadPageType, ConstBase.ON_LOADMORE);

        Log.i(HttpConst.SERVER_BACK, "上级配货订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单返回成功FragmentGiveGoodsOrderChild中==" + jsonArray.toString());

                List<ProductServerBackOrder> loadList = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);

                setNewLoadDataToUI(loadList);

                changePageByLoadCount(loadList);

                refresh_Layout_in_give_goods.loadmoreFinish(PullToRefreshLayout.SUCCEED);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单返回失败FragmentGiveGoodsOrderChild中==" + resultCode + " " + resultMessage);


                ToastUtil.showMsg(mActivity.getApplicationContext(), resultMessage);

                refresh_Layout_in_give_goods.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单订单返回失败FragmentGiveGoodsOrderChild中==" + error);

                ToastUtil.showMsg(mActivity.getApplicationContext(), error);

                refresh_Layout_in_give_goods.loadmoreFinish(PullToRefreshLayout.SUCCEED);

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
            orderStateGiveGoodsAdapter.setList(list);
        }


    }


    private void testLoadMore() {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 千万别忘了告诉控件加载完毕了哦！


                refresh_Layout_in_give_goods.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 3000);

    }





    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            switch (buttonView.getId()) {


                case R.id.rb_wait_for_send://待发货
                    currentOrderType = ConstIntent.BundleValue.ORDER_NOT_SEND;

                    //配货上下修改后
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS, currentOrderType);

                    break;

                case R.id.rb_sended://已出货
                    currentOrderType = ConstIntent.BundleValue.ORDER_SENDING;



                    //配货上下修改后
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS, currentOrderType);
                    break;

                case R.id.rb_finish://已完成

                    currentOrderType = ConstIntent.BundleValue.ORDER_FINISH;


                    //配货上下修改后
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS, currentOrderType);
                    break;

                case R.id.rb_back_goods://退货申请

                    currentOrderType = ConstIntent.BundleValue.ORDER_BACK_GOODS_APPLY;


                    //配货上下修改后
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS, currentOrderType);
                    break;

                case R.id.rb_denied_applay://已拒绝

                    currentOrderType = ConstIntent.BundleValue.ORDER_DENIED;

                    //配货上下修改后
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS, currentOrderType);

                    break;
            }
        }
    }

    /**
     * 显示或者隐藏空图片
     */
    private void showOrHindEmptyPic(List<ProductServerBackOrder> list) {

        if (null != list && list.size() > 0) {

            iv_empty.setVisibility(View.GONE);
        } else {

            iv_empty.setVisibility(View.VISIBLE);

        }

    }




    /**
     * 点击radioButton时，得到当前的loadType
     * //配货上下修改后
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


    /**
     * 根据当前的order类型来增加page的数量变化
     * //配货上下修改后
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
     * 根据状态参数从服务器获不同状态的订单取数据
     * //配货上下修改后
     *
     * @param url
     * @param currentType
     */
    private void getOrderDataFromServerInPage(String url, int currentType) {

        int user_id = user.getUser_id();

        int loadPageType = getFirstPageTypeByCurrentOrderType(currentType);

        Map map = ParaUtils.getAllOrderInfoInTop(user_id, currentType, loadPageType, ConstBase.ON_FRESH);

        Log.i(HttpConst.SERVER_BACK, "上级配货订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>(mActivity) {
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

                orderStateGiveGoodsAdapter.setList(null);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单订单返回失败FragmentGiveGoodsOrderChild中==" + error);

                ToastUtil.showMsg(mActivity.getApplicationContext(), error);

                showOrHindEmptyPic(null);

                orderStateGiveGoodsAdapter.setList(null);
            }
        });

    }


    /**
     * 根据加载的数量来判断是否进行 加载page和下拉刷新page的变更
     * //配货上下修改后
     */
    private void changePageByLoadCount(List<ProductServerBackOrder> loadList) {

        if (null != loadList && loadList.size() <= ConstBase.SINGLE_LOAD_COUNT) {

            pageChangeByCurrentOrderType(currentOrderType);

        }

    }


    /**
     * 将返回数据设置到界面上，并判断是否进行数据显示或隐藏
     * //配货上下修改后
     */
    private void setDataToUi() {

        if (hindEmptyPic()) {

            orderStateGiveGoodsAdapter.setList(list);

        }

    }


    /**
     * 显示或者隐藏空图片
     * //配货上下修改后
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
     * test下拉刷新
     */

    private void TestStartRequestSingle() {


        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {


                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer 6f0dc10366c3498da245d5d95527c914f02493981d564e6d905d646c42df5ce5")
                        .addHeader("Content-Type", "application/json; charset=UTF-8")
                        .build();

                return chain.proceed(newRequest);
            }
        };


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(7, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://things.loocaa.com/api/2.0.0/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        TestHttpApi api = retrofit.create(TestHttpApi.class);

        Observable<TestHttpResult<JsonObject>> observable = api.getAndroidData();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TestHttpResultSubscriber<JsonObject>() {
                               @Override
                               public void onSuccess(JsonObject jsonObject) {

                                   ToastUtil.showMsg(mActivity, jsonObject.toString());
                                   refresh_Layout_in_give_goods.refreshFinish(PullToRefreshLayout.SUCCEED);
                               }

                               @Override
                               public void onNotSuccess(int resultCode, String resultMessage) {
                                   ToastUtil.showMsg(mActivity, resultCode + " " + resultMessage);
                                   refresh_Layout_in_give_goods.refreshFinish(PullToRefreshLayout.FAIL);

                               }

                               @Override
                               public void _onError(String error) {

                                   ToastUtil.showMsg(mActivity, error);
                                   refresh_Layout_in_give_goods.refreshFinish(PullToRefreshLayout.FAIL);
                               }
                           }
                );


    }

    @Override
    public void getAllOrderInTopByOrderStateCallBack() {


//配货上下修改后
        getOrderDataFromServerInPageForCallBack(HttpConst.URL.ALL_ORDERS, currentOrderType);

    }




    /**
     * 确认发货相关请求之后，的callback差量更新
     *     //配货上下修改后
     * @param url
     * @param currentType
     */
    private void getOrderDataFromServerInPageForCallBack(String url, int currentType) {

        int user_id = user.getUser_id();



        int freshPageType = getFreshPageByCurrentOrderType(currentType);

        Map map = ParaUtils.getAllOrderInfoInTop(user_id, currentType, freshPageType, ConstBase.ON_FRESH);

        Log.i(HttpConst.SERVER_BACK, "上级配货订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>(mActivity) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单返回成功FragmentGiveGoodsOrderChild中==" + jsonArray.toString());

                list = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);

                setDataToUi();

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单返回失败FragmentGiveGoodsOrderChild中==" + resultCode + " " + resultMessage);

                ToastUtil.showMsg(mActivity.getApplicationContext(), resultMessage);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===上级获取获取所有下级订单订单返回失败FragmentGiveGoodsOrderChild中==" + error);

                ToastUtil.showMsg(mActivity.getApplicationContext(), error);

            }
        });


    }

}
