package net;



public class KeyFlag {

    public static String WECHAT_OPON_ID = "wechat_open_id";// 微信的openid的key
    public static String WECHAT_USER_NAME = "wechat_user_name";// 微信登录成功以后，返回的微信用户昵称的key


    public static String IP_WECHAT_OPEN_ID = "ip_wechat_open_id";// 根据微信登录以后获取到的openid，去问服务请求回来的ip的key
    public static String IP_BIND_OPEN_ID = "ip_bind_open_id";// 根据openid与邀请码，判断是否绑定过的ip的key


    public static String INVITE_CODE_OPEN_ID = "invite_code_open_id"; // 根据微信登录以后获取到的openid，去问服务请求回来的邀请码的key
    public static String INVITE_CODE_LOGIN = "invite_code_login";// 登录以后返回的邀请码

    public static String INVITE_CODE_KEY = "inviteCode_key"; //邀请码key

    public static String USER_NAME_KEY = "USER_NAME_KEY"; //邀请码key


    // 仅仅标记 用户输入的邀请码，不能证明是否登录，或者该邀请码是否正确
    public static String INVITE_CODE_KEY_INPUT = "INVITE_CODE_KEY_INPUT"; //用户输入的邀请码key

    public static String IMAGE_RUL_WX = "image_url_wx"; //微信登录返回的头像url


    public static String BASE_IP_KEY = "base_ip_key"; // 用户登录返回的邀请码，获取到的ip地址key
    public static String BASE_IP_KEY_INPUT = "BASE_IP_KEY_INPUT"; // 用户输入邀请码对应的ip

    public static String BASE_IP_VALUE_KEY_INPUT = "BASE_IP_VALUE_KEY_INPUT"; // 用户输入邀请码对应的ip 不带http:

    public static String KEY_DOMAIN = "KEY_DOMAIN"; // 用户登录后对应的domain
    public static String KEY_DOMAIN_INPUT = "KEY_DOMAIN_INPUT"; // 用户输入邀请码对应的domain


    public static String NORMAL_UID_KEY = "normal_uid_key"; // 通过邀请码获得的uid保存的key

    // 判断有么有登录，仅仅通过这一个字段
    public static String REAL_UID_KEY = "real_uid_key"; // 注册或登录成功后，保存uid的key


    public static String ZFB_KEY = "zfb_key"; // 支付宝
    public static String REAL_NAME_KEY = "realName_key"; //

    public static String PHONE_KEY = "phone_key"; //手机号码key
    public static String PS_KEY = "ps_key"; // 密码明文key
    public static String PSENC_KEY = "psEnc_key"; //密码密文key


    public static final String FIRST_OPEN = "FIRST_OPEN";//是否首次进入应用

    public static final String YuGuJiFen = "YuGuJiFen";//个人中心 - 预估积分
    public static final String LeiJiYouXiao = "LeiJiYouXiao";//个人中心 - 累计有效
    public static final String LeiJiDuiHuan = "LeiJiDuiHuan";//个人中心 - 累计兑换
    public static final String ZhangHuYuE = "ZhangHuYuE";//个人中心 - 账户余额

    public static final String JueSe = "JueSe";//个人中心 - 角色
    public static final String TouXiangUrl = "TouXiangUrl";//个人中心 - 头像地址
    public static final String PingTai = "PingTai";//个人中心 - 平台名称


    public static final String KEY_INFONUM = "KEY_INFONUM";//消息数量

}
