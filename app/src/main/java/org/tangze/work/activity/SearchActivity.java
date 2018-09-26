package org.tangze.work.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.JsonArray;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.adapter.SearchHistoryAdapter;
import org.tangze.work.constant.ConstDb;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.SearchHistory;
import org.tangze.work.entity.User;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends BaseActivity implements OnClickListener,TextWatcher{


    /**
     * title
     */

    private ImageView iv_search_back;
    private EditText et_common_search;
    private ImageView iv_common_search;
    private ListView lv_search_reminder; //搜索提示
    private SearchHistoryAdapter searchReminderAdapter; //搜索提示所在的adapter


    /**
     * 下部的搜索历史
     */
    private LinearLayout ll_all_history_layout; //最大的搜索历史布局
    private LinearLayout ll_clear_all_history; //清空搜索历史
    private ListView lv_search_history;//历史记录
    private SearchHistoryAdapter searchHistoryAdapter;


    /**
     * 数据相关
     */
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData();

    }

    @Override
    protected void initViews() {
        /**
         * title
         *
         */

        iv_search_back = (ImageView) findViewById(R.id.iv_common_back);
        et_common_search = (EditText) findViewById(R.id.et_common_search);
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        lv_search_reminder = (ListView) findViewById(R.id.lv_search_reminder); //搜索提示
        searchReminderAdapter = new SearchHistoryAdapter(this); //搜索提示所在的adapter
        ViewStub viewStubReminder = new ViewStub(this);
        lv_search_reminder.addHeaderView(viewStubReminder);
        lv_search_reminder.addFooterView(viewStubReminder);
        lv_search_reminder.setAdapter(searchReminderAdapter);

        /**
         * 下面的搜索历史
         *
         */
        ll_all_history_layout = (LinearLayout) findViewById(R.id.ll_all_history_layout);
        ll_all_history_layout.setVisibility(View.GONE);
        ll_clear_all_history = (LinearLayout) findViewById(R.id.ll_clear_all_history); //清空搜索历史
        lv_search_history = (ListView) findViewById(R.id.lv_search_history);//历史记录
        ViewStub viewStubHistory = new ViewStub(this);
        lv_search_history.addHeaderView(viewStubHistory);
        lv_search_history.addFooterView(viewStubHistory);
        searchHistoryAdapter = new SearchHistoryAdapter(this);
        lv_search_history.setAdapter(searchHistoryAdapter);
    }

    @Override
    protected void initListener() {
        /**
         * title
         */
        iv_search_back.setOnClickListener(this);
        iv_common_search.setOnClickListener(this);
        et_common_search.addTextChangedListener(this);


        /**
         * 清空搜索历史
         */

        ll_clear_all_history.setOnClickListener(this);


        /**
         * 智能提示中点击事件
         */
        lv_search_reminder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                int currentPosition = position-1;

                SearchHistory searchRemind = searchReminderAdapter.getList().get(currentPosition);

                String str = searchRemind.getSearch_text().toString();

                Bundle bundle = new Bundle();

                bundle.putString(ConstIntent.BundleKEY.SERCH_TEXT, str);

                openActivity(SearchResultActivity.class, bundle);

                saveHistoryToDb(str); //将点击项保存到数据库

                Log.i("clickSearch","智能提示当前项："+position+" 名称："+str);
            }
        });

        lv_search_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                int currentPosition = position-1;

                SearchHistory searchRemind = searchHistoryAdapter.getList().get(currentPosition);

                String str = searchRemind.getSearch_text().toString();

                Bundle bundle = new Bundle();

                bundle.putString(ConstIntent.BundleKEY.SERCH_TEXT, str);

                openActivity(SearchResultActivity.class, bundle);

                saveHistoryToDb(str); //将点击项保存到数据库

                Log.i("clickSearch", "历史记录当前项：" + position);
            }
        });


    }





    @Override
    protected void processIntent() {

    }


    private void initData() {




        /**
         *
         * 1.本地查询历史列表
         */

        user = DataSupport.findFirst(User.class);


        getSearchHistoryFromDb();





    }










    private void getSearchHistoryFromDb() {

        List<SearchHistory> list = DataSupport.order("id desc").find(SearchHistory.class);

        if (null != list && list.size() > 0) {

            ll_all_history_layout.setVisibility(View.VISIBLE);
            searchHistoryAdapter.setList(list);

        } else {
            ll_all_history_layout.setVisibility(View.GONE);
            searchHistoryAdapter.setList(list);

        }


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.iv_common_back:
                this.finish();
                break;


            case R.id.iv_common_search:

                goToSearchResultInClickSearch();
                break;


            case R.id.ll_clear_all_history: //清空所有搜索记录history

                clearAllSearchHistory();

                break;
        }
    }


    private void goToSearchResultInClickSearch() {


        String str = et_common_search.getText().toString().trim();

        if (TextUtils.isEmpty(str)) {

            ToastUtil.showResMsg(this, R.string.need_text);

            return;
        }

        Bundle bundle = new Bundle();

        bundle.putString(ConstIntent.BundleKEY.SERCH_TEXT,str);

        openActivity(SearchResultActivity.class, bundle);
        //保存搜索记录到数据库

        saveHistoryToDb(str);

    }


    /**
     * 清空所有历史记录
     */
    private void clearAllSearchHistory() {

        DataSupport.deleteAll(SearchHistory.class);

        getSearchHistoryFromDb();

    }

    /**
     * 保存一条历史记录
     */

    private void saveHistoryToDb(String str) {

        if (TextUtils.isEmpty(str)) {

            ToastUtil.showMsg(getApplicationContext(), "请输入关键字");

        } else {

            /**
             * 1.跳转到别的界面
             * 2.之后本地保存收缩记录
             */


            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setSearch_text(str);
            searchHistory.save();

        }


    }


    /**
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

//        remindShowOrDismiss((String) s);
        Log.i("search", "editeText中onTextChanged======" + s);

    }

    @Override
    public void afterTextChanged(Editable s) {



        String str = s.toString().trim();

        boolean result = remindShowOrDismiss(str);
        Log.i("search","editeText中afterTextChanged======截取后的值："+str+" result："+result);
        if(result) {

            startFuzzySearchFromServer(str);
        }


    }


    /**
     * 智能提示的显示与消失
     */

    private boolean remindShowOrDismiss(String string) {

        boolean result = false;

        if(TextUtils.isEmpty(string)) {

            lv_search_reminder.setVisibility(View.GONE);


        }else {

            lv_search_reminder.setVisibility(View.VISIBLE);

            result = true;
        }

        return result;
    }






    /**
     * 开始模糊查询用于搜索框的智能提示
     * 智能提示修改后
     */
    private void startFuzzySearchFromServer( String str) {


        Log.i("searchFromServer","模糊查询关键字======"+str);

        if (null == user) {
            return;
        }



        int userId = user.getUser_id();

        Map map = ParaUtils.getFuzzySearch(userId, str);


        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ALL_PRODUCT, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {

                Log.i(HttpConst.SERVER_BACK, "===SearchActivity中网络模糊查询product返回成功===" + jsonArray.toString());


                 HttpReturnParse.getInstance().parseSearchFuzzBackSave(jsonArray);

                 setFuzzySearchToUi();
            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                ToastUtil.showMsg(getApplicationContext(), resultCode + resultMessage);


                Log.i(HttpConst.SERVER_BACK, "===SearchActivity中网络模糊查询product返回失败==" + resultCode + " " + resultMessage);
            }

            @Override
            public void _onError(String error) {


                ToastUtil.showMsg(getApplicationContext(), error);


                Log.i(HttpConst.SERVER_BACK, "===SearchActivity中网络模糊查询product返回失败===" + error);

            }
        });





    }


    /**
     *  智能提示修改后
     *  将网络请求返回的数据设置到界面上
     */
    private void setFuzzySearchToUi() {



        List<Product> list = DataSupport
                .limit(ConstDb.SEARCH_LIMITE).find(Product.class);


        if(null != list && list.size() > 0) {


            List<SearchHistory> listRemainds = new ArrayList<>();
            for(int i = 0 ; i < list.size() ; i ++) {


                Product product = list.get(i);

                SearchHistory searchRemind = new SearchHistory();
                searchRemind.setSearch_text(product.getProductName());
                listRemainds.add(searchRemind);
            }

            searchReminderAdapter.setList(listRemainds);

        }else {

            searchReminderAdapter.setList(null);
        }


    }
}
