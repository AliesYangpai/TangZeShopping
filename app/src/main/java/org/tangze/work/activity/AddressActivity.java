package org.tangze.work.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.Test.AddressEntity;
import org.tangze.work.adapter.AddressAdapter;
import org.tangze.work.callback.AddressDelCallBack;
import org.tangze.work.callback.AddressSetDefaultCallBack;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Address;
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
import java.util.concurrent.BrokenBarrierException;

public class AddressActivity extends BaseActivity implements View.OnClickListener
        ,AddressSetDefaultCallBack,AddressDelCallBack {


    /**
     * title
     *
     */
    private ImageView iv_address_back;
    private TextView tv_address_title;
    private ImageView iv_address_sure;


    /**
     * 中间listView
     *
     */
    private ListView lv_address_list;
    private AddressAdapter addressAdapter;

    /**
     * 中间空白图片
     */
    private ImageView iv_empty;


    /**
     * 底部
     *
     */
    private LinearLayout ll_add_new_address;
    private TextView tv_add_new_address;


    /**
     * 相关数据
     */
    private User user;

    private int currentTag;//用于标记，是从SettlmentActivit结算界面跳转而来还是从别的界面跳过来的




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        getMyAddressListFromServer();
    }


    /**
     * 从服务器获取我的地址
     */
    private void getMyAddressListFromServer(){


        user = DataSupport.findFirst(User.class);

        int userId = user.getUser_id();

        Map map = ParaUtils.getUserIdToGetAddressList(userId);

        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.USER_ADDRESS_LIST, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                Log.i(HttpConst.SERVER_BACK, "===获取地址返回成功AddressActivity中==" + jsonArray.toString());

                boolean hasAddress = HttpReturnParse.getInstance().parseAddressBack(jsonArray);

                /**
                 * 界面数据变更
                 */
                updateData(hasAddress);

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                ToastUtil.showMsg(getApplicationContext(), resultCode + " " + resultMessage);
                Log.i(HttpConst.SERVER_BACK, "===获取地址返回失败AddressActivity中==" + resultCode + " " + resultMessage);


            }

            @Override
            public void _onError(String error) {

                ToastUtil.showMsg(getApplicationContext(), error);
                Log.i(HttpConst.SERVER_BACK, "===获取地址返回失败AddressActivity中==" + error);

            }
        });

    }


    /**
     * 根据服务器返回进行界面显示
     * @param hasAddressOrNot
     */
    private void updateData(boolean hasAddressOrNot) {

        if(hasAddressOrNot) {


            List<Address> addresses = DataSupport.findAll(Address.class);


            showOrHindEmptyPic(addresses);

            addressAdapter.setList(addresses);

        }else {

            ToastUtil.showMsg(getApplicationContext(),"未设置收货地址");
        }
    }


    @Override
    protected void initViews() {
        /**
         * title
         *
         */
        iv_address_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_address_title = (TextView) findViewById(R.id.tv_common_title);
        iv_address_sure = (ImageView) findViewById(R.id.iv_common_search);
//        iv_address_sure.setImageResource(R.drawable.test_addbig);
        iv_address_sure.setVisibility(View.GONE);
        tv_address_title.setText(getString(R.string.address_title));



        /**
         * 中间listView
         *
         */
        lv_address_list = (ListView) findViewById(R.id.lv_address_list);
        addressAdapter = new AddressAdapter(this);
        lv_address_list.setAdapter(addressAdapter);

        /**
         * 中间空白图片
         */
        iv_empty = (ImageView) findViewById(R.id.iv_empty);


        /**
         * 底部
         *
         */
        ll_add_new_address = (LinearLayout) findViewById(R.id.ll_activity_bottom_common);
        tv_add_new_address = (TextView) findViewById(R.id.tv_activity_bottom_common);
        tv_add_new_address.setText(getString(R.string.address_add_new));
    }

    @Override
    protected void initListener() {
        /**
         * title
         *
         */
        iv_address_back.setOnClickListener(this);
        iv_address_sure.setOnClickListener(this);
        /**
         * 底部
         *
         */
        ll_add_new_address.setOnClickListener(this);
        /**
         * 地址回调的监听
         */
        addressAdapter.setAddressSetDefaultCallBack(this);
        addressAdapter.setAddressDelCallBack(this);
    }

    @Override
    protected void processIntent() {

        Intent intent = this.getIntent();

        if(null != intent) {


            Bundle bundle = intent.getExtras();

            if(null != bundle) {


                currentTag = bundle.getInt(ConstIntent.BundleKEY.SETTMENT_TO_ADDRESS);

            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_common_back:


//                backToFrontPage();//address设置完成修改前


                backToFrontPageNew();//address设置完成修改后
                break;

            case R.id.ll_activity_bottom_common:


                Intent intent = new Intent(this,AddressNewOrEditActivity.class);

                Bundle bundle = new Bundle();

                bundle.putInt(ConstIntent.BundleKEY.ADDRESS, ConstIntent.BundleValue.ADD_NEW_ADDRESS);

                intent.putExtras(bundle);

                startActivityForResult(intent, ConstIntent.RequestCode.ADD_NEW_ADDRESS);

                break;


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(ConstIntent.BundleValue.ADD_NEW_ADDRESS == requestCode) {


            switch (resultCode) {

                /**
                 * 添加地址成功但不是默认地址
                 */
                case ConstIntent.ResponseCode.ADD_NEW_ADDRESS_SUCCESS_NOT_DEFUALT:

                    getAllAddressFromDb();

                    break;


                /**
                 * 添加地址成功并设置为默认地址地址
                 */
                case ConstIntent.ResponseCode.ADD_NEW_ADDRESS_SUCCESS_DEFUALT:

//                    backToFrontPage();//address设置完成修改前


                    backToFrontPageNew();//address设置完成修改后
                    break;

            }



        }else if(ConstIntent.BundleValue.EDIT_ADDRESS == requestCode){

            /**
             * 当请求是编辑地址时候时
             */
            switch (resultCode) {

                /**
                 * 添加地址成功但不是默认地址【编辑时，不能设置为默认地址】
                 */
                case ConstIntent.ResponseCode.EDIT_ADDRESS_SUCCESS:

                    getAllAddressFromDb();

                    break;
            }

        }


    }

    /**
     * 显示或者隐藏空图片
     */
    private void showOrHindEmptyPic(List<Address> list) {

        if( null!=list && list.size() > 0) {

            iv_empty.setVisibility(View.GONE);
        }else {

            iv_empty.setVisibility(View.VISIBLE);

        }

    }

   private void getAllAddressFromDb() {


       List<Address> addresses = DataSupport.findAll(Address.class);


       showOrHindEmptyPic(addresses);

       if(null != addresses && addresses.size() > 0) {

           addressAdapter.setList(addresses);

       }
   }



    /**
     * 界面设置成功的回调接口
     * @param result
     */
    @Override
    public void addressSetSuccess(boolean result) {

        if(result) {

            //address设置完成修改前
//            backToFrontPage();


            //address设置完成修改后
            backToFrontPageNew();
        }


    }


    /**
     * 当设置为默认地址后，界面根据currentTag进行跳转
     * //address设置完成修改前
     */


    private void backToFrontPage() {




        if(currentTag == ConstIntent.BundleValue.SETTMENT_TO_ADDRESS) {

            this.setResult(ConstIntent.ResponseCode.SET_ADDRESS_SUCCESS_TO_SETTLMENT);


            this.finish();


        }else {

            this.finish();
        }


    }



    /**
     * 当设置为默认地址后，界面根据currentTag进行跳转
     * //address设置完成修改后
     */
    private void backToFrontPageNew() {




        if(currentTag == ConstIntent.BundleValue.SETTMENT_TO_ADDRESS) {





            sendBroadCast();

            this.finish();


        }else {

            this.finish();
        }


    }

    //address设置完成修改后
    private void sendBroadCast() {

        Intent intentBroad = new Intent();
        intentBroad.setAction(ConstIntent.IntentAction.ADDRESS_SUCCESS_SET_BACK_SETTLEMENT);
        sendBroadcast(intentBroad);
    }



    /**
     * 成功删除地址的回调监听
     */
    @Override
    public void delAddrCallBack() {
        getAllAddressFromDb();
    }
}
