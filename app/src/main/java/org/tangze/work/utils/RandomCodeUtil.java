package org.tangze.work.utils;

import java.util.Random;

/**
 * Created by Administrator on 2016/10/18 0018.
 * 类描述
 * 版本
 */
public class RandomCodeUtil {


    //随机数数组
    private static final char[] CHARS = {
            '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',
            'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };


    //default settings
    //验证码默认随机数的个数
    private static final int DEFAULT_CODE_LENGTH = 17;



    //生成验证码
    public static String createCode() {

        Random random = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }



    /**
     *     根据指定长度生成纯数字的随机数
     */

    public static void createNumByLenth(int length) {
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();
        for(int i=0;i<length;i++)
        {
            sb.append(rand.nextInt(10));
        }
        String data=sb.toString();
        System.out.println(length+" random data: "+data);
    }



    /**
     *     生成随机数字和字母
     */

    public static String creatNumAndLetter(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


}
