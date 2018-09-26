package org.tangze.work.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tangze.work.R;
import org.tangze.work.Test.TestCons;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.constant.ConstSp;
import org.tangze.work.entity.User;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.SpUtil;
import org.tangze.work.utils.ToastUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends BaseActivity implements OnClickListener {


    /**
     * title
     */
    private RelativeLayout rl_top_bar;
    private ImageView iv_login_back;
    private TextView tv_login_title;
    private ImageView iv_common_search;

    /**
     * 中间
     */
    private EditText et_username;
    private EditText et_password;

    private ImageView iv_clear_account;
    private ImageView iv_clear_password;

    private RelativeLayout rl_login;
    private RelativeLayout rl_register_new;


//    /**
//     *  回传数据
//     */
//    private Intent intent;
//
//    private Bundle bundle;
//
//    private String[] arry; //用于登陆成功与否的返回回传数据使用 0下标代表当前frgmentTag，1下标代表被点击的frgmentTag
//
//    private int flag ; //productDetial点击收藏的传递的值；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initViews() {
        rl_top_bar = (RelativeLayout) findViewById(R.id.rl_top_bar);
        rl_top_bar.setVisibility(View.GONE);
        iv_login_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_login_title = (TextView) findViewById(R.id.tv_common_title);
        tv_login_title.setText(getString(R.string.login));
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_search.setVisibility(View.GONE);


        /**
         * 中间
         */
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);

          iv_clear_account = (ImageView) findViewById(R.id.iv_clear_account);
          iv_clear_password = (ImageView) findViewById(R.id.iv_clear_password);

        rl_login = (RelativeLayout) findViewById(R.id.rl_login);
        rl_register_new = (RelativeLayout) findViewById(R.id.rl_register_new);
    }

    @Override
    protected void initListener() {

        iv_login_back.setOnClickListener(this);
        rl_login.setOnClickListener(this);
        rl_register_new.setOnClickListener(this);

        iv_clear_account.setOnClickListener(this);
        iv_clear_password.setOnClickListener(this);
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

            case R.id.rl_login:

                doLogin();


                break;

            case R.id.rl_register_new:
                openActivity(RegisterActivity.class, null);
                break;


            case R.id.iv_clear_account:
                et_username.setText(ConstBase.STRING_COLON);
                break;


            case R.id.iv_clear_password:
                et_password.setText(ConstBase.STRING_COLON);
                break;
        }
    }


    /**
     * 用户登陆
     */
    private void doLogin() {


        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        Map map = ParaUtils.getLoginParam(username, password);

        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.USER_LOGIN, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonElements) {

                Log.i(HttpConst.SERVER_BACK, "==========登陆成功返回 " + jsonElements.toString());

                boolean result = HttpReturnParse.getInstance().parseLoginBackAndSaveUser(jsonElements);

                if (result) {

                    openActivityAndFinishItself(MainActivity.class, null);

                }

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                ToastUtil.showMsg(getApplicationContext(), resultCode + resultMessage);


                Log.i(HttpConst.SERVER_BACK, "==========登陆返回===" + resultCode + " " + resultMessage);
            }

            @Override
            public void _onError(String error) {

                ToastUtil.showMsg(getApplicationContext(), error);


                Log.i(HttpConst.SERVER_BACK, "==========登陆返回===" + error);
            }
        });


    }


//    /**
//     * 页面关闭时调用
//     */
//    private void finishBack(int resultCode,String tagBack) {
//
//        Intent intentBack = new Intent();
//        Bundle bundleBack = new Bundle();
//        bundleBack.putString(ConstIntent.BundleKEY.FG_TAG_SINGLE_IN_LOGIN_BACK,tagBack);
//        intentBack.putExtras(bundleBack);
//        LoginActivity.this.setResult(resultCode, intentBack);
//        LoginActivity.this.finish();
//    }

}
