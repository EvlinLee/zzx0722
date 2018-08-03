package com.eryue.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
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
import com.library.util.ToastTools;

import net.MineInterface;
import net.TimeUtils;

import java.util.ArrayList;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dazhou on 2018/5/24.
 *
 * 消息中心
 */

public class MessageCenterActivity extends BaseActivity  implements DragRefreshListView.DragRefreshListViewListener {
    private DragRefreshListView listView;

    private BaseAdapter adapter;

    private ArrayList dataArr;

    private int page  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_center);

        navigationBar.setTitle("消息中心");
        setupViews();
        getData();
    }

    private void setupViews() {
        listView = findViewById(R.id.listview);

        listView.setDragRefreshListViewListener(this);
        listView.setHeaderViewEnable(false);
        listView.setAutoLoadMore(true);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                if (dataArr == null) {
                    return 0;
                }
                return dataArr.size();
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
                    convertView = inflater.inflate(R.layout.cell_message_center, null);
                    viewHolder = new ViewHolder();
                    viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                    viewHolder.date = convertView.findViewById(R.id.date);
                    viewHolder.smallTitle = convertView.findViewById(R.id.smalltitle);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                final MineInterface.AllInformation info = (MineInterface.AllInformation) getItem(position);
                if (null == info) {
                    return convertView;
                }


                viewHolder.title.setText(info.title);
                viewHolder.date.setText(TimeUtils.getStrTime(info.createDate + "", "yyyy-MM-dd  HH:mm"));
                viewHolder.smallTitle.setText(info.smallTitle);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MessageCenterActivity.this, WebViewActivity.class);
                        intent.putExtra("url", info.url);
                        intent.putExtra("type", 2);
                        startActivity(intent);
                    }
                });


                return convertView;
            }
        };
        listView.setAdapter(adapter);
    }

    // 获取列表数据
    public void getData() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        // just for test
//        baseip = "http://www.371d.cn/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.AppAllInformationReq callFunc = retrofit.create(MineInterface.AppAllInformationReq.class);
        Call<MineInterface.AppAllInformationRsp> call = callFunc.get(AccountUtil.getUID(), page);
        call.enqueue(new Callback<MineInterface.AppAllInformationRsp>() {
            @Override
            public void onResponse(Call<MineInterface.AppAllInformationRsp> call, final Response<MineInterface.AppAllInformationRsp> response) {
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
            public void onFailure(Call<MineInterface.AppAllInformationRsp> call, Throwable t) {
                t.printStackTrace();
                hideProgressMum();
                ToastTools.showShort(MessageCenterActivity.this, "请求失败");
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

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        page ++;
        getData();
    }


    public class ViewHolder {
        public TextView title ;
        public TextView smallTitle;
        public TextView date ;
    }
}
