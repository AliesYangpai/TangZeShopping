package org.tangze.work.utils;

import android.content.Intent;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/6 0006.
 * 类描述
 * 版本
 */
public class StringUtils {

    /**
     * 电话号码号段验证
     */
    		/*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/


    public static boolean isPhone(String str) {

        String strPattern = "[1][345678]\\d{9}";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 将List<String>转化成为String[];
     * @param list
     * @return
     */
    public static String[] getStringArray(List<Integer> list) {

        String [] arry = new String[list.size()];

        for(int i = 0 ; i < list.size() ; i++) {

            arry[i] = String.valueOf(list.get(i));

        }


        return arry;
    }


    /**
     * 将null转化成为""空字符串；
     * @param str
     * @return
     */
    public static String excludeNull(String str) {
        String s = "";
        if(null == str || "null".equals(str)){

            str = s;
        }
        return str;
    }


    /**
     *  替换掉html中的style
     * @param html
     * @return
     */
    public static String replaceImgStyle(String html){
        String reg = "style=\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(html);
        return matcher.replaceAll("");
    }


    /**
     * 获取手机ip地址
     * @return
     */
    public static String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

}
