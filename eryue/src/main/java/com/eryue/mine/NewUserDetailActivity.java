package com.eryue.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.eryue.R;

import base.BaseActivity;

/**
 * 新手教程 详情页
 */

public class NewUserDetailActivity extends BaseActivity {

    private String tag;
    private TextView titleTV;
    private TextView contentTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_user_detail);

        navigationBar.setTitle("常见问题");

        tag = getIntent().getStringExtra("tag");
        if (TextUtils.isEmpty(tag)) {
            return;
        }

        initViews();
    }

    private void initViews() {
        titleTV = (TextView) findViewById(R.id.title1);
        contentTV = (TextView) findViewById(R.id.content);

        titleTV.setText(getShowTitle());
        contentTV.setText(getShowContent());
    }

    private String getShowTitle() {
        String result = "";
        if (tag.equalsIgnoreCase("11")) {
            result = "什么是猪猪虾APP？";
        } else if (tag.equalsIgnoreCase("12")) {
            result = "猪猪虾APP的亮点在哪？";
        } else if (tag.equalsIgnoreCase("13")) {
            result = "如何使用猪猪虾APP网购省钱？";
        } else if (tag.equalsIgnoreCase("14")) {
            result = "想知道今日最热最优惠的商品是什么吗？";
        } else if (tag.equalsIgnoreCase("15")) {
            result = "过年过节忘了买礼物怎么办？";
        } else if (tag.equalsIgnoreCase("16")) {
            result = "遇到心爱之物又有点犹豫怎么办？";
        } else if (tag.equalsIgnoreCase("17")) {
            result = "如何分享商品给好友？";
        } else if (tag.equalsIgnoreCase("21")) {
            result = "如何分享猪猪虾APP给好友？";
        }

        return result;
    }

    private String getShowContent() {
        String result = "";
        if (tag.equalsIgnoreCase("11")) {
            result = "【猪猪虾】APP一款专门为广大朋友打造专属优惠券，轻轻一点，省钱秘笈一手掌握，年省上千不是梦！ \n" +
                    "猪猪虾与百万厂商合作，每天贴心推送近万件无门槛的大额优惠券，买到就是赚到！优惠力度让你天天双十一，在这里，一元商品都包邮，1折爆款天天有！";
        } else if (tag.equalsIgnoreCase("12")) {
            result = "视频新玩法，网购新体验！翻转，旋转360度，好商品，细节一目了然~ \n" +
                    "\n" +
                    "【今日上新】 \n" +
                    "\n" +
                    "精选每日新鲜优惠券，从日常用品到奢侈大牌应有尽有，每天看一看，低价爆款全到手！ \n" +
                    "\n" +
                    "【9.9包邮】 \n" +
                    "\n" +
                    "不到10元！包邮！抢完就没有了！抢到就是赚到，现在，过日子全靠省！ \n" +
                    "\n" +
                    "【好券优选】 \n" +
                    "\n" +
                    "上万款精选好券等你来，天天更新优选好券，只为收纳优选好货！ \n" +
                    "\n" +
                    "【人气宝贝】 \n" +
                    "\n" +
                    "汇聚了人气高，销量好、口碑佳的宝贝。实打实的优惠力度，优质的商品，让你买的放心！好货不容错过~ \n" +
                    "\n" +
                    "【品牌爆款】 \n" +
                    "\n" +
                    "汇集万千大牌商家，内部优惠券都在这里等你出手！来阿~~客官~~";
        } else if (tag.equalsIgnoreCase("13")) {
            result = "你可以这样： \n" +
                    "\n" +
                    "1.手机淘宝上复制你看中的宝贝标题（如何复制：长按宝贝标题部分选择“复制标题”或“拷贝”）； \n" +
                    "\n" +
                    "2.进入猪猪虾APP即可立即搜索； \n" +
                    "\n" +
                    "3.进入详情页后即可领券购买！ \n" +
                    "\n" +
                    "还可以这样： \n" +
                    "\n" +
                    "1.点击首页上方搜索栏进入搜索页面； \n" +
                    "\n" +
                    "2.好券搜索：商家活动给力，优惠券额度大，直接搜索商品关键词即可， \n" +
                    "\n" +
                    "指定搜索：如果好券搜索没有你想要的也可以在指定搜索中输出15个字以上的完整标题进入搜索；";
        } else if (tag.equalsIgnoreCase("14")) {
            result = "来这里，今日值得买，最热门最优惠的商品正在这里限时抢购中，走过路过不要错过！";
        } else if (tag.equalsIgnoreCase("15")) {
            result = "海报轮播图帮你一步到位，你忘了的节日礼物，我们帮你收集最好的最优惠的，过年过节记得来这里看看~";
        } else if (tag.equalsIgnoreCase("16")) {
            result = "收藏夹来帮你，看中的宝贝点击左下角收藏按钮，即可在个人中心的收藏夹中找到，这下就不用怕错失好物了~";
        } else if (tag.equalsIgnoreCase("17")) {
            result = "你可以通过宝贝列表页单个宝贝右下角的分享按钮，共享优惠宝贝给你的好友~； \n" +
                    "\n" +
                    "也可以进入宝贝详情页，挑选你喜欢的主图分享到朋友圈，让更多朋友共享优惠；";
        } else if (tag.equalsIgnoreCase("21")) {
            result = "个人中心可生成多张海报让你挑选，拿得出手倍有面子~ \n" +
                    "分享APP按钮，一键分享，简单快捷！";
        }

        return result;
    }
}
