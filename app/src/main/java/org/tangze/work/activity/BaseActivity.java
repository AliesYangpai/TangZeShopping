package org.tangze.work.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;


import com.squareup.leakcanary.RefWatcher;

import org.tangze.work.MyApplication;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public abstract class BaseActivity extends FragmentActivity {

    public static final String TAG = BaseActivity.class.getSimpleName(); //获得类名称

    public static ConcurrentHashMap<String,Activity> ACTIVITY_STACK = new ConcurrentHashMap<>(); //activty栈



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.i("ceshi", "==requestWindowFeature");
        addToActivitesStack(this.getClass().getSimpleName(), this);
        Log.i("ceshi", "==addToActivitesStack");


    }



    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Log.i("ceshi", "==setContentView");
        processIntent();
        initViews();
        initListener();

    }


    /**
     * 初始化控件
     * 子activity 覆盖这个方法初始化ui控件
     */
    protected abstract void initViews();

    /**
     * 初始化监听
     *  子activity 覆盖这个方法初始化ui控件的监听事件
     */
    protected abstract void initListener();


    /**
     * @Title processIntent
     * @Description 获取Intent携带数据
     */
    protected abstract void processIntent();

    /**
     * activity加入到回退栈方法
     */
    public void addToActivitesStack(String activityTag,Activity activity) {

        if(activityTag.equals(WelcomeActivity.class.getSimpleName())) {
            return;
        }
        ACTIVITY_STACK.put(activityTag, activity); //将activity加入到回退栈
    }


    /**
     * activity退出回退栈方法
     */
    public void removeFromActiviiesStack(String activityTag) {
        if(activityTag.equals(WelcomeActivity.class.getSimpleName())) {
            return;
        }
        ACTIVITY_STACK.remove(activityTag);
    }



    /**
     * 清空指定activity以外的所有Activty所有的Activity
     */
    public void removeAllFromActiviiesStack(String activityTag) {


        if(null != ACTIVITY_STACK && ACTIVITY_STACK.size() > 0) {


              Iterator<String> iterator =  ACTIVITY_STACK.keySet().iterator();


              while (iterator.hasNext()) {



                  String tag = iterator.next();

                  if(tag.equals(activityTag)) {

                      continue;

                  }

                  ACTIVITY_STACK.get(tag).finish();

              }


        }


    }




    /**
     * 打开activity的方法
     */
    protected void openActivity(Class<?> targetClass,Bundle bundle) {
        Intent intent = new Intent(this,targetClass);
        if( null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 打开activity并且销毁自己
     */
    protected void openActivityAndFinishItself(Class<?> targetClass,Bundle bundle) {

        Intent intent = new Intent(this,targetClass);
        if( null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        this.finish();

    }






    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        removeFromActiviiesStack(this.getClass().getSimpleName());
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
