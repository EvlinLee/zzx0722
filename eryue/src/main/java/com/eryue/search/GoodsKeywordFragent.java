package com.eryue.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.GoodsListTabModel;
import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.eryue.model.GoodsSearchModel;
import com.eryue.util.SharedPreferencesUtil;
import com.library.util.ToastTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enjoy on 2018/2/16.
 */

public class GoodsKeywordFragent extends BaseFragment implements View.OnClickListener{

    private TextView tv_search;

    private EditText tv_search_keyword;

    private ImageView iv_tip;


    /**
     * 搜索历史
     */
    private List<GoodsSearchModel> historyList;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_keyword);
        initView();
        initData();

    }

    private void initView(){
        tv_search = (TextView) getView().findViewById(R.id.tv_search);
        tv_search_keyword = (EditText) getView().findViewById(R.id.tv_search_keyword);
        iv_tip = (ImageView) getView().findViewById(R.id.iv_tip);

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

    private void initData(){
        Glide.with(this).load(R.drawable.icon_search_tip).asBitmap().into(iv_tip);

        historyList = SharedPreferencesUtil.getInstance().getDataList(GoodPaperSearchFragment.key_search);
        if (null == historyList) {
            historyList = new ArrayList<>();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tv_search){
            requestSearch();
        }
    }

    private void requestSearch(){
        String name = tv_search_keyword.getText().toString();
        if (TextUtils.isEmpty(name)){
            ToastTools.showShort(getContext(),"请输入关键词");
            return;
        }
//            if (name.length()<10){
//                ToastTools.showShort(getContext(),"关键词小于10个,请使用好券搜索");
//                return;
//            }
        //输入标题是否超过10个字，如果小于10个字不能使用，提示使用好券搜索
        Intent intent = new Intent(getContext(),GoodsSearchListActivity.class);
        intent.putExtra("title",name);
        intent.putExtra("type","指定搜索");
        startActivity(intent);
        //保存搜索记录到本地
        addToLocal(name);
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
        SharedPreferencesUtil.getInstance().setDataList(GoodPaperSearchFragment.key_search, historyList);

    }
}
