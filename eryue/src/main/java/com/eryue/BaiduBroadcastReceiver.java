package com.eryue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eryue.util.Logger;

/**
 * Created by enjoy on 2018/6/6.
 */

public class BaiduBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

//        PushJumpUtil.getInstance().registerBaiduPush(context);
        Logger.getInstance(context).writeLog_new("baidu", "baidu", "BaiduBroadcastReceiver---onReceive");

    }


}
