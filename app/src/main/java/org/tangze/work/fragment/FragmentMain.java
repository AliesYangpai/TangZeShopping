package org.tangze.work.fragment;


import android.content.Intent;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.JsonArray;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.activity.ShowInfoListActivity;
import org.tangze.work.activity.MyCollectionActivity;
import org.tangze.work.activity.ProductCriteriaActivity;
import org.tangze.work.activity.SearchActivity;
import org.tangze.work.adapter.BannerAdapter;
import org.tangze.work.adapter.MainRecommandPackageAdapter;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.User;
import org.tangze.work.entity.temp.ProductPackage;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;

import org.tangze.work.utils.ImgUtil;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.AutoScrollViewPager;
import org.tangze.work.widget.PageIndexView;
import org.tangze.work.widget.pullfresh.PullToRefreshLayout;
import org.tangze.work.widget.pullfresh.PullableListViewDown;
import org.tangze.work.widget.pullfresh.PullableListViewUpAndDown;

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
 * A simple {@link Fragment} subclass.
 */
public class FragmentMain extends BaseFragment implements View.OnClickListener,
        PullToRefreshLayout.OnRefreshListener, BDLocationListener {

    public static final String TAG = FragmentMain.class.getSimpleName();


    /**
     * titile
     */
    private TextView tv_main_fg_title;
    private ImageView iv_address_icon_fg;
    private ImageView iv_main_fg_search;
    private LinearLayout ll_main_fg_to_search;
    private TextView tv_current_city;


    /**
     * banner广告页
     *
     * @return
     */
    //现在的
    private AutoScrollViewPager vp_auto_banner;
    private BannerAdapter adapter;
    private List<ImageView> images = new ArrayList<>();
    private PageIndexView page;
    private int nPageIndex = 0;



    /**
     * 拼团、秒杀、等4项目
     */
    private ImageView iv_team_buy;
    private ImageView iv_second_kill;
    private ImageView iv_my_collection;
    private ImageView iv_others;


    /**
     * 新品、热卖、精品、特价 4项目
     */
    private RelativeLayout rl_new;
    private RelativeLayout rl_hot;
    private RelativeLayout rl_quality_goods;
    private RelativeLayout rl_special_price;


    /**
     * 首页推荐相关
     *
     */
    /**
     * 下拉刷新布局wen下拉刷新
     */

    private PullToRefreshLayout refresh_Layout_in_mian;
    private PullableListViewUpAndDown lv_mian_recommand;  //首页展示的大listView
    private View viewHeader;            //头文件包含广告页、4项、等


    /**
     * 封装ProductPackage后
     */
    private MainRecommandPackageAdapter mainRecommandPackageAdapter; //封装后的adapter


    /**
     * 数据相关
     *
     * @return
     */

    private User user;
    private List<Product> listFgMain; //用于分页加载的全局变量
    private int loadChangePage = 1;//变量page更新；


    /**
     * 城市定位相关
     *
     * @return
     */
    private LocationClient mLocationClient;


    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void getSendData(Bundle arguments) {

    }

    @Override
    protected void initView() {

        /**
         * title
         */
        tv_main_fg_title = findViewById(R.id.tv_fragment_title);
        iv_address_icon_fg = findViewById(R.id.iv_address_icon_fg);
        iv_main_fg_search = findViewById(R.id.iv_fragment_search);
        ll_main_fg_to_search = findViewById(R.id.ll_fragment_to_search);
        tv_current_city = findViewById(R.id.tv_current_city);


        /**
         * 下拉刷新布局wen下拉刷新
         */
        refresh_Layout_in_mian = findViewById(R.id.refresh_Layout_in_mian);
        viewHeader = LayoutInflater.from(mActivity).inflate(R.layout.mian_list_header, null);


        /**
         * banner
         */
        //现在的
        vp_auto_banner = (AutoScrollViewPager) viewHeader.findViewById(R.id.vp_auto_banner);
        page = (PageIndexView) viewHeader.findViewById(R.id.store_top_indexpage);

        page.seticonWidth(18);
        adapter = new BannerAdapter(images);
        vp_auto_banner.setAdapter(adapter);
        vp_auto_banner.setInterval(3500);

        /**
         * 拼团、秒杀、等4项目
         */
        iv_team_buy = (ImageView) viewHeader.findViewById(R.id.iv_team_buy);
        iv_second_kill = (ImageView) viewHeader.findViewById(R.id.iv_second_kill);
        iv_my_collection = (ImageView) viewHeader.findViewById(R.id.iv_my_collection);
        iv_others = (ImageView) viewHeader.findViewById(R.id.iv_others);


        /**
         * 新品、热卖、精品、特价 4项目
         *
         */
        rl_new = (RelativeLayout) viewHeader.findViewById(R.id.rl_new);
        rl_hot = (RelativeLayout) viewHeader.findViewById(R.id.rl_hot);
        rl_quality_goods = (RelativeLayout) viewHeader.findViewById(R.id.rl_quality_goods);
        rl_special_price = (RelativeLayout) viewHeader.findViewById(R.id.rl_special_price);


        /**
         * 首页推荐相关
         *
         */


        lv_mian_recommand = findViewById(R.id.lv_mian_recommand);
        mainRecommandPackageAdapter = new MainRecommandPackageAdapter(mActivity);
        lv_mian_recommand.addHeaderView(viewHeader);
        lv_mian_recommand.setAdapter(mainRecommandPackageAdapter);


    }

    @Override
    protected void initListener() {
//        iv_main_fg_search.setOnClickListener(this);

        ll_main_fg_to_search.setOnClickListener(this);
        /**
         * 下拉刷新布局wen下拉刷新
         */
        refresh_Layout_in_mian.setOnRefreshListener(this);
        /**
         * banner
         */
        vp_auto_banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                page.setCurrentPage(nPageIndex = arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


        /**
         * 拼团、秒杀、等4项目
         */
        iv_team_buy.setOnClickListener(this);
        iv_second_kill.setOnClickListener(this);
        iv_my_collection.setOnClickListener(this);
        iv_others.setOnClickListener(this);


        /**
         * 新品、热卖、精品、特价 4项目
         *
         */
        rl_new.setOnClickListener(this);
        rl_hot.setOnClickListener(this);
        rl_quality_goods.setOnClickListener(this);
        rl_special_price.setOnClickListener(this);


    }

    @Override
    protected void onLazyLoad() {


//        LogWriter.writeLog("xxxx","asdasdasdasdasdd");



        tv_main_fg_title.setText(mActivity.getResources().getString(R.string.main));
//        ll_main_fg_to_search.setVisibility(View.GONE); 优化前
        iv_address_icon_fg.setVisibility(View.VISIBLE);
        iv_main_fg_search.setVisibility(View.GONE);

        /**
         * 进行banner图片模拟请求
         */



        initData();

    }


    /**
     * 进入到我的收藏界面
     */
    private void goToMyCollection() {
        Intent intent = new Intent(mActivity, MyCollectionActivity.class);
        startActivity(intent);

    }


    /**
     * 定位获取当前城市
     */
    private void getCurrentCity() {

        mLocationClient = new LocationClient(mActivity);
        mLocationClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //打开gps
        option.setAddrType("all");
        option.setPriority(LocationClientOption.NetWorkFirst);
        option.setPriority(LocationClientOption.GpsFirst);       //gps
//        option.setScanSpan(500); //当值<1000时，为app主动定位，之后就可以在下拉刷新中调用，option.setScanSpan(500);
        option.disableCache(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
//        mLocationClient.requestLocation();
    }


    /**
     * 下拉刷新时候使用定位城市
     */
    private void updatCurrentCity() {

        if (null != mLocationClient) {

            mLocationClient.requestLocation();


        } else {

            getCurrentCity();
        }

    }


    /**
     * 加载数据
     */
    private void initData() {

        getCurrentCity();


        user = DataSupport.findFirst(User.class);

        /**
         * 广告页
         */

        getBannerFromServer();

        /**
         * 产品中心
         * (这里获取产品中心的数据，接口不变，支持分页)
         */
        getRecommandProductFromServer();

        /**
         * 获取上级用户信息
         */
        getTopQQFromServer();


        /**
         * 获取用户收货地址
         */

        getMyAddressListFromServer();
    }



    private void getMyAddressListFromServer(){


       if(null == user){

           return;

       }

        int userId = user.getUser_id();

        Map map = ParaUtils.getUserIdToGetAddressList(userId);

        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.USER_ADDRESS_LIST, map, new HttpResultSubscriber<JsonArray>(mActivity) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取地址返回成功FragmentMain中==" + jsonArray.toString());

                boolean hasAddress = HttpReturnParse.getInstance().parseAddressBack(jsonArray);



            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                ToastUtil.showMsg(mActivity.getApplicationContext(), resultCode + " " + resultMessage);
                Log.i(HttpConst.SERVER_BACK, "===获取地址返回失败FragmentMain中==" + resultCode + " " + resultMessage);


            }

            @Override
            public void _onError(String error) {

                ToastUtil.showMsg(mActivity.getApplicationContext(), error);
                Log.i(HttpConst.SERVER_BACK, "===获取地址返回失败FragmentMain中==" + error);

            }
        });

    }




    private void startAutoBannerRunde(List<String> list) {


        if (null != list && list.size() > 0) {

            if(null != images && images.size() > 0) {

                images.clear();

            }

            for (int i = 0; i < list.size(); i++) {

                String url = list.get(i);

                images.add(getView(url));


            }


            page.setTotalPage(list.size());
            page.setCurrentPage(0);
            adapter.notifyDataSetChanged();
            vp_auto_banner.startAutoScroll();

        }


    }


    /**
     * 广告页停止转动
     */
    private void stopAutoBannerRuning() {

        if (null != vp_auto_banner) {

            vp_auto_banner.stopAutoScroll();

        }
    }

    /**
     * 广告页开始转动
     */

    private void goOnAutoBannerRuning() {

        if (null != vp_auto_banner) {

            vp_auto_banner.startAutoScroll();

        }
    }



    /**
     * 下拉刷新是重新启动广告页
     * @param list
     */



    /**
     * 获取广告页
     */
    private void getBannerFromServer() {

        HttpClient.getInstance().method_PostWithOutParams_Dialog(HttpConst.URL.BANNER_LIST, new HttpResultSubscriber<JsonArray>(mActivity) {
            @Override
            public void onSuccess(JsonArray jsonArray) {

                Log.i(HttpConst.SERVER_BACK, "===获取首页Banner返回成功===" + jsonArray.toString());

                List<String> bannerUrls = HttpReturnParse.getInstance().parseBanner(jsonArray);

                startAutoBannerRunde(bannerUrls);

                bannerUrls = null; //tonull;

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                ToastUtil.showMsg(mActivity.getApplicationContext(), resultCode + resultMessage);


                Log.i(HttpConst.SERVER_BACK, "===获取首页Banner返回失败==" + resultCode + " " + resultMessage);
            }

            @Override
            public void _onError(String error) {


                ToastUtil.showMsg(mActivity.getApplicationContext(), error);


                Log.i(HttpConst.SERVER_BACK, "===获取首页Banner返回失败===" + error);

            }
        });


    }


    /**
     * 首页推荐
     */

    private void getRecommandProductFromServer() {

        if (null == user) {
            return;
        }


        int userId = user.getUser_id();



        Map map = ParaUtils.getPageProduct(userId,loadChangePage); //分页加载需要改进

        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ALL_PRODUCT, map, new HttpResultSubscriber<JsonArray>(mActivity) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                //wen订单提交新修改
                Log.i(HttpConst.SERVER_BACK, "===获取产品中心返回成功===" + jsonArray.toString());

                listFgMain = HttpReturnParse.getInstance().parseRecommandBackSave(jsonArray);

                setRecommandProduct_PackagedToUI(listFgMain);

                loadChangePage ++;

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                ToastUtil.showMsg(mActivity.getApplicationContext(), resultCode + resultMessage);


                Log.i(HttpConst.SERVER_BACK, "===获取产品中心返回失败==" + resultCode + " " + resultMessage);
            }

            @Override
            public void _onError(String error) {


                ToastUtil.showMsg(mActivity.getApplicationContext(), error);


                Log.i(HttpConst.SERVER_BACK, "===获取产品中心返回失败===" + error);

            }
        });
    }


    /**
     * 获取所有订单
     */
    private void getOrderDataFromServer() {

        int user_id = user.getUser_id();
        Map map = ParaUtils.getAllOrderInfo(user_id, 0);
        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ALL_ORDERS, map, new HttpResultSubscriber<JsonArray>(mActivity) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回成功FragmentMan中==" + jsonArray.toString());

                HttpReturnParse.getInstance().parseAllOrdersInMainAndSave(jsonArray);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                if (resultCode != 5002) {

                    ToastUtil.showMsg(mActivity.getApplicationContext(), resultCode + " " + resultMessage);
                }

                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败FragmentMan中==" + resultCode + " " + resultMessage);


            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(mActivity.getApplicationContext(), error);
                Log.i(HttpConst.SERVER_BACK, "===获取所有订单返回失败FragmentMan中==" + error);
            }
        });

    }


    /**
     * 获取上级qq
     */
    private void getTopQQFromServer() {

        if (null != user) {

            int userId = user.getUser_id();
            Map map = ParaUtils.getTopInfo(userId);
            HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.TOP_USER_INFO, map, new HttpResultSubscriber<JsonArray>(mActivity) {
                @Override
                public void onSuccess(JsonArray jsonArray) {

                    Log.i(HttpConst.SERVER_BACK, "===获取上级用户信息返回成功===" + jsonArray.toString());

                    boolean result = HttpReturnParse.getInstance().parseTopUserBackAndSave(jsonArray);

                    if (!result) {

                        ToastUtil.showMsg(mActivity.getApplicationContext(), "没有返回上级用户相关信息");

                    }


                }

                @Override
                public void onNotSuccess(int resultCode, String resultMessage) {
                    Log.i(HttpConst.SERVER_BACK, "===获取上级用户信息返回返回失败==" + resultCode + " " + resultMessage);
                }

                @Override
                public void _onError(String error) {
                    Log.i(HttpConst.SERVER_BACK, "===获取上级用户信息返回失败===" + error);

                }
            });

        }


    }








    /**
     * 下拉刷新加载数据
     * 1.刷新banner
     * 2.刷新首页推荐
     */
    private void getDataFromServerByFresh(final int type) {


        HttpClient.getInstance().method_PostWithOutParams_Dialog(HttpConst.URL.BANNER_LIST, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {

                Log.i(HttpConst.SERVER_BACK, "===刷新获取首页Banner返回成功===");

                List<String> bannerUrls = HttpReturnParse.getInstance().parseBanner(jsonArray);


                startAutoBannerRunde(bannerUrls);


                bannerUrls = null;//tonull

//                getRecommandProductByFresh();



                /**
                 * 下拉刷新加载更多数据，并非差量更新
                 */
                onFreshLoadMoreMainByPage(type);
            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                ToastUtil.showMsg(mActivity.getApplicationContext(), resultCode + resultMessage);

                Log.i(HttpConst.SERVER_BACK, "===刷新获取首页Banner返回失败==" + resultCode + " " + resultMessage);

                refresh_Layout_in_mian.refreshFinish(PullToRefreshLayout.FAIL);
            }

            @Override
            public void _onError(String error) {


                ToastUtil.showMsg(mActivity.getApplicationContext(), error);

                refresh_Layout_in_mian.refreshFinish(PullToRefreshLayout.FAIL);

                Log.i(HttpConst.SERVER_BACK, "===刷新获取首页Banner返回失败===" + error);

            }
        });

    }


    private void getRecommandProductByFresh() {


        int userId = user.getUser_id();

        Map map = ParaUtils.getPageProduct(userId,loadChangePage);

        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ALL_PRODUCT, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                //wen订单提交新修改
                Log.i(HttpConst.SERVER_BACK, "===刷新获取首页推荐返回成功===" + jsonArray.toString());

                List<Product> list = HttpReturnParse.getInstance().parseRecommandBackSave(jsonArray);


                setRecommandProduct_PackagedToUI(list);

                list = null;//tonull

                refresh_Layout_in_mian.refreshFinish(PullToRefreshLayout.SUCCEED);

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                ToastUtil.showMsg(mActivity.getApplicationContext(), resultCode + resultMessage);


                Log.i(HttpConst.SERVER_BACK, "===刷新获取产品中心返回失败==" + resultCode + " " + resultMessage);

                refresh_Layout_in_mian.refreshFinish(PullToRefreshLayout.FAIL);
            }

            @Override
            public void _onError(String error) {


                ToastUtil.showMsg(mActivity.getApplicationContext(), error);


                Log.i(HttpConst.SERVER_BACK, "===刷新获取产品中心返回失败===" + error);


                refresh_Layout_in_mian.refreshFinish(PullToRefreshLayout.FAIL);

            }
        });
    }

    /**
     * 进行拆分封装
     * //wen订单提交新修改
     */

    private void setRecommandProduct_PackagedToUI(List<Product> products) {


        List<ProductPackage> productPackages = getPackageProductListBylistAll(products);

        mainRecommandPackageAdapter.setProductPackages(productPackages);

        productPackages = null;//tonull

    }


    /**
     * 得到将list拆分后，封装到ProductPaekge中
     */
    private List<ProductPackage> getPackageProductListBylistAll(List<Product> products) {

        List<ProductPackage> productPackages = new ArrayList<>();

        if (null != products && products.size() > 0) {

            List<List<Product>> subLists = getSubList(products, 3);


            for (List<Product> listTemp : subLists) {

                ProductPackage productPackage = new ProductPackage();

                productPackage.setProductList(listTemp);
                productPackages.add(productPackage);

            }

        }


        return productPackages;
    }


    /**
     * 将一个List按照固定的大小拆成很多个小的List
     *
     * @param listObj  需要拆分的List
     * @param groupNum 每个List的最大长度
     * @return 拆分后的List的集合
     */
    private <T> List<List<T>> getSubList(List<T> listObj, int groupNum) {
        List<List<T>> resultList = new ArrayList<List<T>>();
        // 获取需要拆分的List个数
        int loopCount = (listObj.size() % groupNum == 0) ? (listObj.size() / groupNum)
                : ((listObj.size() / groupNum) + 1);
        // 开始拆分
        for (int i = 0; i < loopCount; i++) {
            // 子List的起始值
            int startNum = i * groupNum;
            // 子List的终止值
            int endNum = (i + 1) * groupNum;
            // 不能整除的时候最后一个List的终止值为原始List的最后一个
            if (i == loopCount - 1) {
                endNum = listObj.size();
            }
            // 拆分List
            List<T> listObjSub = listObj.subList(startNum, endNum);
            // 保存差分后的List
            resultList.add(listObjSub);
        }
        return resultList;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_fragment_search:

                Toast.makeText(mActivity, "跳转到查询界面", Toast.LENGTH_SHORT).show();

                break;

            case R.id.ll_fragment_to_search:

                goSearchPage();

                break;

            case R.id.iv_team_buy:

                ToastUtil.showMsg(mActivity.getApplicationContext(), "团购期待");
                break;

            case R.id.iv_second_kill:
                ToastUtil.showMsg(mActivity.getApplicationContext(), "秒杀期待");
                break;
            case R.id.iv_my_collection:


                goToMyCollection();
                break;
            case R.id.iv_others:


                goToInfoShowPage();

                break;

            case R.id.rl_new:

                gotToCriteriaPageByTag(ConstIntent.BundleValue.NEW_ARRIVAL_PRODUCT);
                break;

            case R.id.rl_hot:

                gotToCriteriaPageByTag(ConstIntent.BundleValue.HOT_PRODUCT);
                break;

            case R.id.rl_quality_goods:
                gotToCriteriaPageByTag(ConstIntent.BundleValue.BOUTIQUE_ARRIVAL);
                break;

            case R.id.rl_special_price:
                gotToCriteriaPageByTag(ConstIntent.BundleValue.MAIN_RECOMMAND);
                break;

        }
    }


    /**
     * 进入到信息展示界面
     */

    private void goToInfoShowPage() {

        Intent intent = new Intent(mActivity, ShowInfoListActivity.class);
        startActivity(intent);

    }


    private void goSearchPage() {

        Intent intent = new Intent(mActivity, SearchActivity.class);
        startActivity(intent);

    }


    /**
     * 从首页中_四项目进入到界面
     *
     * @param tag
     */
    private void gotToCriteriaPageByTag(int tag) {

        Intent intent = new Intent(mActivity, ProductCriteriaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ConstIntent.BundleKEY.PRODUCT_TO_CRITERIA_TYPE, tag);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fragment", TAG + " onCreate==============================");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAutoBannerRuning();
//        stopBaiduMap();//百度地图停止
        Log.i("fragment", TAG + " ***************onDestroy********************************");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);



        if(hidden) {

            Log.i("fragment", TAG + " ***************onHiddenChanged********************************"+hidden+"停止banner转动");


            stopAutoBannerRuning();

        }else {

            Log.i("fragment", TAG + " ***************onHiddenChanged********************************"+hidden+"继续banner转动");

            goOnAutoBannerRuning();

        }

    }


    //    private void stopBaiduMap() {
//
//        if (null != mLocationClient) {
//
//            mLocationClient.stop();
//            mLocationClient.unRegisterLocationListener(this);
//            mLocationClient = null;
//
//        }
//
//    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//        TestStartRequestSingle();


        /**
         * 刷新城市
         */

        updatCurrentCity();


        /**
         * 1.加载广告图
         * 2.加载首页显示数据
         */
        getDataFromServerByFresh(ConstBase.ON_FRESH);



    }


    /**
     * 该方法待定，目前仅仅使用下拉刷新方法刷新增加数据，而并非差量更新
     * @param pullToRefreshLayout
     */
    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {


        // 加载操作
//
//        testLoadMore();


//        onLoadMoreMainByPage(loadChangePage);




        onFreshLoadMoreMainByPage(ConstBase.ON_LOADMORE);
    }


    /**
     * 下拉刷新或者上拉加载
     */

    /**
     * 下拉刷新或者上啦加载
     * @param type
     */

    private void onFreshLoadMoreMainByPage(final int type) {


        if (null == user) {
            return;
        }


        int userId = user.getUser_id();



        Map map = ParaUtils.getPageProduct(userId,loadChangePage); //分页加载需要改进

        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ALL_PRODUCT, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                //wen订单提交新修改
                Log.i(HttpConst.SERVER_BACK, "===获取首页下拉刷新分页加载返回成功===" + jsonArray.toString());

               List<Product> listLoadMore = HttpReturnParse.getInstance().parseRecommandBackSave(jsonArray);



                setNewLoadDataToUI(listLoadMore);

                loadChangePage ++;


                switch (type) {

                    case ConstBase.ON_FRESH:


                        refresh_Layout_in_mian.refreshFinish(PullToRefreshLayout.SUCCEED);

                        break;

                    case ConstBase.ON_LOADMORE:

                        refresh_Layout_in_mian.loadmoreFinish(PullToRefreshLayout.SUCCEED);


                        break;

                }



            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                ToastUtil.showMsg(mActivity.getApplicationContext(), resultCode + resultMessage);


                Log.i(HttpConst.SERVER_BACK, "===获取首页下拉刷新分页加载荐返回失败==" + resultCode + " " + resultMessage);



                switch (type) {

                    case ConstBase.ON_FRESH:


                        refresh_Layout_in_mian.refreshFinish(PullToRefreshLayout.FAIL);

                        break;

                    case ConstBase.ON_LOADMORE:

                        refresh_Layout_in_mian.loadmoreFinish(PullToRefreshLayout.FAIL);


                        break;

                }


            }

            @Override
            public void _onError(String error) {


                ToastUtil.showMsg(mActivity.getApplicationContext(), error);


                Log.i(HttpConst.SERVER_BACK, "===获取首页下拉刷新分页加载返回失败===" + error);

                switch (type) {

                    case ConstBase.ON_FRESH:


                        refresh_Layout_in_mian.refreshFinish(PullToRefreshLayout.FAIL);

                        break;

                    case ConstBase.ON_LOADMORE:

                        refresh_Layout_in_mian.loadmoreFinish(PullToRefreshLayout.FAIL);


                        break;

                }
            }
        });


    }

    /**
     * 设置下拉新加载的数据到界面上
     * @param productnewList
     */
    private void setNewLoadDataToUI(List<Product> productnewList) {

        if(null!= productnewList && productnewList.size()>0) {

            for (int i = 0 ; i < productnewList.size();i++) {


                listFgMain.add(productnewList.get(i));

            }

        }

        setRecommandProduct_PackagedToUI(listFgMain);

    }


    /**
     * test上拉加载
     */
    private void testLoadMore(){
        new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                // 千万别忘了告诉控件加载完毕了哦！




                refresh_Layout_in_mian.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 3000);

    }


    /**
     * 百度地图的回调函数
     *
     * @param bdLocation
     */

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {


//        Log.i("city", "返回码：" + bdLocation.getLocType() + " 当前城市：" + bdLocation.getCity());
//
//
//        tv_current_city.setText(bdLocation.getCity().replace(mActivity.getString(R.string.SHI), ConstBase.STRING_COLON));


        Log.i("city", "返回码：" + bdLocation.getLocType() + " 当前城市：" + bdLocation.getCity());


        tv_current_city.setText(bdLocation.getCity().replace(mActivity.getString(R.string.SHI), ConstBase.STRING_COLON));

        mLocationClient.unRegisterLocationListener(this);
        mLocationClient.stop();
        mLocationClient = null;


    }




//    public ImageView getView(final Banner banner) {  //需要修改
//        ImageView imageView = new ImageView(mActivity);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        imageView.setLayoutParams(new ViewPager.LayoutParams());
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.showMsg(mActivity,"点击图片");
//
//            }
//        });
//        return imageView;
//    }


    public ImageView getView(String picUrl) {
        ImageView imageView = new ImageView(mActivity);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewPager.LayoutParams());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(mActivity.getApplicationContext(), "点击图片");

            }
        });

        ImgUtil.getInstance().getImgFromNetByUrl(picUrl, imageView, R.drawable.img_hold_seat_banner);

        return imageView;
    }


//    public ImageView getView(String picId) {
//        ImageView imageView = new ImageView(mActivity);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        imageView.setImageResource(picId);
//        imageView.setLayoutParams(new ViewPager.LayoutParams());
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.showMsg(mActivity, "点击图片");
//
//            }
//        });
//
//        return imageView;
//    }



}
