package org.tangze.work.widget.webView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.tangze.work.R;
import org.tangze.work.widget.dialog.ArcConfiguration;
import org.tangze.work.widget.dialog.SimpleArcDialog;
import org.tangze.work.widget.dialog.SimpleArcLoader;
import org.tangze.work.widget.dialog.TransportDialog;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/11/7 0007.
 * 类描述
 * 版本
 */
public class WebClientPicDescrib extends WebViewClient {


    private Context context;//内存优化前
    private WeakReference<Context> weakReferenceContext;//内存优化后


    //普通版dialog
    private AlertDialog pd;


    //晋级版dialog
    private SimpleArcDialog mDialog;
    private ArcConfiguration configuration;



    //最终版Dialog
    private TransportDialog transportDialog;

    public WebClientPicDescrib(Context context) {
//        this.context = context;//内存优化前
        this.weakReferenceContext = new WeakReference<Context>(context);//内存优化后
    }





    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {// 网页页面开始加载的时候

        /**
         * 普通版Dialog
         */

//        if (pd == null) {
//            pd = new AlertDialog.Builder(context).create();
//            pd.setCancelable(true);
//            if (!pd.isShowing()) {
//                pd.show();
//            }
//            Window window = pd.getWindow();
//            window.setContentView(R.layout.dialog_loading_common);
//            TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_message);
//            tv_title.setText("加载中...");
//
//            super.onPageStarted(view, url, favicon);
//        }


        /**
         * 晋级版dialog显示
         */
//        if (mDialog == null) {
//            mDialog = new SimpleArcDialog(context);
//            if(null == configuration) {
//                configuration = new ArcConfiguration(context);
//                configuration.setLoaderStyle(SimpleArcLoader.STYLE.SIMPLE_ARC);  //设置dialog是否是圈体格式
//                configuration.setText(context.getString(R.string.in_loading));
//                int[] colorArray = {Color.parseColor("#c91e24"),Color.parseColor("#c91e24")}; //设置圈体颜色
//                configuration.setColors(colorArray);
//                configuration.setTextSize(13);
//            }
//            mDialog.setConfiguration(configuration);
//            mDialog.setCancelable(false);
//            if (!mDialog.isShowing()) {
//                mDialog.show();
//            }
//            super.onPageStarted(view, url, favicon);
//        }


        /**
         * 最终版dialog
         */


        if (transportDialog == null) {
//            transportDialog = new TransportDialog(context,"加载中...",R.anim.transport);//内存优化前
            transportDialog = new TransportDialog(weakReferenceContext.get(),"加载中...",R.anim.transport);//内存优化后
            transportDialog.setCancelable(false);
            if (!transportDialog.isShowing()) {
                transportDialog.show();
            }


        }



    }


    @Override
    public void onPageFinished(WebView view, String url) {// 网页加载结束的时候

        //普通版Dialog
//        if (pd != null && pd.isShowing()) {
//            pd.dismiss();
//            pd = null;
//        }


        /**
         * 晋级版dialog消失
         */
//        dismissProgressDialog2Round();


        /**
         * 最终版Dialog
         */
        dismissProgressDialogTransport();
    }






    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) { // 网页加载时的连接的网址
//        view.loadUrl(url);
//        return true;



        if(url.startsWith("http:") || url.startsWith("https:") ) {
            view.loadUrl(url);
            return false;
        }else{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            context.startActivity(intent);//内存优化前
            weakReferenceContext.get().startActivity(intent);//内存优化后
            return true;
        }
    }






    /**
     * 晋级版dialog消失
     */
    private void dismissProgressDialog2Round(){
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
//            configuration = null;
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


}
