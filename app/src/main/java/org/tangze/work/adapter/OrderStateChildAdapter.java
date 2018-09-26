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
import org.tangze.work.entity.temp.ProductChildServerBack;
import org.tangze.work.utils.ImgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/22 0022.
 * 类描述  订单列表中的子listView 所在adapter
 * 版本
 */
public class OrderStateChildAdapter extends BaseAdapter {



    private Context context;

    private LayoutInflater inflater;

    private List<ProductChildServerBack> list;


    public OrderStateChildAdapter(Context context, List<ProductChildServerBack> list) {
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
        ProductChildServerBack productChildServerBack = list.get(position);
        if(convertView == null) {

            convertView = this.inflater.inflate(R.layout.order_child_list_item, null);
            viewHolder = new ViewHolder();

            viewHolder.iv_order_child_product_head = (ImageView) convertView.findViewById(R.id.iv_order_child_product_head);
            viewHolder.tv_order_child_product_name = (TextView) convertView.findViewById(R.id.tv_order_child_product_name);
            viewHolder.tv_order_child_local_price = (TextView) convertView.findViewById(R.id.tv_order_child_local_price);
            viewHolder.tv_order_child_original_price = (TextView) convertView.findViewById(R.id.tv_order_child_original_price);
            viewHolder.tv_order_child_product_count = (TextView) convertView.findViewById(R.id.tv_order_child_product_count);

            convertView.setTag(viewHolder);
        }else {


            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.tv_order_child_product_name.setText(productChildServerBack.getProduct_name());
        viewHolder.tv_order_child_local_price.setText(productChildServerBack.getLocal_price());
        viewHolder.tv_order_child_product_count.setText(productChildServerBack.getBuyCount());

        String text = context.getString(R.string.original_price_text)+context.getString(R.string.money_mark)+
                productChildServerBack.getOriginal_price()+context.getString(R.string.unit_division);
        viewHolder.tv_order_child_original_price.setText(text);
        viewHolder.tv_order_child_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        ImgUtil.getInstance().getImgFromNetByUrl(productChildServerBack.getThumnail(), viewHolder.iv_order_child_product_head, R.drawable.img_loadx);


        return convertView;
    }


    class  ViewHolder {

        ImageView iv_order_child_product_head;


        TextView tv_order_child_product_name;
        TextView tv_order_child_local_price;
        TextView tv_order_child_original_price;
        TextView tv_order_child_product_count;

    }
}
