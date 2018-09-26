package org.tangze.work.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;

import org.tangze.work.R;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.ShowInfo;
import org.tangze.work.entity.temp.ShowInfoDetial;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.StringUtils;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.webView.WebClientPicDescrib;

import java.util.Map;

public class ShowInfoDetailActivity extends BaseActivity implements OnClickListener{


    /**
     * title
     */

    private ImageView iv_common_back;
    private TextView tv_common_title;
    private ImageView iv_common_search;


    private WebView wv_info_detail_show;



    private ShowInfo showInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_detail);
        initData();
    }


    private void initData() {

      if(null != showInfo) {


          /**
           * 设置界面参数
           */
          tv_common_title.setText(showInfo.getTitle());


          /**
           * 发起请求并加载数据
           */

          getNewMessageDetailFromServerById(showInfo.getShowinfo_Id());
      }


    }



    private void  getNewMessageDetailFromServerById(String id) {

        Map map = ParaUtils.getShowInfoDetailById(id);


        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.NEWS_CONTENT, map, new HttpResultSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取信息详情返回成功==" + jsonArray.toString());

                ShowInfoDetial showInfoDetial = HttpReturnParse.getInstance().parseInfoDetail(jsonArray);

                ShwoServerbackToUi(showInfoDetial.getContent());

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                ToastUtil.showMsg(getApplicationContext(), resultMessage);
                Log.i(HttpConst.SERVER_BACK, "===获取信息详情返回失败==" + resultCode + " " + resultMessage);
            }

            @Override
            public void _onError(String error) {

                ToastUtil.showMsg(getApplicationContext(), error);
                Log.i(HttpConst.SERVER_BACK, "===获取信息详情返回失败==" + error);

            }
        });


    }


    /**
     * 将服务器返回的数据的content字段设置的界面上
     * @param str
     */
    private void ShwoServerbackToUi(String str) {



        String html = combinationStyleForHtml(str);

        wv_info_detail_show.loadDataWithBaseURL(null, html,ConstBase.MIME_TYPE,ConstBase.CODING_TYPE, null);

    }


    /**
     * 给返回的html代码中加入样式
     * @param s
     * @return
     */
    private String combinationStyleForHtml(String s) {



        String html  = s;
        if(TextUtils.isEmpty(html)){
            html ="";
        }else{
            html = StringUtils.replaceImgStyle(html);
        }

        html = ConstBase.WEB_IMG_STYLE+html;
        return html;


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
         * webView
         *
         */
        wv_info_detail_show = (WebView) findViewById(R.id.wv_info_detail_show);
        wv_info_detail_show.getSettings().setJavaScriptEnabled(true);
        wv_info_detail_show.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv_info_detail_show.getSettings().setDomStorageEnabled(true);
        wv_info_detail_show.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv_info_detail_show.setBackgroundColor(0x00000000);
        wv_info_detail_show.getSettings().setLoadWithOverviewMode(true);
        wv_info_detail_show.setWebViewClient(new WebClientPicDescrib(this));
    }

    @Override
    protected void initListener() {
        iv_common_back.setOnClickListener(this);
    }

    @Override
    protected void processIntent() {

        Intent intent = this.getIntent();

        if(null != intent ) {

            Bundle bundle = intent.getExtras();

            if(null != bundle) {


                this.showInfo = (ShowInfo) bundle.getSerializable(ConstIntent.BundleKEY.SHOWINFO_LIST_TO_DETIAL);

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
}
