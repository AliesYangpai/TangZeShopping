package org.tangze.work;



import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;


import org.litepal.LitePalApplication;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstShare;
import org.tangze.work.utils.ImgUtil;
import org.tangze.work.utils.SDCardUtil;

import java.io.File;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 类描述
 * 版本
 */
public class MyApplication extends LitePalApplication {



    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initLeakCanary();
//        initCrashReport();
        initImageLoader(this);
        initUmengShare();


    }

    /**
     * leakCanery相关******************************
     */
    private RefWatcher refWatcher;
    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context
                .getApplicationContext();
        return application.refWatcher;
    }
    private void initLeakCanary() {


        if(ConstBase.INIT_LEAK_CANERY_OR_NOT) {

            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            refWatcher  = LeakCanary.install(this);
        }


    }
    /**
     * leakCanery相关******************************
     */




    private void initUmengShare() {
        Config.DEBUG = true;
        UMShareAPI.get(this);


        PlatformConfig.setWeixin(ConstShare.KEY.WEI_XIN_KEY, ConstShare.VALUE.WEI_XIN_VALUE);
        PlatformConfig.setSinaWeibo(ConstShare.KEY.SINA_KEY, ConstShare.VALUE.SINA_VALUE, ConstShare.VALUE.SINA_REDIRECT_URL);
        PlatformConfig.setQQZone(ConstShare.KEY.QQ_KEY, ConstShare.VALUE.QQ_VALUE);

    }







    public static MyApplication getInstance() {


        if(mInstance == null) {

            synchronized (MyApplication.class) {

                if(mInstance == null) {

                    mInstance = new MyApplication();

                }

            }

        }
        return mInstance;

    }

    /**
     * 崩溃日志收集
     * 暂时不使用
     */
//    private void initCrashReport() {

//        if(SDCardUtil.existSDCard()) {
//
//
//            String path = SDCardUtil.getSDCardPath();
//            LogReport.getInstance()
//                    .setCacheSize(5*1024*1024)//设置支持缓存大小，超出后清空
//                    .setLogDir(this,path + this.getString(this.getApplicationInfo().labelRes)+"/" )//定义路径为：sdcard/[app name]/
//                    .setWifiOnly(true)//设置只在wifi状态下上传
//                    .setLogSaver(new CrashWriter(this))
//                    .init(this);
//
//
//        }else {
//
//            LogReport.getInstance()
//                    .setCacheSize(5*1024*1024)//设置支持缓存大小，超出后清空
//                    .setLogDir(this,"sdcard/" + this.getString(this.getApplicationInfo().labelRes)+"/" )//定义路径为：sdcard/[app name]/
//                    .setWifiOnly(true)//设置只在wifi状态下上传
//                    .setLogSaver(new CrashWriter(this))
//                    .init(this);
//        }
//
//
//
//
//
//        LogReport.getInstance()
//                .setCacheSize(30*1024*1024)//设置支持缓存大小，超出后清空
//                .setLogDir(this,"sdcard/" + this.getString(this.getApplicationInfo().labelRes)+"/" )//定义路径为：sdcard/[app name]/
//                .setWifiOnly(true)//设置只在wifi状态下上传
//                .setLogSaver(new CrashWriter(this))
//                .init(this);






//        initEmailReporter();

//    }

    /**
     * 使用email发送日志
     * 暂时不使用
     */
//    private void initEmailReporter() {

//        EmailReporter email = new EmailReporter(this);
//        email.setReceiver("wenmingvs@gmail.com");//收件人
//        email.setSender("wenmingvs@163.com");//发送人邮箱
//        email.setSendPassword("apptest1234");//邮箱的客户端授权码，注意不是邮箱密码
//        email.setSMTPHost("smtp.163.com");//SMTP地址
//        email.setPort("465");//SMTP 端口
//        LogReport.getInstance().setUploadType(email);

//    }


    /**
     * 初始化ImageLoader
     */
    private void initImageLoader(Context context) {

//        File cacheDir = StorageUtils.getOwnCacheDirectory(
//                getApplicationContext(), "Travel/"); // 设置内存卡的路径
//
//        ImageLoaderConfiguration config = new
//                ImageLoaderConfiguration.Builder(
//                context).threadPriority(Thread.NORM_PRIORITY - 2)// 设置当前线程优先级
//                .denyCacheImageMultipleSizesInMemory() // 缓存显示不同 大小的同一张图片
//                .diskCacheSize(50 * 1024 * 1024) // 本地Sd卡的缓存最大值
//                .diskCache(new UnlimitedDiscCache(cacheDir))// sd卡缓存
//                .memoryCache(new WeakMemoryCache()) // 内存缓存
//                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
//
//
//        ImageLoader.getInstance().init(config);





//        File cacheDir = StorageUtils.getOwnCacheDirectory(
//                getApplicationContext(), "Travel/"); // 设置内存卡的路径

//        File cacheDir =this.getCacheDir(); //设置到/data/data/[packagename]/cache设置到app的安装路径的cache下
        String cachePath =this.getCacheDir().getAbsolutePath()+File.separator+"imageCache/";

        File cacheDir = new File(cachePath);

        ImageLoaderConfiguration config = new
                ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)// 设置当前线程优先级
                .denyCacheImageMultipleSizesInMemory() // 缓存显示不同 大小的同一张图片
                .diskCacheSize(50 * 1024 * 1024) // 本地Sd卡的缓存最大值
                .diskCache(new UnlimitedDiscCache(cacheDir))// sd卡缓存
                .memoryCache(new WeakMemoryCache()) // 内存缓存
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        ImgUtil.getInstance().getImageLoader().init(config);


    }
}
