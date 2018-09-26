package org.tangze.work.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.adapter.OrderStateServiceAdapter;
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
 * 售后3状态的activity
 */
public class CustomerServiceActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener
        ,PullToRefreshLayout.OnRefreshListener{


    /**
     * title
     */
    private ImageView iv_back_in_order_costomer_service;
    private TextView tv_title_in_order_costomer_service;
    private ImageView iv_common_search;


    /**
     * 中间3状态
     */
    private RadioButton rb_wait_to_deal_costomer;   //待受理
    private RadioButton rb_costomer_servicing; //售后中
    private RadioButton rb_costomer_service_finish; //已完成

    /**
     * 中间空白图片
     */
    private ImageView iv_empty;

    /**
     * 底部下拉刷新
     */
    private PullToRefreshLayout refresh_Layout_in_costomer_service;
    private PullableListViewUpAndDown plv_state_orders_costomer_service;
    private OrderStateServiceAdapter orderStateServiceAdapter;

    /**
     *数据相关
     *
     */
    private int currentType;
    private User user;


    /**
     * //CustomerService修改后
     */
    private List<ProductServerBackOrder> list;//配货上下修改后
    /**
     * 待发货、已发货、已完成、退货申请、已拒绝
     * 加载更多变量和 差量下拉刷新更新变量
     * //CustomerService修改后
     */
    private int order_wait_to_deal_loadPage = 1;//待受理上拉加载更多变量page更新；
    private int order_wait_to_deal_freshPage = 1;//待受理下拉差量刷新变量page

    private int order_dealing_loadPage = 1;//受理中上拉加载更多变量page更新；
    private int order_dealing_freshPage = 1;//受理中下拉差量刷新变量page

    private int order_deal_finish_loadPage = 1;//受理完成 （已完成）上拉加载更多变量page更新；
    private int order_deal_finish_freshPage = 1;//受理完成 （已完成）下拉差量刷新变量page







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        initData();
    }


    private void initData() {
        user = DataSupport.findFirst(User.class);
        rb_wait_to_deal_costomer.setChecked(true);
    }

    @Override
    protected void initViews() {
        iv_back_in_order_costomer_service = (ImageView) findViewById(R.id.iv_common_back);
        tv_title_in_order_costomer_service = (TextView) findViewById(R.id.tv_common_title);
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_search.setVisibility(View.GONE);
        tv_title_in_order_costomer_service.setText(getString(R.string.costomer_service));




        /**
         * 中间4状态
         */
          rb_wait_to_deal_costomer = (RadioButton) findViewById(R.id.rb_wait_to_deal_costomer);   //待受理
          rb_costomer_servicing = (RadioButton) findViewById(R.id.rb_costomer_servicing); //售后中
          rb_costomer_service_finish = (RadioButton) findViewById(R.id.rb_costomer_service_finish); //已完成



        /**
         * 中间空白图片
         */
          iv_empty = (ImageView) findViewById(R.id.iv_empty);


        /**
         * 底部下拉刷新
         */
        refresh_Layout_in_costomer_service = (PullToRefreshLayout) findViewById(R.id.refresh_Layout_in_costomer_service);
        plv_state_orders_costomer_service = (PullableListViewUpAndDown) findViewById(R.id.plv_state_orders_costomer_service);
        orderStateServiceAdapter = new OrderStateServiceAdapter(this);
        plv_state_orders_costomer_service.setAdapter(orderStateServiceAdapter);
    }

    @Override
    protected void initListener() {
        iv_back_in_order_costomer_service.setOnClickListener(this);

        /**
         * 中间4状态
         */
        rb_wait_to_deal_costomer.setOnCheckedChangeListener(this);   //待受理
        rb_costomer_servicing .setOnCheckedChangeListener(this); //售后中
        rb_costomer_service_finish .setOnCheckedChangeListener(this); //已完成


        /**
         * 底部下拉刷新
         */
          refresh_Layout_in_costomer_service.setOnRefreshListener(this);
    }

    @Override
    protected void processIntent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_common_back:
                this.finish();
                break;


        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked) {

            switch (buttonView.getId()) {

                case R.id.rb_wait_to_deal_costomer: //待受理

                    currentType = ConstIntent.BundleValue.ORDER_WAIT_TO_DEAL;





                    //CustomerService修改后
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS,currentType);

                    break;


                case R.id.rb_costomer_servicing://售后中
                    currentType = ConstIntent.BundleValue.ORDER_DEALING;

                    //CustomerService修改后
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS,currentType);
                    break;


                case R.id.rb_costomer_service_finish: //已完成
                    currentType = ConstIntent.BundleValue.ORDER_DEAL_FINISH;

                    //CustomerService修改后
                    getOrderDataFromServerInPage(HttpConst.URL.ALL_ORDERS,currentType);
                    break;
            }

        }

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {




//        TestStartRequestSingle();

//CustomerService修改后
        getOrderDataFromServerByFreshPage(HttpConst.URL.ALL_ORDERS,currentType);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {


//        testLoadMore();


        //CustomerService修改后
        getOrderDataFromServerByLoadMore(HttpConst.URL.ALL_ORDERS,currentType);

    }




    /**
     * 根据当前type和来进行差量更新
     * //CustomerService修改后
     */
    private void getOrderDataFromServerByFreshPage(String url, int currentType) {

        int user_id = user.getUser_id();

        int freshPageType =  getFristPageByCurrentOrderTypeInAddLate(currentType);
        Map map = ParaUtils.getAllOrderInfo(user_id, currentType, freshPageType, ConstBase.ON_FRESH);

        Log.i(HttpConst.SERVER_BACK, "我的订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取售后订单返回成功CustomerServiceActivity中=下拉刷新=" + jsonArray.toString());

                list = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);

                orderStateServiceAdapter.setList(list);

                refresh_Layout_in_costomer_service.refreshFinish(PullToRefreshLayout.SUCCEED);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===获取售后订单返回失败CustomerServiceActivity中=下拉刷新=" + resultCode + " " + resultMessage);

                refresh_Layout_in_costomer_service.refreshFinish(PullToRefreshLayout.SUCCEED);

                ToastUtil.showMsg(CustomerServiceActivity.this.getApplicationContext(), resultMessage);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===获取售后订单返回失败CustomerServiceActivity中=下拉刷新=" + error);

                ToastUtil.showMsg(CustomerServiceActivity.this.getApplicationContext(), error);

                refresh_Layout_in_costomer_service.refreshFinish(PullToRefreshLayout.SUCCEED);

            }
        });
    }






    /**
     * wen增量修改后
     * @param currentType
     * @return
     */
    private int getFristPageByCurrentOrderTypeInAddLate(int currentType) {

        int loadPageType = 0;

        switch (currentType) {

            case ConstIntent.BundleValue.ORDER_WAIT_TO_DEAL://待受理
                order_wait_to_deal_loadPage++;
                loadPageType = order_wait_to_deal_freshPage;

                break;

            case ConstIntent.BundleValue.ORDER_DEALING://受理中
                order_dealing_loadPage++;
                loadPageType = order_dealing_freshPage;

                break;

            case ConstIntent.BundleValue.ORDER_DEAL_FINISH://已完成
                order_deal_finish_loadPage++;
                loadPageType = order_deal_finish_freshPage;

                break;


        }

        return loadPageType;
    }




    /**
     * 通过加载更多方式来获取数据
     *  //CustomerService修改后
     * @param url
     * @param currentType 当前的orederType类型
     *
     */
    private void getOrderDataFromServerByLoadMore(String url, int currentType) {


        int user_id = user.getUser_id();

        int loadPageType = getFirstPageTypeByCurrentOrderType(currentType);

        Map map = ParaUtils.getAllOrderInfo(user_id, currentType, loadPageType, ConstBase.ON_LOADMORE);

        Log.i(HttpConst.SERVER_BACK, "我的订单参数===map==" + map.toString());

        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取所有售后订单返回成功CustomerServiceActivity中上拉加载==" + jsonArray.toString());

                List<ProductServerBackOrder> loadList = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);

                setNewLoadDataToUI(loadList);

                changePageByLoadCount(loadList);

                refresh_Layout_in_costomer_service.loadmoreFinish(PullToRefreshLayout.SUCCEED);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有售后订单返回失败CustomerServiceActivity中上拉加载==" + resultCode + " " + resultMessage);


                ToastUtil.showMsg(CustomerServiceActivity.this.getApplicationContext(), resultMessage);

                refresh_Layout_in_costomer_service.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有售后订单返回失败CustomerServiceActivity中上拉加载==" + error);

                ToastUtil.showMsg(CustomerServiceActivity.this.getApplicationContext(), error);

                refresh_Layout_in_costomer_service.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });

    }




    private void testLoadMore() {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 千万别忘了告诉控件加载完毕了哦！


                refresh_Layout_in_costomer_service.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 3000);

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
        Log.i(HttpConst.SERVER_BACK, "售后订单参数===map==" + map.toString());
        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {

                List<ProductServerBackOrder> list = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);


                showOrHindEmptyPic(list);

                orderStateServiceAdapter.setList(list);

                Log.i(HttpConst.SERVER_BACK, "===获取所有售后订单返回成功CustomerServiceActivity中==" + jsonArray.toString());
            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有售后订单返回失败CustomerServiceActivity中==" + resultCode + " " + resultMessage);

                showOrHindEmptyPic(null);

                orderStateServiceAdapter.setList(null);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有售后订单返回失败CustomerServiceActivity中==" + error);

                showOrHindEmptyPic(null);

                orderStateServiceAdapter.setList(null);

            }
        });

    }



    /**
     * 根据状态参数从服务器获不同状态的订单取数据
     * //CustomerService修改后
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


                Log.i(HttpConst.SERVER_BACK, "====获取所有售后订单返回成功CustomerServiceActivity中==" + jsonArray.toString());

                list = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);

                setDataToUi();

                changePageByLoadCount(list);

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有售后订单返回失败CustomerServiceActivity中==" + resultCode + " " + resultMessage);

                showOrHindEmptyPic(null);

                orderStateServiceAdapter.setList(null);

            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "===获取所有售后订单返回失败CustomerServiceActivity中==" + error);

                ToastUtil.showMsg(CustomerServiceActivity.this.getApplicationContext(), error);

                showOrHindEmptyPic(null);

                orderStateServiceAdapter.setList(null);
            }
        });

    }

    /**
     * 点击radioButton时，得到当前的loadType
     * //CustomerService修改后
     *
     * @param currentType
     * @return
     */
    private int getFirstPageTypeByCurrentOrderType(int currentType) {

        int loadPageType = 0;


        switch (currentType) {

            case ConstIntent.BundleValue.ORDER_WAIT_TO_DEAL://待受理

                loadPageType = order_wait_to_deal_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_DEALING://售后中

                loadPageType = order_dealing_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_DEAL_FINISH://受理完成已完成

                loadPageType = order_deal_finish_loadPage;

                break;

        }

        return loadPageType;

    }



    /**
     * 将返回数据设置到界面上，并判断是否进行数据显示或隐藏
     * //CustomerService修改后
     */
    private void setDataToUi() {

        if (hindEmptyPic()) {

            orderStateServiceAdapter.setList(list);

        }

    }


    /**
     * 显示或者隐藏空图片
     * //CustomerService修改后
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
     * //CustomerService修改后
     */
    private void changePageByLoadCount(List<ProductServerBackOrder> loadList) {

        if (null != loadList && loadList.size() <= ConstBase.SINGLE_LOAD_COUNT) {

            pageChangeByCurrentOrderType(currentType);

        }

    }


    /**
     * 根据当前的order类型来增加page的数量变化
     * //CustomerService修改后
     *
     * @param currentType
     */
    private void pageChangeByCurrentOrderType(int currentType) {




        switch (currentType) {

            case ConstIntent.BundleValue.ORDER_WAIT_TO_DEAL://待受理


                order_wait_to_deal_freshPage = order_wait_to_deal_loadPage;

                break;

            case ConstIntent.BundleValue.ORDER_DEALING://售后中


                order_dealing_freshPage = order_dealing_loadPage;
                break;

            case ConstIntent.BundleValue.ORDER_DEAL_FINISH://受理完成已完成


                order_deal_finish_freshPage = order_deal_finish_loadPage;
                break;


        }
    }




    /**
     * 设置上拉加载更多的数据到界面上
     * //CustomerService修改后
     *
     * @param loadList
     */
    private void setNewLoadDataToUI(List<ProductServerBackOrder> loadList) {

        if (null != loadList && loadList.size() > 0) {

            for (int i = 0; i < loadList.size(); i++) {


                list.add(loadList.get(i));

            }
            orderStateServiceAdapter.setList(list);
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
        Log.i(HttpConst.SERVER_BACK, "售后订单参数===map==" + map.toString());
        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {

                List<ProductServerBackOrder> list = HttpReturnParse.getInstance().parseAllOrdersBack(jsonArray);

                showOrHindEmptyPic(list);

                orderStateServiceAdapter.setList(list);

                refresh_Layout_in_costomer_service.refreshFinish(PullToRefreshLayout.SUCCEED);

                Log.i(HttpConst.SERVER_BACK, "===获取所有售后订单返回成功CustomerServiceActivity中下拉刷新==" + jsonArray.toString());
            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                Log.i(HttpConst.SERVER_BACK, "===获取所有售后订单返回失败CustomerServiceActivity中下拉刷新==" + resultCode + " " + resultMessage);

                showOrHindEmptyPic(null);

                orderStateServiceAdapter.setList(null);

                refresh_Layout_in_costomer_service.refreshFinish(PullToRefreshLayout.SUCCEED);

            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(getApplicationContext(), error);

                Log.i(HttpConst.SERVER_BACK, "===获取所有售后订单返回失败CustomerServiceActivity中下拉刷新==" + error);

                showOrHindEmptyPic(null);

                orderStateServiceAdapter.setList(null);

                refresh_Layout_in_costomer_service.refreshFinish(PullToRefreshLayout.FAIL);
            }
        });

    }


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

                                   ToastUtil.showMsg(getApplicationContext(), jsonObject.toString());
                                   refresh_Layout_in_costomer_service.refreshFinish(PullToRefreshLayout.SUCCEED);
                               }

                               @Override
                               public void onNotSuccess(int resultCode, String resultMessage) {
                                   ToastUtil.showMsg(getApplicationContext(), resultCode + " " + resultMessage);
                                   refresh_Layout_in_costomer_service.refreshFinish(PullToRefreshLayout.FAIL);

                               }

                               @Override
                               public void _onError(String error) {

                                   ToastUtil.showMsg(getApplicationContext(), error);
                                   refresh_Layout_in_costomer_service.refreshFinish(PullToRefreshLayout.FAIL);
                               }
                           }
                );


    }
}
