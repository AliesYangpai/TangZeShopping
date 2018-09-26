package org.tangze.work.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.Test.AddressEntity;
import org.tangze.work.callback.PopAddressCallBack;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Address;
import org.tangze.work.entity.User;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.BooleanUtils;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.addresswidget.AddressPopwindow;

import java.util.List;
import java.util.Map;

public class AddressNewOrEditActivity extends BaseActivity implements View.OnClickListener, PopAddressCallBack {


    /**
     * title
     */
    private ImageView iv_addr_new_back;
    private TextView tv_addr_new_title;
    private ImageView iv_common_search;
    /**
     * 中间
     */
    private EditText et_reciever_name;
    private EditText et_reciever_num_in_new;
    private RelativeLayout rl_reciever_area_in_new; //底部弹出popwidow
    private TextView tv_reciever_area_in_new;
    private EditText et_reciever_addr_detail_new;
    private CheckBox cb_setToDefault;

    /**
     * 底部
     */
    private TextView tv_activity_bottom_common;


    /**
     * 携带数据相关
     */

    private Intent intent;

    private Bundle bundle;

    private int requestCode;

    private Address address;

    private User user; //用于新添加地址时候得到 userid；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add_new);
        initdata();
    }


    private void initdata() {


        user = DataSupport.findFirst(User.class);

        if (requestCode == ConstIntent.BundleValue.ADD_NEW_ADDRESS) {

            tv_addr_new_title.setText(getString(R.string.address_add_new));

        } else if (requestCode == ConstIntent.BundleValue.EDIT_ADDRESS) {
            tv_addr_new_title.setText(getString(R.string.address_edit_title));
            address = (Address) bundle.get(ConstIntent.BundleKEY.ADDRESS_ENTITY);
            et_reciever_name.setText(address.getConsignee());
            et_reciever_num_in_new.setText(address.getTelNum());
            tv_reciever_area_in_new.setText(address.getArea());
            et_reciever_addr_detail_new.setText(address.getAddressDetail());
            cb_setToDefault.setChecked(BooleanUtils.getBooleanValue(address.getIsdefault()));
        }

    }

    @Override
    protected void initViews() {
        /**
         * title
         *
         */
        iv_addr_new_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_addr_new_title = (TextView) findViewById(R.id.tv_common_title);
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_search.setVisibility(View.GONE);


        /**
         * 中间
         *
         */
        et_reciever_name = (EditText) findViewById(R.id.et_reciever_name);
        et_reciever_num_in_new = (EditText) findViewById(R.id.et_reciever_num_in_new);
        et_reciever_num_in_new.setInputType(InputType.TYPE_CLASS_PHONE);
        et_reciever_num_in_new.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        rl_reciever_area_in_new = (RelativeLayout) findViewById(R.id.rl_reciever_area_in_new); //底部弹出popwidow
        tv_reciever_area_in_new = (TextView) findViewById(R.id.tv_reciever_area_in_new);
        et_reciever_addr_detail_new = (EditText) findViewById(R.id.et_reciever_addr_detail_new);
        cb_setToDefault = (CheckBox) findViewById(R.id.cb_setToDefault);


        /**
         * 如果是编辑地址，设置默认需要隐藏
         * 如果是添加地址，则设置默认显示
         */
        if (requestCode == ConstIntent.BundleValue.ADD_NEW_ADDRESS) {
            cb_setToDefault.setVisibility(View.VISIBLE);
        } else if (requestCode == ConstIntent.BundleValue.EDIT_ADDRESS) {
            cb_setToDefault.setVisibility(View.GONE);
        }


        /**
         * 底部
         */
        tv_activity_bottom_common = (TextView) findViewById(R.id.tv_activity_bottom_common);
        tv_activity_bottom_common.setText(getString(R.string.address_new_sure));
    }

    @Override
    protected void initListener() {
        /**
         * title
         *
         */
        iv_addr_new_back.setOnClickListener(this);
        tv_addr_new_title.setOnClickListener(this);
        tv_addr_new_title.setText("添加新地址或者编辑地址");

        /**
         * 中间
         */
        rl_reciever_area_in_new.setOnClickListener(this);
        /**
         * 底部
         */
        tv_activity_bottom_common.setOnClickListener(this);
    }

    @Override
    protected void processIntent() {
        intent = this.getIntent();
        if (null != intent) {

            bundle = this.intent.getExtras();


            if (null != bundle) {
                this.requestCode = bundle.getInt(ConstIntent.BundleKEY.ADDRESS);
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_common_back:
                newAddressFinishAndBack(ConstIntent.ResponseCode.ADD_NEW_ADDRESS_SUCCESS_NOT_DEFUALT);
                break;

            case R.id.rl_reciever_area_in_new:
                showAreaPopWindow();
                break;

            case R.id.tv_activity_bottom_common:

                addNewAddressToServer();

                break;

        }
    }


    private void showAreaPopWindow() {

        AddressPopwindow pw = new AddressPopwindow(this);
        pw.showAtLocation(et_reciever_name, Gravity.BOTTOM, 0, 0);
        pw.setPopAddressCallBack(this);

    }

    @Override
    public void addressPickedCallBack(String province, String city, String district) {


        String str = province + "," + city + "," + district;

        tv_reciever_area_in_new.setText(str);
    }


    private void addNewAddressToServer() {

        final String consignee = et_reciever_name.getText().toString();
        final String area = tv_reciever_area_in_new.getText().toString();
        final String detailed = et_reciever_addr_detail_new.getText().toString();
        final String tel_num = et_reciever_num_in_new.getText().toString();
        if (requestCode == ConstIntent.BundleValue.ADD_NEW_ADDRESS) { //添加新地址

            int user_id = user.getUser_id();
            final boolean isCheck = cb_setToDefault.isChecked();
            String defaultcheck = "0";
            if (isCheck) {
                defaultcheck = "1";
            }

            Map map = ParaUtils.addNewUserAddress(user_id, consignee, area, detailed, tel_num, defaultcheck);
            HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ADD_NEW_USER_ADDRESS, map, new HttpResultSubscriber<JsonArray>(this) {
                @Override
                public void onSuccess(JsonArray jsonArray) {
                    int newAddressId = HttpReturnParse.getInstance().parseAddNewAddressBackAndSave(jsonArray);
                    /**
                     * 说明成功保存
                     */
                    if (newAddressId != -1) {
                        List<Address> addresses = DataSupport.where("address_Id = ?", String.valueOf(newAddressId)).find(Address.class);
                        Address address = addresses.get(0);
                        boolean isDefault = BooleanUtils.getBooleanValue(address.getIsdefault());
                        if (isDefault) {

                            /**
                             * 添加地址成功，并设置为默认地址的返回
                             */
                            newAddressFinishAndBack(ConstIntent.ResponseCode.ADD_NEW_ADDRESS_SUCCESS_DEFUALT);
                        } else {

                            newAddressFinishAndBack(ConstIntent.ResponseCode.ADD_NEW_ADDRESS_SUCCESS_NOT_DEFUALT);
                        }

                    } else {

                        ToastUtil.showMsg(getApplicationContext(), "返回的地址id异常");
                    }



                    Log.i(HttpConst.SERVER_BACK, "===添加新地址返回成功==" + jsonArray.toString());
                }

                @Override
                public void onNotSuccess(int resultCode, String resultMessage) {
                    ToastUtil.showMsg(getApplicationContext(), resultCode + " " + resultMessage);
                    Log.i(HttpConst.SERVER_BACK, "===添加新地址返回失败==" + resultCode + " " + resultMessage);


                }

                @Override
                public void _onError(String error) {

                    ToastUtil.showMsg(getApplicationContext(), error);
                    Log.i(HttpConst.SERVER_BACK, "===添加新地址返回失败==" + error);
                }
            });


        } else if (requestCode == ConstIntent.BundleValue.EDIT_ADDRESS) {
            /**
             * 编辑新地址
             */
            int addressId = address.getAddress_Id();

            Map map = ParaUtils.editUserAddress(addressId, consignee, area, detailed, tel_num);

            HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.EDIT_ADDRESS, map, new HttpResultSubscriber<JsonArray>() {

                @Override
                public void onSuccess(JsonArray jsonArray) {

                    Log.i(HttpConst.SERVER_BACK, "===编辑地址地址返回成功==" + jsonArray.toString());

                    boolean result = HttpReturnParse.getInstance().parseEditAddessBackAndSave(jsonArray, consignee, area, detailed, tel_num);


                    if(result) {
                        //这里，不管是不是默认地址，都调用这个方法来实现界面变化
                        newAddressFinishAndBack(ConstIntent.ResponseCode.EDIT_ADDRESS_SUCCESS);

                    }else {

                        ToastUtil.showMsg(getApplicationContext(), "地址更新失败");
                    }

                }

                @Override
                public void onNotSuccess(int resultCode, String resultMessage) {
                    ToastUtil.showMsg(getApplicationContext(), resultCode + " " + resultMessage);
                    Log.i(HttpConst.SERVER_BACK, "===添加新地址返回失败==" + resultCode + " " + resultMessage);

                }

                @Override
                public void _onError(String error) {
                    ToastUtil.showMsg(getApplicationContext(), error);
                    Log.i(HttpConst.SERVER_BACK, "===添加新地址返回失败==" + error);
                }
            });

        }


//        int user_id = user.getUser_id();
//        String consignee  =et_reciever_name.getText().toString();
//
//        String area = tv_reciever_area_in_new.getText().toString();
//        String detailed = et_reciever_addr_detail_new.getText().toString();
//        String tel_num = et_reciever_num_in_new.getText().toString();
//
//        final boolean isCheck = cb_setToDefault.isChecked();
//
//        String defaultcheck = "0";
//        if(isCheck) {
//            defaultcheck = "1";
//        }
//
//
//        Map map = ParaUtils.addNewUserAddress(user_id,consignee,area,detailed,tel_num,defaultcheck);
//        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ADD_NEW_USER_ADDRESS, map, new HttpResultSubscriber<JsonArray>(this) {
//            @Override
//            public void onSuccess(JsonArray jsonArray) {
//
//                int newAddressId = HttpReturnParse.getInstance().parseAddNewAddressBackAndSave(jsonArray);
//
//
//                /**
//                 * 说明成功保存
//                 */
//                if (newAddressId != -1) {
//
//
//                    List<Address> addresses = DataSupport.where("address_Id = ?", String.valueOf(newAddressId)).find(Address.class);
//
//                    Address address = addresses.get(0);
//
//                    boolean isDefault = BooleanUtils.getBooleanValue(address.getIsdefault());
//
//
//                    if (isDefault) {
//
//                        /**
//                         * 添加地址成功，并设置为默认地址的返回
//                         */
//                        newAddressFinishAndBack(ConstIntent.ResponseCode.ADD_NEW_ADDRESS_SUCCESS_DEFUALT);
//
//
//                    } else {
//
//                        newAddressFinishAndBack(ConstIntent.ResponseCode.ADD_NEW_ADDRESS_SUCCESS_NOT_DEFUALT);
//                    }
//
//                } else {
//
//                    ToastUtil.showMsg(AddressNewOrEditActivity.this, "返回的地址id异常");
//                }
//
//
//                Log.i(HttpConst.SERVER_BACK, "===添加新地址返回成功==" + jsonArray.toString());
//            }
//
//            @Override
//            public void onNotSuccess(int resultCode, String resultMessage) {
//                ToastUtil.showMsg(AddressNewOrEditActivity.this, resultCode + " " + resultMessage);
//                Log.i(HttpConst.SERVER_BACK, "===添加新地址返回失败==" + resultCode + " " + resultMessage);
//
//
//            }
//
//            @Override
//            public void _onError(String error) {
//
//                ToastUtil.showMsg(AddressNewOrEditActivity.this, error);
//                Log.i(HttpConst.SERVER_BACK, "===添加新地址返回失败==" + error);
//            }
//        });


    }


    private void newAddressFinishAndBack(int responsesCode) {


        AddressNewOrEditActivity.this.setResult(responsesCode);

        AddressNewOrEditActivity.this.finish();

    }

    @Override
    public void onBackPressed() {

    }
}
