package org.tangze.work.http;

import android.content.Context;
import android.util.Log;


import org.tangze.work.widget.ProgressDialogHandler;
import org.tangze.work.widget.ProgressDialogHandler.ProgressCancelListener;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/9/17 0017.
 * 类描述  服务器返回统一预处理
 * 版本
 */
public abstract class HttpResultSubscriber<T> extends Subscriber<HttpResult<T>> implements ProgressCancelListener {



    private ProgressDialogHandler mProgressDialogHandler;




    private Context context;

    /**
     * 无参构造方法表示不使用使用dialog
     *
     */
    public HttpResultSubscriber() {
    }


    /**
     * 这个构造方法表示使用dialog
     * @param context
     */
    public HttpResultSubscriber(Context context) {
        this.context = context;
        this.mProgressDialogHandler = new ProgressDialogHandler(context, this, false);
        Log.i("SUbscriber", "构造方法");
    }


    @Override
    public void onStart() {
        showProgressDialog();

        Log.i("SUbscriber", "on_start");
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
        Log.i("SUbscriber", "onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        _onError(e.toString());
        Log.i("SUbscriber", "onError");
    }


    @Override
    public void onNext(HttpResult<T> t) {

        if(t.error == 0) {

            onSuccess(t.data);

        }else {

            onNotSuccess(t.error,t.msg);

        }

    }

    public abstract void onSuccess(T t);


    public abstract void onNotSuccess(int resultCode,String resultMessage);


    public abstract void _onError(String error);


    /**
     * 显示dialog
     */
    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    /**
     * 消失dialog
     */
    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
