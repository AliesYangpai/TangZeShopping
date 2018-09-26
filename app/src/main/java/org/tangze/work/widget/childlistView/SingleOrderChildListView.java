package org.tangze.work.widget.childlistView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/10/21 0021.
 * 类描述  该类用于定订单状态中嵌套的childListView
 * 版本
 */
public class SingleOrderChildListView extends ListView {
    public SingleOrderChildListView(Context context) {
        super(context);
    }

    public SingleOrderChildListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleOrderChildListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
