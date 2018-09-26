package org.tangze.work.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/17 0017.
 * 类描述  全局拦截器，用于设置token与刷新token
 * 版本
 */
public class TokenInterceptor implements Interceptor{


    /**
     * token过期时候使用，目前不用
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);


        /**
         * token过期的判断：目前不用，以后用
         */
        if(isTokenExpired(response)) {//根据和服务端的约定判断token过期

            //同步请求的方式获取最新token

            String newToken = getNewToken();

            Request newRequest  = chain.request().newBuilder()
                    .addHeader("xxxx",newToken) //需要修改
                    .addHeader(HttpConst.CONTENT_TYPE,HttpConst.CONTENT_TYPE_VALUE)
                    .build();

            return chain.proceed(newRequest);

        }

        return  response;
    }




    /**
     *判断token失效     //需要修改
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {

        if(response.code() == 404) {

            return true;

        }

        return false;

    }



    /**需要修改
     * 同步请求方式获取token
     * @return
     */
    private String getNewToken() {

        // 通过一个特定的接口获取新的token，此处要用到同步的retrofit请求

        String newToken  = "";


        return newToken;
    }
}
