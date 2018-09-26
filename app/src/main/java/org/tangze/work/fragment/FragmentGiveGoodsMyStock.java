package org.tangze.work.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.adapter.ChildMyStockAdapter;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.User;
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
 * 我的库存fragmentchild
 */
public class FragmentGiveGoodsMyStock extends BaseFragment implements PullToRefreshLayout.OnRefreshListener{


    public static final String TAG = FragmentGiveGoodsMyStock.class.getSimpleName();


    /**
     * 下拉刷新布局
     */
    private PullToRefreshLayout refresh_Layout_in_give_goods_my_stock;
    private PullableListViewUpAndDown lv_orders_common_my_stock;
    private ChildMyStockAdapter childMyStockAdapter;


    /**
     * 中间空白图片
     */

    private ImageView iv_empty;


    /**
     * 数据相关
     */
    private User user;
    private List<TopMyStock> list;
    private int loadChangePage = 1;//上拉加载更多变量page更新；
    private int freshChangePage = 1;//下拉刷新更多变量page

    @Override
    protected int setLayoutResouceId() {
        return  R.layout.fragment_give_goods_my_stock;
    }

    @Override
    protected void getSendData(Bundle arguments) {

    }

    @Override
    protected void initView() {
//        et_search_my_stock = findViewById(R.id.et_search_my_stock);
        refresh_Layout_in_give_goods_my_stock = findViewById(R.id.refresh_Layout_in_give_goods_my_stock);
        lv_orders_common_my_stock = findViewById(R.id.lv_orders_common_my_stock);
        childMyStockAdapter = new ChildMyStockAdapter(mActivity);
        lv_orders_common_my_stock.setAdapter(childMyStockAdapter);

        /**
         * 中间空白图片
         */
        iv_empty = findViewById(R.id.iv_empty);
    }

    @Override
    protected void initListener() {
        refresh_Layout_in_give_goods_my_stock.setOnRefreshListener(this);
    }

    @Override
    protected void onLazyLoad() {




        /**
         * //stock修改后需要调整
         */
        getMyStockFromServerPage();
    }




    /**
     * 将返回数据设置到界面上，并判断是否进行数据显示或隐藏
     */
    private void setDataToUi() {

        if(hindEmptyPic()) {

            childMyStockAdapter.setTopMyStocks(list);

        }

    }


    /**
     * 显示或者隐藏空图片
     *
     */
    private boolean hindEmptyPic() {

        boolean result = false;

        if (null != list && list.size() > 0) {

            iv_empty.setVisibility(View.GONE);

            result = true;

        } else {

            iv_empty.setVisibility(View.VISIBLE);

        }

        return  result;

    }



    /**
     * 显示或者隐藏空图片
     *
     */
    private void showOrHindEmptyPic(List<TopMyStock> list) {

        if( null!=list && list.size() > 0) {

            iv_empty.setVisibility(View.GONE);
        }else {

            iv_empty.setVisibility(View.VISIBLE);

        }

    }



    /**
     * //stock修改后需要调整
     */
    private void getMyStockFromServerPage() {
        user = DataSupport.findFirst(User.class);

        int userId = user.getUser_id();

        Map map = ParaUtils.getMyStockInTop(userId,loadChangePage,ConstBase.ON_LOADMORE);

        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.MY_STOCKS, map, new HttpResultSubscriber<JsonArray>(mActivity) {
            @Override
            public void onSuccess(JsonArray jsonArray) {
                Log.i(HttpConst.SERVER_BACK, "=========上级用户获取库存返回成功====" + jsonArray.toString());

                list = HttpReturnParse.getInstance().parseMyStock(jsonArray);

                setDataToUi();

                changePageByLoadCount(list);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                Log.i(HttpConst.SERVER_BACK, "==========上级用户获取库存返回失败=== " + resultCode + " " + resultMessage);
                ToastUtil.showMsg(mActivity.getApplicationContext(), "" + resultCode + " " + resultMessage);

                showOrHindEmptyPic(null);

                childMyStockAdapter.setTopMyStocks(null);
            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(mActivity.getApplicationContext(), error);

                Log.i(HttpConst.SERVER_BACK, "==========级用户获取库存返回失败====" + error);

                showOrHindEmptyPic(null);

                childMyStockAdapter.setTopMyStocks(null);
            }
        });

    }


    /**
     * 根据加载的数量来判断是否进行 加载page和下拉刷新page的变更
     * //stock修改后需要调整
     */
    private void changePageByLoadCount(List<TopMyStock> loadList) {

        if(null != loadList && loadList.size() == ConstBase.SINGLE_LOAD_COUNT) {



            freshChangePage = loadChangePage;

            loadChangePage++;

        }

    }












    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//        TestStartRequestSingle();


//stock修改后需要调整
        getMyStockFromServerByReFresh();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

//        testLoadMore();

//stock修改后需要调整
        getMyStockFromServerByLoadMore();


    }


    /**
     * 上拉加载新数据
     * //stock修改后需要调整
     */
    private void getMyStockFromServerByLoadMore() {
        if(null == user) {

            return;
        }

        int userId = user.getUser_id();

        Map map = ParaUtils.getMyStockInTop(userId,loadChangePage,ConstBase.ON_LOADMORE);

        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.MY_STOCKS, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {
                Log.i(HttpConst.SERVER_BACK, "=========上级用户获取库存返回成功====" + jsonArray.toString());



                List<TopMyStock> topMyStocks = HttpReturnParse.getInstance().parseMyStock(jsonArray);

                setNewLoadDataToUI(topMyStocks);


                changePageByLoadCount(topMyStocks);

                refresh_Layout_in_give_goods_my_stock.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                Log.i(HttpConst.SERVER_BACK, "==========用户获取库存返回失败=== " + resultCode + " " + resultMessage);

                refresh_Layout_in_give_goods_my_stock.loadmoreFinish(PullToRefreshLayout.FAIL);


                ToastUtil.showMsg(mActivity.getApplicationContext(), resultMessage);
            }

            @Override
            public void _onError(String error) {


                Log.i(HttpConst.SERVER_BACK, "==========级用户获取库存返回失败====" + error);

                refresh_Layout_in_give_goods_my_stock.loadmoreFinish(PullToRefreshLayout.FAIL);

                ToastUtil.showMsg(mActivity.getApplicationContext(), error);

            }
        });



    }


    /**
     * 下拉刷新数据
     *
     * //stock修改后需要调整
     */

    private void getMyStockFromServerByReFresh() {


        if(null == user) {

            return;
        }

        int userId = user.getUser_id();


        Map map = ParaUtils.getMyStockInTop(userId,freshChangePage, ConstBase.ON_FRESH);


        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.MY_STOCKS, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {
                Log.i(HttpConst.SERVER_BACK, "=========上级用户获取库存返回成功====" + jsonArray.toString());


                list = HttpReturnParse.getInstance().parseMyStock(jsonArray);


                childMyStockAdapter.setTopMyStocks(list);


                refresh_Layout_in_give_goods_my_stock.refreshFinish(PullToRefreshLayout.SUCCEED);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                Log.i(HttpConst.SERVER_BACK, "==========上级用户获取库存返回失败=== " + resultCode + " " + resultMessage);

                refresh_Layout_in_give_goods_my_stock.refreshFinish(PullToRefreshLayout.FAIL);

                ToastUtil.showMsg(mActivity.getApplicationContext(),resultMessage);
            }

            @Override
            public void _onError(String error) {

                Log.i(HttpConst.SERVER_BACK, "==========级用户获取库存返回失败====" + error);

                refresh_Layout_in_give_goods_my_stock.refreshFinish(PullToRefreshLayout.FAIL);


                ToastUtil.showMsg(mActivity.getApplicationContext(), error);
            }
        });

    }


    /**
     * 设置上拉加载更多的数据到界面上
     * //stock修改后需要调整
     * @param topMyStocks
     */
    private void setNewLoadDataToUI(List<TopMyStock> topMyStocks) {

        if(null!= topMyStocks && topMyStocks.size()>0) {

            for (int i = 0 ; i < topMyStocks.size();i++) {


                list.add(topMyStocks.get(i));

            }
            childMyStockAdapter.setTopMyStocks(list);
        }



    }


    private void testLoadMore() {
        new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                // 千万别忘了告诉控件加载完毕了哦！




                refresh_Layout_in_give_goods_my_stock.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 3000);

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
                                   refresh_Layout_in_give_goods_my_stock.refreshFinish(PullToRefreshLayout.SUCCEED);
                               }

                               @Override
                               public void onNotSuccess(int resultCode, String resultMessage) {
                                   ToastUtil.showMsg(mActivity, resultCode + " " + resultMessage);
                                   refresh_Layout_in_give_goods_my_stock.refreshFinish(PullToRefreshLayout.FAIL);

                               }

                               @Override
                               public void _onError(String error) {

                                   ToastUtil.showMsg(mActivity, error);
                                   refresh_Layout_in_give_goods_my_stock.refreshFinish(PullToRefreshLayout.FAIL);
                               }
                           }
                );


    }



}
