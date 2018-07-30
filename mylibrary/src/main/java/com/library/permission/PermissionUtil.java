package com.library.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;

import com.library.R;
import com.library.startactivity.StartActivityForResultAction;


/**
 * Created by sgli.Android on 2016/8/2.
 */
public class PermissionUtil {

    public static int targetSdkVersion = -1;

    public static int getTargetSdkVersion(Context context) {
        if (targetSdkVersion != -1)
            return targetSdkVersion;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
            return targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static boolean selfPermissionGranted(Context context, String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (getTargetSdkVersion(context) >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    @TargetApi(11)
    public static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).getActivity();
        } else {
            return null;
        }
    }


    public interface PermissionListener {
        //用户授予了权限
        void onPermissionGot();

        //至少有一个权限未获得
        void onPermissionDenied(String permission);

        //用户未授予并不再提示
        void onPermissionDeniedAndNeverAskAgain();
    }

    /**
     * 权限请求
     *
     * @param context            fragment 或者 activity
     * @param permissions        要请求的权限
     * @param permissionListener 回调
     * @param rationale          申请权限的理由
     * @param showRational       当用户选择不再提示的时候，不弹出对话框
     */
    public static void requestPermission(final Object context, @NonNull final String[] permissions, final PermissionListener permissionListener, final boolean showRational, final String rationale) {
        final StartActivityForResultAction startActivityForResultAction = new StartActivityForResultAction() {
            @Override
            public void handleResult(int resultCode, Intent data) {
                if (PermissionsManager.getInstance().hasAllPermissions(getActivity(context), permissions)) {
                    if (permissionListener != null)
                        permissionListener.onPermissionGot();
                } else {
                    if (permissionListener != null) {
                        for (String permission : permissions) {
                            if (!PermissionsManager.getInstance().hasPermission(getActivity(context), permission)) {
                                permissionListener.onPermissionDenied(permission);
                                break;
                            }
                        }
                    }
                }
            }
        };

        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(context, permissions, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                if (permissionListener != null)
                    permissionListener.onPermissionGot();
            }

            @Override
            public void onDenied(String permission) {
                final Activity activity = getActivity(context);
                String setting = activity.getString(R.string.permission_setting);
                String cancel = activity.getString(R.string.permission_cancel);

                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (permissionListener != null) {
                            permissionListener.onPermissionDeniedAndNeverAskAgain();
                        }
                    }
                };

                if (showRational && !PermissionsManager.checkDeniedPermissionsNeverAskAgain(activity
                        , rationale,
                        setting, cancel, onClickListener, permissions, startActivityForResultAction)) {
                    //没有权限需要显示理由时
                    if (permissionListener != null) {
                        permissionListener.onPermissionDenied(permission);
                    }
                }
            }
        });
    }

    /**
     * 权限请求
     *
     * @param context            fragment 或者 activity
     * @param permissions        要请求的权限
     * @param permissionListener 回调
     * @param rationale          申请权限的理由
     */
    public static void requestPermission(final Object context, @NonNull final String[] permissions, final PermissionListener permissionListener, final String rationale) {
        requestPermission(context, permissions, permissionListener, true, rationale);
    }

    /**
     * 权限请求
     *
     * @param context            fragment 或者 activity
     * @param permission         要请求的权限
     * @param permissionListener 回调
     * @param rationale          申请权限的理由
     */
    public static void requestPermission(final Object context, @NonNull final String permission, final PermissionListener permissionListener, final String rationale) {
        requestPermission(context, new String[]{permission}, permissionListener, rationale);
    }
}
