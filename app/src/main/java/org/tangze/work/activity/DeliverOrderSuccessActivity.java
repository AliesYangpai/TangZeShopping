package org.tangze.work.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.tangze.work.R;
import org.tangze.work.constant.ConstIntent;


/**
 * 订单提交成功的activty界面
 */
public class DeliverOrderSuccessActivity extends BaseActivity {


    /**
     * title
     */
    private ImageView iv_common_back;
    private TextView tv_deliverSuccess_title;
    private ImageView iv_common_search;

    private LinearLayout ll_continue_to_shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_order_success);
    }

    @Override
    protected void initViews() {

        /**
         * title
         *
         */
        iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_deliverSuccess_title = (TextView) findViewById(R.id.tv_common_title);
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_back.setVisibility(View.GONE);
        iv_common_search.setVisibility(View.GONE);
        tv_deliverSuccess_title.setText(getString(R.string.deliver_success));


        ll_continue_to_shopping = (LinearLayout) findViewById(R.id.ll_continue_to_shopping);
    }

    @Override
    protected void initListener() {
        ll_continue_to_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                sendBroadCast();

                openActivityAndFinishItself(MainActivity.class,null);

            }
        });
    }


    private void sendBroadCast() {

        Intent intentBroad = new Intent();
        intentBroad.setAction(ConstIntent.IntentAction.DELIVER_ORDER_SUCCESS);
        sendBroadcast(intentBroad);
    }

    @Override
    protected void processIntent() {

    }


    @Override
    public void onBackPressed() {

    }
}
