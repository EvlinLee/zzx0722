package com.eryue.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.R;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.InterfaceManager;
import net.MineInterface;
import net.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 提现记录
 */

public class TiXianRecordActivity extends BaseActivity implements
        DragRefreshListView.DragRefreshListViewListener {

    private List dataArr;
    private DragRefreshListView listView;
    private BaseAdapter adapter;
    private int page;
    
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationBar.setTitle("提现记录");
        setContentView(R.layout.activity_tixian_record);
        
        type = getIntent().getIntExtra("type", -1);
        if (-1 == type) {
            return;
        }

        dataArr = new ArrayList<>();
        setupViews();
        page = 1;
        getData();
    }
    
    private void getData() {
        if (0 == type) {
            reqNormalRecords();
        } else if (1 == type) {
            reqJdRecords();
        }
    }


    private void reqNormalRecords() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.NormalCashRecordReq callFunc = retrofit.create(MineInterface.NormalCashRecordReq.class);
        Call<MineInterface.NormalCashRecordRsp> call = callFunc.get(AccountUtil.getUID(), page);
        call.enqueue(new Callback<MineInterface.NormalCashRecordRsp>() {
            @Override
            public void onResponse(Call<MineInterface.NormalCashRecordRsp> call, final Response<MineInterface.NormalCashRecordRsp> response) {
                hideProgressMum();
                if (dataArr == null) {
                    dataArr = new ArrayList();
                }

                try {
                    if (1 == page) {
                        dataArr.clear();
                    }

                    if (response.body() == null || response.body().result == null || response.body().result.size() == 0) {
                        showNoMoreData();
                        return;
                    }

                    dataArr.addAll(response.body().result);
                    refreshListView();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                refreshListView();
            }

            @Override
            public void onFailure(Call<MineInterface.NormalCashRecordRsp> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(TiXianRecordActivity.this, "请求失败");
            }
        });
    }

    private void reqJdRecords() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.JdCashRecordReq callFunc = retrofit.create(MineInterface.JdCashRecordReq.class);
        Call<MineInterface.NormalCashRecordRsp> call = callFunc.get(AccountUtil.getUID(), page);
        call.enqueue(new Callback<MineInterface.NormalCashRecordRsp>() {
            @Override
            public void onResponse(Call<MineInterface.NormalCashRecordRsp> call, final Response<MineInterface.NormalCashRecordRsp> response) {
                hideProgressMum();
                if (dataArr == null) {
                    dataArr = new ArrayList();
                }

                try {
                    if (1 == page) {
                        dataArr.clear();
                    }

                    if (response.body() == null || response.body().result == null || response.body().result.size() == 0) {
                        showNoMoreData();
                        return;
                    }

                    dataArr.addAll(response.body().result);
                    refreshListView();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                refreshListView();
            }

            @Override
            public void onFailure(Call<MineInterface.NormalCashRecordRsp> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(TiXianRecordActivity.this, "请求失败");
            }
        });
    }


    private void refreshListView() {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {

                adapter.notifyDataSetChanged();
                listView.setFooterViewState(ListViewFooter.STATE_NORMAL);
            }
        }).sendEmptyMessageDelayed(0,0);
    }

    private void showNoMoreData() {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                listView.setFooterViewState(ListViewFooter.STATE_RESULT, "无更多数据", Color.GRAY);
            }
        }).sendEmptyMessageDelayed(0,0);
    }


    private void setupViews() {

        listView = (DragRefreshListView) findViewById(R.id.listview);
        listView.setDragRefreshListViewListener(this);
        listView.setHeaderViewEnable(false);
        listView.setAutoLoadMore(true);

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
                if (dataArr == null || position < 0 || position > dataArr.size()) {
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
                    convertView = inflater.inflate(R.layout.cell_tixian_record, null);
                    viewHolder = new ViewHolder();
                    viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                    viewHolder.date = convertView.findViewById(R.id.date);
                    viewHolder.money = convertView.findViewById(R.id.money);
                    viewHolder.status = convertView.findViewById(R.id.status);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                MineInterface.CashRecord info = (MineInterface.CashRecord) getItem(position);
                if (null == info) {
                    return convertView;
                }

                viewHolder.name.setText(info.typeName);
                viewHolder.date.setText(TimeUtils.getStrTime(info.exchangeTime + "", "yyyy-MM-dd"));
                viewHolder.money.setText(info.exchangeNum + "");
                viewHolder.status.setText(info.codeStatus );

                return convertView;
            }
        };

        listView.setAdapter(adapter);
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        page++;
        getData();
    }

    public class ViewHolder {
        public TextView name ;
        public TextView date;
        public TextView money ;
        public TextView status ;
    }
}
