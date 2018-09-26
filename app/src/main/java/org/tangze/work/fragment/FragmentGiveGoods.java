package org.tangze.work.fragment;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.JsonObject;


import org.tangze.work.R;
import org.tangze.work.http.TestHttp.TestHttpApi;
import org.tangze.work.http.TestHttp.TestHttpResult;
import org.tangze.work.http.TestHttp.TestHttpResultSubscriber;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.pullfresh.PullToRefreshLayout;
import org.tangze.work.widget.pullfresh.PullableListView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 配货Fragment，如果当前账号等级是终端门店，则不实例化该fragment
 */
public class FragmentGiveGoods extends BaseFragment implements OnCheckedChangeListener {

    public static final String TAG = FragmentGiveGoods.class.getSimpleName();
    /**
     * titile
     */

    private TextView tv_give_goods_fg_title;

    private ImageView iv_give_goods_fg_search;

    private LinearLayout ll_give_goods_to_search;

    private TextView tv_current_city;
    /**
     * 中间布局
     *
     * @return
     */
    private RadioButton rb_give_goods_order; //配货订单
    private RadioButton rb_my_stock;//我的库存分页


    private FragmentManager childfragmentManager;
    private FragmentTransaction childtransaction;
    private String currentFgTag; //记录当前栈顶的fragmentCHild的Tag


    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_give_goods;
    }

    @Override
    protected void getSendData(Bundle arguments) {

    }

    @Override
    protected void initView() {

        tv_give_goods_fg_title = findViewById(R.id.tv_fragment_title);
        iv_give_goods_fg_search = findViewById(R.id.iv_fragment_search);
        ll_give_goods_to_search = findViewById(R.id.ll_fragment_to_search);
        tv_current_city = findViewById(R.id.tv_current_city);
        /**
         * 中间布局
         *
         * @return
         */
        rb_give_goods_order = findViewById(R.id.rb_give_goods_order); //配货订单
        rb_my_stock = findViewById(R.id.rb_my_stock);//我的库存分页


    }

    @Override
    protected void initListener() {

        rb_give_goods_order.setOnCheckedChangeListener(this); //配货订单


        rb_my_stock.setOnCheckedChangeListener(this);//我的库存分页

    }

    @Override
    protected void onLazyLoad() {
        ll_give_goods_to_search.setVisibility(View.GONE);
        iv_give_goods_fg_search.setVisibility(View.GONE);
        tv_current_city.setVisibility(View.GONE);
        tv_give_goods_fg_title.setText(mActivity.getResources().getString(R.string.giveGoods));
        rb_give_goods_order.setChecked(true);

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fragment", TAG + " onCreate==============================");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("fragment", TAG + " ***************onDestroy********************************");
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

            switch (buttonView.getId()) {

                case R.id.rb_give_goods_order: //配货订单

                    getDiffirentFragment(FragmentGiveGoodsOrderChild.TAG);

                    break;


                /**
                 * **********************************一下是我的库存相关********************************************************
                 */

                case R.id.rb_my_stock: //我的库存

                    getDiffirentFragment(FragmentGiveGoodsMyStock.TAG);

                    break;
            }
        }
    }


    /**
     * 根据不同FragmnetTag来得到不同fragment实例
     *
     * @param fgTag
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getDiffirentFragment(String fgTag) {




        childfragmentManager = this.getChildFragmentManager();
        Fragment fragment = childfragmentManager.findFragmentByTag(fgTag); //通过Tag找到fragment
        childtransaction = childfragmentManager.beginTransaction();

        if (null == fragment) {


            if (fgTag.equals(FragmentGiveGoodsOrderChild.TAG)) {
                //配货订单fragment
                fragment = new FragmentGiveGoodsOrderChild();

            } else if (fgTag.equals(FragmentGiveGoodsMyStock.TAG)) {
                //我的库存fragment
                fragment = new FragmentGiveGoodsMyStock();

            }
            Log.i("fragmentTestChlid", "==============实例化的fragment：" + fgTag);

            Fragment currentTopFragment = childfragmentManager.findFragmentByTag(currentFgTag); //得到当前栈顶部的fragmnet

            if (null != currentTopFragment) {


                Log.i("fragmentTestChlid", "当前栈顶的fragment：---->" + currentFgTag + " 被隐藏掉" + "  新的栈顶fragment：" + fgTag);

                childtransaction.hide(currentTopFragment).add(R.id.fragment_container_child, fragment, fgTag).commit();  //如果存在则让其隐藏【解决创建后隐藏问题】
                currentFgTag = fgTag;


            } else {


                Log.i("fragmentTestChlid", "当前栈顶没有fragment！！！！！！！！，将" + fgTag + " 设置到栈顶");

                childtransaction.add(R.id.fragment_container_child, fragment, fgTag).commit();

                currentFgTag = fgTag;


            }

        } else {


            Fragment currentFragment = childfragmentManager.findFragmentByTag(currentFgTag);

            if (currentFgTag.equals(fgTag)) {

                Log.i("fragmentTestChlid", fgTag + " ***********选中它，并且已经被实例化了,但是与currentFgTag相等，执行return");
                return;
            }


            Log.i("fragmentTestChlid", fgTag + " ***********选中它，并且已经被实例化了" + " 当前栈顶的fragment：" + currentFgTag + "被隐藏掉");
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
            childtransaction.hide(from).add(R.id.fragment_container_child, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            childtransaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
    }
}
