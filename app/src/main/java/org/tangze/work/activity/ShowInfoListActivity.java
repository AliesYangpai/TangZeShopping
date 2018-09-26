package org.tangze.work.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;

import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.Test.TestCons;
import org.tangze.work.adapter.ShowInfoListAdapter;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.ShowInfo;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.StringUtils;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.webView.WebClientPicDescrib;

import java.util.ArrayList;
import java.util.List;


/**
 * 用于展示新闻消息
 */

public class ShowInfoListActivity extends BaseActivity implements OnClickListener {


    /**
     * title
     */

    private ImageView iv_common_back;
    private TextView tv_common_title;
    private ImageView iv_common_search;


    private ListView lv_show_info_list;
    private ShowInfoListAdapter showInfoListAdapter;


    /**
     * webView
     */
    private WebView wv_infor_show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_list);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_common_title.setText(this.getString(R.string.new_info));

        /**
         * 从Server获取信息发布列表
         */
        getShowInfoFromServer();













        /**
         * webView
         */
        String html = TestCons.TEXT_INFO_URL;
        if (TextUtils.isEmpty(html)) {
            html = "";
        } else {
            html = StringUtils.replaceImgStyle(html);
        }
        html = ConstBase.WEB_IMG_STYLE + html;
        wv_infor_show.loadDataWithBaseURL(null, html, ConstBase.MIME_TYPE, ConstBase.CODING_TYPE, null);

    }


    /**
     * 从Server获取新闻展示列表
     */

    private void getShowInfoFromServer() {


        HttpClient.getInstance().method_PostWithOutParams_Dialog(HttpConst.URL.NEWS_LIST, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {
                Log.i(HttpConst.SERVER_BACK, "===获取信息展示列表返回成功==" + jsonArray.toString());


                boolean result = HttpReturnParse.getInstance().parseInfoList(jsonArray);


                if (result) {

                    List<ShowInfo> list = DataSupport.findAll(ShowInfo.class);

                    if (null != list && list.size() > 0) {


                        showInfoListAdapter.setList(list);


                    }

                }



                /**
                 * 测试代码
                 */
//                testPage();

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {


                ToastUtil.showMsg(getApplicationContext(), resultMessage);
                Log.i(HttpConst.SERVER_BACK, "===获取信息展示列表返回失败==" + resultCode + " " + resultMessage);
            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(getApplicationContext(), error);
                Log.i(HttpConst.SERVER_BACK, "===获取信息展示列表返回失败==" + error);
            }
        });


    }


    /**
     * 测试代码
     */
    private void testPage() {

        List<ShowInfo> list = new ArrayList<>();

        ShowInfo showInfo1 = new ShowInfo();
        showInfo1.setTitle("asdasdasdasd");
        showInfo1.setShowinfo_Id("5");
        showInfo1.setThumbnail(ConstBase.TEST_2);

        ShowInfo showInfo2 = new ShowInfo();

        showInfo2.setThumbnail(ConstBase.TEST_2);


        ShowInfo showInfo3 = new ShowInfo();
        showInfo3.setThumbnail(ConstBase.TEST_2);

        ShowInfo showInfo4 = new ShowInfo();

        showInfo4.setThumbnail(ConstBase.TEST_2);

        list.add(showInfo1);
        list.add(showInfo2);
        list.add(showInfo3);
        list.add(showInfo4);

        showInfoListAdapter.setList(list);
    }


    @Override
    protected void initViews() {

        /**
         * title
         *
         */

        iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_common_title = (TextView) findViewById(R.id.tv_common_title);
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_search.setVisibility(View.GONE);


        /**
         * listView
         */

        lv_show_info_list = (ListView) findViewById(R.id.lv_show_info_list);
        showInfoListAdapter = new ShowInfoListAdapter(this);
        lv_show_info_list.setAdapter(showInfoListAdapter);

        /**
         * webView
         *
         */
        wv_infor_show = (WebView) findViewById(R.id.wv_infor_show);
        wv_infor_show.getSettings().setJavaScriptEnabled(true);
        wv_infor_show.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv_infor_show.getSettings().setDomStorageEnabled(true);
        wv_infor_show.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv_infor_show.setBackgroundColor(0x00000000);
        wv_infor_show.getSettings().setLoadWithOverviewMode(true);
        wv_infor_show.setWebViewClient(new WebClientPicDescrib(this));

    }

    @Override
    protected void initListener() {

        iv_common_back.setOnClickListener(this);


        lv_show_info_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ShowInfo showInfo = (ShowInfo) showInfoListAdapter.getItem(position);

                Bundle bundle = new Bundle();

                bundle.putSerializable(ConstIntent.BundleKEY.SHOWINFO_LIST_TO_DETIAL,showInfo);

                openActivity(ShowInfoDetailActivity.class, bundle);


            }
        });

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
}
