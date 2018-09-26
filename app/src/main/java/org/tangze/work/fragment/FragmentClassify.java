package org.tangze.work.fragment;


import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;


import org.litepal.crud.DataSupport;
import org.tangze.work.R;
import org.tangze.work.adapter.ClassifyFirstAdapter;
import org.tangze.work.adapter.ClassifySecondGridAdapter;
import org.tangze.work.entity.Classify;
import org.tangze.work.entity.ClassifySecond;
import org.tangze.work.entity.Product;
import org.tangze.work.entity.User;
import org.tangze.work.http.HttpClient;
import org.tangze.work.http.HttpConst;
import org.tangze.work.http.HttpResultSubscriber;
import org.tangze.work.http.HttpReturnParse;
import org.tangze.work.utils.ParaUtils;
import org.tangze.work.utils.ToastUtil;
import org.tangze.work.widget.pullfresh.HeaderGridView;
import org.tangze.work.widget.pullfresh.PullableGridViewWithHeader;
import org.tangze.work.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


/**
 * 分类fragment
 */
public class FragmentClassify extends BaseFragment implements View.OnClickListener {

    public static final String TAG = FragmentClassify.class.getSimpleName();
    /**
     * titile
     */

    private TextView tv_classify_fg_title;

    private ImageView iv_classify_fg_search;

    private LinearLayout ll_classify_fg_to_search;


    private TextView tv_current_city;


    /**
     * 下部分类显示
     */
    /**
     * 左边一级分类
     */
    private ListView lv_left_first_classify;//左边一级分类的listView
    private ClassifyFirstAdapter classifyFirstAdapter;//左边一级分类的listView所在的adapter
    /**
     * 右边二级分类
     *
     * @return
     */
//    private TextView tv_secondClassify_name;//显示一级分类名称
    private StickyGridHeadersGridView gv_second_classify; //显示二级分类的gradView
    private ClassifySecondGridAdapter classifySecondGridAdapter;//显示二级分类的adapter


    /**
     * 数据相关
     * @return
     */

    private User user;



    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_classify;
    }

    @Override
    protected void getSendData(Bundle arguments) {

    }

    @Override
    protected void initView() {
        tv_classify_fg_title = findViewById(R.id.tv_fragment_title);
        iv_classify_fg_search = findViewById(R.id.iv_fragment_search);
        ll_classify_fg_to_search = findViewById(R.id.ll_fragment_to_search);
        tv_current_city = findViewById(R.id.tv_current_city);


        /**
         * 左边一级分类
         */
        lv_left_first_classify = findViewById(R.id.lv_left_first_classify);//左边一级分类的listView
        classifyFirstAdapter = new ClassifyFirstAdapter(mActivity);//左边一级分类的listView所在的adapter
        lv_left_first_classify.setAdapter(classifyFirstAdapter);

        /**
         * 右边二级分类
         * @return
         */
//        tv_secondClassify_name = findViewById(R.id.tv_secondClassify_name);//显示二级分类名称
        gv_second_classify = findViewById(R.id.gv_second_classify); //显示二级分类的gradView
        classifySecondGridAdapter = new ClassifySecondGridAdapter(mActivity);
        gv_second_classify.setAdapter(classifySecondGridAdapter);

    }

    @Override
    protected void initListener() {
        ll_classify_fg_to_search.setOnClickListener(this);
//        pullToRefreshLayout.setOnRefreshListener(this);

        /**
         * 左边一级分类
         */
        lv_left_first_classify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                Classify classify = (Classify) classifyFirstAdapter.getItem(position);

                int classify_first_id = classify.getClassify_id();
                lv_left_first_classify.smoothScrollToPositionFromTop(position, 0); //设置滑动到顶部
                classifyFirstAdapter.selectIndex = position;  //设置变色位置
                classifyFirstAdapter.notifyDataSetChanged(); //adapter刷新


                lv_left_first_classify.setOnScrollListener(new AbsListView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                            lv_left_first_classify.setOnScrollListener(null);//这个步骤是设置了滑动位置后，应该禁止滑动，不然我们滑动时候，listview将再次回到之前点击特效的位置
                            lv_left_first_classify.smoothScrollToPositionFromTop(position, 0);
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    }
                });

                getSecondClassifyFromServer(classify_first_id);

            }
        });

    }




    private void getSecondClassifyFromServer(int topId) {

        Map map = ParaUtils.getAllSecondClassifiesByOneTop(topId);

        HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.CLASSIFIES_SECOND, map, new HttpResultSubscriber<JsonArray>(mActivity) {
            @Override
            public void onSuccess(JsonArray jsonArray) {


                HttpReturnParse.getInstance().parseSecondClassifyBackSave(jsonArray);

                List<ClassifySecond> classifies = DataSupport.findAll(ClassifySecond.class);

                getProductFromServerBySecondClassifyIds(classifies);

                Log.i(HttpConst.SERVER_BACK, "===获取二级分类返回成功==" + jsonArray);


            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                ToastUtil.showMsg(mActivity.getApplicationContext(), resultCode + resultMessage);

                Log.i(HttpConst.SERVER_BACK, "===获取二级分类返回===" + resultCode + resultMessage);


            }

            @Override
            public void _onError(String error) {

                ToastUtil.showMsg(mActivity.getApplicationContext(), error);


                Log.i(HttpConst.SERVER_BACK, "===获取二级分类返回失败===" + error);

            }
        });


    }


    /**
     * 通过二级分类ids 列表来找到对应的数据
     */
    private void getProductFromServerBySecondClassifyIds(List<ClassifySecond> secondClassifyList) {



        if (null == user) {
            return;
        }


        int userId = user.getUser_id();

        List<Integer> classifySecondIds = new ArrayList<>();


        if (null != secondClassifyList && secondClassifyList.size() > 0) {


            for (int i = 0; i < secondClassifyList.size(); i++) {


                classifySecondIds.add(secondClassifyList.get(i).getClassifySecond_id());

            }







            Map map = ParaUtils.getAllProductBySecondClassIds(userId,classifySecondIds);


//            List<Integer> test = new ArrayList<>();
//            test.add(232);
//            test.add(243);
//
//            Map map = ParaUtils.getAllProductBySecondClassIds(userId,test);

            HttpClient.getInstance().method_PostWithParams_Dialog(HttpConst.URL.ALL_PRODUCT, map, new HttpResultSubscriber<JsonArray>(mActivity) {
                @Override
                public void onSuccess(JsonArray jsonArray) {

                    Log.i(HttpConst.SERVER_BACK, "===获取二级分类的所有产品返回成功==" + jsonArray);

                    List<Product> productListInSecondClassify = HttpReturnParse.getInstance().parseRecommandBackSave(jsonArray);


                    setProductBySecondClassifyToUi(productListInSecondClassify);

//                    int section = 1;
//                    Map<String, Integer> sectionMap = new HashMap<String, Integer>();
//
//
//                    for (ListIterator<Product> it = productListInSecondClassify.listIterator(); it.hasNext(); ) {
//                        Product product = it.next();
//                        String ym = String.valueOf(product.getClassify_id());
//                        if (!sectionMap.containsKey(ym)) {
//                            product.setHeaderId(section);
//                            sectionMap.put(ym, section);
//                            section++;
//                        } else {
//                            product.setHeaderId(sectionMap.get(ym));
//                        }
//                    }
//
//
//                    classifySecondGridAdapter.setList(productListInSecondClassify);

                }

                @Override
                public void onNotSuccess(int resultCode, String resultMessage) {
                    Log.i(HttpConst.SERVER_BACK, "===获取二级分类的所有产品返回失败==" + resultMessage + " code:" + resultCode);
                    ToastUtil.showMsg(mActivity.getApplicationContext(), resultMessage);
                }

                @Override
                public void _onError(String error) {
                    Log.i(HttpConst.SERVER_BACK, "===获取二级分类的所有产品返回失败==" + error);

                    ToastUtil.showMsg(mActivity.getApplicationContext(), error);
                }
            });


        }


    }

    /**
     * 将二级分类的数据设置到界面上
     * @param productListInSecondClassify
     */
    private void setProductBySecondClassifyToUi(List<Product> productListInSecondClassify) {



        List<Product> sortList =getSortList(productListInSecondClassify);

        int section = 1;
        Map<String, Integer> sectionMap = new HashMap<String, Integer>();


        for (ListIterator<Product> it = sortList.listIterator(); it.hasNext(); ) {
            Product product = it.next();
            String ym = String.valueOf(product.getClassify_id());
            if (!sectionMap.containsKey(ym)) {
                product.setHeaderId(section);
                sectionMap.put(ym, section);
                section++;
            } else {
                product.setHeaderId(sectionMap.get(ym));
            }
        }


        classifySecondGridAdapter.setList(sortList);

    }


    /**
     * 先进行数据实体 分组
     * @param productListInSecondClassify
     * @return
     */
    private List<Product> getSortList(List<Product> productListInSecondClassify) {
        List<Product> list = new ArrayList<>();
        Map<String, List<Product>> sectionMap = new HashMap<>();
        for (ListIterator<Product> it = productListInSecondClassify.listIterator(); it.hasNext(); ) {
            Product product = it.next();
            String ym = String.valueOf(product.getClassify_id());
            if (sectionMap.containsKey(ym)) {
                List<Product> list1 = sectionMap.get(ym);
                list1.add(product);

            }else {

                List<Product> partList = new ArrayList<>();
                partList.add(product);
                sectionMap.put(ym, partList);
            }
        }


        for (String key:sectionMap.keySet()) {

            list.addAll(sectionMap.get(key)) ;

        }


        for (int i=0 ; i< list.size();i++) {

            Log.i("listValue","cid = "+list.get(i).getClassify_id());

        }



        return list;
    }


    @Override
    protected void onLazyLoad() {


        user = DataSupport.findFirst(User.class);

        tv_classify_fg_title.setText(mActivity.getString(R.string.classify_title));
        tv_current_city.setVisibility(View.GONE);
        iv_classify_fg_search.setVisibility(View.GONE);
        ll_classify_fg_to_search.setVisibility(View.GONE);
        getClassifyFromServer();
    }


    /**
     * 从服务器获取数据
     */
    private void getClassifyFromServer() {

        HttpClient.getInstance().method_PostWithOutParams_Dialog(HttpConst.URL.CLASSIFIES, new HttpResultSubscriber<JsonArray>(mActivity) {
            @Override
            public void onSuccess(JsonArray jsonArray) {

                Log.i(HttpConst.SERVER_BACK, "===获取一级分类返回成功==" + jsonArray.toString());

                HttpReturnParse.getInstance().parseClassifyBackSave(jsonArray);

                List<Classify> classifies = DataSupport.findAll(Classify.class);

                classifyFirstAdapter.setList(classifies);

                setCurrentIndexToTheSecondClassify();

            }

            @Override
            public void onNotSuccess(int resultCode, String resultMessage) {

                ToastUtil.showMsg(mActivity.getApplicationContext(), resultCode + resultMessage);

                Log.i(HttpConst.SERVER_BACK, "===获取一级分类返回失败===" + resultCode + resultMessage);


            }

            @Override
            public void _onError(String error) {

                ToastUtil.showMsg(mActivity.getApplicationContext(), error);


                Log.i(HttpConst.SERVER_BACK, "===获取一级分类返回失败===" + error);

            }
        });

    }


    /**
     * 首次进入界面后，设置一级分类当前默认第0个选中项所对应的二级分类
     */
    private void setCurrentIndexToTheSecondClassify() {

        int selectIndex = classifyFirstAdapter.selectIndex;

        if (0 == selectIndex) {

            List<Classify> classifies = classifyFirstAdapter.getList();

            if (null != classifies && classifies.size() > 0) {


                Classify classify = classifies.get(selectIndex);
                String classify_first_Name = classify.getClassfiyName();
                int classify_first_id = classify.getClassify_id();
//                tv_firstClassify_name.setText(classify_first_Name);
                getSecondClassifyFromServer(classify_first_id);


            }
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_fragment_to_search:
                Toast.makeText(mActivity, "进入搜索界面", Toast.LENGTH_SHORT).show();
                break;

        }
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {

            getClassifyFromServer();

        }
    }
}
