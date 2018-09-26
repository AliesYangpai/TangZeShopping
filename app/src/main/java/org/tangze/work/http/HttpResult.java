package org.tangze.work.http;

/**
 * Created by Administrator on 2016/9/17 0017.
 * 类描述 服务器返回的基类，这个类中的所有字段，均要与服务器的整体json字段一一对应
 * 版本
 */
public class HttpResult<T> {

    public int error;       //需要修改，具体修改情况依照服务器接口返回

    public String msg; //需要修改，具体修改情况依照服务器接口返回

    public T data;         //需要修改，具体修改情况依照服务器接口返回

}
