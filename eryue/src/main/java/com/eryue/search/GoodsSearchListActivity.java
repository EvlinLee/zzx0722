package com.eryue.search;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eryue.GoodsListFragment;
import com.eryue.R;
import com.eryue.activity.BaseFragment;

import base.BaseActivity;

/**
 * Created by enjoy on 2018/2/16.
 */

public class GoodsSearchListActivity extends BaseActivity implements View.OnClickListener{

    /**
     * 返回按钮
     */
    private ImageView iv_back;

    /**
     * 标题
     */
    private TextView tv_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragMgr = getSupportFragmentManager();
        setContentView(R.layout.activity_searchlist);
        hideNavigationBar(true);
        initView();
        initData();
    }

    private void initView(){
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_name = (TextView) findViewById(R.id.tv_name);

        iv_back.setOnClickListener(this);

        GoodsListFragment goodsListFragment = GoodsListFragment.newInstance(false);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("tab",getIntent().getParcelableArrayListExtra("tab"));
        goodsListFragment.setArguments(bundle);
        showFragment(goodsListFragment);
    }

    private void initData(){

        tv_name.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void onClick(View v) {

        if (v == iv_back){
            this.finish();
        }

    }


    protected FragmentManager mFragMgr;

    private BaseFragment fragment;

    public BaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(BaseFragment f) {
        this.fragment = f;
    }


    /**
     * TAB之间切换请使用showFragments方法,各tab定义为fragment
     *
     * @param fragment
     */
    public void showFragment(BaseFragment fragment) {
        if (fragment == this.fragment) {
            return;
        }
        FragmentTransaction trans = mFragMgr.beginTransaction();
//		trans.setCustomAnimations(R.anim.frag_enter, R.anim.frag_exit);
        trans.replace(R.id.layout_searchlist, fragment, fragment.getClass().getSimpleName());
//		if (mFragMgr.findFragmentByTag(tag) == null) {
//			trans.add(R.id.main_body, fragment, tag);
//			listFragment.add(fragment);
//		} else {
//			for (int i = 0; i < listFragment.size(); i++) {
//				trans.hide(listFragment.get(i));
//			}
//			trans.show(fragment);
//		}
        trans.commitAllowingStateLoss();
        this.fragment = fragment;
    }
}
