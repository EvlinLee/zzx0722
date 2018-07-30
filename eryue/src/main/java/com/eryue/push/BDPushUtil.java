package com.eryue.push;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.eryue.BaseApplication;
import com.eryue.R;


/**
 * 百度 推送 工具类
 * Created by cmwei on 2017/3/15.
 */
public class BDPushUtil {

    /**
     * 注册百度推送
     *
     * @param ctx
     */
    public static void registerBDPush(Context ctx) {
        try {
            PushCommUtil.writeDebugFileLog(" BDPushUtil.registerBDPush() ");
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                builder.setStatusbarIcon(R.drawable.ic_launcher);
            else
                builder.setStatusbarIcon(R.drawable.ic_launcher);
            builder.setNotificationFlags(NotificationCompat.FLAG_AUTO_CANCEL);
            PushManager.setDefaultNotificationBuilder(BaseApplication.getInstance(), builder);
            // TODO  这里就直接叫 api_key [manifest根节点] 是不是很容易跟别的 混淆了
            PushManager.startWork(ctx, PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(ctx, "api_key"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 取消注册 百度推送
     */
    public static void unregisterBDPush(Context ctx) {
        try {
            PushCommUtil.writeDebugFileLog(" BDPushUtil.unregisterBDPush() ");
            PushManager.stopWork(ctx);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
