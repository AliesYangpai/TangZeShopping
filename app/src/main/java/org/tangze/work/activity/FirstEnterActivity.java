package org.tangze.work.activity;

import android.os.Bundle;

import org.tangze.work.R;
import org.tangze.work.utils.SpUtil;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.constant.ConstSp;


/**
 * app的入口，
 */
public class FirstEnterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_first);

//        toWelComePageOrMain();


        TestGoMain();

    }









    private void TestGoMain() {

        openActivityAndFinishItself(MainActivity.class, null);

    }





    /**
     * 界面跳转
     * 判断是否是首次安装从而进行不同界面的跳转
     */

    private void toWelComePageOrMain() {

        boolean loadOrNot = SpUtil.getInstance().getBoolenValue(ConstSp.SP_KEY_LOAD_OR_NOT, false);


        if(loadOrNot) {

            /**
             *1. 判断是否登陆，进入登陆界面还是主界面
             */

            goToLoginOrMain();

        }else {

            /**
             * 说明是首次安装
             * 进入欢迎页
             */
            goToWelcomePage();


        }



    }


    private void goToLoginOrMain() {



        if(isLogin()) {

            /**
             *1. 判断是否登陆，进入登陆界面还是主界面
             */

            openActivityAndFinishItself(MainActivity.class, null);

        } else {

            /**
             * 进入登陆界面
             */

            openActivityAndFinishItself(LoginActivity.class,null);


        }


    }

    private boolean isLogin() {

        boolean result = false;

        //判断用户是否登陆
        result = SpUtil.getInstance().getBoolenValue(ConstSp.SP_KEY_IS_LOGIN_OR_NOT, ConstSp.SP_VALUE.IS_LOGIN_DEFAULT);

        return result;

    }



    /**
     * 进入欢迎页
     */
    private void goToWelcomePage () {

        openActivityAndFinishItself(WelcomeActivity.class,null);

    }



    /**
     * 进入主界面
     */



    @Override
    protected void initViews() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void processIntent() {

    }
}
