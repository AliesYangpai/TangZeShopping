package org.tangze.work.widget.addresswidget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.tangze.work.R;
import org.tangze.work.callback.PopAddressCallBack;
import org.tangze.work.widget.addresswidget.adapters.ArrayWheelAdapter;
import org.tangze.work.widget.addresswidget.model.CityModel;
import org.tangze.work.widget.addresswidget.model.DistrictModel;
import org.tangze.work.widget.addresswidget.model.ProvinceModel;
import org.tangze.work.widget.addresswidget.wheels.OnWheelChangedListener;
import org.tangze.work.widget.addresswidget.wheels.WheelView;
import org.tangze.work.widget.addresswidget.wheels.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Administrator on 2016/10/6 0006.
 * 类描述
 * 版本
 */
public class AddressPopwindow extends PopupWindow implements OnClickListener, OnWheelChangedListener {


    private View view;

    private Context context;

    private LayoutInflater inflater;

    /**
     * 控件相关
     * @param context
     */

    /**
     * 所有省
     */
    private String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    private Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    private Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    private String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;
    /**
     * 当前区的名称
     */
    private String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    private String mCurrentZipCode = "";


    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;

    private TextView tv_pop_cancel;
    private TextView tv_pop_sure;


    /**
     * 回调接口
     */

    private PopAddressCallBack popAddressCallBack;


    public void setPopAddressCallBack(PopAddressCallBack popAddressCallBack) {
        this.popAddressCallBack = popAddressCallBack;
    }

    public AddressPopwindow(Context context) {
        super(context);
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        view = this.inflater.inflate(R.layout.popwindow_wheel, null);


        initView();
        initListener();
        initData();
    }

    public AddressPopwindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public AddressPopwindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;


    }


    private void initView() {
        mViewProvince = (WheelView) view.findViewById(R.id.id_province);
        mViewCity = (WheelView) view.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) view.findViewById(R.id.id_district);

        tv_pop_cancel = (TextView) view.findViewById(R.id.tv_pop_cancel);
        tv_pop_sure = (TextView) view.findViewById(R.id.tv_pop_sure);


        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }


    private void initListener() {
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);
        tv_pop_cancel.setOnClickListener(this);
        tv_pop_sure.setOnClickListener(this);
    }


    private void initData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }


    /**
     * 解析省市区的XML数据
     */

    private void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = this.context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mCurrentDistrictName = areas[0];
        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);

        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
        mViewDistrict.setCurrentItem(0);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_pop_cancel:
                this.dismiss();
                break;


            case R.id.tv_pop_sure:

                if(null !=popAddressCallBack) {

                    popAddressCallBack.addressPickedCallBack(mCurrentProviceName,mCurrentCityName,mCurrentDistrictName);

                }
                this.dismiss();
                break;

        }
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    private void showSelectedResult() {


    }


}
