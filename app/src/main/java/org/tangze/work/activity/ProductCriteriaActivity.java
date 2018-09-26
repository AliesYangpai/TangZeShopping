package org.tangze.work.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.adapter.CriteriaAdapter;
import org.tangze.work.adapter.SearchResultProductAdapter;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstDb;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.User;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.pullfresh.PullToRefreshLayout;
import org.tangze.work.widget.pullfresh.PullableGridView;

import java.util.List;
import java.util.Map;

/**
 * 热卖，精品。四项的activity
 */
public class ProductCriteriaActivity extends BaseActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener {

    /**
     * title
     */
    private ImageView iv_criteria_back;
    private TextView tv_criteria_title;
    private ImageView iv_criteria_sure;


    /**
     * 下拉刷新相关
     * //首页四项修改后
     */

    private PullToRefreshLayout refresh_Layout_in_pro_criteria;
    private PullableGridView gv_product_criteria;
    private CriteriaAdapter criteriaAdapter;
    private int loadChangePage = 1;//变量page更新；


    /**
     * 空白显示
     */
    private ImageView iv_empty;


    /**
     * 数据传输
     */

    private Intent intent;

    private Bundle bundle;

    private int currentType;

    private User user;

    private List<Product> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_criteria);
        initData();
    }

    private void initData() {
        user = DataSupport.findFirst(User.class);

        getProductFromServerAndSave();
    }


    /**
     * 从服务器获取数据
     * 首页四项更新：需要加入defaultpage
     */
    private void getProductFromServerAndSave() {


        int userId = user.getUser_id();


        Map map = null;

        switch (currentType) {
            case ConstIntent.BundleValue.NEW_ARRIVAL_PRODUCT:

                map = ParaUtils.getPageProductToMain4(userId, HttpConst.Key.IS_NEW, HttpConst.Value.MAIN_4_VALUE,loadChangePage);
                break;
            case ConstIntent.BundleValue.HOT_PRODUCT:
                map = ParaUtils.getPageProductToMain4(userId, HttpConst.Key.IS_HOT,  HttpConst.Value.MAIN_4_VALUE,loadChangePage);

                break;
            case ConstIntent.BundleValue.BOUTIQUE_ARRIVAL:
                map = ParaUtils.getPageProductToMain4(userId, HttpConst.Key.IS_BOUTIQUE,  HttpConst.Value.MAIN_4_VALUE,loadChangePage);

                break;
            case ConstIntent.BundleValue.MAIN_RECOMMAND:
                map = ParaUtils.getPageProductToMain4(userId, HttpConst.Key.IS_RECOMMAND,  HttpConst.Value.MAIN_4_VALUE,loadChangePage);

                break;

        }


        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ALL_PRODUCT, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取产品成功ProductCriteriaActivity===" + " 当前type " + currentType + " " + jsonArray.toString());

                list = HttpReturnParse.getInstance().parseRecommandBackSave(jsonArray);


                setDataToUi();

                loadChangePage ++;

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                ToastUtil.showMsg(ProductCriteriaActivity.this.getApplicationContext(), resultMessage);
                Log.i(HttpConst.SERVER_BACK, "======获取产品失败ProductCriteriaActivity======" + " 当前type " + currentType + " " + resultCode + resultMessage);
            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(ProductCriteriaActivity.this.getApplicationContext(), error);
                Log.i(HttpConst.SERVER_BACK, "======获取产品失败ProductCriteriaActivity======" + " 当前type " + currentType + " " + error);

            }
        });


    }





    /**
     * 下拉刷新获取数据 分页加载
     * 首页四项更新，下拉刷新
     */


    private void getPageProductFromServerByLoadMore() {


        int userId = user.getUser_id();


        Map map = null;

        switch (currentType) {
            case ConstIntent.BundleValue.NEW_ARRIVAL_PRODUCT:

                map = ParaUtils.getPageProductToMain4(userId, HttpConst.Key.IS_NEW,  HttpConst.Value.MAIN_4_VALUE,loadChangePage);
                break;
            case ConstIntent.BundleValue.HOT_PRODUCT:
                map = ParaUtils.getPageProductToMain4(userId, HttpConst.Key.IS_HOT,  HttpConst.Value.MAIN_4_VALUE,loadChangePage);

                break;
            case ConstIntent.BundleValue.BOUTIQUE_ARRIVAL:
                map = ParaUtils.getPageProductToMain4(userId, HttpConst.Key.IS_BOUTIQUE,  HttpConst.Value.MAIN_4_VALUE,loadChangePage);

                break;
            case ConstIntent.BundleValue.MAIN_RECOMMAND:
                map = ParaUtils.getPageProductToMain4(userId, HttpConst.Key.IS_RECOMMAND,  HttpConst.Value.MAIN_4_VALUE,loadChangePage);

                break;

        }


        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ALL_PRODUCT, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取产品成功ProductCriteriaActivity==="+" 当前type "+currentType+" " + jsonArray.toString());

                List<Product> listAddMore = HttpReturnParse.getInstance().parseRecommandBackSave(jsonArray);

                if(null!= listAddMore && listAddMore.size()>0) {

                    for (int i = 0 ; i < listAddMore.size();i++) {


                        list.add(listAddMore.get(i));

                    }

                }




                setDataToUi();

                loadChangePage ++;

                refresh_Layout_in_pro_criteria.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                refresh_Layout_in_pro_criteria.loadmoreFinish(PullToRefreshLayout.FAIL);


                ToastUtil.showMsg(ProductCriteriaActivity.this.getApplicationContext(),resultMessage);
                Log.i(HttpConst.SERVER_BACK, "======获取产品失败ProductCriteriaActivity======" +" 当前type "+currentType+" "+ resultCode + resultMessage);


            }

            @Override
            public void _onError(String error) {
                Log.i(HttpConst.SERVER_BACK, "======获取产品失败ProductCriteriaActivity======" +" 当前type "+currentType+" "+ error);

                ToastUtil.showMsg(ProductCriteriaActivity.this.getApplicationContext(), error);

                refresh_Layout_in_pro_criteria.loadmoreFinish(PullToRefreshLayout.FAIL);

            }
        });






    }


    /**
     * 将返回数据设置到界面上，并判断是否进行数据显示或隐藏
     */
    private void setDataToUi() {

        if(hindEmptyPic()) {

            criteriaAdapter.setProducts(list);

        }

    }


    /**
     * 显示或者隐藏空图片
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


    @Override
    protected void initViews() {
        /**
         * title
         *
         */
        iv_criteria_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_criteria_title = (TextView) findViewById(R.id.tv_common_title);
        iv_criteria_sure = (ImageView) findViewById(R.id.iv_common_search);
        iv_criteria_sure.setVisibility(View.GONE);

        switch (currentType) {
            case ConstIntent.BundleValue.NEW_ARRIVAL_PRODUCT:
                tv_criteria_title.setText(getString(R.string.new_arrival));
                break;
            case ConstIntent.BundleValue.HOT_PRODUCT:
                tv_criteria_title.setText(getString(R.string.hot_product));
                break;
            case ConstIntent.BundleValue.BOUTIQUE_ARRIVAL:
                tv_criteria_title.setText(getString(R.string.boutique));
                break;
            case ConstIntent.BundleValue.MAIN_RECOMMAND:
                tv_criteria_title.setText(getString(R.string.main_recommand));
                break;
        }



        /**
         * 下拉刷新相关
         * //首页四项修改后
         */
        refresh_Layout_in_pro_criteria = (PullToRefreshLayout) findViewById(R.id.refresh_Layout_in_pro_criteria);
        gv_product_criteria = (PullableGridView) findViewById(R.id.gv_product_criteria);
        criteriaAdapter = new CriteriaAdapter(this);
        gv_product_criteria.setAdapter(criteriaAdapter);

        /**
         * 空白显示
         */
        iv_empty = (ImageView) findViewById(R.id.iv_empty);
    }

    @Override
    protected void initListener() {
        iv_criteria_back.setOnClickListener(this);


        //首页四项修改后
        refresh_Layout_in_pro_criteria.setOnRefreshListener(this);
    }

    @Override
    protected void processIntent() {
        intent = this.getIntent();
        if (null != intent) {

            bundle = this.intent.getExtras();


            if (null != bundle) {
                this.currentType = bundle.getInt(ConstIntent.BundleKEY.PRODUCT_TO_CRITERIA_TYPE);
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



    ////首页四项修改后
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {


//        getPageProductFromServerByFresh();


//        new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg)
//            {
//                // 千万别忘了告诉控件加载完毕了哦！
//
//
//
//
//                refresh_Layout_in_pro_criteria.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//            }
//        }.sendEmptyMessageDelayed(0, 3000);

    }
    ////首页四项修改后
    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        getPageProductFromServerByLoadMore();
    }
}
