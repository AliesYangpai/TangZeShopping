package org.tangze.work.constant;

/**
 * Created by Administrator on 2016/9/19 0019.
 * 类描述  Sp的常量
 * 版本
 */
public class ConstSp {

    public static final String TZ_SP_NAME = "TZ_sp";

    public static final int SP_OPEN = 0;

    /**
     * 首次启动的限制
     */
    public static final String SP_KEY_LOAD_OR_NOT = "load_or_not";

    /**
     * 用户是否登陆
     */

    public static final String SP_KEY_IS_LOGIN_OR_NOT = "is_login_or_not";


    /**
     * 用户类型标记
     * 0、厂商
     * 1、省待
     * 2、市代
     * 3、终端店铺
     */
    public static final String SP_KEY_USER_TYPE = "user_type";
    public static final int SP_USER_TYPE_DEFULT_VALUE = 3;


    /**
     * 上级用户qq
     */

    public static final String SP_KEY_TOP_QQ = "top_qq";


    /**
     * 相关value
     */
    public class SP_VALUE {


        /**
         * 用户已登陆
         */
        public static final boolean IS_LOGIN = true;


        /**
         * 用户默认登陆状态（默认退出）
         */
        public static final boolean IS_LOGIN_DEFAULT = false;


        /**
         * 上级qq默认值设置为"no_qq"
         */
        public static final String TOP_QQ_DEFAULT = "no_qq";


        /**
         * 当前用户等级清空
         * （账号等级 0:厂商 1：省代 2：市代 3：终端店铺）
         * 这里将默认等级设置为4
         */

        public static final int USER_TYPE_DEFAULT = 3;

    }

}
