package com.eryue.search;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.ActivityHandler;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.eryue.model.GoodsSearchModel;
import com.eryue.ui.MultiLineTextView;
import com.eryue.util.SharedPreferencesUtil;
import com.eryue.widget.StockDatePopModel;
import com.eryue.widget.StockDatePopView;
import com.library.util.ToastTools;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 超级搜索(2.0)
 * Created by enjoy on 2018/2/13.
 */

public class GoodsSuperSearchFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, GoodsSearchPresenter.KeyWordListener {

    /**
     * 热门搜索
     */
    private MultiLineTextView gridview_goodstab;

    private LinearLayout layout_history;

    /**
     * 搜索历史
     */
    private MultiLineTextView multitextview;

    private TextView tv_search;

    private EditText tv_search_keyword;


    //热门搜索数据
//    HotSearchAdapter hotSearchAdapter;

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

    private ImageView iv_tip;

    //清除输入框内容
    private ImageView iv_clear;


    /**
     * 下拉选项
     * @param savedInstanceState
     */
    private TextView niceSpinner;
    private StockDatePopView stockDatePopView;
    private final String[] hintStr0 = {"淘宝", "京东", "拼多多", "蘑菇街", "苏宁"};
    private final int[] hintValue0 = {0, 1, 2, 3, 4};
    private int hintIndex0 = 0;


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_goodssearch);
        setTitle("超级搜索");
        initView();
        initData();

        checkClip();
    }


    private void initView() {
        layout_history = getView().findViewById(R.id.layout_history);

        gridview_goodstab =  getView().findViewById(R.id.gridview_goodstab);
        multitextview = (MultiLineTextView) getView().findViewById(R.id.multitextview);
        tv_search_keyword = (EditText) getView().findViewById(R.id.tv_search_keyword);

//        gridview_goodstab.setOnItemClickListener(this);
        gridview_goodstab.setOnMultipleTVItemClickListener(new MultiLineTextView.OnMultipleTVItemClickListener() {
            @Override
            public void onMultipleTVItemClick(View view, int position) {
                if (null==keywords||(keywords.size()-1)<position){
                    return;
                }
                //热词
                Intent intent = new Intent(getContext(), GoodsSearchResultActivity.class);
                String name = keywords.get(position);
                intent.putExtra("title", name);
                startActivity(intent);
            }
        });

        iv_delete = (ImageView) getView().findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(this);

        tv_search = (TextView) getView().findViewById(R.id.tv_search);
        tv_search.setOnClickListener(this);

        iv_clear = getView().findViewById(R.id.iv_clear);

        iv_clear.setOnClickListener(this);

        tv_search_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String textContent =  tv_search_keyword.getText().toString();

                if (!TextUtils.isEmpty(textContent)){
                    iv_clear.setVisibility(View.VISIBLE);
                }else{
                    iv_clear.setVisibility(View.GONE);
                }

            }
        });

        tv_search_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = tv_search_keyword.getText().toString();
                    requestSearch(keyword);
                    return true;
                }
                return false;
            }
        });

        iv_tip = getView().findViewById(R.id.iv_tip);

        getView().findViewById(R.id.nice_spinner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != stockDatePopView) {
                    stockDatePopView.showPopView(v);
                }
            }
        });
        stockDatePopView = new StockDatePopView(getContext());
        stockDatePopView.setData(hintStr0, hintValue0);

        stockDatePopView.setOnDateClickListener(new StockDatePopView.OnDateClickListener() {
            @Override
            public void onDateItemClick(StockDatePopModel model) {
                TextView textView = (TextView) getView().findViewById(R.id.spinner_text);
                textView.setText(model.getShowString());
                hintIndex0 = model.getIndex();
            }
        });

        stockDatePopView.setSelectOption(hintIndex0);

    }

    private void initData() {
        Glide.with(this).load(R.drawable.icon_search_tip).asBitmap().into(iv_tip);
//        iv_tip.setImageResource(R.drawable.icon_search_tip);

        presenter = new GoodsSearchPresenter();
        presenter.setKeyWordListener(this);
        presenter.getKeywords();


        //热门搜索数据
//        hotSearchAdapter = new HotSearchAdapter(getContext());

//        String[] hotDataArray = {"羽绒服","女鞋","零食","连衣裙","年货","女包","卫衣","毛衣女","玩具","毛衣"};
//        List<String> hotList = new ArrayList<>();
//        for (int i=0;i<hotDataArray.length;i++){
//            hotList.add(hotDataArray[i]);
//        }
//        hotSearchAdapter.setDataList(hotList);
//        gridview_goodstab.setAdapter(hotSearchAdapter);

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

                Intent intent = new Intent(getContext(), GoodsSearchResultActivity.class);
                if (!TextUtils.isEmpty(name) && name.length() >= 10) {
                    intent.putExtra("type", "指定搜索");
                }
                intent.putExtra("title", name);
                startActivity(intent);
            }
        });

        refreshHistory();

        showBack(false);
        //是否显示返回按钮
        if (null != getArguments()) {
            boolean showBack = getArguments().getBoolean("showBack");
            showBack(showBack);
        }

    }


    @Override
    public void onClick(View v) {

        if (v == tv_search) {
            String platform = hintStr0[hintIndex0].toString();
            String keyword = null;
            if (null != platform) {
                keyword = platform+"+"+tv_search_keyword.getText().toString();
            }
            requestSearch(keyword);
        } else if (v == iv_delete) {
            //清空历史记录
            SharedPreferencesUtil.getInstance().clear(key_search);
            multitextview.clearView();
            if (null!=historyList){
                historyList.clear();
            }
        }else if (v == iv_clear){

            //清空输入框内容
            if (null!=tv_search_keyword){
                tv_search_keyword.setText("");
            }

        }


    }

    private void requestSearch(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            ToastTools.showShort(getContext(), "请输入关键词");
            return;
        }

        Intent intent = new Intent(getContext(), GoodsSearchResultActivity.class);
        intent.putExtra("title", keyword);
//            if (!TextUtils.isEmpty(name)&&name.length()>=10){
//                ToastTools.showShort(getContext(), "关键词大于10个，跳转到指定搜索");
//                intent.putExtra("type","指定搜索");
//            }
        startActivity(intent);
        //保存搜索记录到本地
        addToLocal(keyword);
        multitextview.clearView();

        if (null != historyList && !historyList.isEmpty()) {
            layout_history.setVisibility(View.VISIBLE);
        }
        multitextview.setTextViews(historyList);
    }

    /**
     * 刷新搜索历史
     */
    public void refreshHistory() {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                List<GoodsSearchModel> localList = SharedPreferencesUtil.getInstance().getDataList(key_search);
                if (null != localList && localList.size() != historyList.size()) {
                    historyList = localList;
                    multitextview.clearView();
                    if (null != historyList && !historyList.isEmpty()) {
                        layout_history.setVisibility(View.VISIBLE);
                    }
                    multitextview.setTextViews(historyList);
                }
            }
        }).sendEmptyMessageDelayed(0, 200);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //热词
//        Intent intent = new Intent(getContext(), GoodsSearchResultActivity.class);
//        String name = (String) gridview_goodstab.getAdapter().getItem(position);
//        intent.putExtra("title", name);
//        startActivity(intent);

    }


    List<String> keywords;
    @Override
    public void onKeyWordBack(final List<String> keywords) {

        this.keywords = keywords;
        post(new Runnable() {
            @Override
            public void run() {
//                if (null != hotSearchAdapter) {
//                    hotSearchAdapter.setDataList(keywords);
//                    hotSearchAdapter.notifyDataSetChanged();
//                }
                if(null!=gridview_goodstab){
                    gridview_goodstab.clearView();

                    List<GoodsSearchModel> goodsSearchList = new ArrayList<>();
                    for (int i=0;i<keywords.size();i++){
                        if (!TextUtils.isEmpty(keywords.get(i))){
                            goodsSearchList.add(new GoodsSearchModel(keywords.get(i)));
                        }
                    }
                    gridview_goodstab.setTextViews(goodsSearchList);

                }
            }
        });

    }

    @Override
    public void onKeyWordError() {

    }

    private void addToLocal(String keyword) {
        if (null == historyList) {
            historyList = new ArrayList<>();
        }
        boolean isRepeat = false;
        for (int i = 0; i < historyList.size(); i++) {
            if (!TextUtils.isEmpty(historyList.get(i).getKeyword()) && historyList.get(i).getKeyword().equalsIgnoreCase(keyword)) {
                isRepeat = true;
                break;
            }

        }
        if (!isRepeat) {
            historyList.add(0,new GoodsSearchModel(keyword));
        }
        SharedPreferencesUtil.getInstance().setDataList(key_search, historyList);

    }


    public void checkClip(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                final String clipString = getClipString();

                //和上次搜索过的内容进行比较，如果相同就不做提示
                String preSearchString = SharedPreferencesUtil.getInstance().getString(GoodsContants.CLIP_CONTENT);

                if (!TextUtils.isEmpty(clipString)&&clipString.length()<=100&&!clipString.equals(preSearchString)) {
                    //提示用户是否搜索
                    if (null == clipPopView) {
                        clipPopView = new SearchClipPopView(getContext());
                        clipPopView.setOnClipClickListener(new SearchClipPopView.onClipClickListener() {
                            @Override
                            public void onClipClick(String content) {
                                if (null!=tv_search_keyword){
                                    tv_search_keyword.setText(content);
                                }
                                requestSearch(content);
                            }
                        });
                    }

                    clipPopView.showClipContent(clipString);
                    clipPopView.showPopView(getActivity().getWindow().getDecorView());
                }
            }
        },200);
    }



    private SearchClipPopView clipPopView;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (null!=getActivity()&&!(getActivity() instanceof GoodsSearchActivityEx)){
            //如果不是搜索详情界面,就不弹出剪贴板提示
            return;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 获取剪贴板信息
     *
     * @return
     */
    private String getClipString() {
        String clipString = "";
        //检测剪贴板信息
        ClipboardManager cbm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = cbm.getPrimaryClip();

        if (null != clipData) {
            int count = clipData.getItemCount();

            Log.d("libo", "ClipboardManager----count=" + count);
            ClipData.Item item;
            for (int i = 0; i < count; i++) {
                item = clipData.getItemAt(i);
                if (null == item) {
                    continue;
                }
                clipString = item.getText().toString();
                if (!TextUtils.isEmpty(clipString)) {
                    return clipString;
                }

                Log.d("libo", "ClipboardManager----item=" + item.getText().toString());

            }
        }

        return clipString;
    }
}
