package org.tangze.work.utils;

/**
 * Created by Administrator on 2016/10/12 0012.
 * 类描述
 * 版本
 */
public class JsonUtil {


    public static Integer translateToInt (String string) {

        Integer num = 0;


        if(string.equals("null")||string==null) {

            return num;

        }else {

            num = Integer.valueOf(string);

        }

        return num;

    }
}
