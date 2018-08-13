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
            result = "猪猪虾推荐的产品便宜而且返积分又高，是真的吗？";
        } else if (tag.equalsIgnoreCase("13")) {
            result = "为什么有些商品在猪猪虾App中搜索不到？";
        } else if (tag.equalsIgnoreCase("14")) {
            result = "什么是积分？";
        } else if (tag.equalsIgnoreCase("15")) {
            result = "积分可以做什么";
        }

        return result;
    }

    private String getShowContent() {
        String result = "";
        if (tag.equalsIgnoreCase("11")) {
            result = "猪猪虾是专门为广大消费者打造的“综合性导购优惠返佣平台”，而且我们与淘宝、天猫、京东、拼多多，苏宁等知名电商平台深度合作，每天贴心推送近万件无门槛的大额优惠券。在这里，一元商品都包邮，爆款1折天天购！";
        } else if (tag.equalsIgnoreCase("12")) {
            result = "猪猪虾有自己专业的选品团队，我们在淘宝、拼多多、京东等各大知名电商平台超上亿的商品库中，挑选最具性价比的商品，所以你看到的都是即便宜品质还好，返积分还高的商品，你直接去分享或者购买自用分分钟爆单！强大的后盾支撑你市场拓展和推广！";
        } else if (tag.equalsIgnoreCase("13")) {
            result = "应该是这些商品商家没有设置优惠券和积分，所以猪猪虾就没有办法抓取到该商品信息。没有优惠券的产品也是不符合我们的导购原则，我们的导购原则是帮你寻找和搜集实用、可以领券，然后能给你返积分的一些商品。 ";
        } else if (tag.equalsIgnoreCase("14")) {
            result = "只要有人通过你的分享链接购买了标明积分的商品，即可获得积分。";
        } else if (tag.equalsIgnoreCase("15")) {
            result = "有效积分是可以提现的，每个月根据平台设置的提现时间，可以提现有效积分\n" +
                    "预计积分：订单付款后就会产生预估积分\n" +
                    "有效积分：订单确认收货后等等待结算日期后转换成有效积分可提现\n" +
                    "无效积分：是指订单未付款，订单取消，订单退款\n";
        }

        return result;
    }
}
