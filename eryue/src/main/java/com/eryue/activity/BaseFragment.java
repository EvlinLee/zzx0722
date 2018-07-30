package com.eryue.activity;

//#ifdef spen
//@import spen.SpenDelegate;
//@import spen.SpenDelegate.ISpenListener;
//@import android.view.MotionEvent;
//#endif
//#ifdef LOG_JXL
//@import log.CmdInfo2XslHelper;
//@import log.CmdInfo2XslHelper.InfoItem;
//#endif

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eryue.ActivityHandler;
import com.eryue.R;
import com.library.permission.PermissionUtil;
import com.library.permission.PermissionsManager;
import com.library.startactivity.StartActivityManager;
import com.library.ui.LoadingDialog;
import com.library.util.UIScreen;

import base.BaseActivity;


/**
 * ****************************************************************
 * 文件名称  : BaseFragment.java
 * 作         者  : ylrong
 * 创建时间  : 2014-9-15 下午5:31:38
 * 文件描述  : 基础Fragment，TAB及之前的页面建议使用fragment，之后的页面使用activity
 * 修改历史  : 2014-9-15 1.00 初始版本
 * ****************************************************************
 */
public class BaseFragment extends Fragment implements ActivityHandler.ActivityHandlerListener{



    /**
     * 是否 主页 tab Fragment, 用来判断是否显示 沉浸状态栏
     * 主页 tab 继承 之， return true.
     *
     * @return
     */
    protected boolean isMainTabFrag() {
        return false;
    }

    /**
     * 菊花式进度条
     */
    private Dialog mProgressDialog;

    /**
     * 显示菊花式进度条
     */
    public void showProgressMum() {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null) {
                    mProgressDialog = new LoadingDialog(getActivity(),
                            R.style.CustomProgressDialog);
                }
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }
        });
    }

    /**
     * 关闭菊花式进度条
     */
    public void hideProgressMum() {
        if (mProgressDialog == null || getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    private FrameLayout fragment_main_body;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//		if (BaseNetHandler.DEBUG) {
//			InfoItem item = CmdInfo2XslHelper.getHelper().new InfoItem(this.getClass().getSimpleName());
//			CmdInfo2XslHelper.recordInfo(item);
//		}
    }


    //导航栏
    protected LinearLayout layout_navigation;
    //返回按钮
    private ImageView iv_back;
    private TextView tv_title;
    private View view_line;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UIScreen.resetMainScreen(getActivity());
        View view = View.inflate(getActivity(), R.layout.base_layout, null);

        layout_navigation = view.findViewById(R.id.layout_fragment_navigation);
        iv_back = view.findViewById(R.id.iv_fragment_back);
        tv_title = view.findViewById(R.id.tv_fragment_title);
        view_line = view.findViewById(R.id.view_fragment_navigationline);
        fragment_main_body = (FrameLayout)view.findViewById(R.id.fragment_main_body);

        layout_navigation.setVisibility(View.GONE);
        view_line.setVisibility(View.GONE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!getActivity().isFinishing()){
                    getActivity().finish();
                }

            }
        });

        return view;
    }

    public void setTitle(String title){

        if (null!=layout_navigation){
            layout_navigation.setVisibility(View.VISIBLE);
            tv_title.setText(title);
            view_line.setVisibility(View.VISIBLE);
        }
    }

    public void showBack(boolean showBack){

        if (null!=iv_back){
            iv_back.setVisibility(showBack?View.VISIBLE:View.INVISIBLE);
        }

    }

    protected int getLayout(){
        return -1;
    }


    public void setContentView(int layoutResID) {
        final LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater.from(getContext()).inflate(layoutResID, fragment_main_body);
    }

    public void setContentView(View view) {
        fragment_main_body.removeAllViews();
        fragment_main_body.addView(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void startActivity(Intent intent) {
        startActivity(intent, BaseActivity.ANIMATION_LEFT_RIGHT);
    }

    public void startActivity(Intent intent, int animationType) {
        startActivityForResult(intent, -1, animationType);
//		intent.putExtra(BaseActivity.ANIMATION_TYPE, animationType);
//		super.startActivity(intent);
//		if (animationType == BaseActivity.ANIMATION_LEFT_RIGHT) {
//			getActivity().overridePendingTransition(R.anim.push_right_in,
//					R.anim.push_left_out);
//		} else if (animationType == BaseActivity.ANIMATION_TOP_BOTTOM) {
//			getActivity().overridePendingTransition(R.anim.push_top_in,
//					R.anim.push_bottom_out);
//		}
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode,
                BaseActivity.ANIMATION_LEFT_RIGHT);
    }

    public void startActivityForResult(Intent intent, int requestCode,
                                       int animationType) {
        intent.putExtra(BaseActivity.ANIMATION_TYPE, animationType);
        super.startActivityForResult(intent, requestCode);
        if (animationType == BaseActivity.ANIMATION_LEFT_RIGHT) {
            getActivity().overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_left_out);
        } else if (animationType == BaseActivity.ANIMATION_TOP_BOTTOM) {
            getActivity().overridePendingTransition(R.anim.push_top_in,
                    R.anim.push_bottom_out);
        }
    }

    protected void onSkyLoginResp(int respCode) {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        dispose();
    }

    /******************************
     * 通用Handler
     ******************************/
    public boolean sendEmptyMessage(int what) {
        return ActivityHandler.getInstance(this).sendEmptyMessage(what);
    }

    public boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        return ActivityHandler.getInstance(this).sendEmptyMessageDelayed(what,
                delayMillis);
    }

    public boolean sendMessage(Message msg) {
        return ActivityHandler.getInstance(this).sendMessage(msg);
    }

    public boolean sendMessageDelayed(Message msg, long delayMillis) {
        return ActivityHandler.getInstance(this).sendMessageDelayed(msg,
                delayMillis);
    }

    public boolean hasMessages(int what) {
        return ActivityHandler.getInstance(this).hasMessages(what);
    }

    public void removeMessages(int what) {
        ActivityHandler.getInstance(this).removeMessages(what);
    }

    public final boolean post(Runnable r) {
        return ActivityHandler.getInstance(this).post(r);
    }

    public final boolean postDelayed(Runnable r, long delayMillis) {
        return ActivityHandler.getInstance(this).postDelayed(r, delayMillis);
    }

    public void handleMessage(Message msg) {
    }


    /**
     * 对请求权限的结果进行处理
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtil.getTargetSdkVersion(getContext()) < Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                grantResults[i] = PermissionUtil.selfPermissionGranted(getContext(), permissions[i]) ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED;
            }
        }
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!StartActivityManager.getInstance().handActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }


    public String getTagStr() {

        return "";
    }

    private BaseFragment fragment;

    public BaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(BaseFragment f) {
        this.fragment = f;
    }

    /**
     * TAB之间切换请使用showFragments方法,各tab定义为fragment 2017-1-13
     *
     * @param fragment
     */
    public void showFragments(BaseFragment fragment) {
        if (fragment == (getFragment())) {
            return;
        }

        setFragment(fragment);
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
//		trans.setCustomAnimations(R.anim.frag_enter, R.anim.frag_exit);
        trans.replace(R.id.fragment_main_body, fragment, fragment.getClass().getSimpleName());
        trans.commitAllowingStateLoss();

    }

    public void dispose(){

    }

}
