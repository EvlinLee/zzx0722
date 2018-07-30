package com.eryue.mine;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.eryue.AccountUtil;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.WXShare;
import com.eryue.activity.BaseFragment;
import com.eryue.home.SharePopView;
import com.eryue.mine.login.LoginActivity1;
import com.eryue.push.Utils;
import com.eryue.search.GoodsSearchListActivity;
import com.eryue.util.Logger;
import com.eryue.util.SharedPreferencesUtil;
import com.eryue.widget.ShareContentView;
import com.library.util.ImageUtils;
import com.library.util.StringUtils;
import com.library.util.ToastTools;
import com.library.util.UIScreen;
import com.tencent.mm.opensdk.modelbase.BaseResp;

import net.DataCenterManager;
import net.KeyFlag;
import net.MineInterface;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import permission.PermissionUtil;
import qrcode.QRCodeUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.KeyFlag.IP_BIND_OPEN_ID;
import static net.KeyFlag.KEY_DOMAIN;

/**
 * Created by dazhou on 2018/2/11.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private TextView loginTV;

    private String inviteCode = AccountUtil.getInviteCode();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setContentView(R.layout.activity_mine);

        initView();

        refreshView();

        initFinishReceiver();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onPause();
        } else {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        reqNormalAccountInfo();

        //用户输入的邀请码
        String inviteInputCode = DataCenterManager.Instance().get(getContext(), KeyFlag.INVITE_CODE_KEY_INPUT);

        this.inviteCode = AccountUtil.getInviteCode();

        Logger.getInstance(getContext()).writeLog_new("invitecode", "invitecode", "MineFragment---onResume----用户输入的邀请码=" + inviteInputCode + "------用户登录返回的邀请码=" + inviteCode);

        refreshView();
    }


    private void initFinishReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("login_finish");
        getContext().registerReceiver(mFinishReceiver, filter);
    }

    private String tmpCode;
    public BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("zdz", "mainfragment mFinishReceiver");

            if ("login_finish".equals(intent.getAction())) {
                refreshView();
                String type = intent.getStringExtra("finish_type");
                // 账号登录方式
                if (!TextUtils.isEmpty(type) && "account_login".equals(type)) {
                    
                } 
                // 微信登录的方式
                else if (!TextUtils.isEmpty(type) && "wx_login".equals(type)){
                    reqBindWX();
                }
            }
        }
    };

    public BroadcastReceiver mwxLoginReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("zdz", "mainfragment mwxLoginReceiver");
            if ("wechat_login_back".equals(intent.getAction())) {

                int errCode = intent.getIntExtra("errCode", -10101);
                if (errCode == BaseResp.ErrCode.ERR_OK) {
                    String code = intent.getStringExtra("code");
                    Log.d("zdz", "wx code: " + code);
                    tmpCode = code;
                    new GetOpenIdThread().start();

                } else if (errCode == BaseResp.ErrCode.ERR_AUTH_DENIED) {
                    ToastTools.showShort(getContext(), "用户拒绝授权");
                }  else if (errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                    ToastTools.showShort(getContext(), "用户取消");
                } else {
                    ToastTools.showShort(getContext(), errCode + "");
                }
            }
        }
    };

    private void refreshView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 登录以后，隐藏 申请合伙人，显示 退出登录；非登录状态，则相反
                if (AccountUtil.isLogin()) {
                    refreshLoginStateView();
                } else {
                    refreshUnLoginStateView();
                }
            }
        });
    }

    private void refreshLoginStateView() {
        String openid = DataCenterManager.Instance().get(getContext(), KeyFlag.WECHAT_OPON_ID);
        if (TextUtils.isEmpty(openid)) {
            bindwx.setText("未绑定微信");
        } else {
            bindwx.setText("已绑定微信");
        }
        login_out.setVisibility(View.VISIBLE);
        loginTV.setVisibility(View.GONE);
        cell_contain_login.setVisibility(View.VISIBLE);
        cell_contain_no_login.setVisibility(View.GONE);
        navigation_item_contain.setVisibility(View.VISIBLE);
        headview_login.setVisibility(View.VISIBLE);
        headview_no_login.setVisibility(View.GONE);

        yugujifen.setText(DataCenterManager.Instance().get(getContext(), KeyFlag.YuGuJiFen));
        leijiyouxiao.setText(DataCenterManager.Instance().get(getContext(), KeyFlag.LeiJiYouXiao));
        leijiduihuan.setText(DataCenterManager.Instance().get(getContext(), KeyFlag.LeiJiDuiHuan));
        zhanghuyue.setText(DataCenterManager.Instance().get(getContext(), KeyFlag.ZhangHuYuE));

        id_tv.setText("ID: " + AccountUtil.getUID());


        type.setText(DataCenterManager.Instance().get(getContext(), KeyFlag.PingTai));
        class_type.setText(DataCenterManager.Instance().get(getContext(), KeyFlag.JueSe));

        setHeaderImage();
        setName();
    }

    private void refreshUnLoginStateView() {
        getView().findViewById(R.id.login_out).setVisibility(View.GONE);
        loginTV.setVisibility(View.VISIBLE);
        cell_contain_login.setVisibility(View.GONE);
        cell_contain_no_login.setVisibility(View.VISIBLE);
        navigation_item_contain.setVisibility(View.GONE);
        headview_login.setVisibility(View.GONE);
        headview_no_login.setVisibility(View.VISIBLE);
    }

    private void setName() {
        String userName = DataCenterManager.Instance().get(getContext(), KeyFlag.USER_NAME_KEY);
        if (!TextUtils.isEmpty(userName)) {
            name.setText(userName);
        }
//        else {
//            String accountStr = DataCenterManager.Instance().get(getContext(), KeyFlag.PHONE_KEY);
//            if (!TextUtils.isEmpty(accountStr) && accountStr.length() == 11 && Utils.isChinaPhoneLegal(accountStr)) {
//                try {
//                    name.setText(accountStr.substring(0, 3) + "****" + accountStr.substring(7, 11));
//                } catch (Exception e) {
//                    name.setText(accountStr);
//                }
//            } else {
//                name.setText(accountStr);
//            }
//        }
    }

    private void setHeaderImage() {
        String url = AccountUtil.getHeaderImageUl();

        if (!TextUtils.isEmpty(url)) {
            Glide.with(this)
                    .load(url)
                    .asBitmap()
                    .error(R.drawable.img_default_contract)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                    .into(new BitmapImageViewTarget(header_imageview_login) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(header_imageview_login.getWidth()); //设置圆角弧度
                            header_imageview_login.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }

    private View login_out;
    private View shiyongbangzhu; // 使用帮助
    private View shiyongbangzhu_1; // 使用帮助
    private View tuiguangyaoqing; // 推广邀请
    private View tuiguangyaoqing_1; // 推广邀请
    private View contact_server; // 联系客服
    private View contact_server_1; // 联系客服

    private View about_us;

    private View cell_contain_no_login;// 未登录状态下才显示的view
    private View cell_contain_login;// 登录状态下才显示的view
    private View navigation_item_contain;
    private View headview_no_login;
    private View headview_login;
    private View wodetuandui; // 我的团队
    private View zhanghutixian; // 账户提现
    private View dingdanxiangqing; // 订单详情
    private View tuidanxiangqing; // 退单详情
    private View dailirenxuexi; // 代理人学习
    private View navigation_message;// 消息中心
    private View dingdantijiao; // 订单提交
    private View qiandao; //签到
    private View liulanzuji; // 浏览足迹
    private View liulanzuji_1; // 浏览足迹
    private View navigation_help;//


    private TextView yugujifen; // 预估积分
    private TextView leijiyouxiao; // 预估积分
    private TextView leijiduihuan; // 预估积分
    private TextView zhanghuyue; // 预估积分
    private TextView id_tv; // id
    private TextView name; // 用户名
    private TextView type; // 平台类型
    private TextView class_type;//代理类型
    private ImageView header_imageview_login;//头像

    private TextView bindwx;

    private void initView() {

        bindwx = getView().findViewById(R.id.bindwx);
        bindwx.setOnClickListener(this);
        yugujifen = getView().findViewById(R.id.yugujifen);
        leijiyouxiao = getView().findViewById(R.id.leijiyouxiao);
        leijiduihuan = getView().findViewById(R.id.leijiduihuan);
        zhanghuyue = getView().findViewById(R.id.zhanghuyue);
        id_tv = getView().findViewById(R.id.id_tv);
        name = getView().findViewById(R.id.name);
        type = getView().findViewById(R.id.type);
        class_type = getView().findViewById(R.id.class_type);
        header_imageview_login = getView().findViewById(R.id.header_imageview_login);

        cell_contain_login = getView().findViewById(R.id.cell_contain_login);
        cell_contain_no_login = getView().findViewById(R.id.cell_contain_no_login);
        navigation_item_contain = getView().findViewById(R.id.navigation_item_contain);
        headview_no_login = getView().findViewById(R.id.headview_no_login);
        headview_login = getView().findViewById(R.id.headview_login);
        wodetuandui = getView().findViewById(R.id.wodetuandui);
        wodetuandui.setOnClickListener(this);
        zhanghutixian = getView().findViewById(R.id.zhanghutixian);
        zhanghutixian.setOnClickListener(this);
        dingdanxiangqing = getView().findViewById(R.id.dingdanxiangqing);
        dingdanxiangqing.setOnClickListener(this);
        dailirenxuexi = getView().findViewById(R.id.dailirenxuexi);
        dailirenxuexi.setOnClickListener(this);

        navigation_help = getView().findViewById(R.id.navigation_help);
        navigation_help.setOnClickListener(this);
        navigation_message = getView().findViewById(R.id.navigation_message);
        navigation_message.setOnClickListener(this);
        tuidanxiangqing = getView().findViewById(R.id.tuidanxiangqing);
        tuidanxiangqing.setOnClickListener(this);
        dingdantijiao = getView().findViewById(R.id.dingdantijiao);
        dingdantijiao.setOnClickListener(this);
        qiandao = getView().findViewById(R.id.qiandao);
        qiandao.setOnClickListener(this);
        liulanzuji = getView().findViewById(R.id.liulanzuji);
        liulanzuji.setOnClickListener(this);
        liulanzuji_1 = getView().findViewById(R.id.liulanzuji_1);
        liulanzuji_1.setOnClickListener(this);

        loginTV = (TextView) (getView().findViewById(R.id.login));
        loginTV.setOnClickListener(this);


        login_out = getView().findViewById(R.id.login_out);
        login_out.setOnClickListener(this);

        shiyongbangzhu = getView().findViewById(R.id.shiyongbangzhu);
        shiyongbangzhu.setOnClickListener(this);
        shiyongbangzhu_1 = getView().findViewById(R.id.shiyongbangzhu_1);
        shiyongbangzhu_1.setOnClickListener(this);

        tuiguangyaoqing = getView().findViewById(R.id.tuiguangyaoqing);
        tuiguangyaoqing.setOnClickListener(this);
        tuiguangyaoqing_1 = getView().findViewById(R.id.tuiguangyaoqing_1);
        tuiguangyaoqing_1.setOnClickListener(this);

        contact_server = getView().findViewById(R.id.contact_server);
        contact_server.setOnClickListener(this);
        contact_server_1 = getView().findViewById(R.id.contact_server_1);
        contact_server_1.setOnClickListener(this);

        about_us = getView().findViewById(R.id.about_us);
        about_us.setOnClickListener(this);

        getView().findViewById(R.id.navigation_setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == liulanzuji || v == liulanzuji_1) {  // 浏览足迹
            Intent intent = new Intent(getContext(), VisitTraceActivity.class);
            startActivity(intent);
        } else if (v == qiandao) {//签到
            Intent intent = new Intent(getContext(), SignCenterActivity.class);
            startActivity(intent);
        } else if (v == dingdantijiao) { //订单提交
            Intent intent = new Intent(getContext(), OrderSubmissionActivity.class);
            startActivity(intent);
        } else if (v == tuidanxiangqing) { // 退单详情
            Intent intent = new Intent(getContext(), ChargeBackActivity.class);
            startActivity(intent);
        } else if (v == dailirenxuexi) { //代理人学习
            Intent intent = new Intent(getContext(), ProxyLearnActivity.class);
            startActivity(intent);
        } else if (v == navigation_message) {//消息中心
            Intent intent = new Intent(getContext(), MessageCenterActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
        } else if (v == navigation_help) {
            showTips();
        } else if (v == dingdanxiangqing) {//订单详情
            Intent intent = new Intent(getContext(), OrderDetailActivity1.class);
            startActivity(intent);
        } else if (v == getView().findViewById(R.id.navigation_setting)) {
            if (AccountUtil.isLogin()) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            } else {
                ToastTools.showShort(getContext(), "请先登录");
            }
        } else if (v == zhanghutixian) { // 账户提现
            Intent intent = new Intent(getContext(), HeHuoRenCashActivity.class);
            startActivity(intent);
        } else if (v == wodetuandui) { // 我的团队
            Intent intent = new Intent(getContext(), MyTeamActivity1.class);
            startActivity(intent);
        } else if (v == tuiguangyaoqing || v == tuiguangyaoqing_1) { //推广邀请
            Intent intent = new Intent(getContext(), ShareAppActivity.class);
            startActivity(intent);
        } else if (v == about_us) {
            Intent intent = new Intent(getContext(), AboutUsActivity.class);
            startActivity(intent);
        } else if (v == loginTV) {
            if (!AccountUtil.isLogin()) {
                Intent intent = new Intent(getContext(), LoginActivity1.class);
                startActivity(intent);
            }
        } else if (v == contact_server || v == contact_server_1) {
            //如果没有登录就进入到登录页
            //点击消息、每日发圈、列表分享、详情分享、立即购买、联系客服,需要登录
            if (!AccountUtil.checkLogin(getActivity())) {
                return;
            }
            Intent intent = new Intent(getContext(), ContactServerActivity.class);
            startActivity(intent);
        } else if (v == shiyongbangzhu || v == shiyongbangzhu_1) {// 使用帮助
            Intent intent = new Intent(getContext(), NewUserActivity.class);
            startActivity(intent);
        } else if (v == login_out) {// 退出登录
            AccountUtil.logout();
            //邀请码设置为用户输入的邀请码
            this.inviteCode = AccountUtil.getInviteCode();

            refreshView();
        } else if (v == bindwx) { // 绑定微信 或 解绑微信
            showBindTips();
        }
    }


    private void unBindWX(String openid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AccountUtil.getBaseIp())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.UnbindReq req = retrofit.create(MineInterface.UnbindReq.class);
        Call<MineInterface.BaseRsp> call = req.get(openid);
        call.enqueue(new Callback<MineInterface.BaseRsp>() {
            @Override
            public void onResponse(Call<MineInterface.BaseRsp> call, Response<MineInterface.BaseRsp> response) {
                try {
                    if (response == null) {
                        return;
                    }

                    if (response.body().status == 1) {
                        bindwx.setText("未绑定微信");
                        DataCenterManager.Instance().save(getContext(), KeyFlag.WECHAT_OPON_ID, "");
                        DataCenterManager.Instance().save(getContext(), KeyFlag.WECHAT_USER_NAME, "");
                        DataCenterManager.Instance().save(getContext(), KeyFlag.IMAGE_RUL_WX, "");


                        refreshView();
                    } else {
                        Toast.makeText(getContext(), "解绑失败，稍后重试", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "解绑失败，稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MineInterface.BaseRsp> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "解绑失败，稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private WXShare wxShare;
    // 调用的是微信登录功能，获取openid、头像、昵称等
    private void bindWX() {
        wxShare = new WXShare(getContext());
        wxShare.register();
        wxShare.wechatLogin();


        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("wechat_login_back");
        getContext().registerReceiver(mwxLoginReceiver, filter1);
    }

    private void showBindTips() {
        final String openid = DataCenterManager.Instance().get(getContext(), KeyFlag.WECHAT_OPON_ID);
        try {
            final Dialog dialog;
            // 设置dialog窗口展示的位置
            dialog = new Dialog(getContext(), R.style.MyDialogStyle);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bind_wx, null);
            TextView titel = view.findViewById(R.id.title);
            if (TextUtils.isEmpty(openid)) {
                titel.setText("去绑定微信？");
            } else {
                titel.setText("去解绑微信？");
            }

            view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(openid)) {
                        bindWX();
                    } else {
                        unBindWX(openid);
                    }
                    dialog.dismiss();
                }
            });


            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);

            // 获取dialog的窗口
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.x = 0;
            params.y = 0;
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.height = UIScreen.screenHeight - UIScreen.statusBarHeight;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 第二步： 根据微信登录成功以后的 code 获取 openid 和 access_token
    class GetOpenIdThread extends Thread{
        public void run(){
            HttpURLConnection conn=null;//声明连接对象
            String urlStr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +
                    WXShare.APP_ID + "&secret=" + WXShare.APP_SECRET +
                    "&code=" + tmpCode + "&grant_type=authorization_code";

            Log.d("zdz", "请求openid的url： " + urlStr);


            InputStream is = null;
            String resultData = "";
            try {
                URL url = new URL(urlStr); //URL对象
                conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接,下面设置这个连接
                conn.setRequestMethod("GET"); //使用get请求

                if(conn.getResponseCode() == 200){//返回200表示连接成功
                    is = conn.getInputStream(); //获取输入流
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader bufferReader = new BufferedReader(isr);
                    String inputLine  = "";
                    while((inputLine = bufferReader.readLine()) != null){
                        resultData += inputLine + "\n";
                    }
                    Log.d("zdz", "微信登录返回的数据：" + resultData);

                    JSONObject jsonObject = new JSONObject(resultData);


                    String openid = (String) jsonObject.get("openid");
                    String access_token = (String) jsonObject.get("access_token");


                    Log.d("zdz", "openid : " + openid);
                    Log.d("zdz", "access_token : " + access_token);


                    if (TextUtils.isEmpty(openid)) {
                        Toast.makeText(getContext(), "openid为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    tmpOpenid = openid;
                    tmpAccessToken = access_token;
                    new GetWXUserInfoThread().start();

                    DataCenterManager.Instance().save(getContext(), KeyFlag.WECHAT_OPON_ID, openid);

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("zdz", e.toString());
                Toast.makeText(getContext(), "获取微信openid失败，稍后重试", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String tmpAccessToken;
    private String tmpOpenid;


    // 第三步： 获取微信用户个人信息
    class GetWXUserInfoThread extends Thread{
        public void run(){
            HttpURLConnection conn=null;//声明连接对象
            String urlStr = "https://api.weixin.qq.com/sns/userinfo?access_token=" + tmpAccessToken +
                    "&openid=" + tmpOpenid;

            Log.d("zdz", "urlStr： " + urlStr);


            InputStream is = null;
            String resultData = "";
            try {
                URL url = new URL(urlStr); //URL对象
                conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接,下面设置这个连接
                conn.setRequestMethod("GET"); //使用get请求

                if(conn.getResponseCode() == 200){//返回200表示连接成功
                    is = conn.getInputStream(); //获取输入流
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader bufferReader = new BufferedReader(isr);
                    String inputLine  = "";
                    while((inputLine = bufferReader.readLine()) != null){
                        resultData += inputLine + "\n";
                    }
                    Log.d("zdz", "微信返回用户信息：" + resultData);

                    JSONObject jsonObject = new JSONObject(resultData);


                    String nickname = (String) jsonObject.get("nickname");
                    String headimgurl = (String) jsonObject.get("headimgurl");


                    Log.d("zdz", nickname);
                    Log.d("zdz", headimgurl);


                    DataCenterManager.Instance().save(getContext(), KeyFlag.WECHAT_USER_NAME, nickname);
                    DataCenterManager.Instance().save(getContext(), KeyFlag.IMAGE_RUL_WX, headimgurl);

                    refreshView();

                    getContext().unregisterReceiver(mwxLoginReceiver);

                    reqBindWX();

                    if (wxShare != null) {
                        wxShare.unregister();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("zdz", e.toString());
                Toast.makeText(getContext(), "获取微信用户信息失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    // 调用绑定openi与账号的关系
    private void reqBindWX() {
        String uid = AccountUtil.getUID();
        String openid = DataCenterManager.Instance().get(getContext(), KeyFlag.WECHAT_OPON_ID);
        
        // 这儿我用的是用户输入的邀请码，而不是登录成功以后返回的邀请码
        String invitecode = DataCenterManager.Instance().get(getContext(), KeyFlag.INVITE_CODE_KEY_INPUT);
        
        Log.d("zdz", "reqBindWX -- uid: " + uid + "  openid: " + openid + "  inviteCode: " + invitecode);
        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(openid) || TextUtils.isEmpty(invitecode)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AccountUtil.getBaseIp())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        String pictUrl = DataCenterManager.Instance().get(getContext(), KeyFlag.IMAGE_RUL_WX);

        MineInterface.BindReq req = retrofit.create(MineInterface.BindReq.class);
        Call<MineInterface.BaseRsp> call = req.get(uid, openid, invitecode, pictUrl);
        call.enqueue(new Callback<MineInterface.BaseRsp>() {
            @Override
            public void onResponse(Call<MineInterface.BaseRsp> call, Response<MineInterface.BaseRsp> response) {
                try {
                    if (response == null) {
                        return;
                    }

                    Log.d("zdz", "reqBindWX server back: " + response.body().status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MineInterface.BaseRsp> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showTips() {
        try {
            final Dialog dialog;
            // 设置dialog窗口展示的位置
            dialog = new Dialog(getContext(), R.style.MyDialogStyle);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_dialog_tips_goodslist, null);
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);

            // 获取dialog的窗口
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.x = 0;
            params.y = 0;
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.height = UIScreen.screenHeight - UIScreen.statusBarHeight;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void reqNormalAccountInfo() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.AccountInfoReq callFunc = retrofit.create(MineInterface.AccountInfoReq.class);
        Call<MineInterface.AccountInfoRsp> call = callFunc.get(AccountUtil.getUID());
        call.enqueue(new Callback<MineInterface.AccountInfoRsp>() {
            @Override
            public void onResponse(Call<MineInterface.AccountInfoRsp> call, Response<MineInterface.AccountInfoRsp> response) {
                if (response == null || response.body() == null) {
                    return;
                }
                final MineInterface.AccountInfoRsp res = response.body();

                if (null != getActivity()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Context context = getContext();
                            if (null != context) {
                                DataCenterManager.Instance().save(context, KeyFlag.YuGuJiFen, res.yg + "");
                                DataCenterManager.Instance().save(context, KeyFlag.LeiJiYouXiao, res.yx + "");
                                DataCenterManager.Instance().save(context, KeyFlag.LeiJiDuiHuan, res.dh + "");
                                DataCenterManager.Instance().save(context, KeyFlag.ZhangHuYuE, res.balance + "");
                            }


                            yugujifen.setText(res.yg + "");
                            leijiyouxiao.setText(res.yx + "");
                            leijiduihuan.setText(res.dh + "");
                            zhanghuyue.setText(res.balance + "");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<MineInterface.AccountInfoRsp> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
