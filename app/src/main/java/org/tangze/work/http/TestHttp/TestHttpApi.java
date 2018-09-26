package org.tangze.work.http.TestHttp;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 类描述
 * 版本
 */
public interface TestHttpApi {





    @POST("basic/all")
    Observable<TestHttpResult<JsonObject>> getAndroidData();

    @GET("{url}")
    Observable<ResponseBody> executeGet(@Path("url") String url, @QueryMap Map<String, String> maps);

    @FormUrlEncoded
    @POST("{url}")
    Observable<ResponseBody> executePost(@Path("url") String url, @QueryMap Map<String, String> maps);



}
