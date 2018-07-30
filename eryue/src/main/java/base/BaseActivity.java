package base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.eryue.util.StatusBarCompat;
import com.library.ui.LoadingDialog;

import java.util.List;

import base.navigationbar.RootWindActivity;
import base.navigationbar.UINavigationBar;

public class BaseActivity extends RootWindActivity implements UINavigationBar.OnBackListener {


    /**
     * 动画类型
     */
    public static final String ANIMATION_TYPE = "activity_animation_type";
    public static final int ANIMATION_LEFT_RIGHT = 0;
    public static final int ANIMATION_TOP_BOTTOM = 1;
    public static final int ANIMATION_RIGHT_LEFT = 2;
    public static final int NO_ANIMATION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        StatusBarCompat.setStatusColor(this,0xFFFF1A40);
        super.onCreate(savedInstanceState);
        init();
        mFragMgr = getSupportFragmentManager();

    }

    private RelativeLayout relativeLayout;
    public UINavigationBar navigationBar;
    public ViewGroup mainBody;

    private void init() {
        super.setContentView(R.layout.base_layout_1);
        relativeLayout = (RelativeLayout) findViewById(R.id.layout);
        navigationBar = (UINavigationBar) findViewById(R.id.navigationBar);
        mainBody = (ViewGroup) findViewById(R.id.main_body);
        hideNavigationBar(false);


        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);

        navigationBar.setOnBackListener(this);
    }



    public void setTitle(String title) {

        if (null != navigationBar) {
            navigationBar.setTitle(title);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public void setContentView(int layoutResID) {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutResID, mainBody);
    }

    public void setContentView(View view) {
        mainBody.removeAllViews();
        mainBody.addView(view);
    }

    public View getContentView() {
        return mainBody.getChildAt(0);
    }

    public void hideNavigationBar(boolean isHide) {
        if (isHide) {
            navigationBar.setVisibility(View.GONE);
        } else {
            navigationBar.setVisibility(View.VISIBLE);
        }
    }

    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    public void startActivity(Intent intent, int animationType) {
        startActivityForResult(intent, -1, animationType);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode, ANIMATION_LEFT_RIGHT);
    }

    public void startActivityForResult(Intent intent, int requestCode,
                                       int animationType) {
        intent.putExtra(ANIMATION_TYPE, animationType);
        super.startActivityForResult(intent, requestCode);
    }

    public void startActivity(Intent intent, int enterAnim, int exitAnim) {
        super.startActivityForResult(intent, -1);
        overridePendingTransition(enterAnim, exitAnim);
    }


    /**
     * 跳转下一个activity
     *
     * @param nextActivity 下一个activity
     */
    public void toNextPageRight(Class nextActivity) {
        Intent intent = new Intent();
        intent.setClass(this, nextActivity);
        startActivity(intent);
    }

    @Override
    public void finish() {
        int animationType = getIntent().getIntExtra(ANIMATION_TYPE,
                ANIMATION_LEFT_RIGHT);
        super.finish();
    }

    /**
     * 系统默认关闭方式
     */
    public void defaultFinish() {
        super.finish();
    }


    @Deprecated
    public static List mlist;

    @Deprecated
    public static void setObjList(List list) {
        mlist = list;
    }

    @Deprecated
    public List getObjList() {
        return mlist;
    }

    @Deprecated
    public void setObj(Object o) {

    }

    /*************************
     * 返回处理
     ************************/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBack() {
        hideSoftInput();
        this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    protected void hideSoftInput() {
        try {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getCurrentFocus() == null) {
                return;
            }
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNeedResumeSpeed() {
        return true;
    }

    @Override
    public ViewGroup getMainBodyView() {
        return mainBody;
    }


    /**
     * 菊花式进度条
     */
    private LoadingDialog mProgressDialog;

    private TextView mProgressTxt;

    /**
     * 显示菊花式进度条
     *
     * @param cancel 点击外围是否可以取消
     */
    public void showProgressMum(final boolean cancel) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgressDialog == null) {
                        mProgressDialog = new LoadingDialog(BaseActivity.this,
                                R.style.CustomProgressDialog);
                        mProgressDialog.setOnTouchOutsideCancel(cancel);
                    }
                    if (!mProgressDialog.isShowing()) {
                        mProgressDialog.show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示菊花式进度条
     */
    public void showProgressMum() {
        showProgressMum(true);
    }

    /**
     * 关闭菊花式进度条
     */
    public void hideProgressMum() {
        if (mProgressDialog == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
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
    protected void showFragment(BaseFragment fragment) {
        if (fragment == this.fragment) {
            return;
        }
        if (getFragment() != fragment && null != fragment) {
            FragmentTransaction transaction = mFragMgr.beginTransaction();
            if (null != getFragment()) {
                transaction.hide(getFragment());
            }
            setFragment(fragment);
            if (!fragment.isAdded()) {
                transaction.add(R.id.main_body, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
        this.fragment = fragment;
    }


}
