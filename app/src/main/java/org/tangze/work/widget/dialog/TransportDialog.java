package org.tangze.work.widget.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.tangze.work.R;

/**
 * Created by Administrator on 2016/12/22 0022.
 * 类描述   最新优化的dialog，设计为自行车滚动或跑车运行
 * 版本
 */
public class TransportDialog extends ProgressDialog {

    private AnimationDrawable mAnimation;
    private Context mContext;
    private ImageView mImageView;
    private String mLoadingTip;
    private TextView mLoadingTv;
    private int mResid;

    public TransportDialog(Context context, String content, int id) {
        super(context);
        this.mContext = context;
        this.mLoadingTip = content;
        this.mResid = id;


//        setCanceledOnTouchOutside(true);
    }

    public TransportDialog(Context context,  String content,int mResid,int theme) {
        super(context, theme);
        this.mResid = mResid;
        this.mContext = context;
        this.mLoadingTip = content;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {

        mImageView.setBackgroundResource(mResid);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
        mLoadingTv.setText(mLoadingTip);

    }

    public void setContent(String str) {
        mLoadingTv.setText(str);
    }

    private void initView() {
        setContentView(R.layout.dialog_transport);
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
        mImageView = (ImageView) findViewById(R.id.loadingIv);
    }



}
