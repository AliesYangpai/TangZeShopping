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
import org.tangze.work.entity.Product;
import org.tangze.work.utils.ImgUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/25 0025.
 * 类描述  展示订单详情的adapter'
 * 版本
 */
public class OrderShowAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    private Map<Integer,Integer> mapBuyCount;


    private List<Product> products;

    public OrderShowAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }



    public void setDatas(List<Product> products,Map mapBuyCount) {
        if(null == products) {
            products = new ArrayList<>();
            mapBuyCount= new HashMap();
        }
        this.products = products;
        this.mapBuyCount = mapBuyCount;
        this.notifyDataSetChanged();
    }




    @Override
    public int getCount() {
        if(null != products && products.size() > 0) {

            return products.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(null != products && products.size() > 0) {

            return products.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    class ViewHolder {

        ImageView iv_product_head;
        TextView tv_product_name;
        TextView tv_local_price;
        TextView tv_original_price;
        TextView tv_product_count;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        Product product = products.get(position);

        if(convertView == null) {

            viewHolder = new ViewHolder();

            convertView = this.inflater.inflate(R.layout.settlement_list_item,null);

            viewHolder.iv_product_head = (ImageView) convertView.findViewById(R.id.iv_product_head);

            viewHolder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);

            viewHolder.tv_local_price = (TextView) convertView.findViewById(R.id.tv_local_price);

            viewHolder.tv_original_price = (TextView) convertView.findViewById(R.id.tv_original_price);

            viewHolder.tv_product_count = (TextView) convertView.findViewById(R.id.tv_product_count);

            convertView.setTag(viewHolder);

        }else {

            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.tv_local_price.setText(product.getLocalPrice());

        String text = context.getString(R.string.original_price_text)+context.getString(R.string.money_mark)+
                product.getOriginalPrice()+context.getString(R.string.unit_division);
        viewHolder.tv_original_price.setText(text);
        viewHolder.tv_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        viewHolder.tv_product_name.setText(product.getProductName());


        viewHolder.tv_product_count.setText(String.valueOf(mapBuyCount.get(product.getProduct_id())));

        ImgUtil.getInstance().getImgFromNetByUrl(product.getThumnail(), viewHolder.iv_product_head, R.drawable.img_loadx);



        return convertView;
    }

}
