package org.tangze.work.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.tangze.work.R;
import org.tangze.work.entity.ShowInfo;
import org.tangze.work.utils.ImgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/16 0016.
 * 类描述   信息列表的adapter
 * 版本
 */
public class ShowInfoListAdapter extends BaseAdapter{


    private List<ShowInfo> list;

    private Context context;

    private LayoutInflater inflater;


    public ShowInfoListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }


    public List<ShowInfo> getList() {
        return list;
    }

    public void setList(List<ShowInfo> list) {

        if(null == list) {

            list = new ArrayList<>();

        }
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if(null!= list && list.size() >0) {

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

        ViewHolder vh = null;

        ShowInfo showInfo = list.get(position);

        if(convertView == null) {

            vh = new ViewHolder();

            convertView = this.inflater.inflate(R.layout.show_info_list_item,null);

            vh.iv_show_info_thumbnail = (ImageView) convertView.findViewById(R.id.iv_show_info_thumbnail);

            convertView.setTag(vh);

        }else {

            vh = (ViewHolder) convertView.getTag();

        }

        ImgUtil.getInstance().getRadiusImgFromNetByUrl(showInfo.getThumbnail(),vh.iv_show_info_thumbnail,20);



        return convertView;
    }


    class ViewHolder {

        ImageView iv_show_info_thumbnail;

    }
}
