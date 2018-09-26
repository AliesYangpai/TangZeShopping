package org.tangze.work.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.adapter.OrderShowAdapter;
import org.tangze.work.adapter.SettlementAdapter;
import org.tangze.work.constant.ConstAddress;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Address;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.temp.ProductOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 显示配货订单或我的订单的详情界面
 */
public class OrderShowActivity extends BaseActivity implements OnClickListener {


    /**
     * title
     */
    private ImageView iv_order_show_back;
    private TextView tv_order_show_title;
    private ImageView iv_common_search;


    /**
     * 上部订单号
     */
    private TextView tv_order_number;
    /**
     * 上部默认地址
     */
    private TextView tv_reciver_name_in_settlement;
    private TextView tv_reciver_number_in_settlement;
    private TextView tv_reciever_address_detai_in_settlement;
    private LinearLayout ll_address_in_settlement; //地址所在布局
    private TextView tv_text_address_for_set;//设置收货地址（这个应该被隐藏）
    private ImageView iv_right_narrow;
    /**
     * 中间listView
     */
    private OrderShowAdapter orderShowAdapter;
    private ListView lv_product_to_order;


    /**
     * listView下面的总数
     */
    private TextView tv_product_count;
    /**
     * 下部订单备注
     */
    private TextView tv_has_remark;

    /**
     * 底部提交
     */
    private TextView tv_total_all_price;
    private LinearLayout ll_deliver_order_in_settlement;


    /**
     * 相关数据
     */

    private Intent intent;
    private Bundle bundle;
    private ProductOrder productOrder;
    private int currentFlag;  //0:我的订单详情，1配货订单详情


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_show);
        initData();

    }

    void initData() {
        /**
         * 1.设置地址
         * 2.设置product产品列表
         * 3.设置一共多少商品
         * 4.设置下部备注
         * 5.显示合计价格（即总计价格）
         */

        /**
         * 1.设置订单号和地址：
         */
        setAddressAndOrderNumberToUi();
        /**
         * 2.设置product产品列表
         * 调整界面
         */
        setProductListToUi();

        /**
         * 3.设置一共买了多少商品
         */
        setBuyCountToUi();

        /**
         * 4设置下部备注
         */
        setRemakToUi();

        /**
         * 5.显示合计价格（即总计价格）
         */
        setTotalPriceToUi();
    }


    /**
     * 设置默认地址到UI
     */
    private void setAddressAndOrderNumberToUi() {



        tv_order_number.setText(productOrder.getOrder_number());
        Address address = productOrder.getAddressDefault();
        tv_reciver_name_in_settlement.setText(address.getConsignee());
        tv_reciver_number_in_settlement.setText(address.getTelNum());
        tv_reciever_address_detai_in_settlement.setText(address.getAddressDetail());

    }

    /**
     * 设置product产品列表
     */
    private void setProductListToUi() {


//wen订单提交新修改


        List<Product> products = productOrder.getProducts_transmit();


        Map<Integer, Integer> map = productOrder.getMap();

        orderShowAdapter.setDatas(products, map);
        adajustListView();
    }

    /**
     * 设置一共要购买了多少商品
     */
    private void setBuyCountToUi() {






        //wen订单提交新修改
        int count = 0;

        List<Product> product_transmit = productOrder.getProducts_transmit();

        Map<Integer,Integer> map = productOrder.getMap();
        if(null != product_transmit && product_transmit.size() > 0) {

            for(int i = 0 ; i < product_transmit.size() ; i ++) {

                count+=map.get(product_transmit.get(i).getProduct_id());

            }

        }
        tv_product_count.setText(String.valueOf(count));
    }

    /**
     * 设置底部备注
     */

    private void setRemakToUi() {

        tv_has_remark.setText(productOrder.getRemarks());
    }

    /**
     * 设置购买总价到界面ui
     */

    private void setTotalPriceToUi() {


        //wen订单提交新修改

        float totalPrice = 0;
        List<Product> product_transmit = productOrder.getProducts_transmit();
        Map<Integer,Integer> map = productOrder.getMap();


        for(int i = 0 ; i < product_transmit.size() ; i ++) {

            Product product = product_transmit.get(i);

            int num = map.get(product.getProduct_id()); //得到对应商品的购买数量


            float singleLocalPrice = Float.valueOf(product.getLocalPrice());

            totalPrice += num*singleLocalPrice;

        }
        productOrder.setTotalprice(String.valueOf(totalPrice));
        tv_total_all_price.setText(String.valueOf(totalPrice));

    }

    /**
     * 调整界面冲突
     */
    private void adajustListView() {

        setListViewHeightBasedOnChildren(lv_product_to_order); //ListView适应ScrollView

        //解决进入界面后，srollview自动滑动到下部的listView，listView上面的数据不再顶部
        ll_address_in_settlement.setFocusable(true);
        ll_address_in_settlement.setFocusableInTouchMode(true);
        ll_address_in_settlement.requestFocus();
    }


    /**
     * 解决listView嵌套与ScrollView中的异常问题
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        OrderShowAdapter listAdapter = (OrderShowAdapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    protected void initViews() {

        /**
         * title
         */
        iv_order_show_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_order_show_title = (TextView) findViewById(R.id.tv_common_title);
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_search.setVisibility(View.GONE);


        switch (currentFlag) {

            case ConstIntent.BundleValue.MY_ORDER_TO_SHOW:
                tv_order_show_title.setText(R.string.my_order_detal);
                break;

            case ConstIntent.BundleValue.GIVE_GOODS_ORDER_TO_SHOW:
                tv_order_show_title.setText(R.string.giveGoods_order_detial);
                break;

        }


        /**
         * 上部分订单
         */

        tv_order_number = (TextView) findViewById(R.id.tv_order_number);
        /**
         * 上部默认地址
         */
        tv_reciver_name_in_settlement = (TextView) findViewById(R.id.tv_reciver_name_in_settlement);
        tv_reciver_number_in_settlement = (TextView) findViewById(R.id.tv_reciver_number_in_settlement);
        tv_reciever_address_detai_in_settlement = (TextView) findViewById(R.id.tv_reciever_address_detai_in_settlement);
        ll_address_in_settlement = (LinearLayout) findViewById(R.id.ll_address_in_settlement);
        tv_text_address_for_set = (TextView) findViewById(R.id.tv_text_address_for_set);
        tv_text_address_for_set.setVisibility(View.GONE);
        iv_right_narrow = (ImageView) findViewById(R.id.iv_right_narrow); //右边的箭头
        iv_right_narrow.setVisibility(View.GONE);
        /**
         * 中间listView
         */
        lv_product_to_order = (ListView) findViewById(R.id.lv_product_to_order);
        orderShowAdapter = new OrderShowAdapter(this);
        lv_product_to_order.setAdapter(orderShowAdapter);


        tv_product_count = (TextView) findViewById(R.id.tv_product_count_in_footView);


        /**
         * 底部订单备注
         */
        tv_has_remark = (TextView) findViewById(R.id.tv_has_remark);

        /**
         * 底部提交
         */
        tv_total_all_price = (TextView) findViewById(R.id.tv_total_all_price);
        ll_deliver_order_in_settlement = (LinearLayout) findViewById(R.id.ll_deliver_order_in_settlement);
        ll_deliver_order_in_settlement.setVisibility(View.GONE);


    }

    @Override
    protected void initListener() {

        /**
         * title
         */
        iv_order_show_back.setOnClickListener(this);
    }

    @Override
    protected void processIntent() {
        intent = this.getIntent();
        if (null != intent) {

            bundle = intent.getExtras();

            if (null != bundle) {

                productOrder = (ProductOrder) bundle.getSerializable(ConstIntent.BundleKEY.PRODUCT_ORDER_TO_SHOW);

                currentFlag = bundle.getInt(ConstIntent.BundleKEY.PRODUCT_ORDER_SHOW_TYPE);
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
