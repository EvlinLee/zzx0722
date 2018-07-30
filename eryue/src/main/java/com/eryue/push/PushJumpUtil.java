package com.eryue.push;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.eryue.BaseApplication;
import com.eryue.R;
import com.eryue.activity.MainActivity;
import com.eryue.activity.SplashActivity;
import com.eryue.activity.WelcomeActivity;
import com.eryue.goodsdetail.GoodsDetailActivityEx;
import com.eryue.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enjoy on 2018/5/20.
 */

public class PushJumpUtil {



    private static volatile PushJumpUtil instance=null;

    /**
     * 保存推送消息的堆栈，只保存一个点击的消息
     */
    private List<PushJumpModel> pushList = new ArrayList<>();


    private PushJumpUtil(){

    }

    public static PushJumpUtil getInstance(){

        if (null == instance){

            synchronized (PushJumpUtil.class){
                if (null == instance){
                    instance = new PushJumpUtil();
                }
            }
        }

        return instance;

    }

    public void addMsg(PushJumpModel jumpModel){
        if (null!=pushList){
            pushList.clear();
        }

        pushList.add(jumpModel);
    }

    public PushJumpModel getMessage(){

        if (null!=pushList&&!pushList.isEmpty()){
            return pushList.get(0);
        }
        return null;

    }

    /**
     * 跳转到相应的功能
     * @param context
     */
    public void pushMsgJump(Context context, PushJumpModel jumpModel){
        if (null == jumpModel){
            return;
        }

        Activity rootActivity = BaseApplication.getInstance().getRootActivity();

        if (null!=rootActivity){
            Logger.getInstance(context).writeLog_new("baidu","baidu","null!=rootActivity");
//            Intent intent = new Intent(context, MainActivity.class);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//            rootActivity.startActivity(intent);

            ActivityManager am = (ActivityManager) rootActivity.getSystemService(Context.ACTIVITY_SERVICE) ;
            am.moveTaskToFront(rootActivity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);

            if (null!=pushList){
                pushList.clear();
            }
        }else{
            Logger.getInstance(context).writeLog_new("baidu","baidu","null==rootActivity");
            addMsg(jumpModel);

            Intent intent = new Intent();
            intent.setClass(context, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            try {
                pendingIntent.send();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    boolean isBaiduRegister;

    public void registerBaiduPush(Context context){
        //用户登录后返回的邀请码
        try {
            if (!isBaiduRegister){
                isBaiduRegister = true;
                PushCommUtil.writeDebugFileLog(" BDPushUtil.registerBDPush() ");
                BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    builder.setStatusbarIcon(R.drawable.ic_launcher);
                else
                    builder.setStatusbarIcon(R.drawable.ic_launcher);
                builder.setNotificationFlags(NotificationCompat.FLAG_AUTO_CANCEL);
                PushManager.setDefaultNotificationBuilder(BaseApplication.getInstance(), builder);

                String apiKey = Utils.getMetaValue(context.getApplicationContext(), "api_key");
                PushManager.startWork(context.getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, apiKey);
                Logger.getInstance(context).writeLog_new("baidu", "baidu", "PushManager.startWork---apiKey=" + apiKey);
            }

        } catch (Exception e) {
            isBaiduRegister = false;

        }
    }



}
