package org.tangze.work.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/15 0015.
 * 类描述
 * 版本
 */
public class BooleanUtils {


    /**
     * String ---->Boolean的转化
     * 1.用于地址默认值转化
     */

    public static boolean getBooleanValue(String str) {

        boolean result = false;


        if("1".equals(str)) {

            result = true;
        }

        return result;
    }


    /**
     * boolean --->String的转化
     *  1.用于地址默认值转化
     * @param boo
     * @return
     */
    public static String getStringValue(boolean boo) {

        String str = "0";

        if(boo) {

            str = "1";
        }

        return  str;
    }

    /**
     * 判断qq客户端是否安装
     */

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断微信客户端是否安装
     */

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}
