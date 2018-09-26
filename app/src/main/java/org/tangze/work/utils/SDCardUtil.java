package org.tangze.work.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by Administrator on 2016/11/3 0003.
 * 类描述
 * 版本
 */
public class SDCardUtil {

    /**
     * 判断sd卡是否存在
     */
    public static boolean existSDCard() {

        boolean result = false;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {


            result = true;
        }

        return result;
    }

    /**
     * 获取sd卡根路径
     */

    public static String getSDCardPath() {


        String path = "";

        path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;//获取跟目录

        return path;

    }








    /**
     * 获取sd卡的剩余容量
     */
    public static long getSDCardFreeSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize)/1024 /1024; //单位MB
    }


    /**
     * 获得sd卡总容量
     */

    public long getSDCardAllSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize)/1024/1024; //单位MB
    }



    /**
     * 获取内部存的剩余容量
     */


}
