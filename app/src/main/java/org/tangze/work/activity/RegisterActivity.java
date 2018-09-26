package org.tangze.work.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import org.tangze.work.R;
import org.tangze.work.entity.User;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.utils.InputFilterMinMax;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.ToastUtil;

import java.util.Map;


public class RegisterActivity extends BaseActivity implements OnClickListener {


    /**
     * title
     */
    private ImageView iv_register_back;
    private TextView tv_register_title;
    private ImageView iv_common_search;


    /**
     * 用户名、密码。。。。
     */
    private EditText et_username_rg;
    private EditText et_password_rg;
    private EditText et_nick_name_rg;
    private EditText et_code_rg;
    private EditText et_tel_rg;
    private EditText et_emial_rg;
    private EditText et_user_qq_rg;

    /**
     * 底部注册按钮
     */
    private RelativeLayout rl_register_new_rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initViews() {
        iv_register_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_register_title = (TextView) findViewById(R.id.tv_common_title);
        tv_register_title.setText(getString(R.string.register));
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_search.setVisibility(View.GONE);


        /**
         * 用户名、密码。。。。
         *
         */
        et_username_rg = (EditText) findViewById(R.id.et_username_rg);
        et_password_rg = (EditText) findViewById(R.id.et_password_rg);
        et_nick_name_rg = (EditText) findViewById(R.id.et_nick_name_rg);
        et_code_rg = (EditText) findViewById(R.id.et_code_rg);
        et_code_rg.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        et_tel_rg = (EditText) findViewById(R.id.et_tel_rg);
        et_emial_rg = (EditText) findViewById(R.id.et_emial_rg);
        et_user_qq_rg = (EditText) findViewById(R.id.et_user_qq_rg);


        /**
         * 底部注册按钮
         */
        rl_register_new_rg = (RelativeLayout) findViewById(R.id.rl_register_new_rg);
    }

    @Override
    protected void initListener() {
        iv_register_back.setOnClickListener(this);
        rl_register_new_rg.setOnClickListener(this);
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


            case R.id.rl_register_new_rg:
                doRegester();
                break;
        }
    }


    private void doRegester() {


        String username = et_username_rg.getText().toString();
        String password = et_password_rg.getText().toString();
        String nickname = et_nick_name_rg.getText().toString();
        String code = et_code_rg.getText().toString();
        String tel = et_tel_rg.getText().toString();
        String emial = et_emial_rg.getText().toString();
        String qq = et_user_qq_rg.getText().toString();

        Map<String, Object> map = ParaUtils.getRegisterParam(username, password, nickname, code, tel, emial,qq);

        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.USER_ADD, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonElements) {

                ToastUtil.showMsg(getApplicationContext(), getString(R.string.register_success));
                finish();

                Log.i(HttpConst.SERVER_BACK, "==========注册成功返回 " + jsonElements.toString());
            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                ToastUtil.showMsg(getApplicationContext(), resultCode + resultMessage);


                Log.i(HttpConst.SERVER_BACK, "==========注册返回 " + resultCode + " " + resultMessage);
            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(getApplicationContext(), error);

                Log.i(HttpConst.SERVER_BACK, "==========注册返回" + error);
            }
        });

    }


//    /**
//     * 注册成功的界面跳转
//     */
//    private void finishBack_inRegisterSuccess() {
//
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        intent.putExtras(bundle);
//        RegisterActivity.this.setResult(ConstIntent.ResponseCode.REGISTER_SUCCESS, intent);
//        RegisterActivity.this.finish();
//
//    }


}
