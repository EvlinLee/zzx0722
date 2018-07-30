package base.navigationbar;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
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

import java.util.List;

import permission.PermissionUtil;
import permission.PermissionsManager;


public abstract class RootWindActivity extends FragmentActivity {

	public abstract ViewGroup getMainBodyView();

	@Override
	protected void onResume() {
		super.onResume();
		if(Build.VERSION.SDK_INT<14){
//			BaseApplication.getInstance().setCurrentActivity(this);
		}
	}

	public void popToRoot() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);

		if (runningTasks.get(0) != null) {
            //栈里面只有自己
            if (1 == runningTasks.get(0).numActivities) {
                return;
            }
			ComponentName base = runningTasks.get(0).baseActivity;
			try {
				Class baseClass = Class.forName(base.getClassName());
				Intent intent = new Intent(this, baseClass);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

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
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {	// 5.0+
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
		}
	}
}
