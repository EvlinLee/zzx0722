package net;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by dzzhao.summer on 2018/2/9.
 */


public class InterfaceManager {
    final public static String baseURL = "http://www.yifengdongli.com/";

    // app用户协议
    static public String info1;
    // 平台用户协议
    static public String info2;

    // 邀请码获取ip
    public interface GetIpByInvitationCode {
        @POST("app/check.do")
        Call<IpResponse> get(@Query("code") String code);
    }

    public class IpResponse {
        // 0：邀请码错误；1：邀请码正确
        public int status;
        public List<IpInfo> result;

        public class IpInfo {
            // 对应服务器的ip
            public String ip;
            // 该邀请码对应的用户id
            public int uid;
        }
    }

    // 获取服务器配置接口
    public interface GetServerConfig {
        @POST("app/downConfig.do")
        Call<ServerConfigResponse> get();
    }

    public class ServerConfigResponse {
        // 0：失败；1：正确
        public int status;
        public ServerInfo result;

        public class ServerInfo {
            // 首页海报地址（要将其与域名拼接成完整的URL去请求）
            public List<String> banners;
            // 对应服务器的域名
            public String domain;

            public String regInfo;//注册协议
            public String regPlatformInfo;//平台注册规则
        }
    }

    // 注册短信验证码接口
    public interface GetPhoneCode {
        @POST("app/sendCode.do")
        Call<PhoneCodeResponse> get(@Query("phone") String phone);
    }

    public class PhoneCodeResponse {
        // 0：手机号已经存在（应该是已注册）；1：发送成功； -1 ： 系统错误
        public int status;
    }

    //    接口名称:找回密码短信验证码接口
    public interface GetPhoneCode2 {
        @POST("app/sendCode2.do")
        Call<PhoneCode2Response> get(@Query("phone") String phone);
    }

    public class PhoneCode2Response {
        // 0=手机号不存在 1=成功 -1=系统错误系统错误
        public int status;
    }

    // 注册接口
    public interface RegisterReq {
        @FormUrlEncoded
        @POST("app/appReg.do")
        Call<RegisterResponse> get(@Query("phone") String phone,
                                   @Query("password") String password,
                                   @Query("code") String code, // 短信验证码
                                   @Query("inviteCode") String inviteCode, // 邀请码
                                   @Field("userName") String userName,
                                   @Field("wx") String wx
        );
    }

    public class RegisterResponse {
        // 0：失败；1：成功； -1 ： 手机验证码错误 -2 手机号存在 -3用户存在

        public int status;
        public RegisterInfo result;

        public class RegisterInfo {
            // 用户的个人id2
            public int uid;
            // 用户个人邀请码
            public String code;
        }
    }


    //    接口名称:找回密码更新密码接口
    public interface UpdatePasswordReq {
        @POST("app/updatePassword.do")
        Call<UpdatePasswordResponse> get(@Query("phone") String phone,
                                         @Query("password") String password,
                                         @Query("code") String code
        );
    }

    public class UpdatePasswordResponse {
        public int status;//0=验证码不正确 1=成功 -1=系统错误
    }


    // 登录接口
    public interface LoginReq {
        @FormUrlEncoded
        @POST("app/appLogin.do")
        Call<LoginResponse> get(@Field("userName") String phone,
                                @Field("password") String password // 明文+s4lt转sha1
        );
    }

    public class LoginResponse {
        // 0：用户名或密码错误；1：成功； -1 ： 账户被禁用
        public int status;
        public LoginInfo result;

        public class LoginInfo {
            // 用户的个人id
            public int uid;
            // 用户个人邀请码
            public String code;
            public double balance; // 余额
            public double dh; // 累计兑换
            public double yg; // 预估积分
            public double yx; // 累计有效
            public String group_name;//角色
            public String pictUrl;//头像地址
            public String terrace; //平台名称
            public String userName;
            public String appWxOpenId;
        }
    }

    // 好券搜索关键词获取接口
    public interface KeywordsReq {
        @POST("app/keywords.do")
        Call<KeywordsResponse> get();
    }

    public class KeywordsResponse {
        // 0：失败；1：成功
        public int status;
        public KeywordsInfo result;

        public class KeywordsInfo {
            public List<String> keywords;
        }
    }


    public class SearchProductInfo {
        public double afterQuan; // 券后价
        public double discountPrice; // 原价
        public double isMall; // 0=淘宝   1=天猫
        public String itemId; // 宝贝编号
        public String itemTitle; // 宝贝标题
        public double jf; // 积分
        public String pictUrl;// 图片地址
        public double quanPrice;//券价格
        public String shortTitle;//短标题
        public int soldQuantity;// 销量

        public String searchFlag; // 搜索标记 【指定搜索接口中特有的返回值】
        public String video;//视频购物字段
    }

    // 好券搜索接口
    public interface SearchProductReq {
        @FormUrlEncoded
        @POST("app/searchProduct.do")
        Call<SearchProductResponse> get(@Field("page") int page, // 页码 （必填，从 1 开始）
                                        @Field("itemTitle") String itemTitle, //关键字 （选填）
                                        @Field("sidx") String sidx, // 排序字段 （选填，综合=updateTime ；券后价=afterQuan ；销量=soldQuantity ; 超优惠=quanPrice
                                        @Field("cat") String cat // 分类 （选填 查看全部就不传）

        );
    }

    public class SearchProductResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;
    }


    public class SearchProductDetailInfo {
        public String itemId; // 宝贝编号
        public String itemTitle; // 宝贝标题
        public String shortTitle;//小编推荐语
        public double discountPrice; // 原价
        public double afterQuan; // 券后价
        public double quanPrice;//券价格
        public int soldQuantity;// 销量
        public double jf; // 积分
        public int isCollect; // 0=未收藏；1=已收藏
        public List<String> pics; // 商品图片
        public String clickUrl;// 淘宝链接
        public String tkl;
        public int isMall;

        public List<SameCatProductInfo> sameCatProducts;

        public String searchFlag; // 搜索标记 【指定券详情页接口中特有，请注意】
        public String pictUrl; //图片地址 【指定券详情页接口中特有/好券优选详情页接口，请注意】
    }

    public class SameCatProductInfo {
        public double afterQuan;
        public String itemId;
        public String itemTitle;
        public String pictUrl;
    }

    // 好券搜索详情页接口
    public interface SearchProductDetailReq {
        @POST("app/searchProductDetail.do")
        Call<SearchProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                              @Query("uid") String uid // 用户id，之前接口获取的
        );
    }

    public class SearchProductDetailResponse {
        // 0：失败；1：成功
        public int status;
        public SearchProductDetailInfo result;
    }

    // 好券搜索分享页接口
    public interface ShareProductDetailReq {
        @POST("app/shareProductDetail.do")
        Call<ShareProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                             @Query("uid") long uid // 用户id，之前接口获取的
        );
    }

    public class ShareProductDetailResponse {
        // 0：失败；1：成功
        public int status;
        public ShareProductDetailInfo result;

    }

    public class ShareProductDetailInfo {
        public String content; // 文案
        public List<String> pics; // 商品图片
    }

    // 指定搜索接口
    public interface SuperSearchProductReq {
        @FormUrlEncoded
        @POST("app/superSearchProduct.do")
        Call<SuperSearchProductResponse> get(@Query("page") int page, // 页码 必填
                                             @Query("uid") long uid, // 用户id，必填
                                             @Field("itemTitle") String itemTitle, // 关键词 可选
                                             @Query("sidx") String sidx, // 排序字段 默认不传 券后价=afterQuan
                                             @Query("isQuan") int isQuan, // 可选 0=全部  1=只看有券
                                             @Query("isMall") int isMall // 可选 0=全部  1=天猫
        );
    }

    public class SuperSearchProductResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;

//        public class SuperSearchProductInfo {
//            public double afterQuan; //券后价
//            public double discountPrice; //原价
//            public int isMall; //0=淘宝，1=天猫
//            public String itemId; //宝贝编号
//            public String itemTitle; //宝贝标题
//            public double jf; //积分
//            public String pictUrl; //图片地址
//            public double quanPrice; //券价格
//            public String searchFlag; // 搜索标记
//            public String shortTitle; //短标题
//            public int soldQuantity; //销量
//        }
    }

    // 指定券详情页接口
    public interface SuperSearchProductDetailReq {
        @POST("app/superSearchProductDetail.do")
        Call<SuperSearchProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                                   @Query("uid") String uid, // 用户id
                                                   @Query("searchFlag") String searchFlag // 搜索标记
        );
    }

    public class SuperSearchProductDetailResponse {
        // 0：失败；1：成功
        public int status;
        public SearchProductDetailInfo result;

//        public class SuperSearchProductDetailInfo {
//            public double afterQuan; //券后价
//            public String clickUrl; // 淘宝链接
//            public double discountPrice; //原价
//            public int isMall; //0=淘宝，1=天猫
//            public String itemId; //宝贝编号
//            public String itemTitle; //宝贝标题
//            public double jf; //积分
//            public List<String> pics; // 商品图片
//            public String pictUrl; //图片地址
//            public double quanPrice; //券价格
//            public String searchFlag; // 搜索标记
//            public String shortTitle; //短标题
//            public int soldQuantity; //销量
//            public String tkl; //
//        }
    }


    // 指定券分享页接口
    public interface SuperShareProductDetailReq {
        @POST("app/superShareProductDetail.do")
        Call<SuperShareProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                                  @Query("uid") long uid, // 用户id
                                                  @Query("searchFlag") String searchFlag // 搜索标记
        );
    }

    public class SuperShareProductDetailResponse {
        // 0：失败；1：成功
        public int status;
        public ShareProductDetailInfo result;

    }


    // 首页推荐产品查询接口
    public interface SearchProductTjReq {
        @POST("app/searchProductTj.do")
        Call<SearchProductTjResponse> get();
    }

    public class SearchProductTjResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;
    }


    //9.9包邮接口
    public interface SearchProduct99Req {
        @FormUrlEncoded
        @POST("app/searchProduct99.do")
        Call<SearchProduct99Response> get(@Query("page") int page, // 页码 （必填，从 1 开始）
                                          @Field("itemTitle") String itemTitle, //关键字 （选填）
                                          @Query("sidx") String sidx, // 排序字段 （选填，综合=updateTime ；券后价=afterQuan ；销量=soldQuantity ; 超优惠=quanPrice
                                          @Query("cat") String cat // 分类 （选填 查看全部就不传）

        );
    }

    public class SearchProduct99Response {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;
    }


    //好券优选查询接口
    public interface SearchProductYxReq {
        @POST("app/searchProductYx.do")
        Call<SearchProductYxResponse> get(@Query("page") int page, // 页码 （必填，从 1 开始）
                                          @Query("sidx") String sidx // 排序字段 （选填，综合=updateTime ；券后价=afterQuan ；销量=soldQuantity ; 超优惠=quanPrice

        );
    }

    public class SearchProductYxResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;
    }


    // 好券优选详情页接口
    public interface SearchProductYxDetailReq {
        @POST("app/searchProductYxDetail.do")
        Call<SearchProductYxDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                                @Query("uid") String uid // 用户id
        );
    }

    public class SearchProductYxDetailResponse {
        // 0：失败；1：成功
        public int status;
        public SearchProductDetailInfo result;

//        public class SearchProductYxDetailInfo {
//            public double afterQuan; //券后价
//            public String clickUrl; // 淘宝链接
//            public double discountPrice; //原价
//            public int isMall; //0=淘宝，1=天猫
//            public String itemId; //宝贝编号
//            public String itemTitle; //宝贝标题
//            public double jf; //积分
//            public List<String> pics; // 商品图片
//            public String pictUrl; //图片地址
//            public double quanPrice; //券价格
//            public String shortTitle; //短标题
//            public int soldQuantity; //销量
//            public String tkl; //
//        }
    }


    // 好券优选分享页接口
    public interface ShareProductYxDetailReq {
        @POST("app/shareProductYxDetail.do")
        Call<ShareProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                             @Query("uid") long uid // 用户id，之前接口获取的
        );
    }

    // 接口名称:我的团队
    public interface MyTeamReq {
        @FormUrlEncoded
        @POST("app/team.do")
        Call<MyTeamResponse> get(@Query("level") int level, // 1=a级 2=b级
                                 @Query("uid") long uid, // 用户id，之前接口获取的
                                 @Query("page") int page, //页码
                                 @Field("userName") String userName //用户名
        );
    }

    public class MyTeamResponse {
        //0=无代理 1=有代理
        public int status;
        public int totalNum; //代理总数
        public List<MyTeamInfo> result;

        public class MyTeamInfo {
            public String userName;//昵称
            public double currentMonthAmount; //本月贡献
            public double lastMonthAmount;//上月贡献
        }
    }

    // 接口名称:查看激活量
    public interface SearchActivatorReq {
        @POST("app/searchActivator.do")
        Call<SearchActivatorResponse> get(@Query("code") String inviteCode);
    }

    public class SearchActivatorResponse {
        public int status;
        public List<SearchActivatorInfo> result;

        public class SearchActivatorInfo {
            public String createDate;// 日期
            public int totalNum; //总激活量
            public int iosNum;//ios激活量
            public int andriodNum;//安卓激活量
        }
    }


    //    接口名称:激活接口(在获取ip后调用)
    public interface AddActivatorReq {
        @POST("app/addActivator.do")
        Call<AddActivatorResponse> get(@Query("code") String code, // 邀请码；
                                       @Query("type") int type // 1=安卓 2=ios
        );
    }

    public class AddActivatorResponse {
        public int status; //0=成功 1=失败
    }


    //    接口名称:今日预估+今日总成交量+账户余额+上月结算预估收入+本月结算预估收入
    public interface AccountInfoReq {
        @POST("app/accountInfo.do")
        Call<AccountInfoResponse> get(@Query("uid") long uid
        );
    }

    public class AccountInfoResponse {
        public int status; //0=成功 1=失败
        public AccountInfo result;

        public class AccountInfo {

            public double todayAmount; //今日预估收入
            public int todayNum; //今日总成交单量
            public double balance; //余额
            public double currentMonthAmount; //本月结算预估收入
            public double lastMonthAmount; //上月结算预估收入
        }
    }


    // 接口名称:统计报表,今日，昨日，近7日，本月，上月
    public interface AccountStatisticsReq {
        @POST("app/accountStatistics.do")
        Call<AccountStatisticsResponse> get(@Query("uid") long uid,
                                            @Query("type") int type // 1=今日 2=昨日 3=近7日 4=本月 5=上月
        );
    }

    public class AccountStatisticsResponse {
        public int status; //0=失败 1=成功
        public AccountStatisticsInfo result;

        public class AccountStatisticsInfo {

            public double selftAmount; //自身贡献收入
            public int selftNum; //自身贡献量
            public double proxyAmount; //代理贡献收入
            public int proxyNum; //代理贡献量
        }
    }


    //    接口名称:订单明细
    public interface AccountOrderReq {
        @POST("app/accountOrder.do")
        Call<AccountOrderResponse> get(@Query("uid") long uid,
                                       @Query("page") long page, // 页码
                                       @Query("userType") long userType,//1=我的订单 2=团队贡献(A级) 3=团队贡献(B级)
                                       @Query("orderStatus") long orderStatus, //1=有效 0=无效
                                       @Query("type") int type // 1=今日 2=昨日 3=近7日 4=本月 5=上月
        );
    }

    public class AccountOrderResponse {
        public int status; //0=失败 1=成功
        public List<AccountOrderInfo> result;
    }

    public class AccountOrderInfo {

        public String pictUrl; //图片地址
        public String itemTitle; //标题
        public String orderNumber; //单号
        public double discountPrice; //付款金额
        public double integral; //预估收入

        public String integralPrividor; //收入来源
        public long orderDate; //订单日期
        public String orderStatus; //订单状态
    }

    //    接口名称:根据单号查订单明细
    public interface AccountOrderDetailReq {
        @POST("app/accountOrderDetail.do")
        Call<AccountOrderDetailResponse> get(@Query("uid") long uid,
                                             @Query("orderNumber") String orderNumber // 单号
        );
    }

    public class AccountOrderDetailResponse {
        public int status; //1=有单子 0=无单子
        public List<AccountOrderInfo> result;
    }


    // 接口名称:提现
    public interface ExchangeReq {
        @POST("app/exchange.do")
        Call<ExchangeResponse> get(@Query("uid") long uid,
                                   @Query("amount") long amount // 提现金额
        );
    }

    public class ExchangeResponse {
        public int status; //0=余额不足 1=成功 -1=未绑定支付宝 -2=发送过快 -3未到提现日期 -4 不满足提现门槛
    }


    //接口名称:绑定支付宝
    public interface BindAlipayReq {
        @FormUrlEncoded
        @POST("app/bindAlipay.do")
        Call<BindAlipayResponse> get(@Query("uid") long uid,
                                     @Field("realName") String realName, // 真实姓名
                                     @Field("zfb") String zfb, // 支付宝账号
                                     @Field("password") String password //
        );
    }

    //接口名称:绑定支付宝
    public interface BindAlipayByCodeReq {
        @FormUrlEncoded
        @POST("app/bindAlipayByCode.do")
        Call<BindAlipayByCodeRsp> get(@Query("uid") long uid,
                                     @Field("realName") String realName, // 真实姓名
                                     @Field("zfb") String zfb, // 支付宝账号
                                     @Field("code") String code, // 验证码
                                     @Query("phone") String phone // 手机号
        );
    }

    public class BindAlipayResponse {
        public int status; //0=失败 1=成功  -1密码不对 / 验证码错误
    }

    public class BindAlipayByCodeRsp {
        public int status; //0=失败 1=成功  -1 验证码错误
    }



    // 接口名称:资金记录
    public interface IntegralRecordReq {
        @POST("app/integralRecord.do")
        Call<IntegralRecordResponse> get(@Query("uid") long uid,
                                         @Query("page") int page
        );
    }

    public class IntegralRecordResponse {
        public int status; //0=失败 1=成功

        public List<IntegralRecordInfo> result;

        public class IntegralRecordInfo {

            public String orderDate; //时间
            public String type; //事件
            public double amount; //金额
        }
    }

    //视频购物查询接口
    public interface searchProductVideoReq {
        @POST("app/searchProductVideo.do")
        Call<searchProductVideoResponse> get(@Query("page") int page, // 页码 （必填，从 1 开始）
                                             @Query("sidx") String sidx // 排序字段 （选填，综合=updateTime ；券后价=afterQuan ；销量=soldQuantity ; 超优惠=quanPrice

        );
    }

    public class searchProductVideoResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;
    }

    //人气宝贝查询接口(好货疯抢、爆款单一致)
    public interface SearchProductTopReq {
        @POST("app/searchProductTop.do")
        Call<SearchProductTopResponse> get(@Query("page") int page, // 页码 （必填，从 1 开始）
                                           @Query("sidx") String sidx,// 排序字段 （选填，综合=updateTime ；券后价=afterQuan ；销量=soldQuantity ; 超优惠=quanPrice
                                           @Query("rice") String rice//可选

        );
    }

    public class SearchProductTopResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;
    }

    //品牌爆款查询接口
    public interface SearchProductTopBrandReq {
        @POST("app/searchProductBrand.do")
        Call<SearchProductTopBrandResponse> get(@Query("page") int page, // 页码 （必填，从 1 开始）
                                                @Query("sidx") String sidx// 排序字段 （选填，综合=updateTime ；券后价=afterQuan ；销量=soldQuantity ; 超优惠=quanPrice

        );
    }

    public class SearchProductTopBrandResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;
    }

    //聚划算查询接口
    public interface SearchProductJhsReq {
        @POST("app/searchProductJhs.do")
        Call<SearchProductJhsResponse> get(@Query("page") int page, // 页码 （必填，从 1 开始）
                                           @Query("sidx") String sidx// 排序字段 （选填，综合=updateTime ；券后价=afterQuan ；销量=soldQuantity ; 超优惠=quanPrice

        );
    }

    public class SearchProductJhsResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;
    }

    //淘抢购查询接口
    public interface SearchProductTqgReq {
        @POST("app/searchProductTqg.do")
        Call<SearchProductTqgResponse> get(@Query("page") int page, // 页码 （必填，从 1 开始）
                                           @Query("sidx") String sidx// 排序字段 （选填，综合=updateTime ；券后价=afterQuan ；销量=soldQuantity ; 超优惠=quanPrice

        );
    }

    public class SearchProductTqgResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;
    }

    //上百券查询接口
    public interface SearchProduct100Req {
        @POST("app/searchProductQuan100.do")
        Call<SearchProduct100Response> get(@Query("page") int page, // 页码 （必填，从 1 开始）
                                           @Query("sidx") String sidx// 排序字段 （选填，综合=updateTime ；券后价=afterQuan ；销量=soldQuantity ; 超优惠=quanPrice

        );
    }

    public class SearchProduct100Response {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;
    }

    //商品详情图片接口
    public interface GoodsDetailImgReq {
        @GET
        Call<String> getImg(@Url String url);
    }

    public class GoodsDetailImgResponse {
        public List<String> ret;
        public String v;
        public String api;
        public GoodsDetailImgInfo data;
    }

    public class GoodsDetailImgInfo {
        public List<String> pages;
        public List<String> images;

    }

    //每日发圈查询接口
    public interface SearchTimeLineReq {
        @POST("app/searchTimeLine.do")
        Call<SearchTimeLineResponse> get(@Query("uid") long uid, // 用户id，之前接口获取的
                                         @Query("sendTime") long sendTime// 时间戳 long类型

        );
    }

    public class SearchTimeLineResponse {
        // 0：失败；1：成功
        public int status;
        public List<TimeLine> result;
    }

    public static class TimeLine {

        public String content;
        public String content1;
        public String content2;
        public List<String> picts;
        public String sendTime;
    }

    //每日发圈查询接口2.0
    public interface SearchTimeLineExReq {
        @POST("app/searchMultiTimeLine.do")
        Call<SearchTimeLineExResponse> get(@Query("uid") long uid, // 用户id，之前接口获取的
                                           @Query("sendTime") long sendTime,// 时间戳 long类型
                                           @Query("page") int page// 页码

        );
    }

    public class SearchTimeLineExResponse {
        // 0：失败；1：成功
        public int status;
        public List<TimeLineEx> result;
    }

    public class TimeLineEx {
        public String type;
        public String title;
        public String content;
        public String sendTime;
        public List<String> picts;
    }

    //商品添加收藏接口
    public interface AddProcutCollectionReq {
        @POST("app/addProcutCollection.do")
        @FormUrlEncoded
        Call<AddProcutCollectionResponse> add(@Field("itemId") String itemId,//商品id
                                              @Field("itemTitle") String itemTitle,// 商品标题
                                              @Field("afterQuan") double afterQuan,// 券后价
                                              @Field("uid") long uid,// 用户id，之前接口获取的
                                              @Field("clickUrl") String clickUrl,// 淘宝url
                                              @Field("quanPrice") double quanPrice,// 券价格
                                              @Field("discountPrice") double discountPrice,// 原价
                                              @Field("shortTitle") String shortTitle,// 显示标题
                                              @Field("pictUrl") String pictUrl,// 图片url
                                              @Field("jf") double jf,
                                              @Field("soldQuantity") int soldQuantity,
                                              @Field("isMall") int isMall


        );
    }

    public class AddProcutCollectionResponse {
        // 0：失败；1：成功
        public int status;
    }


    public static class DeleteProcutModel {
        public String itemId;
        public long uid;
    }

    //商品添加收藏接口
    public interface DeleteProcutCollectionReq {
        @POST("app/deleteProcutCollection.do")
        @FormUrlEncoded
        Call<DeleteProcutCollectionResponse> delete(@Field("param") String param//param=[{"itemId":"1","uid":1}]//传json字符串
        );
    }

    public class DeleteProcutCollectionResponse {
        // 0：失败；1：成功
        public int status;
    }


    // 查看收藏产品
    public interface SearchProcutCollectionReq {
        @FormUrlEncoded
        @POST("app/searchProcutCollection.do")
        Call<SearchProcutCollectionResponse> get(@Field("uid") long uid

        );
    }

    public class SearchProcutCollectionResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfo> result;
    }


    // 收藏夹详情页接口
    public interface SearchProcutCollectionDetailReq {
        @POST("app/searchProcutCollectionDetail.do")
        Call<SearchProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                              @Query("uid") String uid // 用户id，之前接口获取的
        );
    }

    // 收藏夹分享接口
    public interface ShareProductCollectionDetailReq {
        @POST("app/shareProcutCollectionDetail.do")
        Call<ShareProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                             @Query("uid") long uid // 用户id
        );
    }


    // 人气宝贝详情页接口
    public interface SearchProductTopDetailReq {
        @POST("app/searchProductTopDetail.do")
        Call<SearchProductTopDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                                 @Query("uid") long uid // 用户id
        );
    }

    public class SearchProductTopDetailResponse {
        // 0：失败；1：成功
        public int status;
        public SearchProductDetailInfo result;

    }

    // 人气宝贝分享页接口(人气宝贝、爆款单、疯抢榜单)
    public interface ShareProductTopDetailReq {
        @POST("app/shareProductTopDetail.do")
        Call<ShareProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                             @Query("uid") long uid // 用户id
        );
    }

    // 品牌爆款详情页接口
    public interface SearchProductBrandDetailReq {
        @POST("app/searchProductBrandDetail.do")
        Call<SearchProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                              @Query("uid") String uid // 用户id
        );
    }


    // 品牌爆款分享页接口
    public interface ShareProductBrandDetailReq {
        @POST("app/shareProductBrandDetail.do")
        Call<ShareProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                             @Query("uid") long uid // 用户id
        );
    }

    // 聚划算详情页接口(聚划算、淘抢购、视频购物)
    public interface SearchTbActivityProductDetailReq {
        @POST("app/searchTbActivityProductDetail.do")
        Call<SearchProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                              @Query("uid") String uid // 用户id
        );
    }

    // 聚划算\淘抢购分享页接口
    public interface ShareTbActivityProductDetailReq {
        @POST("app/shareTbActivityProductDetail.do")
        Call<ShareProductDetailResponse> get(@Query("itemId") String itemId, // 宝贝编号
                                             @Query("uid") long uid // 用户id
        );
    }

    //商品详情图片接口
    public interface SendUpdateReq {
        @GET
        Call<SendUpdateResponse> get(@Url String url);
    }

    //版本更新
    public class SendUpdateResponse {
        // 0：失败；1：成功
        public int status;
        public UpdateModel result;
    }

    public class UpdateModel {
        public String version;
        //apk地址
        public String cgi;
    }


    /**
     * 猪猪虾2期
     */

    public class SearchProductInfoEx {
        public double afterQuan; // 券后价
        public int couponStatus = -1;//0=已经抢光 1=疯抢中 2=即将售罄
        public double discountPrice; // 原价
        public double isMall; // 0=淘宝   1=天猫
        public String itemId; // 宝贝编号
        public String itemTitle; // 宝贝标题
        public double jf; // 积分
        public List<String> pics;
        public String pictUrl;// 图片地址
        public String productType;//产品来源
        public String quanLink;
        public double quanPrice;//券价格
        public String shortTitle;//短标题
        public int soldQuantity;// 销量
        public String searchFlag; // 搜索标记


    }


    // 1.	首页热门推荐产品接口
    public interface IndexSearchReq {
        @FormUrlEncoded
        @POST("app/indexSearch.do")
        Call<IndexSearchResponse> get(@Field("page") int page // 页码 （必填，从 1 开始）
        );
    }

    public class IndexSearchResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfoEx> result;
    }


    public class SameCatProductInfoEx {
        public double afterQuan;
        public String discountPrice;
        public String itemId;
        public String itemTitle;
        public String jf;
        public String pictUrl;
        public String quanPrice;
        public String soldQuantity;
    }

    public class SearchProductDetailInfoEx {

        public String itemId; // 宝贝编号
        public String itemTitle; // 宝贝标题
        public String shortTitle;//小编推荐语
        public double discountPrice; // 原价
        public double afterQuan; // 券后价
        public double quanPrice;//券价格
        public int soldQuantity;// 销量
        public double jf; // 积分
        public int isCollect; // 0=未收藏；1=已收藏
        public List<String> pics; // 商品图片
        public String clickUrl;// 淘宝链接
        public String tkl;
        public int isMall;

        public List<SearchProductInfoEx> sameCatProducts;

        public String searchFlag; // 搜索标记 【指定券详情页接口中特有，请注意】
        public String pictUrl; //图片地址 【指定券详情页接口中特有/好券优选详情页接口，请注意】
        public String productType;
        public int couponStatus = -1;
    }

    // 搜索接口
    public interface WholeSearchReq {
        @FormUrlEncoded
        @POST("app/wholeSearch.do")
        Call<WholeSearchResponse> get(@Field("page") int page, // 页码 （必填，从 1 开始）
                               @Field("sord") String sord,//desc：降序/asc:升序
                               @Field("type") String type,//all：全网/tb：淘宝/jd：京东/mgj：蘑菇街/pdd：拼多多/sn：苏宁/tbActivity：淘宝活动
                               @Field("itemTitle") String itemTitle,//标题（关键词）非必选
                               @Field("cat") String cat,//shiping：食品/nanzhuang：男装/nvzhuang：女装/xiebao：鞋包/muyin：母婴/meizhuang：美妆
                               @Field("sidx") String sidx//updateTime：综合/afterQuan：价格/quanPrice：券价/soldQuantity：销量
        );
    }

    public class WholeSearchResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfoEx> result;
    }

    // 2.搜索详情接口
    public interface WholeSearchDetailReq {
        @FormUrlEncoded
        @POST("app/wholeSearchDetail.do")
        Call<WholeSearchDetailResponse> get(@Field("itemId") String itemId, // 商品id
                                            @Field("uid") String uid, // 用户uid
                                            @Field("productType") String productType, // 商品渠道
                                            @Field("searchFlag") String searchFlag //
        );
    }

    public class WholeSearchDetailResponse {
        // 0：失败；1：成功
        public int status;
        public SearchProductDetailInfoEx result;
    }

    public class WholeAppShareDetailInfo {

        public String productType; // 产品类型
        public String content; // 内容
        public String tkl;//淘口令
        public List<String> pics; // 商品图片
    }

    // 2.详情分享接口
    public interface WholeAppShareDetailReq {
        @FormUrlEncoded
        @POST("app/wholeShareDetail.do")
        Call<WholeAppShareDetailResponse> get(@Field("itemId") String itemId, //商品id
                                              @Field("uid") String uid, // 用户uid
                                              @Field("productType") String productType, // 商品渠道
                                              @Field("searchFlag") String searchFlag //
        );
    }

    public class WholeAppShareDetailResponse {
        // 0：失败；1：成功
        public int status;
        public WholeAppShareDetailInfo result;
    }


    // 实时播接口
    public interface SearchLiveReq {
        @FormUrlEncoded
        @POST("app/searchLive.do")
        Call<SearchLiveResponse> get(@Field("page") int page, // 页码 （必填，从 1 开始）
                                     @Field("sord") String sord,//desc：降序/asc:升序
                                     @Field("itemTitle") String itemTitle,//标题（关键词）非必选
                                     @Field("sidx") String sidx//updateTime：综合/afterQuan：价格/quanPrice：券价/soldQuantity：销量
        );
    }

    public class SearchLiveResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfoEx> result;
    }


    // 今日爆款接口
    public interface SearchtHotReq {
        @FormUrlEncoded
        @POST("app/searchtHot.do")
        Call<SearchtHotResponse> get(@Field("page") int page, // 页码 （必填，从 1 开始）
                                     @Field("sord") String sord,//desc：降序/asc:升序
                                     @Field("itemTitle") String itemTitle,//标题（关键词）非必选
                                     @Field("sidx") String sidx//updateTime：综合/afterQuan：价格/quanPrice：券价/soldQuantity：销量
        );
    }

    public class SearchtHotResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfoEx> result;
    }

    // 上百券搜索接口
    public interface SearchCouponsReq {
        @FormUrlEncoded
        @POST("app/searchCoupons.do")
        Call<SearchCouponsResponse> get(@Field("page") int page, // 页码 （必填，从 1 开始）
                                        @Field("sord") String sord,//desc：降序/asc:升序
                                        @Field("itemTitle") String itemTitle,//标题（关键词）非必选
                                        @Field("sidx") String sidx//updateTime：综合/afterQuan：价格/quanPrice：券价/soldQuantity：销量
        );
    }

    public class SearchCouponsResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfoEx> result;
    }

    // 首页红包接口
    public interface SearchRedPacketReq {
        @FormUrlEncoded
        @POST("appRedPacket/searchLastAppRedPacket.do")
        Call<SearchRedPacketResponse> get(@Field("uid") long uid);
    }

    public class SearchRedPacketResponse {
        // 0：失败；1：成功
        public int status;
        public SearchRedPacketInfo result;


    }

    public class SearchRedPacketInfo {

        public String databaseID;
        public String productID; // 图片路径
        public String productType; // 图片路径
        public String redPacketAddress; // 图片路径
        public String redPacketContent; // 图片路径
        public String totalb; // 图片路径
        public String updateTime; // 产品id
    }

    // 产品详情订单接口
    public interface SearchDetailsOrderReq {
        @FormUrlEncoded
        @POST("appDetailsOrder/searchDetailsOrder.do")
        Call<SearchDetailsOrderResponse> get(@Field("itemId") String itemId);
    }

    public class SearchDetailsOrderResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchDetailOrderInfo> result;
    }

    public static class SearchDetailOrderInfo {
        public int integral;
        public String integralNote;
        public String integralPrividor;
        public String orderDateStr;
        public String totalb;
    }


    // 首页公告接口
    public interface SearchNoticeReq {
        @FormUrlEncoded
        @POST("appNoticeManagement/appSearchNotice.do")
        Call<SearchNoticeResponse> get(@Field("uid") String uid);
    }

    public class SearchNoticeResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchNoticeInfo> result;
    }

    // 首页公告接口
    public static class SearchNoticeInfo {
        public String create_time;
        public String databaseID;
        public String info;
        public String title;
        public String totalb;
    }

    // 首页公告详情接口
    public interface SearchInfoReq {
        @FormUrlEncoded
        @POST("appNoticeManagement/appSearchInfo.do")
        Call<SearchInfoResponse> get(@Field("uid") long uid,
                                     @Field("databaseID") String databaseID);//公告id
    }

    //首页公告详情接口
    public class SearchInfoResponse {
        // 0：失败；1：成功
        public int status;
        public SearchDetailInfo result;
    }

    // 首页公告详情接口
    public static class SearchDetailInfo {
        public String info;
        public String totalb;
    }

    // 首页轮播图接口
    public interface ShufflingPictureReq {
        @FormUrlEncoded
        @POST("appShufflingPicture/appSearchShufflingPicture.do")
        Call<ShufflingPictureResponse> get(@Field("uid") String uid);
    }

    public class ShufflingPictureResponse {
        // 0：失败；1：成功
        public int status;
        public List<ShufflingPictureInfo> result;
    }

    // 首页轮播图接口
    public static class ShufflingPictureInfo {
        public String appShufflingPicture;//图片地址
        public String databaseID;
        public String itemID;//商品id
        public String totalb;
        public String type;//商品类型
        public int lbType;//轮播图类型 1：普通 2：专场
        public String zcPicture;//专场详情图片地址
    }


    // app接收设备编号和ip接口
    public interface AppEquipmentNumberReq {
        @FormUrlEncoded
        @POST("appEquipmentNumber/saveAppEquipmentNumber.do")
        Call<ResponseBody> get(@Field("channelID") String channelID,
                                           @Field("ip") String ip,//ip地址
                                           @Field("type") String type);//手机类型0苹果1安卓
    }

    public class AppEquipmentNumberResponse {
        // "success"//保存成功 ,"no"保存失败
        public String result;
    }


    // app消息数量接口
    public interface AppInfoCountReq {
        @FormUrlEncoded
        @POST("appProxyLearn/searchAppAllInformationCount.do")
        Call<AppInfoCountResponse> get(@Field("uid") String uid);
    }

    public class AppInfoCountResponse {
        public int status;
        public int result;//消息数量
    }

    // 京东免单接口
    public interface SearchJdFreeReq {
        @FormUrlEncoded
        @POST("app/searchJdFree.do")
        Call<SearchJdFreeResponse> get(@Field("page") int page, // 页码 （必填，从 1 开始）
                                      @Field("sidx") String sidx,//updateTime：综合/afterQuan：价格/quanPrice：券价
                                      @Field("sord") String sord//desc：降序/asc:升序
        );
    }

    public class SearchJdFreeResponse {
        // 0：失败；1：成功
        public int status;
        public List<SearchProductInfoEx> result;
    }

    // 京东免单商品详情接口
    public interface SearchJdDetailReq {
        @FormUrlEncoded
        @POST("app/searchJdFreeDetail.do")
        Call<SearchJdDetailResponse> get(@Field("itemId") String itemId, // 商品id
                                            @Field("uid") String uid // 用户uid
        );
    }

    public class SearchJdDetailResponse {
        // 0：失败；1：成功
        public int status;
        public SearchProductDetailInfoEx result;
    }

    // 2.详情分享接口
    public interface SearchJdShareReq {
        @FormUrlEncoded
        @POST("app/searchJdFreeShareDetail.do")
        Call<SearchJdShareResponse> get(@Field("itemId") String itemId, //商品id
                                              @Field("uid") String uid // 用户uid
        );
    }

    public class SearchJdShareResponse {
        // 0：失败；1：成功
        public int status;
        public WholeAppShareDetailInfo result;
    }

    // 首页获取促销活动接口
    public interface GetActivityJdReq {
        @FormUrlEncoded
        @POST("app/getActivity.do")
        Call<GetActivityJdResponse> get(@Field("uid") String uid // 用户uid
        );
    }

    public class GetActivityJdResponse {
        // 0：失败；1：成功
        public int status;
        public List<GetActivityJdInfo> result;
    }

    public class GetActivityJdInfo {

        public String activityName; //活动名称 jdfree=免单
        public int isOpen;//活动是否开启
        public String pictUrl;//活动海报图
        public int type;//跳转类型0=网页 1=源生
        public String url;//跳转的网页地址
    }



}
