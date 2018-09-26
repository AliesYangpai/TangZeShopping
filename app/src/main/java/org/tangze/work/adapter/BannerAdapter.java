package org.tangze.work.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 广告的adapter
 */
public class BannerAdapter extends PagerAdapter {
	
	
	private List<ImageView> imageViews;
	
	public BannerAdapter(List<ImageView> imageViews){
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
