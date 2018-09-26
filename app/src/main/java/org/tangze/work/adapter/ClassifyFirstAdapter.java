package org.tangze.work.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;


import org.tangze.work.R;
import org.tangze.work.entity.Classify;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28 0028.
 * 类描述
 * 版本
 */
public class ClassifyFirstAdapter extends BaseAdapter {

    private Context context;

    private List<Classify> list;

    private LayoutInflater inflater;


    public int selectIndex; //这里设置position之后进入列表后会自动选中当前项目


    public ClassifyFirstAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public void setList(List<Classify> list) {
        if(null == list) {

            list = new ArrayList<>();

        }
        this.list = list;
        this.notifyDataSetChanged();
    }

    public List<Classify> getList() {
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
        final Classify classify = list.get(position);


        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.classify_first_item, null);

            mViewHolder.tv_classify_first_name = (TextView) convertView.findViewById(R.id.tv_classify_first_name);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }



        if( selectIndex == position ){
            convertView.setBackgroundColor(Color.WHITE);
            mViewHolder.tv_classify_first_name.setTextColor(Color.parseColor("#c91e24"));
        }
        else{
            convertView.setBackgroundColor(Color.alpha(0));
            mViewHolder.tv_classify_first_name.setTextColor(Color.BLACK);
        }


        mViewHolder.tv_classify_first_name.setText(classify.getClassfiyName());




//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, SecondClassifyActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(ConstIntent.BundleKEY.ClASSFITY_TO_SECOND_CLASSIFY, classify);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }


    public class ViewHolder {

        TextView tv_classify_first_name;
    }
}
