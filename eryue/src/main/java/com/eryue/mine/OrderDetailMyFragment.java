package com.eryue.mine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.InterfaceManager;
import net.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 我的订单
 */

public class OrderDetailMyFragment extends BaseFragment implements View.OnClickListener,
        DragRefreshListView.DragRefreshListViewListener {

    private int reqType; // 1=今日 2=昨日 3=近7日 4=本月 5=上月
    private int userType = 1; // 1=我的订单 2=团队贡献(A级) 3=团队贡献(B级)
    private int orderStatus = 1; //1=有效 0=无效

    private DragRefreshListView listView;
    private BaseAdapter adapter;

    private List<InterfaceManager.AccountOrderInfo> aTypeDataArr = new ArrayList<>();
    private List<InterfaceManager.AccountOrderInfo> bTypeDataArr = new ArrayList<>();
    private int currType = 1; // 1=已付款  2=已失效
    private int page1 = 1; // 已付款
    private int page2 = 1; // 已失效

    private int refreshType1 = 0; // 0 表示无更多数据；1表示有更多数据
    private int refreshType2 = 0; // 0 表示无更多数据；1表示有更多数据
    private Context context;

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_order_detail_my);

        context = getContext();
        setupViews();

        getData();
    }

    public void setReqType(int type) {
        reqType = type;

    }

    public void getData() {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }

        int page;
        if (orderStatus == 1) {
            page = page1;
        } else {
            page = page2;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.AccountOrderReq callFunc = retrofit.create(InterfaceManager.AccountOrderReq.class);
        Call<InterfaceManager.AccountOrderResponse> call = callFunc.get(uid, page, userType, orderStatus, reqType);
        call.enqueue(new Callback<InterfaceManager.AccountOrderResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.AccountOrderResponse> call, final Response<InterfaceManager.AccountOrderResponse> response) {
                if (null == getActivity() || getActivity().isFinishing()) {
                    return;
                }
                if (getActivity() != null) {
                    ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
                        @Override
                        public void handleMessage(Message msg) {

                            if (null!=response.body()&&response.body().status == 0) {
//                    ToastTools.showShort(context, "请求数据失败，稍后重试");
                                if (currType == 1) {
                                    refreshType1 = 0;
                                } else if (currType == 2) {
                                    refreshType2 = 0;
                                }
                                refreshListView();
                                return;
                            } else if (null!=response.body()&&response.body().status == 1) {
                                if (currType == 1) {
                                    if (response.body().result != null && response.body().result.size() > 0) {
                                        refreshType1 = 1; // 表示有更多数据
                                        aTypeDataArr.addAll(response.body().result);
                                    } else {
                                        refreshType1 = 0;
                                    }

                                } else if (currType == 2) {
                                    if (response.body().result != null && response.body().result.size() > 0) {
                                        refreshType2 = 1; // 表示有更多数据
                                        bTypeDataArr.addAll(response.body().result);
                                    } else {
                                        refreshType2 = 0;
                                    }
                                }
                                refreshListView();
                            }
                        }
                    }).sendEmptyMessageDelayed(0,200);
                }


            }

            @Override
            public void onFailure(Call<InterfaceManager.AccountOrderResponse> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(context, "请求失败");
            }
        });
    }

    private void refreshListView() {
        int refreshType;
        if (currType == 1) {
            refreshType = refreshType1;
        } else {
            refreshType = refreshType2;
        }
        adapter.notifyDataSetChanged();

        if (refreshType == 0) {
            //无数据
            listView.setFooterViewState(ListViewFooter.STATE_RESULT, "无更多数据", Color.GRAY);
        } else if (refreshType == 1) {
            listView.setFooterViewState(ListViewFooter.STATE_NORMAL);
        }

    }

    private void setupViews() {
        getActivity().findViewById(R.id.already_paid_my).setOnClickListener(this);
        getActivity().findViewById(R.id.invalid_my).setOnClickListener(this);
        listView = (DragRefreshListView) getActivity().findViewById(R.id.listview_my);
        listView.setDragRefreshListViewListener(this);
        listView.setHeaderViewEnable(false);
        listView.setAutoLoadMore(true);

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                if (currType == 1 && aTypeDataArr != null) {
                    return aTypeDataArr.size();
                } else if (currType == 2 && bTypeDataArr != null) {
                    return bTypeDataArr.size();
                } else {
                    return 0;
                }
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view;
                if (convertView == null) {
                    view = inflater.inflate(R.layout.cell_order_detail, null);
                } else {
                    view = convertView;
                }
                TextView tv1 = (TextView) view.findViewById(R.id.title);
                ImageView picture = (ImageView) view.findViewById(R.id.picture);
                TextView dingdanhao = (TextView) view.findViewById(R.id.dingdanhao);
                TextView orderStatus = (TextView) view.findViewById(R.id.dingdanfukuan);
                TextView value1 = (TextView) view.findViewById(R.id.value1);
                TextView value2 = (TextView) view.findViewById(R.id.value2);
                TextView value3 = (TextView) view.findViewById(R.id.value3);
                TextView time = (TextView) view.findViewById(R.id.time);

                InterfaceManager.AccountOrderInfo info;
                if (currType == 1) {
                    info = aTypeDataArr.get(position);
                    orderStatus.setBackgroundResource(R.drawable.dingdanfukuan);
                } else {
                    info = bTypeDataArr.get(position);
                    orderStatus.setBackgroundResource(R.drawable.order_status_1);
                }


                Glide.with(getContext()).load(info.pictUrl).into(picture);
                tv1.setText(info.itemTitle);
                dingdanhao.setText("订单号：" + info.orderNumber);
                orderStatus.setText(info.orderStatus);
                value1.setText("¥" + info.discountPrice);
                value2.setText("¥" + info.integral);
                value3.setText(info.integralPrividor);
                time.setText(TimeUtils.getStrTime(info.orderDate + "") + "创建");


                return view;
            }
        };

        listView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
        if (currType == 1) {
            page1++;
        } else {
            page2++;
        }
        getData();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(getActivity().findViewById(R.id.already_paid_my))) {
            changeType(1);
        } else if (v.equals(getActivity().findViewById(R.id.invalid_my))) {
            changeType(2);
        }
    }

    private void changeType(int type) {
        if (currType == type) {
            return;
        }


        currType = type;
        int color = Color.parseColor("#ff5533");
        int color1 = Color.parseColor("#666666");

        if (currType == 1) {
            TextView tv = (TextView) getActivity().findViewById(R.id.already_paid_my);
            tv.setTextColor(color);
            getActivity().findViewById(R.id.line1_my).setVisibility(View.VISIBLE);

            tv = (TextView) getActivity().findViewById(R.id.invalid_my);
            tv.setTextColor(color1);
            getActivity().findViewById(R.id.line2_my).setVisibility(View.INVISIBLE);

            orderStatus = 1;

            adapter.notifyDataSetChanged();
        } else if (currType == 2) {
            TextView tv = (TextView) getActivity().findViewById(R.id.already_paid_my);
            tv.setTextColor(color1);
            getActivity().findViewById(R.id.line1_my).setVisibility(View.INVISIBLE);

            tv = (TextView) getActivity().findViewById(R.id.invalid_my);
            tv.setTextColor(color);
            getActivity().findViewById(R.id.line2_my).setVisibility(View.VISIBLE);

            orderStatus = 0;

            if (bTypeDataArr != null && bTypeDataArr.size() == 0) {
                getData();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

}
