package org.tangze.work.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import org.tangze.work.R;
import org.tangze.work.activity.ProductDetialActivity;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Product;
import org.tangze.work.utils.ImgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/10 0010.
 * 类描述
 * 版本
 */
public class CriteriaAdapter extends BaseAdapter {


    private List<Product> products;

    private Context context;

    private LayoutInflater inflater;


    public CriteriaAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        if(null == products) {
            products = new ArrayList<>();

        }
        this.products = products;
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

        ImageView iv_criteria_product_head;
        TextView tv_criteria_product_name;
        TextView tv_criteria_local_price;
        TextView tv_criteria_original_price;
        LinearLayout ll_criteria_purchase;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        final Product product = products.get(position);



        if(convertView == null) {

            viewHolder = new ViewHolder();

            convertView = this.inflater.inflate(R.layout.criteria_item,null);

            viewHolder.iv_criteria_product_head = (ImageView) convertView.findViewById(R.id.iv_criteria_product_head);

            viewHolder.tv_criteria_product_name = (TextView) convertView.findViewById(R.id.tv_criteria_product_name);

            viewHolder.tv_criteria_local_price = (TextView) convertView.findViewById(R.id.tv_criteria_local_price);

            viewHolder.tv_criteria_original_price = (TextView) convertView.findViewById(R.id.tv_criteria_original_price);

            viewHolder.ll_criteria_purchase = (LinearLayout) convertView.findViewById(R.id.ll_criteria_purchase);

            convertView.setTag(viewHolder);

        }else {

            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.tv_criteria_local_price.setText(product.getLocalPrice());

        String text = context.getString(R.string.original_price_text)+context.getString(R.string.money_mark)+
                product.getOriginalPrice();
        viewHolder.tv_criteria_original_price.setText(text);
        viewHolder.tv_criteria_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        viewHolder.tv_criteria_product_name.setText(product.getProductName());


        ImgUtil.getInstance().getImgFromNetByUrl(product.getThumnail(), viewHolder.iv_criteria_product_head, R.drawable.img_loadx);

        /**
         * 相关点击事件
         */
        viewHolder.ll_criteria_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConstIntent.BundleKEY.PRODUCT_TO_DETIAL,product);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConstIntent.BundleKEY.PRODUCT_TO_DETIAL,product);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


        Log.i("saleCount", " 销量：" + product.getSalesVolume());

        return convertView;
    }
}
