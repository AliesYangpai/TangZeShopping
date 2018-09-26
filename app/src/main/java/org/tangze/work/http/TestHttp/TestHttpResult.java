package org.tangze.work.http.TestHttp;

/**
 * Created by Administrator on 2016/9/17 0017.
 * 类描述  http返回返回基类 这里的字段必须要和server端返回的字段一致
 * 版本
 */
public class TestHttpResult<T> {

    public int Code;

    public String Message;

    public T Data;

}
