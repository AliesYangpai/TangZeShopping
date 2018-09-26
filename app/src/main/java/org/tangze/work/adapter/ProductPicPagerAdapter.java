package org.tangze.work.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/3 0003.
 * 类描述   产品详情界面中的，产品图片的viewpager
 * 版本
 */
public class ProductPicPagerAdapter extends PagerAdapter {


    private List<ImageView> imageViews;

    public ProductPicPagerAdapter(List<ImageView> imageViews){

        this.imageViews = imageViews;
    }

    @Override
    public int getCount() {
        if(imageViews != null)
            return imageViews.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews.get(position));
        return imageViews.get(position);
    }

}
