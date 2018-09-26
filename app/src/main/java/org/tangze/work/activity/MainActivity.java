package org.tangze.work.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.gson.JsonArray;
import com.umeng.socialize.UMShareAPI;


import org.litepal.crud.DataSupport;
import org.litepal.util.Const;
import org.tangze.work.R;

import org.tangze.work.Test.TestCons;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.constant.ConstSp;
import org.tangze.work.constant.ConstUser;
import org.tangze.work.entity.Product;
import org.tangze.work.fragment.FragmentClassify;
import org.tangze.work.fragment.FragmentGiveGoods;
import org.tangze.work.fragment.FragmentMain;
import org.tangze.work.fragment.FragmentShoppingCar;
import org.tangze.work.fragment.FragmentUserCenter;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.SpUtil;
import org.tangze.work.utils.StringUtils;
import org.tangze.work.utils.ToastUtil;

import java.util.List;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {


    /**
     * 底部
     *
     * @param
     */
    private RadioGroup rg_main_bottom; // 整体rg
    private RadioButton rb_main;        //主页
    private RadioButton rb_classify;    //分类
    private RadioButton rb_shoppingCar; //购物车
    private RadioButton rb_giveGoods;   //配货 【当用户等级是终端门店时，隐藏该按钮】
    private RadioButton rb_mine;        //我的


    /**
     * fragment相关
     */
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private String currentFgTag; //记录当前栈顶的fragment的Tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }








    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


    }


    @Override
    protected void initViews() {


        /**
         * 底部
         * @param
         */
        rg_main_bottom = (RadioGroup) findViewById(R.id.rg_main_bottom);
        rb_main = (RadioButton) findViewById(R.id.rb_main);
        rb_classify = (RadioButton) findViewById(R.id.rb_classify);
        rb_shoppingCar = (RadioButton) findViewById(R.id.rb_shoppingCar);
        rb_giveGoods = (RadioButton) findViewById(R.id.rb_giveGoods);
        rb_mine = (RadioButton) findViewById(R.id.rb_mine);
        showGiveGoodsByUserGrade();





    }

    @Override
    protected void initListener() {
        /**
         * 底部
         * @param
         */
        rb_main.setOnCheckedChangeListener(this);
        rb_classify.setOnCheckedChangeListener(this);
        rb_shoppingCar.setOnCheckedChangeListener(this);
        rb_giveGoods.setOnCheckedChangeListener(this);
        rb_mine.setOnCheckedChangeListener(this);
        rb_main.setChecked(true);
    }

    @Override
    protected void processIntent() {


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        Log.i("sharexxx", " onActivityResult回调执行22222"+" requestCode:"+requestCode+" resultCode:"+resultCode);


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {


            switch (buttonView.getId()) {

                case R.id.rb_main:
                    getDiffirentFragment(FragmentMain.TAG);

                    break;

                case R.id.rb_classify:

                    getDiffirentFragment(FragmentClassify.TAG);
                    break;
                case R.id.rb_shoppingCar:

                    /**
                     * 根据登陆情况来进行界面跳转
                     * 如果未登录，先进入到登陆界面
                     */
                    getDiffirentFragment(FragmentShoppingCar.TAG);

                    break;
                case R.id.rb_giveGoods:

                    /**
                     * 根据登陆情况来进行界面跳转
                     * 如果未登录，先进入到登陆界面
                     */
                    getDiffirentFragment(FragmentGiveGoods.TAG);

                    break;
                case R.id.rb_mine:


                    /**
                     * 根据登陆情况来进行界面跳转
                     * 如果未登录，先进入到登陆界面
                     */


                    getDiffirentFragment(FragmentUserCenter.TAG);

                    break;
            }

        }

    }










    /**
     * 根据不同FragmnetTag来得到不同fragment实例
     *
     * @param fgTag
     */
    private void getDiffirentFragment(String fgTag) {

        fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(fgTag); //通过id找到fragment
        transaction = fragmentManager.beginTransaction();

        if (null == fragment) {


            if (fgTag.equals(FragmentMain.TAG)) {
                //主页fragment
                fragment = new FragmentMain();

            } else if (fgTag.equals(FragmentClassify.TAG)) {
                //分类fragment
                fragment = new FragmentClassify();

            } else if (fgTag.equals(FragmentShoppingCar.TAG)) {

                fragment = new FragmentShoppingCar();

            } else if (fgTag.equals(FragmentGiveGoods.TAG)) {
                fragment = new FragmentGiveGoods();


            } else if (fgTag.equals(FragmentUserCenter.TAG)) {
                fragment = new FragmentUserCenter();

            }
            Log.i("fragmentTest", "==============实例化的fragment：" + fgTag);

            Fragment currentTopFragment = fragmentManager.findFragmentByTag(currentFgTag); //得到当前栈顶部的fragmnet

            if (null != currentTopFragment) {


                Log.i("fragmentTest", "当前栈顶的fragment：---->" + currentFgTag + " 被隐藏掉" + "  新的栈顶fragment：" + fgTag);

                transaction.hide(currentTopFragment).add(R.id.fragment_container, fragment, fgTag).commit();  //如果存在则让其隐藏【解决创建后隐藏问题】
                currentFgTag = fgTag;


            } else {


                Log.i("fragmentTest", "当前栈顶没有fragment！！！！！！！！，将" + fgTag + " 设置到栈顶");

                transaction.add(R.id.fragment_container, fragment, fgTag).commit();

                currentFgTag = fgTag;


            }

        } else {


            Fragment currentFragment = fragmentManager.findFragmentByTag(currentFgTag);

            if (currentFgTag.equals(fgTag)) {

                Log.i("fragmentTest", fgTag + " ***********选中它，并且已经被实例化了,但是与currentFgTag相等，执行return");
                return;
            }


            Log.i("fragmentTest", fgTag + " ***********选中它，并且已经被实例化了" + " 当前栈顶的fragment：" + currentFgTag + "被隐藏掉");
            switchFragment(currentFragment, fragment);

            currentFgTag = fgTag;


        }


    }


    /**
     * fragment隐藏切换
     *
     * @param from
     * @param to
     */
    public void switchFragment(Fragment from, Fragment to) {
        if (!to.isAdded()) {    // 先判断是否被add过
            transaction.hide(from).add(R.id.fragment_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
    }





    /**
     * 登陆成功后，根据用户等级进行界面变更
     */
    private void showGiveGoodsByUserGrade() {
        int grade = SpUtil.getInstance().getIntValue(ConstSp.SP_KEY_USER_TYPE, ConstSp.SP_VALUE.USER_TYPE_DEFAULT);
        switch (grade) {

            case ConstUser.USER_GRADE_PROVINCE: // 省代
                rb_giveGoods.setVisibility(View.VISIBLE);
                break;

            case ConstUser.USER_GRADE_CITY://市代

                rb_giveGoods.setVisibility(View.VISIBLE);
                break;

            case ConstUser.USER_GRADE_STORE://终端店

                rb_giveGoods.setVisibility(View.GONE);
                break;

        }

    }


    /**
     * 根据登陆界面回传结果显示不同的Fragment
     */

//    private void showTheFragmentByLoginBack(String tag) {
//
//
//        setAllRbTagFalse();
//
//
//        if (FragmentMain.TAG.equals(tag)) {
//
//            rb_main.setChecked(true);
//
//        } else if (FragmentClassify.TAG.equals(tag)) {
//
//            rb_classify.setChecked(true);
//
//        } else if (FragmentShoppingCar.TAG.equals(tag)) {
//
//            rb_shoppingCar.setChecked(true);
//
//
//        } else if (FragmentGiveGoods.TAG.equals(tag)) {
//
//            rb_giveGoods.setChecked(true);
//
//        } else {
//
//            rb_mine.setChecked(true);
//        }
//
//
//    }


    /**
     * 设置所有选中项是false
     */
//    private void setAllRbTagFalse() {
//
//        rg_main_bottom.clearCheck();
//
//
//        Log.i("fragmentTest","所有按钮状态还原："+rb_main.isChecked()+" "+rb_classify.isChecked()+" "+rb_shoppingCar.isChecked()+" "+rb_giveGoods.isChecked()+" "+rb_mine.isChecked());
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();

    }
}
