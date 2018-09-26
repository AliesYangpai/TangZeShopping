package org.tangze.work.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.adapter.MyCollectionAdapter;
import org.tangze.work.callback.SliderToDelCollectionCallBack;
import org.tangze.work.constant.ConstCollection;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.User;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.slider.SilderListView;

import java.util.List;
import java.util.Map;

/**
 * 我的收藏界面
 */
public class MyCollectionActivity extends BaseActivity implements OnClickListener,SliderToDelCollectionCallBack {


    /**
     * titile
     */
    private ImageView iv_myCollection_back;
    private TextView tv_myCollection_title;
    private ImageView iv_common_search;


    /**
     * listView
     */

    private SilderListView lv_collection;
    private MyCollectionAdapter myCollectionAdapter;

    /**
     * 中间空白图片
     */
    private ImageView iv_empty;


    /**
     * 数据相关
     */
    private User user;

    private List<Product> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
//        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {



        user = DataSupport.findFirst(User.class);

        if (null != user) {

            /**
             * 发起一个网路请求，请求获取所有的收藏列表
             */
            getAllCollectionFromServer();

        }


    }


    /**
     * 显示或者隐藏空图片
     */
    private void showOrHindEmptyPic() {

        if( null!=list && list.size() > 0) {

            iv_empty.setVisibility(View.GONE);



        }else {

            iv_empty.setVisibility(View.VISIBLE);

        }

    }









    private void getAllCollectionFromServer() {


         int userId = user.getUser_id();

        Map map = ParaUtils.getMyCollections(userId);
//        Map map = ParaUtils.getMyCollections(3);//测试收藏使用
        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.MY_COLLECTIONS, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {
                Log.i(HttpConst.SERVER_BACK, "===获取我的收藏返回成功==" + jsonArray.toString());

                list =  HttpReturnParse.getInstance().parseMyCollectionBack(jsonArray);

                showOrHindEmptyPic();

                myCollectionAdapter.setProducts(list);

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {


                ToastUtil.showMsg(getApplicationContext(), resultMessage+" "+resultCode);

                Log.i(HttpConst.SERVER_BACK, "===获取我的收藏返回失败==" + resultCode + " " + resultMessage);


            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(getApplicationContext(), error);
                Log.i(HttpConst.SERVER_BACK, "===获取我的收藏返回失败==" + error);
            }
        });

    }


    @Override
    protected void initViews() {

        /**
         * titile
         */
        iv_myCollection_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_myCollection_title = (TextView) findViewById(R.id.tv_common_title);
        tv_myCollection_title.setText(getString(R.string.my_collection));
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_search.setVisibility(View.GONE);
        /**
         * listView
         */
        lv_collection = (SilderListView) findViewById(R.id.lv_collection);
        myCollectionAdapter = new MyCollectionAdapter(this);
        myCollectionAdapter.setSliderToDelCollectionCallBack(this);
        lv_collection.setAdapter(myCollectionAdapter);


        /**
         * 中间数据相关
         */

        iv_empty = (ImageView) findViewById(R.id.iv_empty);
    }

    @Override
    protected void initListener() {


        iv_myCollection_back.setOnClickListener(this);
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
    public void delCollectionCallBack(int productId) {
        startToCancelCollectToServer(productId);
    }


    /**
     * 执行取消收藏
     */


    private void startToCancelCollectToServer(int sid) {


        final int userID = user.getUser_id();
        final int productId = sid;
        Map map = ParaUtils.getAddOrCancelCollect(productId, userID);
        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.CANCEL_COLLECT, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===取消收藏返回成功==" + jsonArray.toString());


                parseBackByCollectCancel(jsonArray, productId, userID);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                ToastUtil.showMsg(getApplicationContext(), resultCode + " " + resultMessage);
                Log.i(HttpConst.SERVER_BACK, "===取消收藏返回失败==" + resultCode + " " + resultMessage);

            }

            @Override
            public void _onError(String error) {

                ToastUtil.showMsg(getApplicationContext(), error);
                Log.i(HttpConst.SERVER_BACK, "===取消收藏返回失败==" + error);

            }
        });
    }



    private void parseBackByCollectCancel(JsonArray jsonArray,int product_id,int userId) {


        boolean updatDbBack = false;


        //如果当前是被收藏状态，则这里执使用取消解析
        updatDbBack = HttpReturnParse.getInstance().parseCancelCollectBack(jsonArray, product_id,userId);




        if(updatDbBack ) {

            //本地更新成功发起网络请求
            getAllCollectionFromServer();
        }


    }



}
