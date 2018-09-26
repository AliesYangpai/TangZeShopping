package org.tangze.work.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2016/9/29 0029.
 * 类描述   统一的图片加载封装
 * 版本
 */
public class ImgUtil implements ImageLoadingListener {

    private ImageLoader imageLoader;

    private static ImgUtil mInstance;

    private static ReentrantLock mLock = new ReentrantLock();

    private List<String> displayedImages;

    public static ImgUtil getInstance() {

        try {
            mLock.lock();

            if(null == mInstance) {

                mInstance = new ImgUtil();

            }

            return mInstance;
        }
        finally {

            mLock.unlock();
        }

    }


    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public ImgUtil() {

        imageLoader = ImageLoader.getInstance();
        displayedImages = Collections.synchronizedList(new LinkedList<String>());
    }


    /**
     * 获取正常图片
     */
    /**
     * 获取正常的图片
     * @param imgUrl     imgUrl网络图片的地址
     * @param imageView  图片所在控件
     * @param drawableId  加载图片过程中时，显示的图片
     * @return
     */
    public ImageLoader getImgFromNetByUrl(String imgUrl, ImageView imageView,
                                          int drawableId) {
//        ImageLoader loder = ImageLoader.getInstance();
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(drawableId) // 设置正在下载是显示的图片
//                .showImageForEmptyUri(drawableId)// 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(drawableId)// 设置图片加载/解码过程中错误时候显示的图片
//                .cacheInMemory(true)// 是否緩存都內存中
//                .cacheOnDisk(true)// 是否緩存到sd卡上
//                .considerExifParams(true) // 启用EXIF和JPEG图像格式
//                .bitmapConfig(Bitmap.Config.ARGB_4444)
//                .build();
//        loder.displayImage(imgUrl, imageView, options);
//        return loder;


        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(drawableId) // 设置正在下载是显示的图片
//                .showImageForEmptyUri(drawableId)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(drawableId)// 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 是否緩存都內存中
                .cacheOnDisk(true)// 是否緩存到sd卡上
                .considerExifParams(true) // 启用EXIF和JPEG图像格式
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        imageLoader.displayImage(imgUrl, imageView, options,this);
        return imageLoader;
    }


    /**
     * 获取圆角图片
     * @param imgUrl     网络图片的地址
     * @param imageView  图片所在控件
     * @param drawableId 加载图片过程中时，显示的图片
     * @param radius    圆角的弧度
     */
    public void getRadiusImgFromNetByUrl(String imgUrl, ImageView imageView,
                                         int drawableId, int radius) {
//        ImageLoader loder = ImageLoader.getInstance();
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(drawableId)
//                        // 设置正在下载是显示的图片
//                .showImageForEmptyUri(drawableId)
//                        // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(drawableId)
//                        // 设置图片加载/解码过程中错误时候显示的图片
//                .cacheInMemory(true)
//                        // 是否緩存都內存中
//                .cacheOnDisk(true)
//                        // 是否緩存到sd卡上
//                .considerExifParams(true)
//                        // 启用EXIF和JPEG图像格式
//                .displayer(new RoundedBitmapDisplayer(radius))
//                .bitmapConfig(Bitmap.Config.ARGB_4444)
//                .build();
//        loder.displayImage(imgUrl, imageView, options);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId) // 设置正在下载是显示的图片
                .showImageForEmptyUri(drawableId)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(drawableId)// 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 是否緩存都內存中
                .cacheOnDisk(true)// 是否緩存到sd卡上
                .considerExifParams(true) // 启用EXIF和JPEG图像格式
                .displayer(new RoundedBitmapDisplayer(radius))
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        imageLoader.displayImage(imgUrl, imageView, options,this);

    }


    /**
     * 获取圆角图片
     * @param imgUrl     网络图片的地址
     * @param imageView  图片所在控件
     * @param radius    圆角的弧度
     */
    public void getRadiusImgFromNetByUrl(String imgUrl, ImageView imageView,
                                          int radius) {


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)// 是否緩存都內存中
                .cacheOnDisk(true)// 是否緩存到sd卡上
                .considerExifParams(true) // 启用EXIF和JPEG图像格式
                .displayer(new RoundedBitmapDisplayer(radius))
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        imageLoader.displayImage(imgUrl, imageView, options,this);

    }



    @Override
    public void onLoadingStarted(String imageUri, View view) {

    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

        /**
         * 实现渐进动画
         */


        if (loadedImage != null) {
            ImageView imageView = (ImageView) view;
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
                FadeInBitmapDisplayer.animate(imageView, 500);
                displayedImages.add(imageUri);
            }
        }


    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }
}
