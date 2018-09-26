package org.tangze.work.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.tangze.work.R;
import org.tangze.work.activity.ProductDetialActivity;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.temp.ProductPackage;
import org.tangze.work.utils.ImgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/29 0029.
 * 类描述
 * 版本
 */
public class MainRecommandPackageAdapter extends BaseAdapter{

    private List<ProductPackage> productPackages;

    private Context context;

    private LayoutInflater inflater;


    public MainRecommandPackageAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }


    public List<ProductPackage> getProductPackages() {
        return productPackages;
    }

    public void setProductPackages(List<ProductPackage> productPackages) {
        if(null == productPackages) {

            productPackages = new ArrayList<>();

        }
        this.productPackages = productPackages;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if(null != productPackages && productPackages.size() > 0) {

            return productPackages.size();

        }
        return 0;
    }

    @Override
    public Object getItem(int position) {


        if(null != productPackages && productPackages.size() > 0) {

            return productPackages.get(position);

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

        if(null == convertView) {


            convertView = this.inflater.inflate(R.layout.main_recommand_package_item, null);

            viewHolder = new ViewHolder();


            viewHolder.layouts[0] = (LinearLayout) convertView.findViewById(R.id.ll_grid1);
            viewHolder.layouts[1] = (LinearLayout) convertView.findViewById(R.id.ll_grid2);
            viewHolder.layouts[2] = (LinearLayout) convertView.findViewById(R.id.ll_grid3);

            viewHolder.iv_pic[0] = (ImageView) convertView.findViewById(R.id.iv_pic1);
            viewHolder.iv_pic[1] = (ImageView) convertView.findViewById(R.id.iv_pic2);
            viewHolder.iv_pic[2] = (ImageView) convertView.findViewById(R.id.iv_pic3);


            viewHolder.tv_str[0] = (TextView) convertView.findViewById(R.id.tv_str1);
            viewHolder.tv_str[1] = (TextView) convertView.findViewById(R.id.tv_str2);
            viewHolder.tv_str[2] = (TextView) convertView.findViewById(R.id.tv_str3);

            convertView.setTag(viewHolder);
        }else {

            viewHolder = (ViewHolder) convertView.getTag();

        }


        ProductPackage productPackage = productPackages.get(position);

        List<Product> products = productPackage.getProductList(); //获取被拆分好的产品


        for(int i = 0; i < products.size() ; i ++) {






            Product product = products.get(i);

            String product_name = product.getProductName();
            String thumbnail = product.getThumnail();



            MyClickListener myClickListener = new MyClickListener(product);

            viewHolder.layouts[i].setOnClickListener(myClickListener);

            viewHolder.tv_str[i].setText(product_name);


            ImgUtil.getInstance().getImgFromNetByUrl(thumbnail,viewHolder.iv_pic[i],R.drawable.img_loadx);


        }




        return convertView;
    }

    class ViewHolder  {


        /**
         * 大Layout布局
         */
        LinearLayout[]   layouts = new LinearLayout[3];

        /**
         * 底部产品名称
         */
        TextView[] tv_str = new TextView[3];
        /**
         * 产品图片
         */
        ImageView[] iv_pic = new ImageView[3];
    }


    class MyClickListener implements View.OnClickListener {

        private Product product;


        public MyClickListener(Product product) {
            this.product = product;
        }



        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, ProductDetialActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstIntent.BundleKEY.PRODUCT_TO_DETIAL,product);
            intent.putExtras(bundle);
            context.startActivity(intent);

        }
    }

}
