package permission;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.eryue.util.SharedPreferencesUtil;

import java.util.Calendar;



/**
 * Created by sgli.Android on 2016/11/16.
 * 满足条件	推送信息	推送时间
 * 安装成功，超过2天没有没有登录	这位客官，使用手机号即可获取试用账号，详情咨询4008209463，立即获取>>	10:00
 * 超过14天没有登录	午餐时间到了，小万我含情脉脉，客官却好久都没有光顾，点击立即登录>>	12:00
 * 超过20天没有登录	陆家嘴早餐吃过了吗？小万含情脉脉数十天，客官却从未出现，点击立即看一眼>>	8:00
 * 超过25天没有登录	 山无陵，天地合，乃敢与君绝！客官还在吗？立刻看一眼>> 7:30
 */

public class LocalNotification {

    public static final String ACTION_LOCAL_PUSH_1 = "windinfo.android.localpush.1";
    public static final String ACTION_LOCAL_PUSH_2 = "windinfo.android.localpush.2";
    public static final String ACTION_LOCAL_PUSH_3 = "windinfo.android.localpush.3";
    public static final String ACTION_LOCAL_PUSH_4 = "windinfo.android.localpush.4";

    public static final String KEY_INSTALL_ALARM = "localpush.install";

    public static void clearPreviousInstallAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(context, 0, new Intent(ACTION_LOCAL_PUSH_1), PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public static void setInstallAlarm(Context context) {

        boolean hasNotified = SharedPreferencesUtil.getInstance().getBoolean(KEY_INSTALL_ALARM, false);
        if (!hasNotified) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            long now = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            //两天后的10点
            calendar.setTimeInMillis(now);
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.DAY_OF_YEAR, 2);

            Intent intent = new Intent(ACTION_LOCAL_PUSH_1);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long mills = calendar.getTimeInMillis();
            alarmManager.set(AlarmManager.RTC, mills, pi);
        } else {
            clearPreviousInstallAlarm(context);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(LocalNotification.ACTION_LOCAL_PUSH_1, 0);
        }
    }

    public static void setLoginAlarm(Context context) {
        clearPreviousInstallAlarm(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();

        //14天后的12点
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 14);

        Intent intent = new Intent(ACTION_LOCAL_PUSH_2);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long mills = calendar.getTimeInMillis();
        alarmManager.set(AlarmManager.RTC, mills, pi);

        //20天后的8点
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 20);

        intent = new Intent(ACTION_LOCAL_PUSH_3);
        pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mills = calendar.getTimeInMillis();
        alarmManager.set(AlarmManager.RTC, mills, pi);

        //25天后的7:30点
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 25);

        intent = new Intent(ACTION_LOCAL_PUSH_4);
        pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mills = calendar.getTimeInMillis();
        alarmManager.set(AlarmManager.RTC, mills, pi);
    }
}
