package org.tangze.work.widget.pullfresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/3/17 0017.
 * 类描述  只有下拉刷新
 * 版本
 */
public class PullableListViewDown extends ListView implements Pullable {

    public PullableListViewDown(Context context)
    {
        super(context);
    }

    public PullableListViewDown(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PullableListViewDown(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

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
            // 滑到ListView的顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp()
    {
//        if (getCount() == 0)
//        {
//            // 没有item的时候也可以上拉加载
//            return true;
//        } else if (getLastVisiblePosition() == (getCount() - 1))
//        {
//            // 滑到底部了
//            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
//                    && getChildAt(
//                    getLastVisiblePosition()
//                            - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
//                return true;
//        }
        return false;


    }


}
