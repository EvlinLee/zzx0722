package com.eryue.mine;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.InterfaceManager;
import net.MineInterface;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 搜索订单
 */

public class SearchOrderActivity extends BaseActivity {

    public static String SearchType = "searchType";

    private EditText orderET;
    private ListView listView;
    private BaseAdapter adapter;
    private ArrayList dataArr;
    Context context;


    private int searchType; // 1：订单搜索  2：退单搜索

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationBar.setTitle("搜索订单");
        searchType = getIntent().getIntExtra(SearchType, 1);
        if (searchType == 2) {
            navigationBar.setTitle("搜索退单");
        }

        setContentView(R.layout.activity_serarch_order);
        context = this;

        initViews();
    }

    // 查 成交订单
    private void reqDealOrder() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        int page = 1;
        int flag = 2;
        int orderType = 0;
        int day = 0;
        int integralStatus = 2;

        String auctionTitle = orderET.getText().toString();
//        orderNum = "120545125339064933";
        if (TextUtils.isEmpty(auctionTitle)) {
            ToastTools.showShort(context, "请输入正确的信息");
            return;
        }

        showProgressMum();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.SearchOrderReq callFunc = retrofit.create(MineInterface.SearchOrderReq.class);
        Call<MineInterface.SearchOrderRsp> call = callFunc.get(
                page,
                auctionTitle,
                flag,
                orderType,
                day,
                integralStatus,
                AccountUtil.getUID());
        call.enqueue(new Callback<MineInterface.SearchOrderRsp>() {
            @Override
            public void onResponse(Call<MineInterface.SearchOrderRsp> call, final Response<MineInterface.SearchOrderRsp> response) {
                hideProgressMum();
                if (dataArr == null) {
                    dataArr = new ArrayList();
                } else {
                    dataArr.clear();
                }

                try {
                    if (response.body() == null || response.body().rows == null || response.body().rows.size() == 0) {
                        ToastTools.showShort(SearchOrderActivity.this, "未查询到订单");
                        return;
                    }

                    dataArr.addAll(response.body().rows);
                    refreshListView();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                refreshListView();
            }

            @Override
            public void onFailure(Call<MineInterface.SearchOrderRsp> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(SearchOrderActivity.this, "请求失败");
            }
        });
    }

    // 查 退单
    private void reqChargeBackOrder() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        int page = 1;
        int flag = 2;
        int orderType = 0;
        int day = 0;

        String auctionTitle = orderET.getText().toString();
//        orderNum = "120545125339064933";
        if (TextUtils.isEmpty(auctionTitle)) {
            ToastTools.showShort(context, "请输入正确的信息");
            return;
        }

        showProgressMum();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.SearchOrderRefundReq callFunc = retrofit.create(MineInterface.SearchOrderRefundReq.class);
        Call<MineInterface.SearchOrderRsp> call = callFunc.get(
                page,
                auctionTitle,
                flag,
                orderType,
                day,
                AccountUtil.getUID());
        call.enqueue(new Callback<MineInterface.SearchOrderRsp>() {
            @Override
            public void onResponse(Call<MineInterface.SearchOrderRsp> call, final Response<MineInterface.SearchOrderRsp> response) {
                {
                    hideProgressMum();
                    if (dataArr == null) {
                        dataArr = new ArrayList();
                    } else {
                        dataArr.clear();
                    }

                    try {

                        try {
                            if (response.body() == null || response.body().rows == null || response.body().rows.size() == 0) {
                                ToastTools.showShort(SearchOrderActivity.this, "未查询到订单");
                                return;
                            }

                            dataArr.addAll(response.body().rows);
                            refreshListView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        refreshListView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    refreshListView();
                }
            }

            @Override
            public void onFailure(Call<MineInterface.SearchOrderRsp> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(SearchOrderActivity.this, "请求失败");
            }
        });
    }

    private void refreshListView() {
        adapter.notifyDataSetChanged();
    }


    private void initViews() {
        orderET = (EditText) findViewById(R.id.order);
        listView = (ListView) findViewById(R.id.listview);
        findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchType == 1) {
                    reqDealOrder();
                } else if (searchType == 2) {
                    reqChargeBackOrder();
                }
            }
        });

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                if (dataArr != null) {
                    return dataArr.size();
                }

                return 0;
            }

            @Override
            public Object getItem(int position) {
                if (dataArr == null || dataArr.size() < position) {
                    return null;
                }
                return dataArr.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if (convertView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = inflater.inflate(R.layout.cell_order_detail_1, null);
                    viewHolder = new ViewHolder();
                    viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                    viewHolder.picture = (ImageView) convertView.findViewById(R.id.picture);
                    viewHolder.dingdanhao_value = (TextView) convertView.findViewById(R.id.dingdanhao_value);
                    viewHolder.pay_value = (TextView) convertView.findViewById(R.id.pay_value);
                    TextView pay = (TextView) convertView.findViewById(R.id.pay);

                    viewHolder.jifen_value = (TextView) convertView.findViewById(R.id.jifen_value);
                    viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                    viewHolder.offer = (TextView) convertView.findViewById(R.id.offer);
                    convertView.setTag(viewHolder);

                    TextView jifen_value = (TextView) convertView.findViewById(R.id.jifen_value);
                    TextView jifen = (TextView) convertView.findViewById(R.id.jifen);

                    if (searchType == 2) {
                        convertView.findViewById(R.id.bg_status).setVisibility(View.INVISIBLE);
                        pay.setText("退款金额:");
                        jifen.setVisibility(View.INVISIBLE);
                        jifen_value.setVisibility(View.INVISIBLE);
                    }
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                MineInterface.OrderInfo info = (MineInterface.OrderInfo) getItem(position);
                if (null == info) {
                    return convertView;
                }


                Glide.with(SearchOrderActivity.this).load(info.picUrl).into(viewHolder.picture);
                viewHolder.title.setText(info.auctionTitle);
                viewHolder.dingdanhao_value.setText(info.order_number);
                viewHolder.pay_value.setText(info.realPay + "");
                viewHolder.jifen_value.setText(info.integral + "");
                viewHolder.time.setText(info.orderDateStr);
                if (searchType == 2) {
                    viewHolder.offer.setText("由" + info.integralPrividor + "退单");
                } else if (searchType == 1) {
                    viewHolder.offer.setText("由" + info.integralPrividor + "提供");
                }


                return convertView;
            }
        };

        listView.setAdapter(adapter);

    }

    public class ViewHolder {
        public TextView title ;
        public ImageView picture ;
        public TextView dingdanhao_value ;
        public TextView pay_value ;
        public TextView jifen_value;
        public TextView time;
        public TextView offer;
    }
}
