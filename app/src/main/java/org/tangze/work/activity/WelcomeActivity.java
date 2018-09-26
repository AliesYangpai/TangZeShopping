package org.tangze.work.activity;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import org.tangze.work.R;
import org.tangze.work.adapter.WelViewPagerAdapter;
import org.tangze.work.utils.SpUtil;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.constant.ConstSp;
import org.tangze.work.widget.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * 欢迎页，viewpager
 */
public class WelcomeActivity extends BaseActivity  {


    ViewPager viewPager;
    CirclePageIndicator circlePageIndicator;
    WelViewPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


    }









    @Override
    protected void initViews() {

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        View pager01 = getLayoutInflater().inflate(R.layout.view_single_page, null);
        View pager02 = getLayoutInflater().inflate(R.layout.view_single_page, null);
        View pager03 = getLayoutInflater().inflate(R.layout.view_single_page_go, null);

        ImageView pageImage01 = (ImageView) pager01.findViewById(R.id.wizard_image);
        ImageView pageImage02 = (ImageView) pager02.findViewById(R.id.wizard_image);
        RelativeLayout rl_page = (RelativeLayout) pager03.findViewById(R.id.rl_page);

        TextView tv_go = (TextView) pager03.findViewById(R.id.tv_go);

        tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openActivityAndFinishItself(LoginActivity.class,null);

                SpUtil.getInstance().saveBooleanTosp(ConstSp.SP_KEY_LOAD_OR_NOT, true);
            }
        });


        try {

            pageImage01.setBackgroundResource(R.drawable.welcome_page1);
            pageImage02.setBackgroundResource(R.drawable.welcome_page2);
            rl_page.setBackgroundResource(R.drawable.welcome_page3);

        } catch (Exception e) {
            Log.i(TAG, "viewPager设置图片异常：" + e.toString());
        }

        List<View> views = new ArrayList<View>();

        views.add(pager01);
        views.add(pager02);
        views.add(pager03);


        mAdapter = new WelViewPagerAdapter(views);

        viewPager.setAdapter(mAdapter);
        circlePageIndicator.setViewPager(viewPager, 0);
        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


    }

    @Override
    protected void initListener() {


    }

    @Override
    protected void processIntent() {

    }


}
