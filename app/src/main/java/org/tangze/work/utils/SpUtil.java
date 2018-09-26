package org.tangze.work.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;


import org.tangze.work.MyApplication;
import org.tangze.work.constant.ConstSp;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2016/9/19 0019.
 * 类描述   全局SharedPreferences可以保存 int 、String、Long、boolean类型
 * 版本
 */
public class SpUtil {


    public static final String TAG = SpUtil.class.getSimpleName();

    private static SharedPreferences mSp;

    private static SpUtil mInstance;

    private static ReentrantLock mLock = new ReentrantLock();


    public SpUtil() {

        mSp = MyApplication.getInstance().getSharedPreferences(ConstSp.TZ_SP_NAME,ConstSp.SP_OPEN);

    }


    public static SpUtil getInstance() {

        try {

            mLock.lock();
            if(null == mInstance) {

                mInstance = new SpUtil();

            }
            return mInstance;

        }finally {

            mLock.unlock();

        }

    }





    /**
     * @Title saveStringSp
     * @Description 保存字符串到sp文件
     * @param key 字符串对应的key
     * @param value 字符串的vaule
     * @return true 保存成功  false 保存失败
     */
    public boolean saveStringToSp(String key , String value){

        if(TextUtils.isEmpty(key) || TextUtils.isEmpty(value)){
            return false;
        }
        return mSp.edit().putString(key, value).commit();
    }

    /**
     *
     * @Title saveIntToSp
     * @Description 保存整形值到sp
     * @param key 整形值对应的key
     * @param value 整形值对应的value
     * @return true 保存成功  false 保存失败
     */
    public boolean saveIntToSp(String key , int value){

        if(TextUtils.isEmpty(key)){
//            LogUtil.e(TAG, "=== saveIntToSp(),保存失败,key不能为null === ");
            Log.i(TAG, "=== saveIntToSp(),保存失败,key不能为null === ");
            return false;
        }
        return mSp.edit().putInt(key, value).commit();
    }

    /**
     *
     * @Title saveIntToSp
     * @Description 保存整形值到sp
     * @param key 整形值对应的key
     * @param value 整形值对应的value
     * @return true 保存成功  false 保存失败
     */
    public boolean saveLongToSp(String key , long value){

        if(TextUtils.isEmpty(key)){
//            LogUtil.e(TAG , "=== saveLongToSp(),保存失败,key不能为null === ");
            Log.i(TAG, "=== saveLongToSp(),保存失败,key不能为null === ");
            return false;
        }
        return mSp.edit().putLong(key, value).commit();
    }

    /**
     *
     * @Title saveBoolenTosp
     * @Description 保存布尔值到sp
     * @param key 布尔值对应的key
     * @param value 布尔值对应的value
     * @return true 保存成功 fasle 保存失败
     */
    public boolean saveBooleanTosp(String key , boolean value){

        if(TextUtils.isEmpty(key)){
//            LogUtil.e(TAG , "=== saveBoolenTosp(),保存失败,key不能为null === ");
            Log.i(TAG,"=== saveBoolenTosp(),保存失败,key不能为null === ");
            return false;
        }
        return mSp.edit().putBoolean(key, value).commit();
    }

    /**
     * @Title getStringValue
     * @Description 根据保存到sp的key 取出String value
     * @param key
     * @param defaultValue
     */
    public String getStringValue(String key , String defaultValue){

        if(null == defaultValue){
            defaultValue = "";
        }
        if(TextUtils.isEmpty(key)){
            return defaultValue;
        }
        return mSp.getString(key, defaultValue);
    }

    /**
     * @Title getIntValue
     * @Description 根据保存到sp的key 取出Int value
     * @param key 保存的key
     * @param defaultValue 默认值
     * @return key 对应的 int value
     */
    public int getIntValue(String key , int defaultValue){

        if(TextUtils.isEmpty(key)){
            return defaultValue;
        }
        return mSp.getInt(key, defaultValue);

    }

    /**
     * @Title getIntValue
     * @Description 根据保存到sp的key 取出Int value
     * @param key 保存的key
     * @param defaultValue 默认值
     * @return key 对应的 int value
     */
    public long getLongValue(String key , long defaultValue){

        if(TextUtils.isEmpty(key)){
            return defaultValue;
        }
        return mSp.getLong(key, defaultValue);

    }

    /**
     * @Title getBoolenValue
     * @Description TODO 这里用一句话描述这个方法的作用
     * @param key
     * @param defaultValue
     * @return key 对应的boolean value
     */
    public boolean getBoolenValue(String key , boolean defaultValue){
        if(TextUtils.isEmpty(key)){
            return defaultValue;
        }
        return mSp.getBoolean(key, defaultValue);
    }

}
