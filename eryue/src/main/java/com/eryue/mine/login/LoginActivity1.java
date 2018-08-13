package com.eryue.mine.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.eryue.WXShare;
import com.eryue.mine.LoginActivity;
import com.eryue.util.Logger;
import com.library.util.ToastTools;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import net.DataCenterManager;
import net.KeyFlag;
import net.MineInterface;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import base.BaseActivity;
import base.BaseActivityTransparent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by dazhou on 2018/7/6.
 * 登录 （第三版）
 */

public class LoginActivity1 extends BaseActivityTransparent implements View.OnClickListener{

    private TextView wechat_login;

    private TextView account_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideNavigationBar(true);
        setContentView(R.layout.activity_login_1);

        setupViews();
        initFinishReceiver();
    }

    private void setupViews() {
        wechat_login = findViewById(R.id.wechat_login);
        wechat_login.setOnClickListener(this);

        account_login = findViewById(R.id.account_login);
        account_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (wechat_login == view) {
            doWechatLogin();
//            justtest1();
        } else if (view == account_login) {
            accountLogin();
        }
    }


    private void initFinishReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("login_finish");
        registerReceiver(mFinishReceiver, filter);


        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("wechat_login_back");
        registerReceiver(mFinishReceiver, filter1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mFinishReceiver);

        if (wxShare != null) {
            wxShare.unregister();
        }
    }

    private String tmpCode;
    public BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("zdz", "mFinishReceiver");
            Logger.getInstance(context).writeLog_new("login", "login", "receive login finish");

            if ("login_finish".equals(intent.getAction())) {
                finish();
            } else if ("wechat_login_back".equals(intent.getAction())) {

                int errCode = intent.getIntExtra("errCode", -10101);
                if (errCode == BaseResp.ErrCode.ERR_OK) {
                    String code = intent.getStringExtra("code");
                    Log.d("zdz", "wx code: " + code);
                    Logger.getInstance(context).writeLog_new("login", "login", "wx code: " + code);

                    tmpCode = code;
                    new GetOpenIdThread().start();

                } else if (errCode == BaseResp.ErrCode.ERR_AUTH_DENIED) {
                    ToastTools.showShort(LoginActivity1.this, "用户拒绝授权");
                }  else if (errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                    ToastTools.showShort(LoginActivity1.this, "用户取消");
                } else {
                    ToastTools.showShort(LoginActivity1.this, errCode + "");
                }
            }
        }
    };

    private WXShare wxShare;
    // 第一步：调起 微信登录 第三方页面
    private void doWechatLogin() {
        wxShare = new WXShare(LoginActivity1.this);
        wxShare.register();
        wxShare.wechatLogin();
    }



    private void justtest1() {
        tmpAccessToken = "11_DBN2ihLevjVvPsWbVIRDae5G6fws9KMpXvbaNdKaN4AznwO2bXdLvmzqk-8kvmx4wNQ1jpWmNg7jar9e1b28X1uqKfdBIq_LRuLaj_yAbHs";
        tmpOpenid = "oeBOU1JBb25QN_Selwrm6BEhX5vI";
//        new GetWXUserInfoThread().start();

        isBindIp(tmpOpenid);

        DataCenterManager.Instance().save(LoginActivity1.this, KeyFlag.WECHAT_OPON_ID, tmpOpenid);
    }

    // 第二步： 根据微信登录成功以后的 code 获取 openid 和 access_token
    class GetOpenIdThread extends Thread{
        public void run(){
            HttpURLConnection conn=null;//声明连接对象
            String urlStr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +
                    WXShare.APP_ID + "&secret=" + WXShare.APP_SECRET +
                    "&code=" + tmpCode + "&grant_type=authorization_code";

            Log.d("zdz", "请求openid的url： " + urlStr);
            Logger.getInstance(LoginActivity1.this).writeLog_new("login", "login", "请求openid的url： " + urlStr);


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
                    Logger.getInstance(LoginActivity1.this).writeLog_new("login", "login", "微信登录返回的数据：" + resultData);

                    JSONObject jsonObject = new JSONObject(resultData);


                    String openid = (String) jsonObject.get("openid");
                    String access_token = (String) jsonObject.get("access_token");


                    Log.d("zdz", "openid : " + openid);
                    Log.d("zdz", "access_token : " + access_token);


                    if (TextUtils.isEmpty(openid)) {
                        Toast.makeText(LoginActivity1.this, "openid为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    tmpOpenid = openid;
                    tmpAccessToken = access_token;
                    new GetWXUserInfoThread().start();
                    isBindIp(openid);

                    DataCenterManager.Instance().save(LoginActivity1.this, KeyFlag.WECHAT_OPON_ID, openid);

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("zdz", e.toString());
                Logger.getInstance(LoginActivity1.this).writeLog_new("login", "login", "获取微信openid失败，稍后重试：" + e.toString());

                Toast.makeText(LoginActivity1.this, "获取微信openid失败，稍后重试", Toast.LENGTH_SHORT).show();
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
            Logger.getInstance(LoginActivity1.this).writeLog_new("login", "login", "获取微信用户个人信息urlStr： " + urlStr);


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
                    Logger.getInstance(LoginActivity1.this).writeLog_new("login", "login", "微信返回用户信息：" + resultData);

                    JSONObject jsonObject = new JSONObject(resultData);


                    String nickname = (String) jsonObject.get("nickname");
                    String headimgurl = (String) jsonObject.get("headimgurl");


                    Log.d("zdz", nickname);
                    Log.d("zdz", headimgurl);


                    DataCenterManager.Instance().save(LoginActivity1.this, KeyFlag.WECHAT_USER_NAME, nickname);
                    DataCenterManager.Instance().save(LoginActivity1.this, KeyFlag.IMAGE_RUL_WX, headimgurl);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("zdz", e.toString());
                Toast.makeText(LoginActivity1.this, "获取微信用户信息失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // 根据openid判断是否绑定过IP （缺接口）note:该接口返回 ip 与 邀请码， 都需要保存
//    如果没有绑定过，则走 输入邀请码 —— 输入手机号 —— 输入短信验证码 的逻辑；
//    如果已经绑定过，则先 判断根据 openid 有没有用户；（缺接口）
//                  如果没有用户，则走 输入邀请码 —— 输入手机号 —— 输入短信验证码 的逻辑；
//                  如果有用户，则返回到个人中心

    private void isBindIp(final String openid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.yifengdongli.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.SearchOpenIdBindReq req = retrofit.create(MineInterface.SearchOpenIdBindReq.class);
        Call<MineInterface.SearchOpenIdBindRsp> call = req.get(openid);
        call.enqueue(new Callback<MineInterface.SearchOpenIdBindRsp>() {
            @Override
            public void onResponse(Call<MineInterface.SearchOpenIdBindRsp> call, Response<MineInterface.SearchOpenIdBindRsp> response) {
                try {
                    if (response == null) {
                        showMessage("根据openid获取绑定ip的数据为空");
                        return;
                    }

                    Log.d("zdz", "isBindIp status : " + response.body().status);
                    Logger.getInstance(LoginActivity1.this).writeLog_new("login", "login", "isBindIp status : " + response.body().status);


                    if (response.body().status == 1) {
                        String ip = response.body().ip;
                        String code = response.body().code;
                        Log.d("zdz", "ip : " + ip);
                        Log.d("zdz", "code : " + code);
                        Logger.getInstance(LoginActivity1.this).writeLog_new("login", "login", "isBindIp ip : " + ip);
                        Logger.getInstance(LoginActivity1.this).writeLog_new("login", "login", "isBindIp code : " + code);

                        DataCenterManager.Instance().save(LoginActivity1.this, KeyFlag.IP_WECHAT_OPEN_ID, "http://" + ip + "/");
                        DataCenterManager.Instance().save(LoginActivity1.this, KeyFlag.INVITE_CODE_OPEN_ID, code);

                        alreadyHaveUser(openid);
                    }
                    // 没有绑定过ip
                    else {
                        gotoInputInviteCode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity1.this, "解析openid获取绑定ip数据失败，稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MineInterface.SearchOpenIdBindRsp> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity1.this, "获取openid获取绑定ip数据失败，稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 根据openid判断有没有用户
    private void alreadyHaveUser(String openid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AccountUtil.getBaseIp())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d("zdz", "alreadyHaveUser req");
        Logger.getInstance(LoginActivity1.this).writeLog_new("login", "login", "alreadyHaveUser req");

        MineInterface.SearchUserByOpenIdReq req = retrofit.create(MineInterface.SearchUserByOpenIdReq.class);
        Call<MineInterface.JudgeRsp> call = req.get(openid);
        call.enqueue(new Callback<MineInterface.JudgeRsp>() {
            @Override
            public void onResponse(Call<MineInterface.JudgeRsp> call, Response<MineInterface.JudgeRsp> response) {
                try {
                    if (response == null) {
                        Toast.makeText(LoginActivity1.this, "根据openid判断有没有用户的数据为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("zdz", "alreadyHaveUser status : " + response.body().status);
                    Logger.getInstance(LoginActivity1.this).writeLog_new("login", "login", "alreadyHaveUser status : " + response.body().status);


                    if (response.body().status == 1) {
                        MineInterface.JudgeInfo judgeInfo = response.body().result;

                        AccountUtil.saveUserInfo(judgeInfo);


                        ToastTools.showShort(LoginActivity1.this, "登录成功");

                        Intent intent = new Intent("login_finish");
                        intent.putExtra("finish_type", "wx_login");
                        getApplicationContext().sendBroadcast(intent);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    }
                    // 没有用户信息
                    else {
                        gotoInputInviteCode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity1.this, "解析根据openid判断有没有用户的数据失败，稍后重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MineInterface.JudgeRsp> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity1.this, "获取根据openid判断有没有用户数据失败，稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 在微信登录获取到个人信息以后，去输入邀请码
    private void gotoInputInviteCode() {
        Intent intent = new Intent(this, InputInviteCodeActivity.class);
        startActivity(intent);
    }



    // 账号登录
    private void accountLogin() {
        Intent intent = new Intent(LoginActivity1.this, InputInviteCodeActivity.class);
        intent.putExtra("loginType", "account_login");
        startActivity(intent);
    }


    private void showMessage(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity1.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
