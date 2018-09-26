package org.tangze.work.widget.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import org.tangze.work.R;
import org.tangze.work.callback.PayCallBack;
import org.tangze.work.constant.ConstPay;
import org.tangze.work.utils.ToastUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/4/5 0005.
 * 类描述
 * 版本
 */
public class PayPopWindow extends PopupWindow implements OnClickListener{

    /**
     * 初始化配置
     */
    private Context context;

    private LayoutInflater inflater;


    private WeakReference<Context> weakReference;


    /**
     * 控件相关
     */

    private View view;

    private RelativeLayout rl_wx_pay;//微信支付
    private RelativeLayout rl_ali_pay;//支付宝支付


    private PayCallBack payCallBack;//点击支付项，本地自行编写的回调函数

    public void setPayCallBack(PayCallBack payCallBack) {
        this.payCallBack = payCallBack;
    }

    public PayPopWindow(Context context) {
        super(context);

        weakReference = new WeakReference<Context>(context);

        this.inflater = LayoutInflater.from(weakReference.get());
        initViews();
        initListener();

    }


    private void initViews() {

        view = this.inflater.inflate(R.layout.popwindow_pay, null);

        rl_wx_pay = (RelativeLayout) view.findViewById(R.id.rl_wx_pay);//微信支付
        rl_ali_pay = (RelativeLayout) view.findViewById(R.id.rl_ali_pay);//支付宝支付

        this.setContentView(view);

        ColorDrawable colorDrawable = new ColorDrawable(0x0000000);

        this.setBackgroundDrawable(colorDrawable);


        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        this.setOutsideTouchable(true); //这一步要在showAsDropDown之前调用

        this.setFocusable(true);

        this.setAnimationStyle(R.style.popwindow_anim_style);



    }

    private void initListener() {

        rl_wx_pay.setOnClickListener(this);
        rl_ali_pay.setOnClickListener(this);

    }


    @Override
    public boolean isShowing() {


        boolean type = super.isShowing();

        Log.i("payPop", "isSHowIng:" + type);


        if(type) {


            backgroundAlpha((Activity)weakReference.get(),1f);

        }else {

            backgroundAlpha((Activity)weakReference.get(),0.5f);
        }




        return type;
    }

    @Override
    public void onClick(View v) {

        this.dismiss();

        switch (v.getId()) {

            case R.id.rl_wx_pay:
                startToPay(ConstPay.VALUE.WX);
                break;

            case R.id.rl_ali_pay:

                startToPay(ConstPay.VALUE.ALIPAY);
                break;

        }
    }



    private void startToPay(String channel) {

        if(null != payCallBack) {


            payCallBack.payByChannel(channel);
        }

    }


    /**
     * 背景色变暗
     */

    public void backgroundAlpha(Activity context, float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
