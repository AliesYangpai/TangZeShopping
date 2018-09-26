package org.tangze.work.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.squareup.leakcanary.RefWatcher;

import com.squareup.leakcanary.RefWatcher;

import org.tangze.work.MyApplication;
import org.tangze.work.R;
import org.tangze.work.constant.ConstBase;

/**
 * 基类fragment的封装
 * 1.获取mActivty
 * 2.onCreateView方法的封装
 * 3.部分变量的写入
 * 4.部分方法封装
 */
public abstract class BaseFragment extends Fragment {





    /**
     * 获取所依附的Activty实例
     * @param activity
     */
    protected Activity mActivity;

    /**
     * 根view
     */
    protected View mRootView;


    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;

    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = getActivity();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(setLayoutResouceId(), container, false);

        getSendData(getArguments()); //传参

        initView();

        initListener();

        mIsPrepare = true;

        onLazyLoad();

//        initListener();

        return mRootView;
    }




    /**
     * 设置根布局的资源id
     */
    protected abstract int setLayoutResouceId();
    /**
     * 初始化数据
     * @param arguments 接收到从其他地方传递过来的参数
     */
    protected abstract void getSendData(Bundle arguments);

    /**
     * 初始化view
     */
    protected abstract void initView();


    /**
     * 初始化监听
     */
    protected abstract void initListener();


    /**
     * 懒加载，仅当用户可见切view初始化结束后才会执行
     * //懒加载的方法,在这个方法里面我们为Fragment的各个组件去添加数据
     */
    protected abstract void onLazyLoad();


    /**
     * findViewById方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int id) {
        if (mRootView == null)
        {
            return null;
        }

        return (T) mRootView.findViewById(id);
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        this.mIsVisible = isVisibleToUser;

        if (isVisibleToUser)
        {
            onVisibleToUser();
        }

        Log.i("baseFragment","调用setUserVisibleHint方法");
    }

    /**
     * 用户可见时执行的操作:界面完全可见时候，再去执行各种交互
     *
     *
     */
    protected void onVisibleToUser() {
        if (mIsPrepare && mIsVisible)
        {
            onLazyLoad();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();



        if(ConstBase.INIT_LEAK_CANERY_OR_NOT) {

            RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
            refWatcher.watch(this);

        }
    }
}
