package org.tangze.work.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.Test.TestCons;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.constant.ConstSp;
import org.tangze.work.entity.Address;
import org.tangze.work.entity.Classify;
import org.tangze.work.entity.ClassifySecond;
import org.tangze.work.entity.DeliverOrder;
import org.tangze.work.entity.MyCollection;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.SearchHistory;
import org.tangze.work.entity.ShoppingCar;
import org.tangze.work.entity.User;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.SpUtil;
import org.tangze.work.utils.ToastUtil;

import java.util.Map;

public class SettingActivity extends BaseActivity implements View.OnClickListener {



    public static final String TAG = SettingActivity.class.getSimpleName();

    /**
     * title
     */
    private ImageView iv_logout_back;
    private TextView tv_logout_title;
    private ImageView iv_common_finish;


    /**
     * 中间相关信息
     */

    private EditText et_nick_name; //用户昵称
//    private EditText et_code;
    private EditText et_password;//用户密码
    private TextView tv_code; //邀请码
    private EditText et_tel;  //电话号码
    private EditText et_email;// email
    private EditText et_user_qq; //用户自己的qq

    /**
     * 退出布局
     */
    private RelativeLayout rl_logout;


    /**
     * 数据相关
     */

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initData();
    }


    private void initData() {
        user = DataSupport.findFirst(User.class);


        if(null != user) {

            et_nick_name.setText(user.getNickName());
            et_password.setText(user.getPassWord());
            et_tel.setText(user.getTelNum());
            et_email.setText(user.getEmail());
            tv_code.setText(user.getRegCode());
            et_user_qq.setText(user.getUser_qq());
        }


    }


    @Override
    protected void initViews() {
        /**
         * title
         */
        iv_logout_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_logout_title = (TextView) findViewById(R.id.tv_common_title);
        tv_logout_title.setText(getString(R.string.setting));
        iv_common_finish = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_finish.setImageResource(R.drawable.img_sure);

        /**
         * 中间相关信息
         */

        et_nick_name = (EditText) findViewById(R.id.et_nick_name);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_code = (TextView) findViewById(R.id.tv_code);

//        et_code = (EditText) findViewById(R.id.et_code);
//        et_code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        et_tel = (EditText) findViewById(R.id.et_tel);
        et_email = (EditText) findViewById(R.id.et_email);
        et_user_qq = (EditText) findViewById(R.id.et_user_qq);


        /**
         * 退出布局
         */
        rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);

    }

    @Override
    protected void initListener() {
        iv_logout_back.setOnClickListener(this);
        iv_common_finish.setOnClickListener(this);
        rl_logout.setOnClickListener(this);
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

            case R.id.rl_logout:
                logout();
                break;

            case R.id.iv_common_search:
                editUserFinishAndBack();
                break;
        }
    }


    /**
     * 完成编辑返回的方法
     */

    private void editUserFinishAndBack() {


        String nickName = et_nick_name.getText().toString();
        String password = et_password.getText().toString();
        String code = tv_code.getText().toString();
        String userTel = et_tel.getText().toString();
        String email = et_email.getText().toString();
        String top_qq = et_user_qq.getText().toString();

        final User userEdit = new User();
        userEdit.setUser_id(user.getUser_id());
        userEdit.setUserName(user.getUserName());
        userEdit.setPassWord(password);
        userEdit.setNickName(nickName);
        userEdit.setRegCode(code);
        userEdit.setTelNum(userTel);
        userEdit.setEmail(email);
        userEdit.setUser_qq(top_qq);

        Map map = ParaUtils.getUserInfoParam(userEdit);
        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.USER_EDIT, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {
                Log.i(HttpConst.SERVER_BACK, "=========用户编辑成功返回====" + jsonArray.toString());

                boolean result = HttpReturnParse.getInstance().parseUserEditBackAndSaveUser(jsonArray, userEdit);

                if (result) {

                    /**
                     * 关闭这个界面，并且回传数据
                     */
                    sendUserEditSuccessBroadCastAndFinishIt();
                }


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                Log.i(HttpConst.SERVER_BACK, "==========用户编辑失败 " + resultCode + " " + resultMessage);
            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(getApplicationContext(), error);

                Log.i(HttpConst.SERVER_BACK, "==========用户编辑失败" + error);
            }
        });


    }


    private void sendUserEditSuccessBroadCastAndFinishIt() {
        Intent intentBroad = new Intent();
        intentBroad.setAction(ConstIntent.IntentAction.USER_EDIT_SUCCESS);
        sendBroadcast(intentBroad);
        finish();
    }


    /**
     * 退出的方法
     * 有退出接口了再处理
     */
    private void logout() {

        /**
         * 1.将是否登陆标记为false
         * 2.将上级qq清空
         * 3.将用户等级清空
         * 4.将所有data清空
         * 5.xxx
         */

        clearAllDatasAndFinishAll();



    }


    /**
     * 清空所有数据
     * 1.将是否登陆标记为false
     * 2.将上级qq清空
     * 3.将用户等级清空
     * 4.将所有data清空
     * 5.xxx
     */
    private void clearAllDatasAndFinishAll() {
        // 1.将是否登陆标记为false
        boolean is_logOut= SpUtil.getInstance().saveBooleanTosp(ConstSp.SP_KEY_IS_LOGIN_OR_NOT,ConstSp.SP_VALUE.IS_LOGIN_DEFAULT);
        // 2.将上级qq清空
        boolean top_qqclear = SpUtil.getInstance().saveStringToSp(ConstSp.SP_KEY_TOP_QQ, ConstSp.SP_VALUE.TOP_QQ_DEFAULT);
        // 3.将用户等级清空 （账号等级 0:厂商 1：省代 2：市代 3：终端店铺）
        boolean grade_clear= SpUtil.getInstance().saveIntToSp(ConstSp.SP_KEY_USER_TYPE, ConstSp.SP_VALUE.USER_TYPE_DEFAULT);  //设置用户等级

        //4.清除所有表的记录

        clearAllTableData();


        removeAllFromActiviiesStack(TAG);


        openActivityAndFinishItself(LoginActivity.class,null);

    }


    /**
     * 清空所有表中的记录
     * @return
     */
    private void clearAllTableData() {

        DataSupport.deleteAll(Address.class);
        DataSupport.deleteAll(Classify.class);
        DataSupport.deleteAll(Product.class);
        DataSupport.deleteAll(User.class);
        DataSupport.deleteAll(ClassifySecond.class);
        DataSupport.deleteAll(MyCollection.class);
        DataSupport.deleteAll(ShoppingCar.class);
        DataSupport.deleteAll(DeliverOrder.class);
        DataSupport.deleteAll(SearchHistory.class);

    }
}
