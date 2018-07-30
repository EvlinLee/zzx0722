package com.eryue;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.eryue.mine.SignCenterActivity;
import com.eryue.mine.login.LoginActivity1;
import com.eryue.util.Logger;

import net.DataCenterManager;
import net.KeyFlag;
import net.MineInterface;

import java.security.MessageDigest;
import static net.KeyFlag.IP_BIND_OPEN_ID;
import static net.KeyFlag.KEY_DOMAIN;
import static net.KeyFlag.PSENC_KEY;
import static net.KeyFlag.PS_KEY;

/**
 * Created by enjoy on 2018/5/11.
 */

public class AccountUtil {

//    public static final String DEFAULT_INVITECODE = "1280166";



    // sha1加密
    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    /**
     * 获取邀请码
     * @return
     */
    public static String getInviteCode(){
        String resultStr = "";


        //猪猪虾改版2.1，用户不输入邀请码，可以浏览程序

        String inviteCode = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.INVITE_CODE_LOGIN);
        if (! TextUtils.isEmpty(inviteCode)) {
            return inviteCode;
        }

        inviteCode = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.INVITE_CODE_OPEN_ID);
        if (! TextUtils.isEmpty(inviteCode)) {
            return inviteCode;
        }

        inviteCode = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.INVITE_CODE_KEY);
        if (!TextUtils.isEmpty(inviteCode)) {
            resultStr = inviteCode;
        }


        Log.d("libo","getInviteCode------"+resultStr);
        return resultStr;

    }

    /**
     * 获取baseIp
     * @return
     */
    public static String getBaseIp(){
//        return "http://39.105.112.91:8888";
//        return "http://www.371d.cn/";

        String resultStr = "http://118.25.188.178/";
        //用户输入的邀请码后的baseip
        String baseIpInput = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.BASE_IP_KEY_INPUT);
        //用户登录后返回的邀请码
        String baseIp = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.BASE_IP_KEY);

        // 根据openid与邀请码，判断是否绑定过的ip的key
        String ip = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.IP_BIND_OPEN_ID);
        // 根据微信登录以后获取到的openid，去问服务请求回来的ip的key
        String ip1 = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.IP_WECHAT_OPEN_ID);

        if (!TextUtils.isEmpty(ip)) {
            resultStr = ip;
        } else if (!TextUtils.isEmpty(ip1)) {
            resultStr = ip1;
        } else if (!TextUtils.isEmpty(baseIp)){
            resultStr = baseIp;
        } else if (!TextUtils.isEmpty(baseIpInput)){
            resultStr = baseIpInput;
        }

        Log.d("zdz", "ip is: " + resultStr);
        return resultStr;
    }

    /**
     * 获取baseIp
     * @return
     */
    public static String getBaseIpValue(){
//        return "http://39.105.112.91:8888";
//        return "http://www.371d.cn/";

        String resultStr = "111.231.57.126";
        //用户输入的邀请码后的baseip
        resultStr = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.BASE_IP_VALUE_KEY_INPUT);
        return resultStr;
    }

    /**
     * 获取domain
     * @return
     */
    public static String getServerURL() {
        String resultStr = "";
        //用户输入的邀请码后返回的domain
        String domainIpInput = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.KEY_DOMAIN_INPUT);
        //用户登录后返回的邀请码
        String domainIp = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.KEY_DOMAIN);
        if (!TextUtils.isEmpty(domainIp)){
            resultStr = domainIp;
        }else if (!TextUtils.isEmpty(domainIpInput)){
            resultStr = domainIpInput;
        }
        Log.d("libo","getServerURL------"+resultStr);
        return resultStr;

    }

    /**
     * 获取domain
     * @return
     */
    public static String getUID(){
        //默认uid为1
        String resultStr = "1";
        //用户输入邀请码后返回的uid
        String realUID = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.REAL_UID_KEY);
        //用户登录后返回的uid
        String normalUID = DataCenterManager.Instance().get(BaseApplication.getInstance(), KeyFlag.NORMAL_UID_KEY);
        if (!TextUtils.isEmpty(realUID)){
            resultStr = realUID;
        }else if (!TextUtils.isEmpty(normalUID)){
            resultStr = normalUID;
        }
        Log.d("libo","getUID------"+resultStr);
        return resultStr;

    }

    /**
     * 获取登录状态
     * @return
     */
    public static boolean isLogin(){
        Context context = BaseApplication.getInstance();
        boolean isLogin = false;


        String realUid = DataCenterManager.Instance().get(context, KeyFlag.REAL_UID_KEY);
        if (!TextUtils.isEmpty(realUid)) {
            isLogin = true;
        }

        Log.d("libo","AccountUtil---isLogin----用户登录返回的uid="+ realUid +"---isLogin="+isLogin);
        Logger.getInstance(context).writeLog_new("invitecode","invitecode","AccountUtil---isLogin----用户登录返回的uid="+realUid+"---isLogin="+isLogin);
        return  isLogin;
    }

    /**
     * 注销
     */
    public static void logout(){
        Context context = BaseApplication.getInstance();
        //手机密码明文
        DataCenterManager.Instance().save(context, PS_KEY, "");
        //手机密码密文
        DataCenterManager.Instance().save(context, PSENC_KEY, "");

        //用户登录返回的uid
        DataCenterManager.Instance().save(context, KeyFlag.REAL_UID_KEY, "");
        DataCenterManager.Instance().save(context, KeyFlag.NORMAL_UID_KEY, "");
        //用户登录返回的邀请码
        DataCenterManager.Instance().save(context, KeyFlag.INVITE_CODE_KEY, "");
        //支付宝账号
        DataCenterManager.Instance().save(context, KeyFlag.ZFB_KEY, "");
        //支付宝姓名
        DataCenterManager.Instance().save(context, KeyFlag.REAL_NAME_KEY, "");

        DataCenterManager.Instance().save(context, KeyFlag.WECHAT_OPON_ID, "");

        DataCenterManager.Instance().save(context, KeyFlag.IP_BIND_OPEN_ID, "");
        DataCenterManager.Instance().save(context, KeyFlag.INVITE_CODE_KEY_INPUT, "");

        DataCenterManager.Instance().save(context, KeyFlag.INVITE_CODE_LOGIN, "");
        DataCenterManager.Instance().save(context, KeyFlag.INVITE_CODE_OPEN_ID, "");

        //ip
        DataCenterManager.Instance().save(context,KeyFlag.BASE_IP_KEY_INPUT,"");
        DataCenterManager.Instance().save(context,KeyFlag.BASE_IP_KEY,"");
        DataCenterManager.Instance().save(context,KeyFlag.IP_BIND_OPEN_ID,"");
        DataCenterManager.Instance().save(context,KeyFlag.IP_WECHAT_OPEN_ID,"");

        DataCenterManager.Instance().save(context, KeyFlag.USER_NAME_KEY, "");


    }


    public static boolean checkLogin(Context context){
        //如果没有登录就进入到登录页
        //点击每日发圈、分享、立即购买 需要登录
        boolean isLogin = AccountUtil.isLogin();
        if(!isLogin){
            Intent intent = new Intent(context, LoginActivity1.class);
            context.startActivity(intent);
        }
        return isLogin;
    }

    public static void saveUserInfo(MineInterface.JudgeInfo userInfo) {
        if (null == userInfo) {
            return;
        }

        Context context = BaseApplication.getInstance();
        DataCenterManager.Instance().save(context, KeyFlag.YuGuJiFen, userInfo.yg + "");
        DataCenterManager.Instance().save(context, KeyFlag.LeiJiYouXiao, userInfo.yx + "");
        DataCenterManager.Instance().save(context, KeyFlag.LeiJiDuiHuan, userInfo.dh + "");
        DataCenterManager.Instance().save(context, KeyFlag.ZhangHuYuE, userInfo.balance + "");
        DataCenterManager.Instance().save(context, KeyFlag.REAL_UID_KEY, userInfo.uid + "");
        DataCenterManager.Instance().save(context, KeyFlag.INVITE_CODE_LOGIN, userInfo.code);

        DataCenterManager.Instance().save(context, KeyFlag.JueSe, userInfo.group_name);
        DataCenterManager.Instance().save(context, KeyFlag.TouXiangUrl, userInfo.pictUrl);
        DataCenterManager.Instance().save(context, KeyFlag.PingTai, userInfo.terrace);

        DataCenterManager.Instance().save(context, KeyFlag.USER_NAME_KEY, userInfo.userName);
    }


    public static String getHeaderImageUl() {
        Context context = BaseApplication.getInstance();

        String url = DataCenterManager.Instance().get(context, KeyFlag.TouXiangUrl);
        if (! TextUtils.isEmpty(url) && url.startsWith("http")) {
            return url;
        }

        url = DataCenterManager.Instance().get(context, KeyFlag.IMAGE_RUL_WX);
        if (!TextUtils.isEmpty(url)) {
            return url;
        }

        String serverURL = DataCenterManager.Instance().get(context, KeyFlag.KEY_DOMAIN);
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(serverURL)) {
            return serverURL + "user/" + url;
        }


        serverURL = DataCenterManager.Instance().get(context, IP_BIND_OPEN_ID);
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(serverURL)) {
            return serverURL + "user/" + url;
        }

        return "";
    }



}
