package org.tangze.work.widget.pullfresh;

import android.content.Context;
import android.util.AttributeSet;


/**
 * Created by Administrator on 2016/11/29 0029.
 * 类描述
 * 版本
 */
public class PullableGridViewWithHeader extends HeaderGridView implements Pullable {

	/**
	 *
	 * @param context
	 */
	public PullableGridViewWithHeader(Context context) {
		super(context);
	}

	public PullableGridViewWithHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableGridViewWithHeader(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}


	/**
	 * 仅仅是下拉刷新
	 * @param
	 */
	@Override
	public boolean canPullDown()
	{
		if (getCount() == 0)
		{
			// 没有item的时候也可以下拉刷新
			return true;
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0).getTop() >= 0)
		{
			// 滑到顶部了
			return true;
		} else
			return false;
	}



	/**
	 * 仅仅是下拉刷新
	 * @param context
	 */
	/**
	 * 这里不需要上拉加载，直接return false
	 * @return
	 */
	@Override
	public boolean canPullUp(){

		/**
		 * 需要使用上啦加载时候，再取消注释
		 */
//		if (getCount() == 0)
//		{
//			// 没有item的时候也可以上拉加载
//			return true;
//		} else if (getLastVisiblePosition() == (getCount() - 1))
//		{
//			// 滑到底部了
//			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
//					&& getChildAt(
//					getLastVisiblePosition()
//							- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
//				return true;
//		}
//		return false;
		/**
		 * 不需要上啦加载时，直接return false
		 */
		return false;
	}



}
