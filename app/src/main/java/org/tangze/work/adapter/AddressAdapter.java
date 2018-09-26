package org.tangze.work.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;


import com.google.gson.JsonArray;


import org.tangze.work.R;
import org.tangze.work.activity.AddressNewOrEditActivity;
import org.tangze.work.callback.AddressDelCallBack;
import org.tangze.work.callback.AddressSetDefaultCallBack;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Address;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.BooleanUtils;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.StringUtils;
import org.tangze.work.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/6 0006.
 * 类描述   地址管理的Adapter
 * 版本
 */
public class AddressAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    private List<Address> list;


    /**
     * map
     */

    private Map<Integer, Boolean> mSelectMap;


    /**
     * 设置默认地址成功的回调接口
     *
     */

    private AddressSetDefaultCallBack addressSetDefaultCallBack;

    /**
     * 设置删除地址成功的回调接口
     * @param addressSetDefaultCallBack
     */

    private AddressDelCallBack addressDelCallBack;


    public void setAddressDelCallBack(AddressDelCallBack addressDelCallBack) {
        this.addressDelCallBack = addressDelCallBack;
    }

    public void setAddressSetDefaultCallBack(AddressSetDefaultCallBack addressSetDefaultCallBack) {
        this.addressSetDefaultCallBack = addressSetDefaultCallBack;
    }

    public AddressAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.mSelectMap = new HashMap<>();
    }


    public Map<Integer, Boolean> getmSelectMap() {
        return mSelectMap;
    }

    public void setmSelectMap(Map<Integer, Boolean> mSelectMap) {
        this.mSelectMap = mSelectMap;
    }

    private void initSelectState(List<Address> list) {

        if (null != list && list.size() > 0) {


            for (int i = 0; i < list.size(); i++) {

                Address address = list.get(i);

                mSelectMap.put(i, BooleanUtils.getBooleanValue(address.getIsdefault()));

            }

        }

    }

    public List<Address> getList() {
        return list;
    }

    public void setList(List<Address> list) {

        if (list == null) {

            list = new ArrayList<>();

        }
        this.list = list;
        initSelectState(this.list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (null != list && list.size() > 0) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (null != list && list.size() > 0) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final Address address = list.get(position);

        ViewHolder vh = null;

        if (convertView == null) {

            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.address_list_item, null);
            vh.tv_reciever_name = (TextView) convertView.findViewById(R.id.tv_reciever_name);
            vh.tv_reciever_num = (TextView) convertView.findViewById(R.id.tv_reciever_num);
            vh.tv_address_detial = (TextView) convertView.findViewById(R.id.tv_address_detial);
            vh.rb_default = (RadioButton) convertView.findViewById(R.id.rb_default);
            vh.tv_edite_address = (TextView) convertView.findViewById(R.id.tv_edite_address);
            vh.tv_del_address = (TextView) convertView.findViewById(R.id.tv_del_address);

            convertView.setTag(vh);

        } else {

            vh = (ViewHolder) convertView.getTag();

        }


        vh.tv_reciever_name.setText(address.getConsignee());
        vh.tv_reciever_num.setText(address.getTelNum());
        vh.tv_address_detial.setText(address.getArea() +ConstBase.STRING_SPACE+ address.getAddressDetail());
        vh.rb_default.setChecked(this.mSelectMap.get(position));



        vh.rb_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v instanceof RadioButton) {

                    RadioButton rb = (RadioButton) v;
                    Log.i("selectedtMap", "####################################################");
                    if (rb.isChecked()) {

                        for (int i = 0; i < mSelectMap.size(); i++) {

                            if (i == position) {

                                mSelectMap.put(i, rb.isChecked());
                                Log.i("selectedtMap", "当前被选中项:" + i);
                                setToDefaultAddressToUpServer(address);
                            } else {
                                mSelectMap.put(i, !rb.isChecked());
                                Log.i("selectedtMap", "当前其他项:" + i);
                            }

                        }

                        notifyDataSetChanged();


                    }

                }

            }
        });

        final ViewHolder finalVh = vh;
        vh.tv_edite_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AddressNewOrEditActivity.class);

                Bundle bundle = new Bundle();

                address.setIsdefault(BooleanUtils.getStringValue(finalVh.rb_default.isChecked()));

                bundle.putInt(ConstIntent.BundleKEY.ADDRESS, ConstIntent.BundleValue.EDIT_ADDRESS);

                bundle.putSerializable(ConstIntent.BundleKEY.ADDRESS_ENTITY, address);

                intent.putExtras(bundle);

                ((Activity)context).startActivityForResult(intent,ConstIntent.BundleValue.EDIT_ADDRESS);

            }
        });


        vh.tv_del_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delTheAddressToServer(address);

            }
        });


        return convertView;
    }


    /**
     * 执行删除方法，删除该该项
     * @param address
     */
    private void delTheAddressToServer(final Address address) {

        if(address.getIsdefault().equals("1")) {

            ToastUtil.showResMsg(context,R.string.del_defult_address_deny);

        }else {

            /**
             * 执行删除
             */
            Map map = ParaUtils.delUserAddress(address.getAddress_Id());
            HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.DEL_ADDRESS, map, new HttpResultSubscriber<JsonArray>(context) {
                @Override
                public void onSuccess(JsonArray jsonArray) {
                    Log.i(HttpConst.SERVER_BACK, "===删除地址返回成功==" + jsonArray.toString());

                    //执行删除方法
                   int affectRow = address.delete();

                    //执行回调方法
                    if(affectRow == 1 && null != addressDelCallBack) {

                        addressDelCallBack.delAddrCallBack();

                    }
                }

                @Override
                public void onNotSuccess(int resultCode, String resultMessage) {
                    ToastUtil.showMsg(context.getApplicationContext(), resultCode + " " + resultMessage);
                    Log.i(HttpConst.SERVER_BACK, "====删除地址返回失败==" + resultCode + " " + resultMessage);
                }

                @Override
                public void _onError(String error) {
                    ToastUtil.showMsg(context.getApplicationContext(), error);
                    Log.i(HttpConst.SERVER_BACK, "===删除地址返回失败==" + error);
                }
            });

        }


    }

    class ViewHolder {

        TextView tv_reciever_name;
        TextView tv_reciever_num;
        TextView tv_address_detial;
        RadioButton rb_default;
        TextView tv_edite_address;
        TextView tv_del_address;

    }


    /**
     * 设置该地址为默认地址
     * @param address
     */
    private void setToDefaultAddressToUpServer(Address address) {


        int address_id = address.getAddress_Id();

        Map map = ParaUtils.getDefaultAddresId(address_id);
        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.SET_DEFULT_ADDRESS, map, new HttpResultSubscriber<JsonArray>(context){
            @Override
            public void onSuccess(JsonArray jsonArray) {


             boolean result =  HttpReturnParse.getInstance().parseSettingDefaultAddess(jsonArray);

                if(result) {

                    //这里回调一下接口，实现界面关闭

                    if(null != addressSetDefaultCallBack) {

                        addressSetDefaultCallBack.addressSetSuccess(result);

                    }

                }

                Log.i(HttpConst.SERVER_BACK, "===adapter中设置默认地址返回成功==" + jsonArray.toString());


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {
                ToastUtil.showMsg(context.getApplicationContext(),resultCode+" "+resultMessage);
                Log.i(HttpConst.SERVER_BACK, "===dapter中设置默认地址返回失败==" + resultCode + " " + resultMessage);

            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(context.getApplicationContext(),error);
                Log.i(HttpConst.SERVER_BACK, "===dapter中设置默认地址返回失败==" + error);
            }
        });

    }
}
