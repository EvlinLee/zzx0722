package com.eryue.mine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.InterfaceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 我的团队
// zdz:待服务返回数据进行验证
public class MyTeamActivity extends BaseActivity implements View.OnClickListener, DragRefreshListView.DragRefreshListViewListener{
    private DragRefreshListView listView;
    private BaseAdapter adapter;
    private int currType = 1;
    private int page1 = 1; // A级
    private int page2 = 1; // B级
    private int page3 = 1; // 搜索状态下的展示页面

    private int aTotalNum;
    private int bTotalNum;

    private String userName;

    private int refreshType1 = 0; // 0 表示无更多数据；1表示有更多数据
    private int refreshType2 = 0; // 0 表示无更多数据；1表示有更多数据
    private int refreshType3 = 0; // 0 表示无更多数据；1表示有更多数据

    private List<InterfaceManager.MyTeamResponse.MyTeamInfo> aTypeDataArr = new ArrayList<>();
    private List<InterfaceManager.MyTeamResponse.MyTeamInfo> bTypeDataArr = new ArrayList<>();
    private List<InterfaceManager.MyTeamResponse.MyTeamInfo> sTypeDataArr = new ArrayList<>();


    private EditText searchET;
    private TextView searchTV;

    private String baseIP = AccountUtil.getBaseIp();
    private long uid = StringUtils.valueOfLong(AccountUtil.getUID());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_my_team);
        navigationBar.setTitle("我的团队");

        setupViews();
        getData(currType);
    }

    // 非关键字搜索
    private void getData(final int type) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }

        int page;
        if (type == 1) {
            page = page1;
        } else {
            page = page2;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.MyTeamReq callFunc = retrofit.create(InterfaceManager.MyTeamReq.class);
        Call<InterfaceManager.MyTeamResponse> call = callFunc.get(type, uid, page, null);
        call.enqueue(new Callback<InterfaceManager.MyTeamResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.MyTeamResponse> call, Response<InterfaceManager.MyTeamResponse> response) {
                if (type == 1) {
                    if (null!=response.body()&&response.body().status == 1
                            && response.body().result != null && response.body().result.size() > 0) {
                        aTypeDataArr.addAll(response.body().result);
                        aTotalNum = response.body().totalNum;
                        refreshType1 = 1; // 表示有更多数据
                    } else {
                        refreshType1 = 0;
                    }
                } else if (type == 2) {
                    if (null!=response.body()&&response.body().status == 1
                            && response.body().result != null && response.body().result.size() > 0) {
                        bTypeDataArr.addAll(response.body().result);
                        refreshType2 = 1; // 表示有更多数据
                        bTotalNum = response.body().totalNum;
                    } else {
                        refreshType2 = 0;
                    }
                }
                refreshListView();
            }

            @Override
            public void onFailure(Call<InterfaceManager.MyTeamResponse> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(getBaseContext(), "请求失败");
            }
        });
    }

    // 关键字搜索
    private void getDataByWord(final int type, String userName) {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.MyTeamReq callFunc = retrofit.create(InterfaceManager.MyTeamReq.class);
        Call<InterfaceManager.MyTeamResponse> call = callFunc.get(type, uid, page3, userName);
        call.enqueue(new Callback<InterfaceManager.MyTeamResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.MyTeamResponse> call, Response<InterfaceManager.MyTeamResponse> response) {
                if (null!=response.body()&&response.body().status == 1
                        && response.body().result != null && response.body().result.size() > 0) {
                    sTypeDataArr.addAll(response.body().result);
                    refreshType3 = 1; // 表示有更多数据
                } else {
                    refreshType3 = 0;
                }
                refreshListView();
            }

            @Override
            public void onFailure(Call<InterfaceManager.MyTeamResponse> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(getBaseContext(), "查询失败");
            }
        });
    }

    private void setupViews() {
        searchET = (EditText) findViewById(R.id.search_et);
        searchTV = (TextView) findViewById(R.id.search);
        searchTV.setOnClickListener(this);

        listView = (DragRefreshListView) findViewById(R.id.listview);
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
                } else if (currType == 3 && sTypeDataArr != null) {
                    return sTypeDataArr.size();
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
                LayoutInflater inflater = getLayoutInflater();
                View view;
                if (convertView == null) {
                    view = inflater.inflate(R.layout.cell_my_team, null);
                } else {
                    view = convertView;
                }

                TextView tv1 = (TextView) view.findViewById(R.id.nick_name);
                TextView tv2 = (TextView) view.findViewById(R.id.curr_month);
                TextView tv3 = (TextView) view.findViewById(R.id.last_month);
                InterfaceManager.MyTeamResponse.MyTeamInfo info = null;
                if (currType == 1) {
                    info = aTypeDataArr.get(position);
                } else if (currType == 2) {
                    info = bTypeDataArr.get(position);
                } else if (currType == 3) {
                    info = sTypeDataArr.get(position);
                }
                if (info == null) {
                    return view;
                }

                tv1.setText(info.userName);
                tv2.setText(info.currentMonthAmount + "");
                tv3.setText(info.lastMonthAmount + "");

                return view;
            }
        };

        listView.setAdapter(adapter);

        findViewById(R.id.a_type).setOnClickListener(this);
        findViewById(R.id.b_type).setOnClickListener(this);
    }

    private void refreshListView() {
        int refreshType;
        if (currType == 1) {
            refreshType = refreshType1;
            ((TextView)findViewById(R.id.a_type)).setText("A级成员（" + aTotalNum + "）");
        } else {
            refreshType = refreshType2;
            ((TextView)findViewById(R.id.b_type)).setText("B级成员（" + bTotalNum + "）");
        }
        adapter.notifyDataSetChanged();

        if (refreshType == 0) {
            listView.setFooterViewState(ListViewFooter.STATE_RESULT, "无更多成员信息", Color.GRAY);
        } else if (refreshType == 1) {
            listView.setFooterViewState(ListViewFooter.STATE_NORMAL);
        }
    }

    int lastType;
    @Override
    public void onClick(View v) {
        if (v.equals(findViewById(R.id.a_type))) {
            setType(1);
        } else if (v.equals(findViewById(R.id.b_type))) {
            setType(2);
        } else if (v.equals(searchTV)) {
            if (currType != 3) {
                lastType = currType;
                currType = 3;
            }

            userName = searchET.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                ToastTools.showShort(this, "请输入内容再搜索");
                return;
            }

            sTypeDataArr.clear();
            getDataByWord(lastType, userName);
        }

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchET.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setType(int type) {
        if (currType == type) {
            return;
        }
        currType = type;
        if (type == 1) {
            TextView tv1 = (TextView) findViewById(R.id.a_type);
            tv1.setTextColor(Color.parseColor("#ff5533"));
            findViewById(R.id.line1).setVisibility(View.VISIBLE);

            TextView tv2 = (TextView) findViewById(R.id.b_type);
            tv2.setTextColor(Color.parseColor("#090909"));
            findViewById(R.id.line2).setVisibility(View.INVISIBLE);
        } else if (type == 2) {
            TextView tv1 = (TextView) findViewById(R.id.a_type);
            tv1.setTextColor(Color.parseColor("#090909"));
            findViewById(R.id.line1).setVisibility(View.INVISIBLE);

            TextView tv2 = (TextView) findViewById(R.id.b_type);
            tv2.setTextColor(Color.parseColor("#ff5533"));
            findViewById(R.id.line2).setVisibility(View.VISIBLE);

            if (bTypeDataArr.size() == 0) {
                getData(currType);
            }
        }

        refreshListView();
    }

    @Override
    public void onRefresh() {
        Log.d("1","1");
        listView.refreshComplete(false, new Date().getTime());
    }

    @Override
    public void onLoadMore() {
        Log.d("1","1");
        if (currType == 1) {
            page1 ++;
        } else if (currType == 2){
            page2 ++;
        }  else if (currType == 3){
            page3 ++;
        }
        getData(currType);
    }
}
