package org.tangze.work.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/9/22 0022.
 * 类描述
 * 版本
 */
public class ToastUtil {



    public static void showMsg(Context context, String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static void showResMsg(Context context, int id) {

         Toast.makeText(context,context.getString(id),Toast.LENGTH_SHORT).show();

    }






//    /**
//     * 短时间显示Toast
//     *
//     * @param context
//     * @param message
//     */
//    public static void showShort(Context context, int message) {
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }
//
//    /**
//     * 长时间显示Toast
//     *
//     * @param context
//     * @param message
//     */
//    public static void showLong(Context context, CharSequence message) {
//            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//    }
//
//    /**
//     * 长时间显示Toast
//     *
//     * @param context
//     * @param message
//     */
//    public static void showLong(Context context, int message) {
//            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//    }
//
//    /**
//     * 自定义显示Toast时间
//     *
//     * @param context
//     * @param message
//     * @param duration
//     */
//    public static void show(Context context, CharSequence message, int duration) {
//            Toast.makeText(context, message, duration).show();
//    }
//
//    /**
//     * 自定义显示Toast时间
//     *
//     * @param context
//     * @param message
//     * @param duration
//     */
//    public static void show(Context context, int message, int duration) {
//            Toast.makeText(context, message, duration).show();
//    }
}
