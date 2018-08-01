package com.eryue.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.library.permission.PermissionUtil;
import com.library.permission.PermissionsManager;


/**
 * ADF TOP aaa
 * <p/>
 * 为 BaseActivity 和 MainUIActivity 提取共有 父Activity
 *
 * @author cmwei
 */
public abstract class RootWindActivity extends FragmentActivity {

    public abstract ViewGroup getMainBodyView();

    @Override
    protected void onResume() {
        super.onResume();
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
        if (PermissionUtil.getTargetSdkVersion(this) < Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                grantResults[i] = PermissionUtil.selfPermissionGranted(this, permissions[i]) ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED;
            }
        }
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }


    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    // 5.0+
            Window window = this.getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);

            ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        } /*else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4+
            Window window = this.getWindow();
			ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
			//First translucent status bar.
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			int statusBarHeight = getStatusBarHeight();
			View mChildView = mContentView.getChildAt(0);
			if (mChildView != null) {
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
				//如果已经为 ChildView 设置过了 marginTop, 再次调用时直接跳过
				if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
					//不预留系统空间
					ViewCompat.setFitsSystemWindows(mChildView, false);
					lp.topMargin += statusBarHeight;
					mChildView.setLayoutParams(lp);
				}
			}
			View statusBarView = mContentView.getChildAt(0);
			if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statusBarHeight) {
				//避免重复调用时多次添加 View
				statusBarView.setBackgroundColor(color);
				return;
			}
			statusBarView = new View(this);
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
			statusBarView.setBackgroundColor(color);
			//向 ContentView 中添加假 View
			mContentView.addView(statusBarView, 0, lp);
		}*/
    }


}
