package org.tangze.work.http;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 类描述   所有的网络交互api
 * 版本
 */
public interface HttpApi {


    /**
     * **************************************data返回值是String************************************
     */



    /**
     * **************************************data返回值是jsonArray************************************
     */

    /**
     * post请求有参数
     * 有参数
     * @param url 后缀路径
     * @param map @Body的作用是将请求的对象转化为json字符串发到服务器，因此
     * //@Body中千万不能写String类型，否则会导致请求体构造错误（json格式不正确，多出\）
     * @return
     */
    @POST("{url}")
    Observable<HttpResult<JsonArray>> executePostWithParams(@Path("url") String url,@Body Map<String,Object> map);



    /**
     * post请求无参数
     * 有参数
     * @param url 后缀路径
     * @return
     */
    @POST("{url}")
    Observable<HttpResult<JsonArray>> executePostWithOutParams(@Path("url") String url);


}
