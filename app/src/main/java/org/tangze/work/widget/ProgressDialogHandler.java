package org.tangze.work.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;

import org.tangze.work.R;
import org.tangze.work.widget.dialog.ArcConfiguration;
import org.tangze.work.widget.dialog.SimpleArcDialog;
import org.tangze.work.widget.dialog.SimpleArcLoader;
import org.tangze.work.widget.dialog.TransportDialog;

import java.lang.ref.WeakReference;


/**
 * Created by Administrator on 2016/10/13 0013.
 * 类描述
 * 版本
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private AlertDialog pd;

    private Context context;//内存优化前

    private WeakReference<Context> weakReferenceContext;//内存优化后
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;


    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
//        this.context = context;//内存优化前
        weakReferenceContext = new WeakReference<Context>(context);//内存优化后
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }


    /**
     * ********************************普通版dialog*****************************************************
     */

    /**
     * 普通版dialog初始化
     */
    private void initProgressDialog(){
        if (pd == null) {
            pd = new AlertDialog.Builder(context).create();
            pd.setCancelable(cancelable);
            if (!pd.isShowing()) {
                pd.show();
            }
            Window window = pd.getWindow();
            window.setContentView(R.layout.dialog_loading_common);
            TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_message);
            tv_title.setText("加载中...");
            if (cancelable) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }


        }
    }

    /**
     * 普通版dialog消失
     */
    private void dismissProgressDialog(){
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    /**
     * 普通的dialog的handleMessage
     * @param msg
     */
//    @Override
//    public void handleMessage(Message msg) {
//        switch (msg.what) {
//            case SHOW_PROGRESS_DIALOG:
//                initProgressDialog();
//                break;
//            case DISMISS_PROGRESS_DIALOG:
//                dismissProgressDialog();
//                break;
//        }
//    }


    /**
     * **************************************晋级版dialog*******************************************************************
     *
     */

    /**
     * 晋级版的dialog的handleMessage
     * @param
     */
//    @Override
//    public void handleMessage(Message msg) {
//        switch (msg.what) {
//            case SHOW_PROGRESS_DIALOG:
//                initProgressDialog2Round();
//                break;
//            case DISMISS_PROGRESS_DIALOG:
//                dismissProgressDialog2Round();
//                break;
//        }
//    }
    //晋级版dialog
    private SimpleArcDialog mDialog;
    private ArcConfiguration configuration;
    /**
     * 晋级版dialog显示
     */
    private void initProgressDialog2Round() {
        if (mDialog == null) {
            mDialog = new SimpleArcDialog(context);

            if(null == configuration) {
                configuration = new ArcConfiguration(context);
                configuration.setLoaderStyle(SimpleArcLoader.STYLE.SIMPLE_ARC);  //设置dialog是否是圈体格式
                configuration.setText(context.getString(R.string.in_loading));
                int[] colorArray = {Color.parseColor("#c91e24"),Color.parseColor("#c91e24")}; //设置圈体颜色

                configuration.setColors(colorArray);
                configuration.setTextSize(13);
            }

            mDialog.setConfiguration(configuration);

            mDialog.setCancelable(cancelable);
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
            if (cancelable) {
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }


        }
    }

    /**
     * 晋级版dialog消失
     */
    private void dismissProgressDialog2Round(){
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


    /**
     *
     * ****************************************最终版dialog*****************************************************
     */

    /**
     * 最终版dialog的handleMessage
     */
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialogTransport();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialogTransport();
                break;
        }
    }




    private TransportDialog transportDialog;

    /**
     * 最终版dialog初始化
     */
    private void initProgressDialogTransport() {

        if (transportDialog == null) {
//            transportDialog = new TransportDialog(context,"加载中...",R.anim.transport);//内存优化前
            transportDialog = new TransportDialog(weakReferenceContext.get(),"加载中...",R.anim.transport);//内存优化后

            transportDialog.setCancelable(cancelable);
            if (!transportDialog.isShowing()) {
                transportDialog.show();
            }

            if (cancelable) {
                transportDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }


        }

    }

    /**
     * 最终版dialog消失
     */

    private void dismissProgressDialogTransport(){
        if (transportDialog != null) {
            transportDialog.dismiss();
            transportDialog = null;
        }


    }



    public interface ProgressCancelListener {
        void onCancelProgress();
    }
}
