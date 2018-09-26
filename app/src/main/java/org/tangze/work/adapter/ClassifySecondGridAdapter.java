package org.tangze.work.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.activity.ProductDetialActivity;
import org.tangze.work.constant.ConstIntent;

import org.tangze.work.entity.ClassifySecond;
import org.tangze.work.entity.Product;
import org.tangze.work.utils.ImgUtil;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28 0028.
 * 类描述
 * 版本
 */
public class ClassifySecondGridAdapter extends BaseAdapter implements
        StickyGridHeadersSimpleAdapter {

    private Context context;

    private List<Product> list;


    private LayoutInflater inflater;



    public ClassifySecondGridAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public void setList(List<Product> list) {
        if(null == list) {

            list = new ArrayList<>();
        }
        this.list = list;
        this.notifyDataSetChanged();
    }

    public List<Product> getList() {
        return list;
    }

    @Override
    public int getCount() {
        if(list != null && list.size() > 0) {

            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list != null && list.size() > 0) {

            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {




        final Product product = list.get(position);

        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.test_grid_item, null);
            mViewHolder.iv_classify_second_pic = (ImageView) convertView.findViewById(R.id.iv_classify_pic);
            mViewHolder.tv_classify_second_name = (TextView) convertView.findViewById(R.id.tv_classify_name);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }



        mViewHolder.tv_classify_second_name.setText(product.getProductName());
        ImgUtil.getInstance().getImgFromNetByUrl(product.getThumnail(), mViewHolder.iv_classify_second_pic, R.drawable.img_loadx);

        Log.i("classifythings","getView   中"+"productname:"+product.getProductName()+" cid:"+product.getClassify_id()+" headerId:"+product.getHeaderId());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                ToastUtil.showMsg(context,"productname:"+product.getProductName()+" cid:"+product.getClassify_id()+" headerId:"+product.getHeaderId());
                Intent intent = new Intent(context, ProductDetialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConstIntent.BundleKEY.PRODUCT_TO_DETIAL,product);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {

        if(null!=list && list.size() > 0) {

            return list.get(position).getHeaderId();

        }

        return 0;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder mHeaderHolder;
        if (convertView == null) {
            mHeaderHolder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.gridview_header, parent, false);
            mHeaderHolder.mTextView = (TextView) convertView
                    .findViewById(R.id.header);
            convertView.setTag(mHeaderHolder);
        } else {
            mHeaderHolder = (HeaderViewHolder) convertView.getTag();
        }



        Product product = list.get(position);
         List<ClassifySecond> classifySecondList = DataSupport.where("classifySecond_id = ?", String.valueOf(product.getClassify_id())).find(ClassifySecond.class);
        Log.i("classifythings","headerView中："+"productname:"+product.getProductName()+" cid:"+product.getClassify_id()+" headerId:"+product.getHeaderId());

        if(classifySecondList != null && classifySecondList.size() > 0) {

            mHeaderHolder.mTextView.setText(classifySecondList.get(0).getClassfiyName());
        }

        //248顺序问题
//        mHeaderHolder.mTextView.setText(product.getClassify_id()+"");

        return convertView;
    }


    public class ViewHolder {
        ImageView iv_classify_second_pic;
        TextView tv_classify_second_name;
    }


    public  class HeaderViewHolder {
        public TextView mTextView;
    }






}
