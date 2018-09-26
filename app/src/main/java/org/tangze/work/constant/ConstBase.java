package org.tangze.work.constant;

/**
 * Created by Administrator on 2016/9/29 0029.
 * 类描述   基础常量类
 * 版本
 */
public class ConstBase {

    /**
     * 广告图片
     */

    public static final String ADD_1 = "http://www.maobuzhai.cn/youbayuncang/images/ad1.jpg";

    public static final String ADD_2 = "http://www.maobuzhai.cn/youbayuncang/images/ad2.jpg";


    /**
     * 产品界面测试图片
     */
    public static final String TEST_1 = "http://img3.imgtn.bdimg.com/it/u=7717414,1992486083&fm=21&gp=0.jpg";
    public static final String TEST_2 = "http://www.sinaimg.cn/dy/slidenews/2_img/2016_04/61364_1703844_117651.jpg";
    public static final String TEST_3 = "http://bak.bet007.com/Files/News/GetPic/a3736723-3338-4ad3-973b-a4aaccefcd29.jpg";
    public static final String TEST_4 = "http://img4.imgtn.bdimg.com/it/u=605550153,3985450355&fm=21&gp=0.jpg";
    public static final String TEST_5 = "http://img2.imgtn.bdimg.com/it/u=1580867429,3904024105&fm=21&gp=0.jpg";


    /**
     * 跳转到qq的url
     */
    public static final String JUMP_TO_QQ = "mqqwpa://im/chat?chat_type=wpa&uin=";


    /**
     * ordersTag
     */


    public static final String ORDER_TAG = "order_tag";

    /**
     * prduct的逗号，
     */
    public static final String COMMA = ",";


    /**
     * 获取上级收到的下级订单传入的top值
     */

    public static final int TOP_VALUE = 99;


    /**
     * 产品描述界面中的mimeType
     */

    public static final String MIME_TYPE = "text/html";


    /**
     * 产品描述界面中的解码类型
     */
    public static final String CODING_TYPE = "utf-8";


    /**
     * 空字符串常量
     */
    public static final String STRING_COLON ="";

    /**
     * 空格
     */
    public static final String STRING_SPACE =" ";


    /**
     * web的style属性
     */
    public static final String WEB_IMG_STYLE ="<style> img{ max-width:100%; height:auto;} </style>";


    /**
     * boolean leakCannery标记
     */

    public static boolean INIT_LEAK_CANERY_OR_NOT = false ;//初始化leakcanery用于检测内存泄漏，true：初始化，用于测试，false：用于正式版不初始化


    /**
     * 下拉刷新更新type
     */

    public static final int ON_FRESH = 2; //下拉刷新

    /**
     * 上拉加载刷新type
     */

    public static final int ON_LOADMORE = 1;//上拉加载


    /**
     * 上拉加载的数量
     * 目的：根据已经加载的数量来判断是否进行 加载page或刷新page的变更
     */

    public static final int SINGLE_LOAD_COUNT = 12;



}

