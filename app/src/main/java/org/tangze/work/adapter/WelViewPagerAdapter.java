package org.tangze.work.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20 0020.
 * 类描述
 * 版本
 */
public class WelViewPagerAdapter extends PagerAdapter {


    private List<View> mViews = new ArrayList<View>();

    public WelViewPagerAdapter(List<View> views) {
        mViews = views;
    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewGroup)container).addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    public void update(List<View> mInfoViews) {
        mViews = mInfoViews;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
