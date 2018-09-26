package org.tangze.work.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.adapter.MainRecommandAdapter;
import org.tangze.work.adapter.ProductAdapter;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Classify;
import org.tangze.work.entity.ClassifySecond;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.User;
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
 * 产品界面的Activty
 */
public class ProductActivity extends BaseActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener{

    /**
     * title
     *
     */
    private ImageView iv_product_back;
    private TextView tv_product_title;
    private ImageView iv_product_search;


    /**
     * 综合、销量、价格
     *
     */
    private RadioGroup rg_product_property;
    private RadioButton rb_synthetic;
    private RadioButton rb_sales_volume;
    private RadioButton rb_price;




    /**
     * listView相关
     *
     */
    private PullToRefreshLayout refresh_Layout_in_product;
    private PullableListView lv_product;
    private ProductAdapter productAdapter;

    /**
     * 首次进入界面相关配置
     */
//    private boolean isFirstIn = true; //首次进入界面自动刷新

    private Intent intent ;

    private Bundle bundle;

    private ClassifySecond classifySecond;


    private int currentBtnId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        rb_synthetic.setChecked(true);

    }




    @Override
    protected void initViews() {


        /**
         * title
         */
        iv_product_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_product_title = (TextView) findViewById(R.id.tv_common_title);
        iv_product_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_product_search.setVisibility(View.GONE);
        tv_product_title.setText(classifySecond.getClassfiyName());


        /**
         * 综合、销量、价格
         */
          rg_product_property = (RadioGroup) findViewById(R.id.rg_product_property);
          rb_synthetic = (RadioButton) findViewById(R.id.rb_synthetic);
          rb_sales_volume = (RadioButton) findViewById(R.id.rb_sales_volume);
          rb_price = (RadioButton) findViewById(R.id.rb_price);



        /**
         * listView相关
         *
         */
          refresh_Layout_in_product = (PullToRefreshLayout) findViewById(R.id.refresh_Layout_in_product);
          lv_product = (PullableListView) findViewById(R.id.lv_product);
          productAdapter = new ProductAdapter(this);
          lv_product.setAdapter(productAdapter);
    }

    @Override
    protected void initListener() {
        iv_product_back.setOnClickListener(this);
        iv_product_search.setOnClickListener(this);

        refresh_Layout_in_product.setOnRefreshListener(this);

        rg_product_property.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch (checkedId) {


                    case R.id.rb_synthetic:


                        currentBtnId = checkedId;

                        getProductFromServerByColumAndSave(checkedId);
                        break;

                    case R.id.rb_sales_volume:

                        currentBtnId = checkedId;

                        getProductFromServerByColumAndSave(checkedId);
                        break;

                    case R.id.rb_price:

                        currentBtnId = checkedId;

                        getProductFromServerByColumAndSave(checkedId);
                        break;
                }

            }
        });




    }


    /**
     * 从服务器获取数据
     * @param checkeId
     *
     */
    private void getProductFromServerByColumAndSave(final int checkeId) {

      User user =  DataSupport.findFirst(User.class);

        final int userId = user.getUser_id();


        Map map = ParaUtils.getAllProduct(userId);




        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ALL_PRODUCT, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取当前分类下产品成功===" + jsonArray.toString());

                HttpReturnParse.getInstance().parseProductByClassify(jsonArray);

                switch (checkeId) {


                    case R.id.rb_synthetic:


                         List<Product>  allProductsInClassify = DataSupport.where("classify_id = ?",String.valueOf(classifySecond.getClassifySecond_id())).find(Product.class);

                        //测试用
                        productAdapter.setProducts(allProductsInClassify);


                        break;

                    case R.id.rb_sales_volume:


                        List<Product> productListBySale = DataSupport.where("classify_id = ?",String.valueOf(classifySecond.getClassifySecond_id())).order("salesvolume desc").find(Product.class);



                        productAdapter.setProducts(productListBySale);
                        break;

                    case R.id.rb_price:


                        List<Product> productsByPrice = DataSupport.where("classify_id = ?",String.valueOf(classifySecond.getClassifySecond_id())).order("localPrice asc").find(Product.class);




                        productAdapter.setProducts(productsByPrice);

                        break;
                }


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                Log.i(HttpConst.SERVER_BACK, "===获取当前分类下产品返回失败===" + resultCode + resultMessage);




            }

            @Override
            public void _onError(String error) {
                Log.i(HttpConst.SERVER_BACK, "===获取当前分类下产品返回失败" + error);

            }
        });


    }


    /**
     * 下拉刷新获取数据
     * @param checkeId
     *
     */
    private void getProductFromServerByColumAndSaveByFresh(final int checkeId) {

        User user =  DataSupport.findFirst(User.class);

        final int userId = user.getUser_id();


        Map map = ParaUtils.getAllProduct(userId);


        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ALL_PRODUCT, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取当前分类下产品成功===");

                HttpReturnParse.getInstance().parseProductByClassify(jsonArray);

                switch (checkeId) {


                    case R.id.rb_synthetic:


                         List<Product>  allProductsInClassify = DataSupport.where("classify_id = ?",String.valueOf(classifySecond.getClassifySecond_id())).find(Product.class);

                        //测试用
                        productAdapter.setProducts(allProductsInClassify);


                        break;

                    case R.id.rb_sales_volume:



                        List<Product> productListBySale = DataSupport.where("classify_id = ?",String.valueOf(classifySecond.getClassifySecond_id())).order("salesvolume desc").find(Product.class);



                        productAdapter.setProducts(productListBySale);
                        break;

                    case R.id.rb_price:


                        List<Product> productsByPrice = DataSupport.where("classify_id = ?",String.valueOf(classifySecond.getClassifySecond_id())).order("localPrice asc").find(Product.class);





                        productAdapter.setProducts(productsByPrice);

                        break;
                }




                    refresh_Layout_in_product.refreshFinish(PullToRefreshLayout.SUCCEED);

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                Log.i(HttpConst.SERVER_BACK, "===获取当前分类下产品返回失败===" + resultCode + resultMessage);





                    refresh_Layout_in_product.refreshFinish(PullToRefreshLayout.FAIL);

            }

            @Override
            public void _onError(String error) {
                Log.i(HttpConst.SERVER_BACK, "===获取当前分类下产品返回失败" + error);


                    refresh_Layout_in_product.refreshFinish(PullToRefreshLayout.FAIL);
            }
        });


    }








    @Override
    protected void processIntent() {

        intent = this.getIntent();

        if(null != intent) {

            bundle = intent.getExtras();

            classifySecond = (ClassifySecond) bundle.getSerializable(ConstIntent.BundleKEY.CLASSIFY_SECONDS_TO_PRODUCT);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_common_back:
                this.finish();
                break;
            case R.id.iv_common_search:
                ToastUtil.showMsg(getApplicationContext(),"进入到查询界面");
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {


        getProductFromServerByColumAndSaveByFresh(currentBtnId);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        /**
         * 此处暂不实现
         */
    }




}
