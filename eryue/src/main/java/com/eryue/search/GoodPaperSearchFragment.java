package com.eryue.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eryue.ActivityHandler;
import com.eryue.GoodsListTabModel;
import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.eryue.model.GoodsSearchModel;
import com.eryue.ui.MultiLineTextView;
import com.eryue.ui.NoScrollGridView;
import com.eryue.util.SharedPreferencesUtil;
import com.library.util.ToastTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enjoy on 2018/2/13.
 */

public class GoodPaperSearchFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, GoodsSearchPresenter.KeyWordListener {

    /**
     * 热门搜索
     */
    private NoScrollGridView gridview_goodstab;

    /**
     * 搜索历史
     */
    private MultiLineTextView multitextview;

    private TextView tv_search;

    private EditText tv_search_keyword;

    ArrayList<GoodsListTabModel> tabList = new ArrayList<>();

    //热门搜索数据
    HotSearchAdapter hotSearchAdapter;

    /**
     * 请求控制类
     */
    private GoodsSearchPresenter presenter;

    public static final String key_search = "key_search";
    /**
     * 搜索历史
     */
    private List<GoodsSearchModel> historyList;
    /**
     * 清空历史搜索
     */
    private ImageView iv_delete;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_goodpaper);
        initView();
        initData();
    }


    private void initView() {
        gridview_goodstab = (NoScrollGridView) getView().findViewById(R.id.gridview_goodstab);
        multitextview = (MultiLineTextView) getView().findViewById(R.id.multitextview);
        tv_search_keyword = (EditText) getView().findViewById(R.id.tv_search_keyword);

        gridview_goodstab.setOnItemClickListener(this);

        iv_delete = (ImageView) getView().findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(this);

        tv_search = (TextView) getView().findViewById(R.id.tv_search);
        tv_search.setOnClickListener(this);

        tv_search_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    requestSearch();
                    return true;
                }
                return false;
            }
        });

    }

    private void initData() {

        presenter = new GoodsSearchPresenter();
        presenter.setKeyWordListener(this);
        presenter.getKeywords();


        //热门搜索数据
        hotSearchAdapter = new HotSearchAdapter(getContext());

//        String[] hotDataArray = {"羽绒服","女鞋","零食","连衣裙","年货","女包","卫衣","毛衣女","玩具","毛衣"};
//        List<String> hotList = new ArrayList<>();
//        for (int i=0;i<hotDataArray.length;i++){
//            hotList.add(hotDataArray[i]);
//        }
//        hotSearchAdapter.setDataList(hotList);
        gridview_goodstab.setAdapter(hotSearchAdapter);

        String[] historyDataArray = {"羽绒服", "女鞋", "零食", "连衣裙", "年货", "女包", "卫衣", "毛衣女", "玩具", "毛衣", "女鞋", "零食", "连衣裙",
                "年货", "女包", "卫衣", "毛衣女", "玩具", "毛衣", "女鞋", "零食", "连衣裙", "年货",
                "女包", "卫衣", "毛衣女", "玩具", "毛衣", "女鞋", "零食", "连衣裙", "年货", "女包", "卫衣", "毛衣女", "玩具", "毛衣"
                , "女鞋", "零食", "连衣裙", "年货", "女包", "卫衣", "毛衣女", "玩具", "毛衣"
                , "女鞋", "零食", "连衣裙", "年货", "女包", "卫衣", "毛衣女", "玩具", "毛衣"
                , "女鞋", "零食", "连衣裙", "年货", "女包", "卫衣", "毛衣女", "玩具", "毛衣"};
//        List<String> historyList = new ArrayList<>();
//        for (int i=0;i<historyDataArray.length;i++){
//            historyList.add(historyDataArray[i]);
//        }
        if (null == historyList) {
            historyList = new ArrayList<>();
        }

        multitextview.setOnMultipleTVItemClickListener(new MultiLineTextView.OnMultipleTVItemClickListener() {
            @Override
            public void onMultipleTVItemClick(View view, int position) {
                //搜索历史
                String name = historyList.get(position).getKeyword();

                Intent intent = new Intent(getContext(), GoodsSearchListActivity.class);
                if (!TextUtils.isEmpty(name)&&name.length()>=10){
                    intent.putExtra("type","指定搜索");
                }
                intent.putExtra("title", name);
                startActivity(intent);
            }
        });

        refreshHistory();

    }


    @Override
    public void onClick(View v) {

        if (v == tv_search) {
            requestSearch();
        } else if (v == iv_delete) {
            //清空历史记录
            SharedPreferencesUtil.getInstance().clear(key_search);
            multitextview.clearView();
        }


    }

    private void requestSearch(){
        String name = tv_search_keyword.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastTools.showShort(getContext(), "请输入关键词");
            return;
        }

        Intent intent = new Intent(getContext(), GoodsSearchListActivity.class);
        intent.putExtra("title", name);
//            if (!TextUtils.isEmpty(name)&&name.length()>=10){
//                ToastTools.showShort(getContext(), "关键词大于10个，跳转到指定搜索");
//                intent.putExtra("type","指定搜索");
//            }
        startActivity(intent);
        //保存搜索记录到本地
        addToLocal(name);
        multitextview.clearView();
        multitextview.setTextViews(historyList);
    }

    /**
     * 刷新搜索历史
     */
    public void refreshHistory(){
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                List<GoodsSearchModel> localList = SharedPreferencesUtil.getInstance().getDataList(key_search);
                if (null!=localList&&localList.size()!=historyList.size()){
                    historyList = localList;
                    multitextview.clearView();
                    multitextview.setTextViews(historyList);
                }
            }
        }).sendEmptyMessageDelayed(0,200);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //热词
        Intent intent = new Intent(getContext(), GoodsSearchListActivity.class);
        String name = (String) gridview_goodstab.getAdapter().getItem(position);
        intent.putExtra("title", name);
        intent.putParcelableArrayListExtra("tab", tabList);
        startActivity(intent);

    }

    @Override
    public void onKeyWordBack(final List<String> keywords) {

        post(new Runnable() {
            @Override
            public void run() {
                if (null != hotSearchAdapter) {
                    hotSearchAdapter.setDataList(keywords);
                    hotSearchAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onKeyWordError() {

    }

    private void addToLocal(String keyword){
        if (null==historyList){
            historyList = new ArrayList<>();
        }
        boolean isRepeat = false;
        for (int i=0;i<historyList.size();i++){
            if (!TextUtils.isEmpty(historyList.get(i).getKeyword())&&historyList.get(i).getKeyword().equalsIgnoreCase(keyword)){
                isRepeat = true;
                break;
            }

        }
        if (!isRepeat){
            historyList.add(new GoodsSearchModel(keyword));
        }
        SharedPreferencesUtil.getInstance().setDataList(key_search, historyList);

    }
}
