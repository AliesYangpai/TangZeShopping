package org.tangze.work.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.JsonArray;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.adapter.ProductPicPagerAdapter;
import org.tangze.work.constant.ConstAddress;
import org.tangze.work.constant.ConstBase;
import org.tangze.work.constant.ConstCollection;
import org.tangze.work.constant.ConstIntent;
import org.tangze.work.constant.ConstOrder;
import org.tangze.work.constant.ConstSp;
import org.tangze.work.entity.Address;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.temp.ProductOrder;
import org.tangze.work.entity.ShoppingCar;
import org.tangze.work.entity.User;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.BooleanUtils;
import org.tangze.work.utils.ImgUtil;
import org.tangze.work.utils.InputFilterMinMax;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.SpUtil;
import org.tangze.work.utils.StringUtils;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.PageIndexView;
import org.tangze.work.widget.webView.WebClientPicDescrib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductDetialActivity extends BaseActivity implements View.OnClickListener {


    /**
     * title
     */
    private ImageView iv_product_detail_back;
    private TextView tv_product_detial_title;
    private ImageView iv_common_search;

    /**
     * 中间viewPager
     */
    private ViewPager vp_product_pic;
    private PageIndexView pic_product_pic_indexpage;
    private List<ImageView> images = new ArrayList<>();//底部的imageView点bot
    private int nPageIndex = 0;
    private ProductPicPagerAdapter productPicPagerAdapter;
    /**
     * 相关详情
     */
    private TextView tv_product_name_in_productDetial; //产品名称（标题）
    private TextView tv_local_price_in_productDetail; //产品价格
    private LinearLayout ll_collection;//点击收藏
    private ImageView iv_collect_in_productDetial;//图片变化
    private RadioButton rb_custom_service;//点击呼叫客服
    private TextView tv_product_stock;//产品库存
//    private TextView tv_product_description;//产品描述

    private WebView wv_picAndDescription;// 图文详情

    /**
     * 底部layout
     */
    private EditText ed_buy_count;
    private LinearLayout ll_add_to_shopping_car;
    private LinearLayout ll_buy_it;


    /**
     * 相关传递数据
     */
    private Intent intent;
    private Bundle bundle;
    private Product currentProduct; //真正用于界面显示的product
    private Product  passProduct; //仅仅用于传递
    private User user;


    /**
     * 广播，用于实现订单提交成功后界面关闭
     */

    private DeliverOrderSuccessReceiver deliverOrderSuccessReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detial);
        initData();


    }




    @Override
    protected void initViews() {

        /**
         * title
         *
         */
        iv_product_detail_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_product_detial_title = (TextView) findViewById(R.id.tv_common_title);
        tv_product_detial_title.setText(getString(R.string.product_detail_title));
        iv_common_search = (ImageView) findViewById(R.id.iv_common_search);
        iv_common_search.setVisibility(View.GONE);


        /**
         * 中间viewPager
         *
         */
        vp_product_pic = (ViewPager) findViewById(R.id.vp_product_pic);
        pic_product_pic_indexpage = (PageIndexView) findViewById(R.id.pic_product_pic_indexpage);
        productPicPagerAdapter = new ProductPicPagerAdapter(images);
        vp_product_pic.setAdapter(productPicPagerAdapter);
        pic_product_pic_indexpage.seticonWidth(15);


        /**
         * 相关详情
         */
        tv_product_name_in_productDetial = (TextView) findViewById(R.id.tv_product_name_in_productDetial); //产品名称
        tv_local_price_in_productDetail = (TextView) findViewById(R.id.tv_local_price_in_productDetail); //产品说明

          ll_collection = (LinearLayout) findViewById(R.id.ll_collection);//点击收藏
          iv_collect_in_productDetial = (ImageView) findViewById(R.id.iv_collect_in_productDetial);//图片变化

        rb_custom_service = (RadioButton) findViewById(R.id.rb_custom_service);//点击呼叫客服
//        tv_product_description = (TextView) findViewById(R.id.tv_product_description);//产品描述


        tv_product_stock = (TextView) findViewById(R.id.tv_product_stock); //产品库存




        wv_picAndDescription = (WebView) findViewById(R.id.wv_picAndDescription); //图文详情
        wv_picAndDescription.getSettings().setJavaScriptEnabled(true);
        wv_picAndDescription.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv_picAndDescription.getSettings().setDomStorageEnabled(true);
        wv_picAndDescription.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv_picAndDescription.setBackgroundColor(0x00000000);

        wv_picAndDescription.getSettings().setLoadWithOverviewMode(true);

        wv_picAndDescription.setWebViewClient(new WebClientPicDescrib(this));








        /**
         * 底部layout
         */
        ed_buy_count = (EditText) findViewById(R.id.ed_buy_count);

        ed_buy_count.setFilters(new InputFilter[]{ new InputFilterMinMax(ConstOrder.MIN_COUNT, ConstOrder.MAX_COUNT)});

        ed_buy_count.setText(ConstOrder.MIN_COUNT);
        ll_add_to_shopping_car = (LinearLayout) findViewById(R.id.ll_add_to_shopping_car);
        ll_buy_it = (LinearLayout) findViewById(R.id.ll_buy_it);

    }








    @Override
    protected void initListener() {


        /**
         * title
         *
         */
        iv_product_detail_back.setOnClickListener(this);


        /**
         * 中间viewPgaer
         */
        vp_product_pic.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                pic_product_pic_indexpage.setCurrentPage(nPageIndex = arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


        /**
         * 相关详情
         */


        ll_collection.setOnClickListener(this);
        rb_custom_service.setOnClickListener(this);//点击呼叫客服


        /**
         * 底部layout
         */
        ll_add_to_shopping_car.setOnClickListener(this);
        ll_buy_it.setOnClickListener(this);
    }

    @Override
    protected void processIntent() {

        intent = this.getIntent();

        if(null!=intent) {

            bundle = intent.getExtras();

            passProduct = (Product) bundle.getSerializable(ConstIntent.BundleKEY.PRODUCT_TO_DETIAL);
        }

    }



    private void initData() {

//        registerDeliverOrderSuccessBroadCast(); //订单提交成功的广播注册


         user = DataSupport.findFirst(User.class);


        int user_Id = user.getUser_id();

        final int product_id = passProduct.getProduct_id();

        /**
         * 获取最新的单个产品数据
         */

        Map map = ParaUtils.getSingleProduct(user_Id, product_id);
        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.REQUEST_SINGLE_PRODUCT, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {



                //wen订单提交新修改
                Log.i(HttpConst.SERVER_BACK, "===获取单个产品数据返回成功===" + jsonArray.toString());

                Product product = HttpReturnParse.getInstance().parseSingleProduct(jsonArray);

                showServerBackProductToUi(product);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {


                ToastUtil.showMsg(getApplicationContext(), resultCode + resultMessage);
                Log.i(HttpConst.SERVER_BACK, "===获取单个产品数据返回失败==" + resultCode + " " + resultMessage);

            }

            @Override
            public void _onError(String error) {
                ToastUtil.showMsg(getApplicationContext(), error);


                Log.i(HttpConst.SERVER_BACK, "===获取单个产品数据返回失败===" + error);
            }
        });

    }



    /**
     * 根据Server的返回更新Ui
     * //wen订单提交新修改
     */
    private void showServerBackProductToUi(Product product) {



        if(null != product) {

            currentProduct = product;

            tv_product_name_in_productDetial.setText(currentProduct.getProductName()); //产品名称
            tv_local_price_in_productDetail.setText(currentProduct.getLocalPrice()); //产品本地价格


            tv_product_stock.setText(String.valueOf(currentProduct.getStock()));


            ed_buy_count.setFilters(new InputFilter[]{new InputFilterMinMax(ConstOrder.MIN_COUNT, String.valueOf(currentProduct.getStock()))});

//            tv_product_description.setText(currentProduct.getProductDescribe());


            /**
             * 测试........
             */
//            String data2 = "<p >【美骑编辑按】货运自行车在当下并不是热点话题，及不上智能自行车十分之一，但它折射出了中国消费者不同于别的消费习惯与中国自行车市场的独特性。</p><hr /><p ><span >美骑网的车友王瑶最近日子过得很舒坦，按照朋友们的话说，那才叫真正的生活。具体说起来，王瑶正和他老婆一人骑着一辆货运自行车行走川藏地区，不以拉萨为目标，且行且停，累了就住下来生火做饭、和当地人串门拉家常。不过除了旅行过日子，王瑶还有另一个目的，那就是测试他亲自设计制作的货运自行车。</span><br /></p><p >王瑶也许是中国第一个吃螃蟹的人，他的梦想就是有一天货运自行车能在中国风靡起来，成为城市符号。而现在的状况是，货运自行车在国外风生水起，在国内却几乎无人问津，境遇比起电助力车好不到哪去。</p><p > <img src=\\\"/d/file/tour/inspiration/2015-10-15/086085389f1a9882e4a3f73ea1836843.jpg\\\" data-width=\\\"532\\\" data-height=\\\"788\\\"    /></p><p >王瑶设计的两辆货运自行车</p><p >所谓货运自行车，英文名叫cargo bike，顾名思义是可以装载货物的自行车。不同于普通自行车或电动车，货运自行车首先载重量更大，其次允许人力蹬踩，再者就是外形好看。国内或许还没怎么见到这种车的身影，但在欧洲，货运自行车使用率极高，结构形式也多种多样。例如2014年在哥本哈根这座面积不到北京城0.6%的城市，就有25000-35000辆货运自行车（数据来源：欧洲自行车协会）。 </p><p ><img src=\\\"/d/file/tour/inspiration/2015-10-15/3acbad02cd4b5df8abd355a8ee3ff720.jpg\\\" data-width=\\\"698\\\" data-height=\\\"469\\\"   /></p><p >目前常见的几种货运自行车形式</p><hr /><p ><strong >货运自行车的3大优点</strong><br /></p><p >1．载重量大、方便安全，在城市的短途运输中大有可为。</p><p >同普通自行车相比，货运自行车可以利用同样的人力运输更多的货物——通常能装载200kg以上，而且更方便安全。</p><p >同汽车相比，货运自行车更节能环保而且经济实惠，小体积的它在小街窄巷同样通行无阻——这可是汽车做不到的。</p><p >如果加上一块电池，靠电力驱动的货运自行车在爬坡、中短途运输上更具优势。</p><p >当然这并不是说货运自行车就可以替代汽车，例如洗衣机、电冰箱之类的大件商品就不能指望自行车了。但对于很多小货物，在实现快递公司的“最后一公里”配送上，货运自行车大有可为。 </p><p ><img src=\\\"/d/file/tour/inspiration/2015-10-15/8166ca49401ff8150c00e9cc53f9a847.jpg\\\" data-width=\\\"675\\\" data-height=\\\"750\\\"   /></p><p >DHL中外运敦豪快递采购了大量货运自行车</p><p >2．带小孩短途出行的最佳亲子交通工具。</p><p >对于父母来说，那种把车斗放在前头的自行车，免去了很多麻烦：他们可以把小孩、玩具、零碎物品都放在货舱里，然后一边骑车锻炼，一边监督坐在货舱里的小屁孩别玩出什么幺蛾子，又不用像坐公交地铁一样担心熊孩子哭闹招人烦；而对于小孩来说，车斗是个很安全、舒适又有趣的半开放空间。</p><p >只要尺寸合适，年轻的父母还可以把婴儿车放在货舱里，到达目的地后再把婴儿车取下来推走。 </p><p ><img src=\\\"/d/file/tour/inspiration/2015-10-15/554401896dfc40baf7d03835dbefdd62.jpg\\\" data-width=\\\"750\\\" data-height=\\\"382\\\"   /></p><p >健身、载货、带小孩，一车三用</p><p >3．让商品贩售更便利。</p><p >对于街头流动小商贩来讲，一辆外形够酷的自行车不仅方便摆售，而且打扮靓丽的外表可以招来更多关注。车斗比较大的自行车更能实现“仓储销售一体化”。 </p><p ><img src=\\\"/d/file/tour/inspiration/2015-10-15/9551b4bbe0291c15dc7acbf29e6d5081.jpg\\\" data-width=\\\"750\\\" data-height=\\\"500\\\"   /></p><p >街头卖啤酒的货运自行车</p><p >上述种种优点，使得货运自行车在西方国家蔚为风行。而且一辆好看的货运自行车情怀满满，自带亮点，稍微包装一下还蛮吸引眼球的。目前，欧洲正在探讨如何让货运自行车发挥更多功能，有相关的组织在为之努力，并寻求更大的盈利点与发展空间。国外有专门的货运自行车车迷组织，定期组织改装等的讨论。</p><p >然而当我们放眼国内，会发现照片里的那些货运自行车，在中国完全属于非主流。</p><hr /><p ><strong >看起来很高大上的货运自行车在中国为什么不吃香？</strong><br /></p><p >据了解，目前目前这种货运自行车在国内是有不少厂家生产，但都是出口的外单货，不在国内销售。在国内当然不是没有货运自行车，但它们有个更接地气的名字——人力三轮车。</p><p >1．中国没有这种cargobike文化。</p><p >在国外有为数不少的货运自行车协会/组织，每年举办国际货运自行车节（international cargo bike festival），大伙儿骑着各式各样的货运自行车上街亮相，而国内并没有相应的氛围与文化。</p><p > <img src=\\\"/d/file/tour/inspiration/2015-10-15/ca7b3e9b10fda54318820092d3356b9f.jpg\\\" data-width=\\\"547\\\" data-height=\\\"382\\\"    /></p><p >国外的货运自行车节</p><p >2．利润空间有限，市场前景不太看好，所以做的人少。</p><p >一辆普通的两轮车斗中置式货运自行车，售价在500美元以上，通常带电助力驱动的两轮/三轮货运自行车售价都在1000～3000美元甚至更高，而在国内，最常见的普通人力三轮车大约500元/辆，稍微讲点设计概念或者说骑乘舒适点的价格会去到2000～3000元，销量一般。</p><p >显然对于以赢利为目的的品牌商来讲，这样的价格区间并没有多少利润。</p><p >毕竟，中国市场对自行车的要求普遍是结实耐用、容易维修、价格划算、一物多用。也正是因为这种消费观念，山地车才会成为中国销量最高的车型，山马党才会大行其道。我们不能指责什么，一方面这是发展中国家的人之常情，另一方面根深蒂固的消费习惯短期之内还无法大幅改变。你说中国人没钱买自行车吗？其实不是，而是因为他不认可这个价值。</p><p > <img src=\\\"/d/file/tour/inspiration/2015-10-15/b0783e4946c00976f389dbf09f90abd8.jpg\\\" data-width=\\\"740\\\" data-height=\\\"466\\\"    /></p><p >国内最常见的货运自行车，300多块钱一辆，淘宝上的全名叫做“老年人力三轮自行车载重型”</p><p >3．政策问题</p><p >骑一辆很酷的货运自行车进行街头贩售，或许能迅速吸引到爱新鲜的顾客，但城管也随之而来。</p><p >装上电池、靠电力驱动——它是机动车还是非机动车？在国内许多城市，是禁止电动自行车上路的。</p><p >4．停车位与自行车道问题</p><p >货运自行车的体积是比普通自行车更大一些，但目前国内有限的自行车道与停车位尚不能满足普通自行车的需求。</p><hr /><p ><span >以上种种限制决定了国外那种造型各异、装载量大而安全的货运自行车在中国就是流行不起来，而外表普通的人力三轮车随处可见。</span><br /></p><p >但笔者以为，货运自行车在中国还是有未来的。利好消息是，越来越多的各级政府都在鼓励民众骑行健身，并大力发展特色旅游项目。那么兼具文化情怀与形象特色的货运自行车是有可能作为一个突破口的。</p><p >我们不妨畅想一下货运自行车在中国的未来：</p><p >-在XX车友节、音乐节之类派对上，充满个性的货运自行车是不是更容易博眼球？</p><p >-国内快递公司在“最后一公里”送货环节使用外形有趣的货运自行车是不是还能做街头流动广告？</p><p >-风景区租车点多几辆可供小孩乘坐的亲子货运自行车，是不是更受欢迎？</p><p >-城市租车点多一些这样的公共自行车，是不是能给市民提供更多便利？</p><p ><img src=\\\"/d/file/tour/inspiration/2015-10-16/50fecb09b278d56b5e307b7dbc0e2bad.jpg\\\" data-width=\\\"569\\\" data-height=\\\"700\\\"   /></p><p >欧洲自行车展上，一辆以鹿角装饰的货运自行车，吸引来大小相机一阵狂拍</p><p >或许有人会说，以目前普通自行车尚在争取路权的阶段，怎能奢望货运自行车流行起来？我们不敢做什么预言，但已经有事实证明货运自行车的种种优点，我们希望，能看到更多的尝试。</p><p >再说回到本文开头提到的车友王瑶，他以一己之力试着开发一款适合中国国情的货运自行车产品，以目前在318国道上实地骑行测试的结果来看，这样的车够结实，走318都没问题，那么日常城市骑行更不在话下。当然，王瑶并不太建议拿货运自行车来做长途旅行，由于维修、续航等问题，货运自行车最大的舞台在城市，包括日常骑行、短途轻旅行，等等。</p><p >据笔者了解，现在国内关注这种货运自行车的骑友其实为数不少，那么如果是你，你会喜欢吗？多少价位你能接受呢？</p><p ><strong>责任编辑  BAMBOO</strong></p>";
//            String data = "<p style=\"text-align: center;\">天使轮胎<img src=\"http://www.maobuzhai.cn/youbayuncang/images/ad1.jpg\" title=\"1478484804.jpg\" alt=\"2.jpg\"/></p>";
//            String data = "<img src=\"http://www.ub-7.cn/ueditor/php/upload/image/20161212/1481512278.jpg\" title=\"1481509758.jpg\" alt=\"详情.jpg\"  width=\"100%\"/>";
//            wv_picAndDescription.loadDataWithBaseURL(null, data,ConstBase.MIME_TYPE,ConstBase.CODING_TYPE, null);



            String html  = currentProduct.getProductDescribe();
            if(TextUtils.isEmpty(html)){
                html ="";
            }else{
                html = StringUtils.replaceImgStyle(html);
            }

            html = ConstBase.WEB_IMG_STYLE+html;
            wv_picAndDescription.loadDataWithBaseURL(null, html,ConstBase.MIME_TYPE,ConstBase.CODING_TYPE, null);


//            wv_picAndDescription.loadDataWithBaseURL(null, currentProduct.getProductDescribe(),ConstBase.MIME_TYPE,ConstBase.CODING_TYPE, null);





            Log.i("dddd",currentProduct.getProductDescribe());
            isCollectedOrNotToShowOnView();//设置是否收藏
            setPicToBigImage();//设置产品图片信息

        }
    }




    private String replaceImgStyle(String html){
        String reg = "style=\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(html);
        return matcher.replaceAll("");
    }



    /**
     * 判断该产品是否被收藏
     */
    private void isCollectedOrNotToShowOnView() {

       String collected =  currentProduct.getIsCollect();

        if (ConstCollection.IS_COLLECTED.equals(collected)) {

            iv_collect_in_productDetial.setImageResource(R.drawable.img_collection_on);

        }else {


            iv_collect_in_productDetial.setImageResource(R.drawable.img_collection_off);
        }

    }


    /**
     * 将产品图片信息设置到界面上
     */
    private void setPicToBigImage() {

        String rawPic = currentProduct.getPicture();

        String longPic = rawPic.substring(0, rawPic.length()); //去掉引号


        if(longPic.contains(ConstBase.COMMA)) {


           String[] picArray =  longPic.split(ConstBase.COMMA);

            for (int i = 0; i < picArray.length; i++) {
                images.add(getView(picArray[i]));
                Log.i("picArray", "单个图片路径：" + picArray[i]);
            }

            pic_product_pic_indexpage.setTotalPage(picArray.length);
            pic_product_pic_indexpage.setCurrentPage(nPageIndex);
            productPicPagerAdapter.notifyDataSetChanged();

        }else {
            images.add(getView(longPic));
            pic_product_pic_indexpage.setTotalPage(1);
            pic_product_pic_indexpage.setCurrentPage(nPageIndex);
            productPicPagerAdapter.notifyDataSetChanged();

            Log.i("picArray", "单个图片路径：" + longPic);
        }
    }

    /**
     * wen测试
     *
     * @param picUrl
     * @return
     */
    public ImageView getView(String picUrl) {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new ViewPager.LayoutParams());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(getApplicationContext(), "点击图片");

            }
        });

        ImgUtil.getInstance().getImgFromNetByUrl(picUrl, imageView, R.drawable.img_hold_seat_banner);

        return imageView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_common_back:
                this.finish();
                break;


            case R.id.ll_add_to_shopping_car:


                addToShoppingCarOrNot();

                break;


            case R.id.ll_collection:

                collectItOrCancel();

                break;

            case R.id.rb_custom_service:

                goToCustomerChat();
                break;


            case R.id.ll_buy_it:
                goToSettlement();
                break;

        }
    }


    /**
     * 点击立即订购进入到结算界面
     */
    private void goToSettlement() {


//        if(!hasDefultAddressOrNot()) {
//
//            ToastUtil.showResMsg(this,R.string.address_First);
//            return;
//        }

        if(isEnterCount()) {
            Bundle bundle = new Bundle();
            ProductOrder productOrder = getNewProductOrder();
            bundle.putSerializable(ConstIntent.BundleKEY.PRODUCT_ORDER_TO_SETTLEMENT, productOrder);
            openActivity(SettlementActivity.class, bundle);

        }else {


            ToastUtil.showMsg(getApplicationContext(),getApplicationContext().getString(R.string.product_count_first));

        }

    }


    /**
     * 判是否设置默认地址
     * @return
     */
//    private boolean hasDefultAddressOrNot() {
//
//        boolean result = false;
//
//        List<Address> addresses = DataSupport.where("isdefault = ?", ConstAddress.IS_DEFUALT_ADDR).find(Address.class);
//
//        if(null!= addresses && addresses.size() > 0) {
//
//            result = true;
//
//        }
//
//        return result;
//
//    }


    /**
     * 是否输入产品数量验证
     */

    private boolean isEnterCount(){

        boolean result = false;

        if(!TextUtils.isEmpty(ed_buy_count.getText().toString())) {

            result = true;
        }

        return result;

    }


    /**
     * 生成一个productOrder
     * @return
     */
    private ProductOrder getNewProductOrder() {



//wen订单提交新修改
        ArrayList<Product> productArrayList = new ArrayList<>();
        productArrayList.add(currentProduct);   //将当前商品加入集合
        int user_id = user.getUser_id();
        int top_id = user.getTopId();
        Map<Integer,Integer> mapBuyCount = new HashMap<>();
        mapBuyCount.put(currentProduct.getProduct_id(),Integer.valueOf(ed_buy_count.getText().toString())); //购买数量的map
        //将商品的id与商品的购买数量对应起来


        ProductOrder productOrder = new ProductOrder();
        productOrder.setProducts_transmit(productArrayList);
        productOrder.setUser_id(user_id);
        productOrder.setTop_id(top_id);
        productOrder.setMap(mapBuyCount);

        return productOrder;

    }



    /**
     * 判断该产品是不是已经加入到购物车
     */

    private boolean isAddToShopCar() {

        boolean result = false;

        List<ShoppingCar> shoppingCars =
                DataSupport.where("productId = ?",String.valueOf(currentProduct.getProduct_id())).find(ShoppingCar.class);

        if(null != shoppingCars && shoppingCars.size() > 0) {

            result = true;

        }

        return result;

    }

    /**
     * 将产品加入购物车
     */
    private void addToShoppingCarOrNot() {

            if(isAddToShopCar()) {

                ToastUtil.showMsg(getApplicationContext(),getApplicationContext().getString(R.string.has_add_to_shop_car));

            }else {
                addToShopCar();
            }
    }

    private void addToShopCar() {




//wen订单提交新修改
        if(isEnterCount()) {

            ShoppingCar shoppingCar = new ShoppingCar();
            shoppingCar.setProductId(currentProduct.getProduct_id());
            shoppingCar.setUser_id(user.getUser_id());
            shoppingCar.setBuyCount(Integer.valueOf(ed_buy_count.getText().toString()));
            currentProduct.save();
            boolean result = shoppingCar.save();

            if(result) {

                ToastUtil.showResMsg(this,R.string.add_shopcar_success);
            }

        }else {

            ToastUtil.showMsg(getApplicationContext(),getApplicationContext().getString(R.string.product_count_first));
        }


    }





    /**
     * 跳转到客服聊天界面
     */

    private void goToCustomerChat() {
        if (BooleanUtils.isQQClientAvailable(this.getApplicationContext())) {
            /**
             * 这个方法会自动判断是否登陆，如果没有登陆则跳转到qq登陆界面
             */

            String qqNum = SpUtil.getInstance().getStringValue(ConstSp.SP_KEY_TOP_QQ,ConstSp.SP_VALUE.TOP_QQ_DEFAULT);

            if(TextUtils.isEmpty(qqNum)) {


                ToastUtil.showMsg(getApplicationContext(),getApplicationContext().getString(R.string.not_get_the_upperQQ));

            }else {

                String url = ConstBase.JUMP_TO_QQ + qqNum;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }

        } else {
            ToastUtil.showMsg(getApplicationContext(), getApplicationContext().getString(R.string.setUp_qq_first));
        }
    }


    /**
     * 收藏还是界面跳转
     */
    private void collectItOrCancel() {




//           Product product = DataSupport.find(Product.class,currentProduct.getId());
//
//            String collected = product.getIsCollect();

        String collected = currentProduct.getIsCollect();


        if (ConstCollection.IS_COLLECTED.equals(collected)) {

            //执行取消收藏

            startToCollectCancelThisProductToServer(HttpConst.URL.CANCEL_COLLECT,collected);


        }else {

            //执行收藏

            startToCollectThisProductToServer(HttpConst.URL.START_COLLECT, currentProduct);
        }



    }


    /**
     * 发起网络请求，执行收藏产品
     * @param url
     * @param product
     */

    private void startToCollectThisProductToServer(String url, final Product product) {

        User user = DataSupport.findFirst(User.class);

        final int productId = product.getProduct_id();
        final int userID = user.getUser_id();
        String productName = product.getProductName();
        String localPrice = product.getLocalPrice();
        String originalPrice = product.getOriginalPrice();
        String thumbnail = product.getThumnail();


        Map map = ParaUtils.getAddCollect(productId, userID, productName,localPrice,originalPrice,thumbnail);
        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {





                Log.i(HttpConst.SERVER_BACK, "===收藏返回成功==" + jsonArray.toString());


                parseBackByCollectOrNot(jsonArray, product.getIsCollect(), productId,userID);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                ToastUtil.showMsg(getApplicationContext(), resultCode + " " + resultMessage);
                Log.i(HttpConst.SERVER_BACK, "===收藏返回失败==" + resultCode + " " + resultMessage);

            }

            @Override
            public void _onError(String error) {

                ToastUtil.showMsg(getApplicationContext(), error);
                Log.i(HttpConst.SERVER_BACK, "===收藏返回失败==" + error);

            }
        });


    }





    /**
     * 发起网络请求 取消收藏该产品
     * @param collectOrNot  当前收藏状态
     *
     */
    private void startToCollectCancelThisProductToServer(String url, final String collectOrNot) {

        User user = DataSupport.findFirst(User.class);

        final int productId = currentProduct.getProduct_id();
        final int userID = user.getUser_id();
        Map map = ParaUtils.getAddOrCancelCollect(productId, userID);
        HttpClient.getInstance().method_PostWithParams_Dialog(url, map, new HttpResultSubscriber<JsonArray>(this) {
            @Override
            public void onSuccess(JsonArray jsonArray) {





                Log.i(HttpConst.SERVER_BACK, "===收藏或取消收藏返回成功==" + jsonArray.toString());


                parseBackByCollectOrNot(jsonArray, collectOrNot, productId,userID);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                ToastUtil.showMsg(getApplicationContext(), resultCode + " " + resultMessage);
                Log.i(HttpConst.SERVER_BACK, "===收藏或取消收藏返返回失败==" + resultCode + " " + resultMessage);

            }

            @Override
            public void _onError(String error) {

                ToastUtil.showMsg(getApplicationContext(), error);
                Log.i(HttpConst.SERVER_BACK, "===收藏或取消收藏返返回失败==" + error);

            }
        });
    }



    private void parseBackByCollectOrNot(JsonArray jsonArray,String collectOrNot,int product_id,int userId) {


        boolean updatDbBack = false;

        if(collectOrNot.equals(ConstCollection.IS_COLLECTED)) {

            //如果当前是被收藏状态，则这里执使用取消解析
            updatDbBack = HttpReturnParse.getInstance().parseCancelCollectBack(jsonArray, product_id,userId);



        }else {

            updatDbBack = HttpReturnParse.getInstance().parseAddCollectBack(jsonArray, product_id,userId);


        }


        if(updatDbBack) {

            collectOrNot = currentProduct.getIsCollect();

            showColectedOrNotWhenServerBack(collectOrNot);
        }


    }



    /**
     * 服务器返回后 界面收藏变更
     * @param isCollected
     */
    private void showColectedOrNotWhenServerBack(String isCollected) {


        if (ConstCollection.IS_COLLECTED.equals(isCollected)) {

            iv_collect_in_productDetial.setImageResource(R.drawable.img_collection_off);

            currentProduct.setIsCollect(ConstCollection.NOT_COLLECTED);

        }else {


            iv_collect_in_productDetial.setImageResource(R.drawable.img_collection_on);

            currentProduct.setIsCollect(ConstCollection.IS_COLLECTED);

        }
    }



    /**
     * 订单提交成功的广播注册
     */
    private void registerDeliverOrderSuccessBroadCast() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstIntent.IntentAction.DELIVER_ORDER_SUCCESS);

        deliverOrderSuccessReceiver = new DeliverOrderSuccessReceiver();

        this.registerReceiver(deliverOrderSuccessReceiver, filter);

    }



    /**
     * 订单提交成功的广播
     */
    private class DeliverOrderSuccessReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConstIntent.IntentAction.DELIVER_ORDER_SUCCESS.equals(action)) {

                /**
                 * 开始执行界面关闭
                 */
               ProductDetialActivity.this.finish();

            }
        }
    }
}
