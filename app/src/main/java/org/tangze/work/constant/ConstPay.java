package org.tangze.work.constant;

/**
 * Created by Administrator on 2017/4/21.
 * 类描述
 * 版本   用于支付的自定义常量
 */




/**
 * 限制规则：
 * alipay : 1-64 位，  支付宝支付
 * wx : 2-32 位，     微信支付
 * bfb : 1-20 位，     百度钱包
 * upacp : 8-40 位，银联支付
 * yeepay_wap :1-50 位， 易宝手机网页支付
 * jdpay_wap :1-30 位，京东手机网页支付
 * qpay :1-30 位，QQ 钱包
 * cmb_wallet :10 位纯数字字符串。   招行一网通
 * 注：除  cmb_wallet 外的其他渠道推荐使用 8-20 位，要求数字或字母，不允许特殊字符)。
 */

//            map.put("order_no","a515s1dfdssfssasd"); //生成一个随机字符串，注意要参看API 有的支付渠道对字符串的长度有限制
//                    map.put("amount",100);
//                    map.put("app","app_Sa1GGSCW5q9GqXT4");
//                    map.put("channel","alipay");
//                    map.put("currency","cny");
//                    map.put("client_ip","127.0.0.1");
//                    map.put("subject","支付宝测试商品标题");
//                    map.put("body", "支付宝测试商品描述");

public class ConstPay {



    public static final String COUNT_TO_PAY = " 数量：";

    public class KEY{
        public static final String ORDER_NO = "order_no"; //随机key
        public static final String AMOUNT = "amount";   //货币单位 “分”
        public static final String APP = "app";   //应用标识key
        public static final String CHANNEL = "channel"; //支付渠道
        public static final String CURRENCY = "currency"; //货币种类，比如 人民币
        public static final String SUBJECT = "subject"; //商品标题
        public static final String BODY = "body"; //商品描述
        public static final String CLIENT_IP = "client_ip"; //手机ip地址
    }



    public class VALUE{

        public static final String APP_SIGN = "app_Sa1GGSCW5q9GqXT4"; //ping++应用标识
        public static final String ALIPAY = "alipay"; //支付宝渠道
        public static final String WX = "wx";//微信渠道
        public static final String CURRENCY = "cny"; //货币种类
        public static final String COMMON_SUBJECT = "唐泽商品";

    }


}
