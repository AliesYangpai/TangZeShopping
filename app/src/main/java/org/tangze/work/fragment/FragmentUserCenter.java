package org.tangze.work.fragment;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.Test.TestCons;
import org.tangze.work.activity.AddressActivity;
import org.tangze.work.activity.CustomerServiceActivity;
import org.tangze.work.activity.LoginActivity;
import org.tangze.work.activity.MyCollectionActivity;
import org.tangze.work.activity.OrderStateActivity;
import org.tangze.work.activity.SettingActivity;
import org.tangze.work.activity.SettlementActivity;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.constant.ConstShare;
import org.tangze.work.constant.ConstSp;
import org.tangze.work.entity.User;
import org.tangze.work.utils.BooleanUtils;
import org.tangze.work.utils.SpUtil;
import org.tangze.work.utils.ToastUtil;

/**
 * genre中心fragment
 */
public class FragmentUserCenter extends BaseFragment implements View.OnClickListener,UMShareListener {


    public static final String TAG = FragmentUserCenter.class.getSimpleName();
    /**
     * titile
     */

    private TextView tv_user_center_fg_title;
    private ImageView iv_user_center_fg_setting;  //齿轮
    private LinearLayout ll_user_center_to_search;
    private TextView tv_current_city;


    /**
     * scrollview
     */
    private ScrollView sv_user_center;

    /**
     * 用户相关信息
     */
    private TextView tv_userName;//用户名
    private TextView tv_userNumber;//电话号码


    /**
     * 订单状态Item
     *
     * @return
     */

    private RadioButton rb_not_deliver_goods;//未发货
    private RadioButton rb_delivering;//发货中
    private RadioButton rb_sure_finish;//已完成
    private RadioButton rb_back_goods_in_down;//退货申请
    private RadioButton rb_denied;//已拒绝
    private RadioButton rb_after_sale_service;//售后 【不做实现】


    /**
     *
     * 功能Line1
     * 意见反馈、地址管理、我的收藏、联系客服
     * @return
     */

    private LinearLayout ll_feedback_item; //意见反馈
    private LinearLayout ll_address_item; //地址管理
    private LinearLayout ll_collect_item; //我的收藏
    private LinearLayout ll_customer_service_item;//联系客服
//    private TextView tv_customer_service_number; //客服电话号码

    /**
     * 功能line2
     * 系统设置
     */
    private LinearLayout ll_user_center_fg_setting;//系统设置
    private LinearLayout ll_user_center_fg_share;//平台分享

    /**
     * 广播接收编辑成功的数据
     */
    private UserEditSuccessBackReceiver userEditSuccessBackReceiver;
    private User user;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_user_center;
    }

    @Override
    protected void getSendData(Bundle arguments) {

    }

    @Override
    protected void initView() {
        /**
         * titile
         */
        tv_user_center_fg_title = findViewById(R.id.tv_fragment_title);
        iv_user_center_fg_setting = findViewById(R.id.iv_fragment_search);
        ll_user_center_to_search = findViewById(R.id.ll_fragment_to_search);
        tv_current_city = findViewById(R.id.tv_current_city);
        /**
         * 用户相关信息
         */
        tv_userName = findViewById(R.id.tv_userName);//用户名
        tv_userNumber = findViewById(R.id.tv_userNumber);//电话号码



        /**
         * scrollview
         */
          sv_user_center = findViewById(R.id.sv_user_center);
          sv_user_center.setOverScrollMode(View.OVER_SCROLL_NEVER); //去掉底部蓝色边缘
        /**
         * 订单状态Item
         * @return
         */

        rb_not_deliver_goods = findViewById(R.id.rb_not_deliver_goods);//未发货
        rb_delivering = findViewById(R.id.rb_delivering);//发货中
        rb_sure_finish = findViewById(R.id.rb_sure_finish);//已完成
        rb_back_goods_in_down = findViewById(R.id.rb_back_goods_in_down); //退货申请
        rb_denied = findViewById(R.id.rb_denied);//已拒绝
        rb_after_sale_service = findViewById(R.id.rb_after_sale_service);//售后


        /**
         *
         * 功能Line1
         * 意见反馈、地址管理、我的收藏、联系客服
         * @return
         */
        ll_feedback_item = findViewById(R.id.ll_feedback_item);//意见反馈
        ll_address_item = findViewById(R.id.ll_address_item); //地址管理
        ll_collect_item = findViewById(R.id.ll_collect_item); //我的收藏
        ll_customer_service_item = findViewById(R.id.ll_customer_service_item);//联系客服
//        tv_customer_service_number = findViewById(R.id.tv_customer_service_number);//客服电话

        /**
         * 功能line2
         * 系统设置
         */
        ll_user_center_fg_setting = findViewById(R.id.ll_user_center_fg_setting);//系统设置
        ll_user_center_fg_share = findViewById(R.id.ll_user_center_fg_share);



    }

    @Override
    protected void initListener() {


        /**
         * title
         */
//        iv_user_center_fg_setting.setOnClickListener(this);

        /**
         * 订单状态Item
         * @return
         */
        rb_not_deliver_goods.setOnClickListener(this);//未发货
        rb_delivering.setOnClickListener(this);//发货中
        rb_sure_finish.setOnClickListener(this);//已完成
        rb_back_goods_in_down.setOnClickListener(this);//退货申请
        rb_denied.setOnClickListener(this);//已拒绝
        rb_after_sale_service.setOnClickListener(this);//售后【不做实现】



        /**
         *
         * 功能Line1
         * 意见反馈、地址管理、我的收藏、联系客服
         * @return
         */
        ll_feedback_item.setOnClickListener(this); //意见反馈
        ll_address_item.setOnClickListener(this); //地址管理
        ll_collect_item.setOnClickListener(this); //我的收藏
        ll_customer_service_item.setOnClickListener(this);//联系客服


        /**
         * 功能line2
         * 系统设置
         */
        ll_user_center_fg_setting.setOnClickListener(this);//系统设置
        ll_user_center_fg_share.setOnClickListener(this);//分享操作

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            /**
             * title
             */

            case R.id.iv_fragment_search:  //点击小齿轮进入到设置界面

//                Intent intentSetting = new Intent(mActivity, SettingActivity.class);
//                startActivity(intentSetting);
                break;


            /**
             * 订单状态相关点击
             */
            case R.id.rb_not_deliver_goods://未发货

                goToOrderState(ConstIntent.BundleValue.ORDER_NOT_SEND);
                break;
            case R.id.rb_delivering://发货中
                goToOrderState(ConstIntent.BundleValue.ORDER_SENDING);
                break;
            case R.id.rb_sure_finish://已完成
                goToOrderState(ConstIntent.BundleValue.ORDER_FINISH);
                break;
            case R.id.rb_back_goods_in_down://申请退货
                goToOrderState(ConstIntent.BundleValue.ORDER_BACK_GOODS_APPLY);
                break;

            case R.id.rb_denied://申请退货
                goToOrderState(ConstIntent.BundleValue.ORDER_DENIED);
                break;


            case R.id.rb_after_sale_service://售后

                goToCustomerServiceActivity();
                break;

            case R.id.ll_feedback_item:  //意见反馈
                ToastUtil.showMsg(mActivity.getApplicationContext(), "意见反馈");

                break;
            case R.id.ll_address_item:  //地址管理
                Intent intent = new Intent(mActivity, AddressActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_collect_item:  //我的收藏
                goToMyCollection();
                break;
            case R.id.ll_customer_service_item:  //联系客服
//                startToCallService();
                goToCustomerChat();
                break;

            case R.id.ll_user_center_fg_setting: //系统设置

                Intent intentSetting = new Intent(mActivity, SettingActivity.class);
                startActivity(intentSetting);
                break;


            case R.id.ll_user_center_fg_share:


                startToShare();
                break;

        }
    }


    /**
     * 分享操作
     */
    private void startToShare() {


        ShareBoardConfig config = new ShareBoardConfig();
        config.setTitleVisibility(false); //设置title是否显示
        config.setCancelButtonText("取消");
        config.setShareboardBackgroundColor(Color.rgb(245, 245, 245));//设置分享面板的背景颜色


        UMImage thumb =  new UMImage(mActivity, R.drawable.in_logo);

        UMWeb  web = new UMWeb(ConstShare.Content.DOWNLOAD_URL);
        web.setTitle(ConstShare.Content.TITLE);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(ConstShare.Content.DESCRIPTION);//描述

        new ShareAction(mActivity)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(this)
                .withMedia(web)
                .open(config);



    }


    /**
     * 分享的回调监听
     * @param share_media
     */

    @Override
    public void onStart(SHARE_MEDIA share_media) {


    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

        ToastUtil.showMsg(mActivity,share_media+" 分享成功");

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        ToastUtil.showMsg(mActivity,share_media+" 分享失败");
        Log.i("sharexxx"," "+throwable.toString());
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        ToastUtil.showMsg(mActivity,share_media+" 分享取消");
    }


    /**
     * 进入到我的收藏界面
     */
    private void goToMyCollection() {
        Intent intent = new Intent(mActivity, MyCollectionActivity.class);
        startActivity(intent);

    }

    /**
     * 进入到orderstate界面并且传递数据
     *
     * @param tag
     */
    private void goToOrderState(int tag) {

        Intent intent = new Intent(mActivity, OrderStateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ConstIntent.BundleKEY.ORDER_STATE, tag);
        intent.putExtras(bundle);
        startActivity(intent);


    }


    /**
     * 进入到售后界面
     * 不做实现
     */
    private void goToCustomerServiceActivity() {

        Intent intent = new Intent(mActivity, CustomerServiceActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onLazyLoad() {

        ll_user_center_to_search.setVisibility(View.GONE);
        tv_current_city.setVisibility(View.GONE);
//        iv_user_center_fg_setting.setImageResource(R.drawable.img_setting);
        iv_user_center_fg_setting.setVisibility(View.GONE);
        tv_user_center_fg_title.setText(mActivity.getResources().getString(R.string.user_center));
//        tv_customer_service_number.setText(SpUtil.getInstance().getStringValue(ConstSp.SP_KEY_TOP_QQ,ConstSp.SP_VALUE.TOP_QQ_DEFAULT));
        registerBroadCast();
        getUserInfoFromDb();

    }


    /**
     * 获取用户名称信息
     */


    private void getUserInfoFromDb() {


        user = DataSupport.findFirst(User.class);

        if(null != user) {

            tv_userName.setText(user.getNickName());
            tv_userNumber.setText(user.getTelNum());

        }



    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fragment", TAG + " onCreate==============================");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (userEditSuccessBackReceiver != null) {
            mActivity.unregisterReceiver(userEditSuccessBackReceiver);
        }

        Log.i("fragment", TAG + " ***************onDestroy********************************");
    }


//    private void startToCallService() {
//        String num = tv_customer_service_number.getText().toString();
//        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
//        startActivity(intentCall);
//    }


    private void goToCustomerChat() {
        if (BooleanUtils.isQQClientAvailable(mActivity.getApplicationContext())) {
            /**
             * 这个方法会自动判断是否登陆，如果没有登陆则跳转到qq登陆界面
             */

            String qqNum = SpUtil.getInstance().getStringValue(ConstSp.SP_KEY_TOP_QQ,ConstSp.SP_VALUE.TOP_QQ_DEFAULT);

            if(TextUtils.isEmpty(qqNum)) {


                ToastUtil.showMsg(mActivity.getApplicationContext(),mActivity.getApplicationContext().getString(R.string.not_get_the_upperQQ));

            }else {

                String url = ConstBase.JUMP_TO_QQ + qqNum;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }

        } else {
            ToastUtil.showMsg(mActivity.getApplicationContext(), mActivity.getApplicationContext().getString(R.string.setUp_qq_first));
        }
    }


    /**
     * 用户编辑返回的广播
     */
    private void registerBroadCast() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstIntent.IntentAction.USER_EDIT_SUCCESS);

        userEditSuccessBackReceiver = new UserEditSuccessBackReceiver();

        mActivity.registerReceiver(userEditSuccessBackReceiver, filter);

    }







    private class UserEditSuccessBackReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConstIntent.IntentAction.USER_EDIT_SUCCESS.equals(action)) {

                /**
                 * 开始执行刷新设备
                 */
                refreshUserInfoFromDb();

            }
        }
    }


    private void refreshUserInfoFromDb() {

        User userFreshed = DataSupport.findFirst(User.class);
        tv_userName.setText(userFreshed.getNickName());//用户名
        tv_userNumber.setText(userFreshed.getTelNum());//电话号码

    }

}
