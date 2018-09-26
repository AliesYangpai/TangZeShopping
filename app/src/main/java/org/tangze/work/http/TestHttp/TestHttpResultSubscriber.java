package org.tangze.work.http.TestHttp;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/9/17 0017.
 * 类描述  统一的httpSubscriber返回
 * 版本
 */
public abstract class TestHttpResultSubscriber<T> extends Subscriber<TestHttpResult<T>>{

    @Override
    public void onCompleted() {

    }


    @Override
    public void onError(Throwable e) {

        _onError(e.toString());
    }


    @Override
    public void onNext(TestHttpResult<T> t) {

        if(t.Code == 0) {

            onSuccess(t.Data);

        }else {

            onNotSuccess(t.Code,t.Message);

        }

    }

    public abstract void onSuccess(T t);


    public abstract void onNotSuccess(int resultCode,String resultMessage);


    public abstract void _onError(String error);




}
