package com.eryue.mine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
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
 * 团队贡献订单
 */

public class OrderDetailTeamFragment extends BaseFragment implements View.OnClickListener,
        DragRefreshListView.DragRefreshListViewListener {


    private int reqType; // 1=今日 2=昨日 3=近7日 4=本月 5=上月
    private int userType = 2; // 1=我的订单 2=团队贡献(A级) 3=团队贡献(B级)
    private int orderStatus = 1; //1=有效 0=无效

    private DragRefreshListView listView;
    private BaseAdapter adapter;

    private int pageType = 0; // 1=A级 已付款; 2=A级 已失效; 3=B级 已付款; 4=B级 已失效
    private int page1; // A级 已付款
    private int page2; // A级 已失效
    private int page3; // B级 已付款
    private int page4; // B级 已失效

    private List<InterfaceManager.AccountOrderInfo> aTypePaidDataArr = new ArrayList<>();
    private List<InterfaceManager.AccountOrderInfo> bTypePaidDataArr = new ArrayList<>();
    private List<InterfaceManager.AccountOrderInfo> aTypeInvildDataArr = new ArrayList<>();
    private List<InterfaceManager.AccountOrderInfo> bTypeInvildDataArr = new ArrayList<>();

    private int refreshType1 = 0; // 0 表示无更多数据；1表示有更多数据
    private int refreshType2 = 0; // 0 表示无更多数据；1表示有更多数据
    private int refreshType3 = 0; // 0 表示无更多数据；1表示有更多数据
    private int refreshType4 = 0; // 0 表示无更多数据；1表示有更多数据
    private Context context;

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_order_detail_team);
        page1 = 1; // A级 已付款
        page2 = 1; // A级 已失效
        page3 = 1; // B级 已付款
        page4 = 1; // B级 已失效
        context = getContext();

        setupViews();
        changeType(1);

    }

    public void setReqType(int type) {
        reqType = type;

    }

    public void getData() {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }

        int page = 1;
        if (userType == 2 && orderStatus == 1) {
            page = page1;
        } else if (userType == 2 && orderStatus == 0) {
            page = page2;
        }  else if (userType == 3 && orderStatus == 1) {
            page = page3;
        }  else if (userType == 3 && orderStatus == 0) {
            page = page4;
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
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null!=response.body()&&response.body().status == 0) {
//                    ToastTools.showShort(context, "请求数据失败，稍后重试");
                                if (pageType == 1) {
                                    refreshType1 = 0;
                                } else if (pageType == 2) {
                                    refreshType2 = 0;
                                }  else if (pageType == 3) {
                                    refreshType3 = 0;
                                }  else if (pageType == 4) {
                                    refreshType4 = 0;
                                }
                                refreshListView();
                                return;
                            } else if (null!=response.body()&&response.body().status == 1) {
                                if (pageType == 1) {
                                    if (response.body().result != null && response.body().result.size() > 0) {
                                        refreshType1 = 1; // 表示有更多数据
                                        aTypePaidDataArr.addAll(response.body().result);
                                    } else {
                                        refreshType1 = 0;
                                    }

                                } else if (pageType == 2) {
                                    if (response.body().result != null && response.body().result.size() > 0) {
                                        refreshType2 = 1; // 表示有更多数据
                                        aTypeInvildDataArr.addAll(response.body().result);
                                    } else {
                                        refreshType2 = 0;
                                    }
                                }  else if (pageType == 3) {
                                    if (response.body().result != null && response.body().result.size() > 0) {
                                        refreshType3 = 1; // 表示有更多数据
                                        bTypePaidDataArr.addAll(response.body().result);
                                    } else {
                                        refreshType3 = 0;
                                    }
                                }  else if (pageType == 4) {
                                    if (response.body().result != null && response.body().result.size() > 0) {
                                        refreshType4 = 1; // 表示有更多数据
                                        bTypeInvildDataArr.addAll(response.body().result);
                                    } else {
                                        refreshType4 = 0;
                                    }
                                }
                                refreshListView();
                            }
                        }
                    });
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
        if (null==getActivity()||getActivity().isFinishing()){
            return;
        }

        int refreshType = 0;
        if (pageType == 1) {
            refreshType = refreshType1;
        } else if (pageType == 2) {
            refreshType = refreshType2;
        }  else if (pageType == 3) {
            refreshType = refreshType3;
        }  else if (pageType == 4) {
            refreshType = refreshType4;
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
        getActivity().findViewById(R.id.a_type_team).setOnClickListener(this);
        getActivity().findViewById(R.id.b_type_team).setOnClickListener(this);
        getActivity().findViewById(R.id.already_paid_team).setOnClickListener(this);
        getActivity().findViewById(R.id.invalid_team).setOnClickListener(this);
        listView = (DragRefreshListView) getActivity().findViewById(R.id.listview_team);
        listView.setDragRefreshListViewListener(this);
        listView.setHeaderViewEnable(false);
        listView.setAutoLoadMore(true);


        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                if (pageType == 1 && aTypePaidDataArr != null) {
                    return aTypePaidDataArr.size();
                } else if (pageType == 2 && aTypeInvildDataArr != null) {
                    return aTypeInvildDataArr.size();
                } else if (pageType == 3 && bTypePaidDataArr != null) {
                    return bTypePaidDataArr.size();
                } else if (pageType == 4 && bTypeInvildDataArr != null) {
                    return bTypeInvildDataArr.size();
                }
                else {
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
                if (pageType == 1) {
                    info = aTypePaidDataArr.get(position);
                    orderStatus.setBackgroundResource(R.drawable.dingdanfukuan);
                } else if (pageType == 2) {
                    info = aTypeInvildDataArr.get(position);
                    orderStatus.setBackgroundResource(R.drawable.order_status_1);
                }  else if (pageType == 3) {
                    info = bTypePaidDataArr.get(position);
                    orderStatus.setBackgroundResource(R.drawable.dingdanfukuan);
                }  else {
                    info = bTypeInvildDataArr.get(position);
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
        if (pageType == 1) {
            page1 ++;
        } else if (pageType == 2) {
            page2 ++;
        } else if (pageType == 3) {
            page3 ++;
        } else if (pageType == 4) {
            page4 ++;
        }
        getData();
    }


    private void changeType(int type) {

        int red = Color.parseColor("#ff5533");
        int gray = Color.parseColor("#666666");

        List<InterfaceManager.AccountOrderInfo> tmpList = null;
        if (type == 1) { // 点击 已付款
            TextView tv = (TextView) getActivity().findViewById(R.id.already_paid_team);
            tv.setTextColor(red);

            tv = (TextView) getActivity().findViewById(R.id.invalid_team);
            tv.setTextColor(gray);

            if (userType == 2) { // A级
                pageType = 1;
                tmpList = aTypePaidDataArr;
            } else if (userType == 3) { // B级
                pageType = 3;
                tmpList = bTypeInvildDataArr;
            }

        }  else if (type == 2) { // 点击 已失效
            TextView tv = (TextView) getActivity().findViewById(R.id.already_paid_team);
            tv.setTextColor(gray);

            tv = (TextView) getActivity().findViewById(R.id.invalid_team);
            tv.setTextColor(red);

            if (userType == 2) { // A级
                pageType = 2;
                tmpList = aTypeInvildDataArr;
            } else if (userType == 3) { // B级
                pageType = 4;
                tmpList = bTypeInvildDataArr;
            }
        }   else if (type == 3) { // 点击 A级
            TextView aTV = (TextView) getActivity().findViewById(R.id.a_type_team);
            aTV.setTextColor(red);
            getActivity().findViewById(R.id.line1_team).setVisibility(View.VISIBLE);

            TextView bTV = (TextView) getActivity().findViewById(R.id.b_type_team);
            bTV.setTextColor(gray);
            getActivity().findViewById(R.id.line2_team).setVisibility(View.INVISIBLE);

            if (orderStatus == 1) { // 已付款
                tmpList = aTypePaidDataArr;
                pageType = 1;
            } else if (orderStatus == 0) { // 无效
                tmpList = aTypeInvildDataArr;
                pageType = 2;
            }
        }   else if (type == 4) { // 点击 B级
            TextView aTV = (TextView) getActivity().findViewById(R.id.a_type_team);
            aTV.setTextColor(gray);
            getActivity().findViewById(R.id.line1_team).setVisibility(View.INVISIBLE);

            TextView bTV = (TextView) getActivity().findViewById(R.id.b_type_team);
            bTV.setTextColor(red);
            getActivity().findViewById(R.id.line2_team).setVisibility(View.VISIBLE);

            if (orderStatus == 1) { // 已付款
                tmpList = bTypePaidDataArr;
                pageType = 3;
            } else if (orderStatus == 0) { // 无效
                tmpList = bTypeInvildDataArr;
                pageType = 4;
            }
        }


        if (tmpList != null && tmpList.size() == 0) {
            getData();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(getActivity().findViewById(R.id.already_paid_team))) {
            orderStatus = 1;
            changeType(1);
        } else if (v.equals(getActivity().findViewById(R.id.invalid_team))) {
            orderStatus = 0;
            changeType(2);
        } else if (v.equals(getActivity().findViewById(R.id.a_type_team))) {
            userType = 2;
            changeType(3); // A级
        } else if (v.equals(getActivity().findViewById(R.id.b_type_team))) {
            userType = 3;
            changeType(4); // B级
        }
    }
}
