package org.tangze.work.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
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
import org.tangze.work.widget.pullfresh.PullToRefreshLayout;
import org.tangze.work.widget.pullfresh.PullableListView;
import org.tangze.work.widget.pullfresh.PullableListViewUp;

import java.util.List;
import java.util.Map;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener{
    /**
     * title
     */
    private ImageView iv_search_result_back;
    private TextView tv_search_result_title;
    private ImageView iv_search_result_sure;


    /**
     * 底部下拉刷新
     */
    private PullToRefreshLayout refresh_Layout_earch_result;
    private PullableListViewUp lv_product_search_result;
    private SearchResultProductAdapter searchResultProductAdapter;


    /**
     * 空白显示
     */
    private ImageView iv_empty;


    /**
     * 数据传输
     */

    private Intent intent;

    private Bundle bundle;

    private User user;

    private List<Product> list;

    private int loadChangeCount = 5;//变量page更新；


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initData();
    }

    private void initData() {
        user = DataSupport.findFirst(User.class);


        getProductListByConditon();//智能提示修改后

    }




    /**
     * 根据当前界面格式类型，来获取产品列表，“热卖”、"精品推荐".....
     */
    private void getProductListByConditon() {

        startFuzzySearchInDbForProduct();

    }


    /**
     * 显示或者隐藏空图片
     */
    private void showOrHindEmptyPic(List<Product> list) {

        if (null != list && list.size() > 0) {



            iv_empty.setVisibility(View.GONE);
        } else {



            iv_empty.setVisibility(View.VISIBLE);

        }

    }


    private void startFuzzySearchInDbForProduct() {


        if (null == bundle) {

            return;

        }
        String str = bundle.getString(ConstIntent.BundleKEY.SERCH_TEXT, ConstBase.STRING_COLON);

//        list = DataSupport.
//                where(ConstDb.FUZZY_SEARCH, "%" + str + "%", "%" + str + "%", "%" + str + "%")
//                .limit(ConstDb.SEARCH_LIMITE).find(Product.class);


        list = DataSupport
                .limit(ConstDb.SEARCH_LIMITE).find(Product.class);


        showOrHindEmptyPic(list);

        searchResultProductAdapter.setProducts(list);



    }


    @Override
    protected void initViews() {
        /**
         * title
         *
         */

        iv_search_result_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_search_result_title = (TextView) findViewById(R.id.tv_common_title);
        iv_search_result_sure = (ImageView) findViewById(R.id.iv_common_search);
        iv_search_result_sure.setVisibility(View.GONE);
        tv_search_result_title.setText(getString(R.string.product_list));


        /**
         * listView相关
         *
         */


        refresh_Layout_earch_result = (PullToRefreshLayout) findViewById(R.id.refresh_Layout_earch_result);
        lv_product_search_result = (PullableListViewUp) findViewById(R.id.lv_product_search_result);
        searchResultProductAdapter = new SearchResultProductAdapter(this);
        lv_product_search_result.setAdapter(searchResultProductAdapter);


        /**
         * 空白显示
         */
        iv_empty = (ImageView) findViewById(R.id.iv_empty);
    }

    @Override
    protected void initListener() {
        iv_search_result_back.setOnClickListener(this);

        /**
         * 底部下拉刷新
         */
        refresh_Layout_earch_result.setOnRefreshListener(this);
    }

    @Override
    protected void processIntent() {
        intent = this.getIntent();
        if (null != intent) {

            bundle = this.intent.getExtras();
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

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        /**
         * 暂时不用
         */
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//        testLoadMore();
        startFuzzySearchInDbForProductByLoadMore();
    }


    /**
     * 上拉加载从数据库中加载数据
     */
    private void startFuzzySearchInDbForProductByLoadMore() {


        if (null == bundle) {

            return;

        }
        String str = bundle.getString(ConstIntent.BundleKEY.SERCH_TEXT, ConstBase.STRING_COLON);

//        list = DataSupport.
//                where(ConstDb.FUZZY_SEARCH, "%" + str + "%", "%" + str + "%", "%" + str + "%")
//                .limit(ConstDb.SEARCH_LIMITE).offset(loadChangeCount).find(Product.class);


      List<Product>  listLoadMore = DataSupport
                .limit(ConstDb.SEARCH_LIMITE).offset(loadChangeCount).find(Product.class);





        for(int i = 0;i<listLoadMore.size();i++) {

            list.add(listLoadMore.get(i));

        }

        searchResultProductAdapter.setProducts(list);


        refresh_Layout_earch_result.loadmoreFinish(PullToRefreshLayout.SUCCEED);

        loadChangeCount+=5;
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


    /**
     * 将返回数据设置到界面上，并判断是否进行数据显示或隐藏
     */
    private void setDataToUi() {

        if(hindEmptyPic()) {

            searchResultProductAdapter.setProducts(list);

        }

    }
}
