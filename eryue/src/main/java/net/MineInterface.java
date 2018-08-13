package net;

import com.tencent.mm.opensdk.modelbase.BaseResp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dazhou on 2018/5/13.
 * 2.0版本中，【个人中心】 里相关的接口定义
 */


public class MineInterface {

    // 接口名称:个人信息获取接口
    public interface SearchUserInfoReq {
        @POST("app/personal/searchUserlInfo.do")
        Call<SearchUserInfoRsp> get(@Query("uid") String uid);
    }

    public class SearchUserInfoRsp {
        // 0=失败 1=成功
        public int status;
        public UserInfo result;

        public class UserInfo {
            public String phone; //手机号
            public String wx; //微信
            public String qq;//qq
            public String zfb;//支付宝
            public String realName;//真实姓名
            public String userName;// 用户名

        }
    }


    // 接口名称:修改手机号+微信号+qq号接口
    public interface UpdateUserInfoReq {
        @FormUrlEncoded
        @POST("app/personal/updateUserInfo.do")
        Call<UpdateUserInfoRsp> get(@Query("phone") String phone,
                                    @Query("wx") String wx,
                                    @Query("qq") String qq,
                                    @Query("uid") String uid,
                                    @Field("userName") String userName);
    }

    public class UpdateUserInfoRsp {
        // -2=qq号已经存在 -1=微信号已经存在 0=手机号已经存在 1=成功 -3=用户名存在
        public int status;
        // 备注：user开头的用户名可以修改，非user开头的用户名不能修改
    }


    // 接口名称:修改密码接口
    public interface UpdatePwdReq {
        @POST("app/personal/updatePwd.do")
        Call<UpdatePwdRsp> get(@Query("oldPwd") String oldPwd,
                                    @Query("newPwd") String newPwd, //（密码传明文）
                                    @Query("uid") String uid);
    }

    public class UpdatePwdRsp {
        // 0=原始密码不对 1=成功
        public int status;
    }


    // 接口名称:订单查询
    public interface SearchOrderReq {
        @FormUrlEncoded
        @POST("appSearchOrder/searchOrder.do")
        Call<SearchOrderRsp> get(@Query("page") int page, // 页数
                                    @Field("auctionTitle") String auctionTitle, // 标题或者订单号
                                    @Query("flag") int flag, // 查询自己还是代理的单子（0自己的单子 1代理的 2自己跟代理的）
                                    @Query("orderType") int orderType,// 订单类型（淘宝：1,京东：2,蘑菇街：4,拼多多：5,苏宁：6,京东免单		3,全部:0）

                                    @Query("day") int day,//查询日期（1为当天，2为7天， 3本月，4上月，0全部）
                                    @Query("integralStatus") int integralStatus,//积分类型（0预估 1有效 2所有）
                                    @Query("uid") String uid
        );
    }

    public class SearchOrderRsp {
        public int status;

        public int balance;
        public int currentPage;
        public int dd; // 订单数量
        public int jdFreeBalance;
        public List<Integer> pageCount;
        public int perPage;
        public int refundMoney;

        public List<OrderInfo> rows;

        public int total;
        public int totalExchange;
        public int totalPage;
        public int totalReal;
        public int totalReocrd;


    }

    public class OrderInfo {
        public String auctionId;
        public String auctionTitle; // 订单标题
        public long create_date;
        public long create_time;
        public long databaseID;
        public double finalDiscountToString;
        public String groupName;
        public double integral; // 预估积分
        public String integralNote;
        public String integralOrign;
        public String integralPrividor; // 积分提供人
        public double integralRate;
        public long orderDate;
        public String orderDateStr;
        public String orderStatus;
        public String order_number; // 订单号
        public String picUrl; // 图片地址
        public double realIntegral; // 有效积分
        public double realPay; // 付款金额
        public String tbName;
        public double totalAlipayFeeString;
        public double totalb;
        public String user_name;
    }


    // 接口名称:订单积分汇总查询
    public interface SearchOrderIntegralReq {
        @FormUrlEncoded
        @POST("appSearchOrder/searchOrderIntegral.do")
        Call<SearchOrderIntegralRsp> get(
                                    @Field("auctionTitle") String auctionTitle, // 标题或者订单号
                                    @Query("flag") int flag, // 查询自己还是代理的单子（0自己的单子 1代理的 2自己跟代理的）
                                    @Query("orderType") int orderType,// 订单类型（淘宝：1,京东：2,蘑菇街：4,拼多多：5,苏宁：6,京东免单		3,全部:0）
                                    @Query("day") int day,//查询日期（1为当天，2为7天， 3本月，4上月，0全部）
                                    @Query("integralStatus") int integralStatus,//积分类型（0预估 1有效 2所有）
                                    @Query("uid") String uid
        );
    }

    public class SearchOrderIntegralRsp {
        public double integral; //总预估积分 
        public double realIntegral;//总有效积分
    }


    // 接口名称:订单查询 查退单
    public interface SearchOrderRefundReq {
        @FormUrlEncoded
        @POST("appSearchOrder/searchOrderRefund.do")
        Call<SearchOrderRsp> get(@Query("page") int page, // 页数
                                    @Field("auctionTitle") String auctionTitle, // 标题或者订单号
                                    @Query("flag") int flag, // 查询自己还是代理的单子（0自己的单子 1代理的 2自己跟代理的）
                                    @Query("orderType") int orderType,// 订单类型（淘宝：1,京东：2,蘑菇街：4,拼多多：5,苏宁：6,京东免单		3,全部:0）

                                    @Query("day") int day,//查询日期（1为当天，2为7天， 3本月，4上月，0全部）
                                    @Query("uid") String uid
        );
    }

    // 接口名称:订单积分查询 退单查询
    public interface SearchRefundMoneyReq {
        @FormUrlEncoded
        @POST("appSearchOrder/searchRefundMoney.do")
        Call<SearchRefundMoneyRsp> get(
                                         @Field("auctionTitle") String auctionTitle, // 标题或者订单号
                                         @Query("flag") int flag, // 查询自己还是代理的单子（0自己的单子 1代理的 2自己跟代理的）
                                         @Query("orderType") int orderType,// 订单类型（淘宝：1,京东：2,蘑菇街：4,拼多多：5,苏宁：6,京东免单		3,全部:0）
                                         @Query("day") int day,//查询日期（1为当天，2为7天， 3本月，4上月，0全部）
                                         @Query("uid") String uid
        );
    }
    public class  SearchRefundMoneyRsp {
        public double integral;
        public String integralNote;//订单有效
        public String orderDateStr;
        public String order_number;
        public double realPay;//退款总金额    
        public double totalb;
    }


    // 接口名称:签到接口
    public interface SignReq {
        @POST("appSign/sign.do")
        Call<String> get(@Query("uid") String uid);
    }


    // 接口名称:连续签到天数查询接口
    public interface SearchContinuousSignDayReq {
        @POST("appSign/searchContinuousSignDay.do")
        Call<SearchContinuousSignDayRsp> get(@Query("uid") String uid);
    }
    public class SearchContinuousSignDayRsp {
        public long databaseID;
        public long lastSignDate;
        public long signCount; // 连续签到天数
        public long signScore;
        public long totalSignCount;
        public double totalb;
        public String userName;
    }

    // 接口名称:具体签到天数查询接口
    public interface SearchSignDayReq {
        @POST("appSign/searchSignDay.do")
        Call<List<SignInfo>> get(@Query("uid") String uid);
    }

    public class SignInfo {
        public String comments;
        public long databaseID;
        public long date;//":1524816908000,//签到时间
        public long integral;
        public double totalb;
        public String userName;
    }

    // 接口名称:代理学习列表页接口
    public interface SearchAppProxyLearnReq {
        @POST("appProxyLearn/searchAppProxyLearn.do")
        Call<SearchAppProxyLearnRsp> get(@Query("uid") String uid,
                                 @Query("page") int page);
    }
    public class SearchAppProxyLearnRsp {
        public int currentPage;
        public long totalPage;
        public long totalReocrd;
        public int perPage;
        public List<ProxyLearnInfo> rows;
    }
    public class ProxyLearnInfo {
        public long createDate;
        public long databaseID;
        public String title;
        public long totalb;
        public String content;
        public int type;
        public String url;
    }


    // 接口名称:我的团队 2.0版本
    public interface TeamReq {
        @FormUrlEncoded
        @POST("app/myTeam.do")
        Call<TeamRsp> get(
                                 @Query("uid") String uid, // 用户id，之前接口获取的
                                 @Query("page") int page, //页码
                                 @Query("type") int type, // 0我的团队，1团队邀请
                                 @Query("sidx") String sidx, //注册时间排序reg_date ，累计提现排序totalWithdraw
                                 @Query("sord") String sord, //降序desc，升序asc
                                 @Field("userName") String userName
        );
    }
    public class TeamRsp {
        public int status;
        public int totalNum;
        public List<TeamInfo> result;
    }
    public class TeamInfo {
        public long regDate; //注册时间 
        public double totalWithdraw; //累计提现  
        public String userName; //用户名字             
        public String wxName;//微信名字 
        public String phone; // 手机号
    }


//    接口名称:app提现页面信息接口
    public interface AccountInfoReq {
        @POST("appWithdrawalCash/searchAppWithdrawalCashPage.do")
        Call<AccountInfoRsp> get(
                @Query("uid") String uid
        );
    }
    public class AccountInfoRsp {
        public double balance;//余额
        public double dh;
        public double yg;//预估
        public double yx;//累计有效;
    }

    //    接口名称:app提现页面信息接口
    public interface JDAccountInfoReq {
        @POST("appWithdrawalCash/searchAppJdFreeIntegral.do")
        Call<AccountInfoRsp> get(
                @Query("uid") String uid
        );
    }


//    接口名称:app提现规则
    public interface WithdrwalRulesReq {
        @POST("appWithdrawalCash/withdrawalRules.do")
        Call<WithdrwalRulesRsp> get();
    }
    public class WithdrwalRulesRsp {
        public double status;
        public String txRule;
    }


    //    接口名称:app京东免提现
    public interface JDTiXianReq {
        @POST("appWithdrawalCash/appJdWithdrawalCash.do")
        Call<String> get(
                @Query("uid") String uid,
                @Query("integral") long integral
        );
    }


    //    接口名称:app我的订单提交接口
    public interface OrderSubmitReq {
        @POST("app/orderSubmit.do")
        Call<String> get(
                @Query("uid") String uid,
                @Query("orderNumber") String orderNumber,
                @Query("type") int type //0京东免单，1 其他订单
        );
    }


    //    接口名称: app微信客服图片以及名称接口文档
    public interface ServerInfoReq {
        @POST("appNoticeManagement/appWeixinService.do")
        Call<ServerInfoRsp> get(
                @Query("uid") String uid
        );
    }
    public class ServerInfoRsp {
        public int status;
        public WechatServerInfo result;
    }

    public class WechatServerInfo {
        public String url;
        public String wxName;
    }


    // 接口名称:app消息接口
    public interface AppAllInformationReq {
        @POST("appProxyLearn/searchAppAllInformation.do")
        Call<AppAllInformationRsp> get(@Query("uid") String uid,
                                         @Query("page") int page);
    }
    public class AppAllInformationRsp {
        public int status;
        public List<AllInformation> result;
    }
    public class AllInformation {
        public String content;
        public long createDate;//时间             
        public long databaseID;
        public String smallTitle;//简介内容             
        public String title;//标题             
        public int totalb;
        public String type;
        public String url; //跳转地址
    }


    // 接口名称:app普通订单提现记录
    public interface NormalCashRecordReq {
        @POST("appWithdrawalCash/appWithdrawalCashRecord.do")
        Call<NormalCashRecordRsp> get(@Query("uid") String uid,
                                       @Query("page") int page);
    }
    public class NormalCashRecordRsp {
        public int status;
        public int num;
        public List<CashRecord> result;
    }
    public class CashRecord {
        public String codeStatus; //提现状态            
        public double exchangeNum; //提现金额             
        public long exchangeTime;//提现时间             
        public long totalb;
        public String typeName; //提现类型
    }

    // 接口名称:app京东免单订单提现记录
    public interface JdCashRecordReq {
        @POST("appWithdrawalCash/appJDFreeWithdrawalCashRecord.do")
        Call<NormalCashRecordRsp> get(@Query("uid") String uid,
                                      @Query("page") int page);
    }


//    一．接口名称:绑定open_id接口
//    接口地址: http://www.yifengdongli.com/OpenIdBind/addOpenIdBind.do
    public interface AddOpenIdBindReq {
        @POST("OpenIdBind/addOpenIdBind.do")
        Call<AddOpenIdBindRsp> get(@Query("code") String code, //上级邀请码  （必填）
                                        @Query("openId") String openId);//微信唯一识别  （必填）
    }

    public class AddOpenIdBindRsp extends BaseRsp {
        public String ip;
    }

    public class BaseRsp {
        public long status;
    }

//    接口名称：微信授权登录发送短信接口 
//    接口地址:http://ip/app/sendCodeForOpenId.do
//    json返回示例： {  "status": 1 // 1=成功 -1=系统错误 }
    public interface SendCodeForOpenIdReq {
        @POST("app/sendCodeForOpenId.do")
        Call<BaseRsp> get(@Query("phone") String code);
    }


//    二．接口名称:根据微信open_id查ip
//    接口地址: http://www.yifengdongli.com/OpenIdBind/searchOpenIdBind.do
    public interface SearchOpenIdBindReq {
        @POST("OpenIdBind/searchOpenIdBind.do")
        Call<SearchOpenIdBindRsp> get(@Query("openId") String openId);
    }
    public class SearchOpenIdBindRsp extends BaseRsp {
        public String code;
        public String ip;
    }


//    三．接口名称: app判断验证码
//    接口地址: http://ip/app/judge.do
public interface JudgeReq {
    @POST("app/judge.do")
    Call<JudgeRsp> get(@Query("appWxOpenId") String appWxOpenId,//微信openID（必填）
                       @Query("inviteCode") String inviteCode,//邀请码（必填）
                       @Query("code") String code,//手机验证码（必填）
                       @Query("phone") String phone, //手机号
                       @Query("pictUrl") String pictUrl//头像地址
    );
    }   //status:1   0=邀请码不存在 1=成功 -2=用户名已经存在 -3=手机号存在 -1=验证码不存在 -5用户被封禁
    public class JudgeRsp extends BaseRsp {
//        public int status;
        public JudgeInfo result;
    }

    public class JudgeInfo {
        public double balance;
        public String code;
        public double dh;
        public String group_name;
        public String pictUrl;
        public String terrace;
        public long uid;
        public double yg;
        public double yx;
        public String userName;
    }



//    四.接口名称:app根据openid查用户信息
//    接口地址: http://ip/app/searchUserByOpenId.do
    public interface SearchUserByOpenIdReq {
        @POST("app/searchUserByOpenId.do")
        Call<JudgeRsp> get(@Query("appWxOpenId") String appWxOpenId//绑定信息的openId  （必填）
        );
    }




//    五.接口名称: app微信解绑接口
//    接口地址: http://ip/app/unbind.do
public interface UnbindReq {
    @POST("app/unbind.do")
    Call<BaseRsp> get(@Query("appWxOpenId") String appWxOpenId//微信openID
    );
}



//    六．接口名称: app微信绑定openid接口
//    接口地址: http://ip/app/bind.do
    // "status": 1  //1：绑定成功；0：不成功；-1：appWxOpenId已绑定过用户
    public interface BindReq {
        @POST("app/bind.do")
        Call<BaseRsp> get(@Query("uid") String uid,
                          @Query("appWxOpenId") String appWxOpenId, //微信openID
                          @Query("code") String code, //邀请码
                          @Query("pictUrl") String pictUrl//头像地址

        );
    }
}

