package com.eryue;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.eryue.goodsdetail.GoodsWebActivity;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;

/**
 * 京东跳转
 * Created by enjoy on 2018/5/23.
 */

public class JDManager {

    private static JDManager instance;

    private Context context;

    private JDManager(){

    }

    public static JDManager getInstance(){
        if (null == instance){
            instance = new JDManager();
        }
        return instance;
    }

    // mKeplerAttachParameter 存储第三方传入参数
    private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();

    private Handler mHandler = new Handler();

    // mOpenAppAction  呼京东主站回调
    private OpenAppAction mOpenAppAction = new OpenAppAction() {
        @Override
        public void onStatus(final int status, final String url) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
//                        dialogShow();
                    } else {
//                        mKelperTask = null;
//                        dialogDiss();
                    }
                    if (status == OpenAppAction.OpenAppAction_result_NoJDAPP) {
                        //未安装京东
                        if (null!=context){
                            Intent intent = new Intent(context,GoodsWebActivity.class);
                            intent.putExtra("url",url);
                            context.startActivity(intent);
                        }
                    } else if (status == OpenAppAction.OpenAppAction_result_BlackUrl) {
                        //不在白名单
                    } else if (status == OpenAppAction.OpenAppAction_result_ErrorScheme) {
                        //协议错误
                    } else if (status == OpenAppAction.OpenAppAction_result_APP) {
                        //呼京东成功
                    } else if (status == OpenAppAction.OpenAppAction_result_NetError) {
                        //网络异常
                    }
                }
            });
        }
    };


    /**
     * 通过url呼京东主站
     * @param mContext
     * @param url
     */
    public void openWebView(Context mContext, String url) {
        this.context = mContext;
        KeplerApiManager.getWebViewService().openAppWebViewPage(mContext,
                url,
                mKeplerAttachParameter,
                mOpenAppAction);
    }


}
