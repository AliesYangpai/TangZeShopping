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
import org.tangze.work.entity.temp.TopMyStock;
import org.tangze.work.utils.ImgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/16 0016.
 * 类描述
 * 版本
 */
public class ChildMyStockAdapter extends BaseAdapter {


    private List<TopMyStock> topMyStocks;

    private Context context;

    private LayoutInflater inflater;


    public ChildMyStockAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public List<TopMyStock> getTopMyStocks() {
        return topMyStocks;
    }

    public void setTopMyStocks(List<TopMyStock> topMyStocks) {

        if(null == topMyStocks) {

            topMyStocks = new ArrayList<>();
        }
        this.topMyStocks = topMyStocks;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(null != topMyStocks && topMyStocks.size() > 0) {

            return topMyStocks.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(null != topMyStocks && topMyStocks.size() > 0) {

            return topMyStocks.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    class ViewHolder {

        ImageView iv_stock_head;
        TextView tv_stock_name;
        TextView tv_stock_local_price;
        TextView tv_stock_original_price;
        TextView tv_stock_count;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        TopMyStock topMyStock = topMyStocks.get(position);



        if(convertView == null) {

            viewHolder = new ViewHolder();

            convertView = this.inflater.inflate(R.layout.my_stock_list_item,null);

            viewHolder.iv_stock_head = (ImageView) convertView.findViewById(R.id.iv_product_head);

            viewHolder.tv_stock_name = (TextView) convertView.findViewById(R.id.tv_product_name);

            viewHolder.tv_stock_local_price = (TextView) convertView.findViewById(R.id.tv_local_price);

            viewHolder.tv_stock_original_price = (TextView) convertView.findViewById(R.id.tv_original_price);

            viewHolder.tv_stock_count = (TextView) convertView.findViewById(R.id.tv_stock_count);

            convertView.setTag(viewHolder);

        }else {

            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.tv_stock_local_price.setText(topMyStock.getLocalPrice());

        String text = context.getString(R.string.original_price_text)+context.getString(R.string.money_mark)+
                topMyStock.getOriginal_price()+context.getString(R.string.unit_division);
        viewHolder.tv_stock_original_price.setText(text);
        viewHolder.tv_stock_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        viewHolder.tv_stock_name.setText(topMyStock.getStockName());
        viewHolder.tv_stock_count.setText(topMyStock.getStockCount());

        ImgUtil.getInstance().getImgFromNetByUrl(topMyStock.getThumbnail(), viewHolder.iv_stock_head, R.drawable.img_loadx);


        return convertView;
    }

}
