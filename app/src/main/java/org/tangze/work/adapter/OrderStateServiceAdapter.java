package org.tangze.work.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.tangze.work.R;
import org.tangze.work.activity.OrderShowActivity;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Address;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.temp.ProductChildServerBack;
import org.tangze.work.entity.temp.ProductOrder;
import org.tangze.work.entity.temp.ProductServerBackOrder;
import org.tangze.work.utils.ObjectUtils;
import org.tangze.work.widget.childlistView.SingleOrderChildListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/23 0023.
 * 类描述  售后中的adpter
 * 版本
 */
public class OrderStateServiceAdapter extends BaseAdapter {


    private Context context;

    private LayoutInflater inflater;

    private List<ProductServerBackOrder> list;

    private int currentOrderState; //当前订单状态


    public OrderStateServiceAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public List<ProductServerBackOrder> getList() {
        return list;
    }

    public void setList(List<ProductServerBackOrder> list) {
        if(null == list) {

            list = new ArrayList<>();

        }
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(null != list && list.size() > 0) {

            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(null != list && list.size() > 0) {

            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;


        final ProductServerBackOrder productServerBackOrder = list.get(position);

        if(convertView == null) {


            convertView = this.inflater.inflate(R.layout.order_state_layout,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_order_number = (TextView) convertView.findViewById(R.id.tv_order_number);
            viewHolder.lv_order_child = (SingleOrderChildListView) convertView.findViewById(R.id.lv_order_child);
            viewHolder.tv_total_in_order = (TextView) convertView.findViewById(R.id.tv_total_in_order);
            viewHolder.rl_layout_order_child_bottom = (RelativeLayout) convertView.findViewById(R.id.rl_layout_order_child_bottom);
            viewHolder.tv_common_1 = (TextView) convertView.findViewById(R.id.tv_common_1);
            viewHolder.tv_common_2 = (TextView) convertView.findViewById(R.id.tv_common_2);


            convertView.setTag(viewHolder);
        }else {

            viewHolder = (ViewHolder) convertView.getTag();

        }
        /**
         * 设置数据
         */

        //当前订单状态
        currentOrderState = productServerBackOrder.getState();




        //设置单号
        viewHolder.tv_order_number.setText(productServerBackOrder.getOrder_num());

        //设置总价
        viewHolder.tv_total_in_order.setText(productServerBackOrder.getTotalprice());
        //底部相关更新



        //界面更新
        updateBottomUiByOrderState(viewHolder.tv_common_1, viewHolder.tv_common_2);

        //点击事件更新




        //设置中间ChildlistView数据

        final List<ProductChildServerBack> listChild = ObjectUtils.getListData(productServerBackOrder.getContent());

        setDataToChildListView(viewHolder.lv_order_child,listChild);


        viewHolder.lv_order_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, OrderShowActivity.class);
                Bundle bundle = new Bundle();
                ProductOrder productOrder = getProductOrder(listChild, productServerBackOrder);
                bundle.putSerializable(ConstIntent.BundleKEY.PRODUCT_ORDER_TO_SHOW, productOrder);
                bundle.putInt(ConstIntent.BundleKEY.PRODUCT_ORDER_SHOW_TYPE,ConstIntent.BundleValue.MY_ORDER_TO_SHOW);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderShowActivity.class);
                Bundle bundle = new Bundle();
                ProductOrder productOrder = getProductOrder(listChild, productServerBackOrder);
                bundle.putSerializable(ConstIntent.BundleKEY.PRODUCT_ORDER_TO_SHOW, productOrder);
                bundle.putInt(ConstIntent.BundleKEY.PRODUCT_ORDER_SHOW_TYPE,ConstIntent.BundleValue.MY_ORDER_TO_SHOW);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


        return convertView;
    }




    /**
     * 得到当前订单productOrder
     *
     * @return
     */
    private ProductOrder getProductOrder(List<ProductChildServerBack> listChild, ProductServerBackOrder productServerBackOrder) {

        //wen订单提交新修改
        List<Integer> productIds = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        Map<Integer, Integer> mapBuyCount = new HashMap<>();
        for (int i = 0; i < listChild.size(); i++) {

            ProductChildServerBack productChildServerBack = listChild.get(i);

            int product_id = Integer.valueOf(productChildServerBack.getProduct_id());

            productIds.add(product_id);

            mapBuyCount.put(product_id, Integer.valueOf(productChildServerBack.getBuyCount()));


            Product product = new Product();
            product.setProduct_id(Integer.valueOf(productChildServerBack.getProduct_id()));
            product.setProductName(productChildServerBack.getProduct_name());
            product.setOriginalPrice(productChildServerBack.getOriginal_price());
            product.setLocalPrice(productChildServerBack.getLocal_price());
            product.setThumnail(productChildServerBack.getThumnail());
            products.add(product);

        }

        /**
         * 设置地址
         */
        String detial = productServerBackOrder.getAddress();
        String consignee = productServerBackOrder.getConsignee();
        String tel = productServerBackOrder.getTel();
        Address address = new Address();
        address.setAddressDetail(detial);
        address.setConsignee(consignee);
        address.setTelNum(tel);

        ProductOrder productOrder = new ProductOrder();
        productOrder.setAddressDefault(address);
        productOrder.setOrder_number(productServerBackOrder.getOrder_num());
        productOrder.setProducts_transmit(products); //设置产品对象集合
        productOrder.setMap(mapBuyCount); //这里是关键
        productOrder.setRemarks(productServerBackOrder.getRmarks());


        return productOrder;

    }
    /**
     * 更新底部按钮
     * @param tv1
     * @param tv2
     */
    private void updateBottomUiByOrderState(TextView tv1,TextView tv2) {
        tv2.setVisibility(View.GONE);
        tv1.setVisibility(View.GONE);

    }


    /**
     * 设置子listView的数据
     * @param lv_order_child
     * @param listChild
     */
    private void setDataToChildListView(SingleOrderChildListView lv_order_child,List<ProductChildServerBack> listChild) {



        if(null != listChild && listChild.size() > 0) {

            OrderStateChildAdapter orderStateChildAdapter = new OrderStateChildAdapter(this.context, listChild);

            lv_order_child.setAdapter(orderStateChildAdapter);

        }
    }





    class ViewHolder {

        public TextView tv_order_number;

        public SingleOrderChildListView lv_order_child;

        //下部布局


        public TextView tv_total_in_order; //合计的价格

        public RelativeLayout rl_layout_order_child_bottom;

        public TextView tv_common_1; //右起第1个

        public TextView tv_common_2; //右起第2个



    }
}
