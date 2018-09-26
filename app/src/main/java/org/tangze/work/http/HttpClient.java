package org.tangze.work.http;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/9/17 0017.
 * 类描述  网络交互的核心类
 * 版本
 */
public class HttpClient {


    private static HttpClient mInstance;

    private static ReentrantLock mLock = new ReentrantLock();

    private HttpApi httpApi;


    public HttpClient() {


        OkHttpClient okHttpClient =  new OkHttpClient.Builder()
                .connectTimeout(7, TimeUnit.SECONDS)
                .readTimeout(7, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new TokenInterceptor()) //加入拦截器
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpConst.BASE_URL) //根路径
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();




        httpApi = retrofit.create(HttpApi.class);



    }


    public static HttpClient getInstance() {

        try {

            mLock.lock();
            if(null == mInstance) {
                mInstance = new HttpClient();

            }

        }
        finally {
            mLock.unlock();
        }

        return mInstance;

    }


    /**
     * get方法无后缀
     */

//    public void method_GetWithoutsuffix(String url,HttpResultSubscriber httpResultSubscriber) {
//
//
//        httpApi.executeGetWithOutSuffix(url)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(httpResultSubscriber);
//
//    }


    /**
     * get方法有后缀
     */
















    /**
     * **************************************data返回是jsonArray的***************************************
     */

    //需要加入加载框
    /**
     * post方法无请求参数
     *
     */

    public void method_PostWithOutParams_Dialog(String url,HttpResultSubscriber httpResultSubscriber) {


        httpApi.executePostWithOutParams(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(httpResultSubscriber);
        Log.i("logRequest", "url:" + url);
    }

    //需要加入加载框
    /**
     * post方法有请求参数
     *
     */
    public void method_PostWithParams_Dialog(String url, Map mapCommit, HttpResultSubscriber httpResultSubscriber) {

        httpApi.executePostWithParams(url, mapCommit)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(httpResultSubscriber);
        Log.i("logRequest", "url:" + url + " 参数：" + mapCommit.toString());

    }



//    public void   test_method_PostWithParams_Dialog(String url, Map mapCommit, HttpResultSubscriber httpResultSubscriber) {
//
//        httpApi.testexecutePostWithParams(url, mapCommit)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(httpResultSubscriber);
//        Log.i("logRequest", "url:" + url + " 参数：" + mapCommit.toString());
//
//    }



    /**
     * get方法有参数
     */


//    public void method_GetWithParams_Dialog(String url, String id_key,String id_value, HttpResultSubscriber httpResultSubscriber) {
//
//        httpApi.executeGetWithParamsInPath(url, id_key,id_value)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(httpResultSubscriber);
//        Log.i("logRequest", "url:" + url + " 参数：" + id_key +" "+id_value);
//
//    }


}
