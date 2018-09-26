package org.tangze.work.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.ShoppingCar;
import org.tangze.work.utils.ImgUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/7 0007.
 * 类描述
 * 版本
 */
public class ShoppingCarAdapter extends BaseAdapter {


    private Context context;

    private LayoutInflater inflater;

    private List<ShoppingCar> list;

    private Map<Integer,Boolean> mSelectMap ;//wen批量删除，加入Map


//    private ViewHolder vh;


    public ShoppingCarAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        mSelectMap = new HashMap<>();
    }



    public Map<Integer, Boolean> getmSelectMap() {
        return mSelectMap;
    }

    public void setmSelectMap(Map<Integer, Boolean> mSelectMap) {
        this.mSelectMap = mSelectMap;
    }


    private void initSelectState() {


        mSelectMap.clear();

        if(null != list && list.size() > 0) {

            for(int i = 0 ; i <list.size() ; i ++) {

                mSelectMap.put(i,false);

            }

        }

    }

    public List<ShoppingCar> getList() {
        return list;
    }

    public void setList(List<ShoppingCar> list) {
        if (list == null) {

            list = new ArrayList<>();

        }


        this.list = list;

        initSelectState();
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




    private Product getProductFromShopCar (int product_id) {

        Product product = null;

        List<Product> products = DataSupport.where("product_id = ?",String.valueOf(product_id)).find(Product.class);


        if(null != products && products.size() > 0) {

            product = products.get(0);
        }

        return product;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {




        ShoppingCar shoppingCar = list.get(position);

        int productId = shoppingCar.getProductId();

        final Product product = getProductFromShopCar(productId);

       ViewHolder vh = null;

        if (convertView == null) {

            convertView = this.inflater.inflate(R.layout.shopping_car_list_item, null);

            vh = new ViewHolder();
            vh.cb_selected_in_shopping_car = (CheckBox) convertView.findViewById(R.id.cb_selected_in_shopping_car);
            vh.iv_product_head = (ImageView) convertView.findViewById(R.id.iv_product_head);
            vh.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
            vh.tv_local_price = (TextView) convertView.findViewById(R.id.tv_local_price);
            vh.tv_original_price = (TextView) convertView.findViewById(R.id.tv_original_price);
            vh.iv_reduce = (ImageView) convertView.findViewById(R.id.iv_reduce);
            vh.tv_product_count = (TextView) convertView.findViewById(R.id.tv_product_count);
            vh.iv_increase = (ImageView) convertView.findViewById(R.id.iv_increase);

            convertView.setTag(vh);
        } else {

            vh = (ViewHolder) convertView.getTag();

        }



        vh.cb_selected_in_shopping_car.setChecked(this.mSelectMap.get(position));


        vh.tv_product_name.setText(product.getProductName());
        vh.tv_local_price.setText(product.getLocalPrice());

        String text = context.getString(R.string.original_price_text)+context.getString(R.string.money_mark)+
                product.getOriginalPrice()+context.getString(R.string.unit_division);
        vh.tv_original_price.setText(text);
        vh.tv_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);



        vh.tv_product_count.setText(String.valueOf(shoppingCar.getBuyCount()));
        /**
         * 监听按钮部分
         */

        final ViewHolder finalVh = vh;
        vh.iv_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               int count = Integer.valueOf( finalVh.tv_product_count.getText().toString());


                int stock = product.getStock();

                if(count < stock) {

                    count++;

                    finalVh.tv_product_count.setText(String.valueOf(count));
                }


            }
        });
        vh.iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = Integer.valueOf(finalVh.tv_product_count.getText().toString());

                if (count > 1) {

                    count--;

                    finalVh.tv_product_count.setText(String.valueOf(count));


                }

            }
        });

        /**
         * 设置图片
         */
        ImgUtil.getInstance().getImgFromNetByUrl(product.getThumnail(),vh.iv_product_head,R.drawable.img_loadx);

        return convertView;
    }


  public  class ViewHolder {
      public CheckBox cb_selected_in_shopping_car;
      public  ImageView iv_product_head;
      public  TextView tv_product_name;
      public  TextView tv_local_price;
      public  TextView tv_original_price;
      public  ImageView iv_reduce;
      public  TextView tv_product_count;
      public  ImageView iv_increase;
    }
}
