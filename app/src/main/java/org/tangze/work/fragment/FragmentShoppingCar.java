package org.tangze.work.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.activity.SettlementActivity;
import org.tangze.work.adapter.ShoppingCarAdapter;
import org.tangze.work.constant.ConstAddress;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Address;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.temp.ProductOrder;
import org.tangze.work.entity.ShoppingCar;
import org.tangze.work.entity.User;
import org.tangze.work.http.TestHttp.TestHttpApi;
import org.tangze.work.http.TestHttp.TestHttpResult;
import org.tangze.work.http.TestHttp.TestHttpResultSubscriber;
import org.tangze.work.utils.StringUtils;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.pullfresh.PullToRefreshLayout;
import org.tangze.work.widget.pullfresh.PullableListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
 * 购物车fragment
 */
public class FragmentShoppingCar extends BaseFragment implements OnClickListener, PullToRefreshLayout.OnRefreshListener {


    public static final String TAG = FragmentShoppingCar.class.getSimpleName();
    /**
     * titile
     */
    private TextView tv_shopping_car_fg_title;
    private ImageView iv_shopping_car_fg_search;
    private LinearLayout ll_shopping_car_to_search;
    private TextView tv_current_city;

    /**
     * 中间listView
     */

    private PullToRefreshLayout refresh_Layout_in_shopping_car;
    private PullableListView plv_orders;
    private ShoppingCarAdapter shoppingCarAdapter;


    /**
     * 空白图片
     */
    private ImageView iv_empty;


    /**
     * bottom
     */
    private CheckBox cb_select;
    private LinearLayout ll_del_seled;
    private LinearLayout ll_deliver_order;
    private LinearLayout ll_shopping_car_bottom_manager; //底部删除布局 （购物车内无数据时，则不显示）
    private boolean isSelectedAll = false;


    /**
     * 其他数据
     *
     * @return
     */

    private OrderSuccessUpdateShopcarReceiver orderSuccessUpdateShopcarReceiver;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_shopping_car;
    }

    @Override
    protected void getSendData(Bundle arguments) {

    }

    @Override
    protected void initView() {
        tv_shopping_car_fg_title = findViewById(R.id.tv_fragment_title);
        iv_shopping_car_fg_search = findViewById(R.id.iv_fragment_search);
        ll_shopping_car_to_search = findViewById(R.id.ll_fragment_to_search);
        tv_current_city = findViewById(R.id.tv_current_city);

        /**
         * 中间listView
         */

        refresh_Layout_in_shopping_car = findViewById(R.id.refresh_Layout_in_shopping_car);
        plv_orders = findViewById(R.id.plv_orders);
        shoppingCarAdapter = new ShoppingCarAdapter(mActivity);
        plv_orders.setAdapter(shoppingCarAdapter);


        /**
         * 空白图片
         */
        iv_empty = findViewById(R.id.iv_empty);


        /**
         * bottom
         */
        cb_select = findViewById(R.id.cb_select);
        ll_del_seled = findViewById(R.id.ll_del_seled);
        ll_deliver_order = findViewById(R.id.ll_deliver_order);
        ll_shopping_car_bottom_manager = findViewById(R.id.ll_shopping_car_bottom_manager);
    }

    @Override
    protected void initListener() {


        /**
         * 中间listView
         */

        refresh_Layout_in_shopping_car.setOnRefreshListener(this);

        plv_orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (null != shoppingCarAdapter) {

                    ShoppingCarAdapter.ViewHolder vh = (ShoppingCarAdapter.ViewHolder) view.getTag();

                    vh.cb_selected_in_shopping_car.toggle();

                    shoppingCarAdapter.getmSelectMap().put(position, vh.cb_selected_in_shopping_car.isChecked());


                    /**
                     * 根据选择点击项底部按钮的选中状态
                     */
                    changeLayoutBySingleSelected(shoppingCarAdapter.getmSelectMap(), shoppingCarAdapter.getList());


                    boolean type = shoppingCarAdapter.getmSelectMap().get(position);


                    Log.i("selected", "当前项：" + position + " 状态" + type);


                }

            }
        });

        /**
         * bottom
         */
        ll_del_seled.setOnClickListener(this);
        ll_deliver_order.setOnClickListener(this);
        cb_select.setOnClickListener(this);

    }


    @Override
    protected void onLazyLoad() {
        ll_shopping_car_to_search.setVisibility(View.GONE);
        iv_shopping_car_fg_search.setVisibility(View.GONE);
        tv_current_city.setVisibility(View.GONE);
        tv_shopping_car_fg_title.setText(mActivity.getResources().getString(R.string.shoppingCar));

        registerDeliverOrderSuccessBroadCast();

        getDataFromDb();

        Log.i("FragShoppingcar", "onLazyLoad");

    }


    /**
     * 显示或者隐藏空图片
     */
    private void showOrHindEmptyPic(List<ShoppingCar> list) {

        if (null != list && list.size() > 0) {

            iv_empty.setVisibility(View.GONE);
        } else {

            iv_empty.setVisibility(View.VISIBLE);

        }

    }


    /**
     * 从数据库中获取购物车内的数据
     */
    private void getDataFromDb() {


        List<ShoppingCar> shoppingCars = DataSupport.findAll(ShoppingCar.class);

        showOrHindEmptyPic(shoppingCars);

        if (shoppingCars == null || shoppingCars.size() == 0) {


            cb_select.setChecked(isSelectedAll);

            cb_select.setText(getString(R.string.sel_all));

            ll_shopping_car_bottom_manager.setVisibility(View.GONE);


        } else {

            cb_select.setChecked(isSelectedAll);

            cb_select.setText(getString(R.string.sel_all));

            ll_shopping_car_bottom_manager.setVisibility(View.VISIBLE);
        }

        shoppingCarAdapter.setList(shoppingCars);


    }


    /**
     * 点击单个项目时，根据点击数量，动态变化底部按钮状态是“全部选择/取消全选”
     */
    private void changeLayoutBySingleSelected(Map<Integer, Boolean> mSelectMap, List<ShoppingCar> shoppingCars) {

        if (null != mSelectMap && shoppingCars != null) {


            List<Boolean> allSelected = new ArrayList<>();


            for (int i = 0; i < shoppingCars.size(); i++) {


                boolean selecte = mSelectMap.get(i);

                if (selecte) {

                    allSelected.add(selecte);

                }

            }

            if (allSelected.size() == shoppingCars.size()) {

                isSelectedAll = true;


                cb_select.setText(getString(R.string.cancel_all));

                cb_select.setChecked(isSelectedAll);


            } else if (allSelected.size() < shoppingCars.size()) {

                isSelectedAll = false;

                cb_select.setText(getString(R.string.sel_all));

                cb_select.setChecked(isSelectedAll);

            }


        }

    }


    /**
     * 全选或取消全选
     *
     * @param cb
     */
    private void selectedAllOrCancelAll(CheckBox cb) {


        if (shoppingCarAdapter != null) {

            Map<Integer, Boolean> map = shoppingCarAdapter.getmSelectMap();

            if (null != map && map.size() > 0) {

                /**
                 * 判断是全选状态，还是非全选状态
                 */
                if (isSelectedAll) {

                    for (int i = 0; i < map.size(); i++) {

                        shoppingCarAdapter.getmSelectMap().put(i, false);

                    }

                    isSelectedAll = false;
                    //wen文字变化

                    cb.setText(getString(R.string.sel_all));


                    cb.setChecked(isSelectedAll);

                } else {

                    for (int i = 0; i < map.size(); i++) {

                        shoppingCarAdapter.getmSelectMap().put(i, true);

                    }

                    isSelectedAll = true;
                    //wen文字变化
                    cb.setText(getString(R.string.cancel_all));
                    cb.setChecked(isSelectedAll);
                }
                shoppingCarAdapter.notifyDataSetChanged();
            }


        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fragment", TAG + " onCreate==============================");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != orderSuccessUpdateShopcarReceiver) {

            mActivity.unregisterReceiver(orderSuccessUpdateShopcarReceiver);
        }
        Log.i("fragment", TAG + " ***************onDestroy********************************");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.cb_select:

                selectedAllOrCancelAll(cb_select);

                break;


            case R.id.ll_del_seled:

                delTheSelectItem();

                break;

            case R.id.ll_deliver_order:

                getSelecteDataToSettlement();


                testlog();


                break;

        }
    }


    private void testlog() {


        for (int i = 0; i < shoppingCarAdapter.getmSelectMap().size(); i++) {


            Log.i("selectResult", "当前项：" + i + " 结果：" + shoppingCarAdapter.getmSelectMap().get(i));

        }
    }


    /**
     * 添加产品列表到结算界面
     */

    private void getSelecteDataToSettlement() {


        //wen订单提交新修改
        /**
         *
         * 1.得到被选择的数据
         * 2.生成一个ProductOrder订单
         */


//        List<Integer> productIds = new ArrayList<>();


        List<Product> products = new ArrayList<>();
        for (int i = 0; i < shoppingCarAdapter.getmSelectMap().size(); i++) {

            if (shoppingCarAdapter.getmSelectMap().get(i)) {

                ShoppingCar shoppingCarDel = shoppingCarAdapter.getList().get(i);


                int proId = shoppingCarDel.getProductId();
                Log.i("selectResult", "被选中的项：" + i + " 商品Id ：" + proId);


                List<Product> productList = DataSupport.where("product_id = ?",String.valueOf(proId)).find(Product.class);


                Product product = productList.get(0);

                products.add(product);
            }
        }

        if (products.size() > 0) {

            /**
             * 生成一个订单
             */
            ProductOrder productOrder = getNewProductOrder(products);

            /**
             * 进入结算界面
             */
            goToSettlement(productOrder);


        } else {
            ToastUtil.showResMsg(mActivity, R.string.please_select);
        }


    }


    /**
     * 界面跳转到SettleMentActitvy
     */
    private void goToSettlement(ProductOrder productOrder) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstIntent.BundleKEY.PRODUCT_ORDER_TO_SETTLEMENT, productOrder);
        Intent intent = new Intent(mActivity, SettlementActivity.class);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    /**
     * 生成一个productOrder
     *  //wen订单提交新修改
     * @return
     */
    private ProductOrder getNewProductOrder(List<Product> products) {

        User user = DataSupport.findFirst(User.class);
        int user_id = user.getUser_id();
        int top_id = user.getTopId();

        Map<Integer, Integer> mapBuyCount = new HashMap<>();
        for (int i = 0; i < products.size(); i++) {

            Product product = products.get(i);
            int prduct_id = product.getProduct_id();
            TextView tv = (TextView) plv_orders.getChildAt(i).findViewById(R.id.tv_product_count);
            Log.i("carBuyCount", "准备进入结算界面当前索引：" + i + "被选中的数量：" + tv.getText().toString());
            mapBuyCount.put(prduct_id, Integer.valueOf(tv.getText().toString()));
            Log.i("shopcarProId", "购物车中传递过去的product_id：" + prduct_id);
        }


        ProductOrder productOrder = new ProductOrder();
        productOrder.setProducts_transmit(products); //设置产品ids
        productOrder.setUser_id(user_id);
        productOrder.setTop_id(top_id);
        productOrder.setMap(mapBuyCount); //这里是关键


        return productOrder;

    }




    /**
     * 删除购物车中的选中项
     */
    private void delTheSelectItem() {

        /**
         * 1.先找到选中项
         * 2.执行删除
         */


        List<Integer> productIds = new ArrayList<>();
        for (int i = 0; i < shoppingCarAdapter.getmSelectMap().size(); i++) {

            if (shoppingCarAdapter.getmSelectMap().get(i)) {

                ShoppingCar shoppingCarDel = shoppingCarAdapter.getList().get(i);
                Log.i("selectResult", "被选中的项：" + i + " 商品Id ：" + shoppingCarDel.getProductId());

                productIds.add(shoppingCarDel.getProductId());

                /**
                 * 用于测试数据
                 */
                TextView tv = (TextView) plv_orders.getChildAt(i).findViewById(R.id.tv_product_count);
                Log.i("carBuyCount", "当前索引：" + i + "被选中的数量：" + tv.getText().toString());
            }
        }

        if (productIds.size() > 0) {


            for (int i = 0; i < productIds.size(); i++) {

                int productId = productIds.get(i);

                DataSupport.deleteAll(ShoppingCar.class, "productId = ?", String.valueOf(productId));

            }


            if (shoppingCarAdapter.getmSelectMap().size() == productIds.size()) {

                isSelectedAll = false;

            }


            getDataFromDb();

        } else {
            ToastUtil.showResMsg(mActivity, R.string.please_select);
        }
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {


        isSelectedAll = false;

        getDataFromDb();


        refresh_Layout_in_shopping_car.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        Log.i("FragShoppingcar", " onHiddenChanged调用了" + hidden);

        if (!hidden) {

            isSelectedAll = false;

            getDataFromDb();

//            getDataFromDb();  //以前
//
//            isSelectedAll = false;


        }
    }

    /**
     * 订单提交发过来的广播注册
     * 用于执行购物车中 商品id与订单中的商品id相同时，的数据更新
     */
    private void registerDeliverOrderSuccessBroadCast() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstIntent.IntentAction.ORDER_SUCCESS_UPDAT_SHOPCAR);

        orderSuccessUpdateShopcarReceiver = new OrderSuccessUpdateShopcarReceiver();

        mActivity.registerReceiver(orderSuccessUpdateShopcarReceiver, filter);

    }


    /**
     * 订单提交成功的广播
     */
    private class OrderSuccessUpdateShopcarReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConstIntent.IntentAction.ORDER_SUCCESS_UPDAT_SHOPCAR.equals(action)) {


                isSelectedAll = false;
                getDataFromDb();


            }
        }
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

                                   ToastUtil.showMsg(mActivity.getApplicationContext(), jsonObject.toString());
                                   refresh_Layout_in_shopping_car.refreshFinish(PullToRefreshLayout.SUCCEED);
                               }

                               @Override
                               public void onNotSuccess(int resultCode, String resultMessage) {
                                   ToastUtil.showMsg(mActivity.getApplicationContext(), resultCode + " " + resultMessage);
                                   refresh_Layout_in_shopping_car.refreshFinish(PullToRefreshLayout.FAIL);

                               }

                               @Override
                               public void _onError(String error) {

                                   ToastUtil.showMsg(mActivity.getApplicationContext(), error);
                                   refresh_Layout_in_shopping_car.refreshFinish(PullToRefreshLayout.FAIL);
                               }
                           }
                );


    }
}
