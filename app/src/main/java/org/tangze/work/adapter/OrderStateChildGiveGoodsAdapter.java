package org.tangze.work.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.tangze.work.R;
import org.tangze.work.Test.TestOrderChild;
import org.tangze.work.utils.ImgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/22 0022.
 * 类描述
 * 版本
 */
public class OrderStateChildGiveGoodsAdapter extends BaseAdapter {
    private Context context;

    private LayoutInflater inflater;

    private List<TestOrderChild> list;


    public OrderStateChildGiveGoodsAdapter(Context context, List<TestOrderChild> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);

        if(null == list) {

            list = new ArrayList<>();
        }
        this.list = list;
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
        TestOrderChild testOrderChild = list.get(position);
        if(convertView == null) {

            convertView = this.inflater.inflate(R.layout.order_child_list_item, null);
            viewHolder = new ViewHolder();

            viewHolder.iv_order_child_product_head_in_give_goods = (ImageView) convertView.findViewById(R.id.iv_order_child_product_head);
            viewHolder.tv_order_child_product_name_give_goods = (TextView) convertView.findViewById(R.id.tv_order_child_product_name);
            viewHolder.tv_order_child_local_price_give_goods = (TextView) convertView.findViewById(R.id.tv_order_child_local_price);
            viewHolder.tv_order_child_original_price_give_goods = (TextView) convertView.findViewById(R.id.tv_order_child_original_price);
            viewHolder.tv_order_child_product_count_give_goods = (TextView) convertView.findViewById(R.id.tv_order_child_product_count);

            convertView.setTag(viewHolder);
        }else {


            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.tv_order_child_product_name_give_goods.setText(testOrderChild.getName());
        viewHolder.tv_order_child_local_price_give_goods.setText(testOrderChild.getLocal_price());
        viewHolder.tv_order_child_product_count_give_goods.setText(testOrderChild.getBuyCount());

        String text = context.getString(R.string.original_price_text)+context.getString(R.string.money_mark)+
                testOrderChild.getOriginal_price()+context.getString(R.string.unit_division);
        viewHolder.tv_order_child_original_price_give_goods.setText(text);
        viewHolder.tv_order_child_original_price_give_goods.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        ImgUtil.getInstance().getImgFromNetByUrl(testOrderChild.getThumnail(), viewHolder.iv_order_child_product_head_in_give_goods, R.drawable.img_loadx);


        return convertView;
    }

    class  ViewHolder {

        ImageView iv_order_child_product_head_in_give_goods;


        TextView tv_order_child_product_name_give_goods;
        TextView tv_order_child_local_price_give_goods;
        TextView tv_order_child_original_price_give_goods;
        TextView tv_order_child_product_count_give_goods;

    }
}
