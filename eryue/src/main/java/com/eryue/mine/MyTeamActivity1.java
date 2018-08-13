package com.eryue.mine;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.R;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;
import com.library.util.ToastTools;

import net.MineInterface;
import net.TimeUtils;

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
public class MyTeamActivity1 extends BaseActivity implements View.OnClickListener, DragRefreshListView.DragRefreshListViewListener{
    private DragRefreshListView listView;
    private BaseAdapter adapter;
    private int currType = 0; // 0我的团队，1团队邀请
    private int page1 = 1; // 我的团队
    private int page2 = 1; // 团队邀请

    private List<MineInterface.TeamInfo> aTypeDataArr = new ArrayList<>();
    private List<MineInterface.TeamInfo> bTypeDataArr = new ArrayList<>();

    private View time_contain;
    private View cash_contain;
    private ImageView time_IV;
    private ImageView cash_IV;

    private ImageView searchImageView;

    private String sidx; //注册时间排序reg_date ，累计提现排序totalWithdraw
    private String sord; //降序desc，升序asc

    private View contain_0;
    private View contain_1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team_1);
        navigationBar.setHidden(true);


        sidx = "reg_date";
        sord = "desc";

        setupViews();
        getData(currType);
    }


    private void getData(final int type) {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        final int page;
        if (type == 0) {
            page = page1;
        } else {
            page = page2;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.TeamReq callFunc = retrofit.create(MineInterface.TeamReq.class);
        Call<MineInterface.TeamRsp> call = callFunc.get(AccountUtil.getUID(), page, type, sidx, sord, null);
        call.enqueue(new Callback<MineInterface.TeamRsp>() {
            @Override
            public void onResponse(Call<MineInterface.TeamRsp> call, Response<MineInterface.TeamRsp> response) {
                if (response.body() == null) {
                    ToastTools.showShort(MyTeamActivity1.this, "暂无数据");
                    return;
                }

                if (type == 0) {
                    if (response.body().status == 1
                            && response.body().result != null && response.body().result.size() > 0) {
                        aTypeDataArr.addAll(response.body().result);
                        refreshListView();
                    } else {
                        showNoMoreData();
                    }
                } else if (type == 1) {
                    if (response.body().status == 1
                            && response.body().result != null && response.body().result.size() > 0) {
                        bTypeDataArr.addAll(response.body().result);
                        refreshListView();
                    } else {
                        showNoMoreData();
                    }
                }
            }

            @Override
            public void onFailure(Call<MineInterface.TeamRsp> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(getBaseContext(), "请求失败");
            }
        });
    }

    private void setupViews() {
        contain_0 = findViewById(R.id.contain_0);
        contain_0.setOnClickListener(this);
        contain_1 = findViewById(R.id.contain_1);
        contain_1.setOnClickListener(this);

        searchImageView = findViewById(R.id.search_image);
        searchImageView.setOnClickListener(this);

        time_contain = findViewById(R.id.time_contain);
        time_contain.setOnClickListener(this);
        cash_contain = findViewById(R.id.cash_contain);
        cash_contain.setOnClickListener(this);

        time_IV = (ImageView) findViewById(R.id.time_indicator);
        cash_IV = (ImageView) findViewById(R.id.cash_indicator);


        listView =  findViewById(R.id.listview);
        listView.setDragRefreshListViewListener(this);
        listView.setHeaderViewEnable(false);
        listView.setAutoLoadMore(true);

        View headerView = LayoutInflater.from(MyTeamActivity1.this).inflate(R.layout.myteam_header,null);
        listView.addHeaderView(headerView);

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                if (currType == 0 && aTypeDataArr != null) {
                    return aTypeDataArr.size();
                } else if (currType == 1 && bTypeDataArr != null) {
                    return bTypeDataArr.size();
                }
                else {
                    return 0;
                }
            }

            @Override
            public Object getItem(int position) {
                if (currType == 0 && aTypeDataArr != null
                        && position >= 0 && position < aTypeDataArr.size()) {
                    return aTypeDataArr.get(position);
                } else if (currType == 1 && bTypeDataArr != null
                        && position >= 0 && position < bTypeDataArr.size()) {
                    return bTypeDataArr.get(position);
                }
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final ViewHolder viewHolder;
                if (convertView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = inflater.inflate(R.layout.cell_my_team_1, null);
                    viewHolder = new ViewHolder();
                    viewHolder.nick_name = (TextView) convertView.findViewById(R.id.nick_name);
                    viewHolder.wechat = (TextView) convertView.findViewById(R.id.wechat);
                    viewHolder.register_time = (TextView) convertView.findViewById(R.id.register_time);
                    viewHolder.take_cash = (TextView) convertView.findViewById(R.id.take_cash);
                    viewHolder.profile_photo = (ImageView) convertView.findViewById(R.id.profile_photo);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                MineInterface.TeamInfo info = (MineInterface.TeamInfo) getItem(position);

                viewHolder.nick_name.setText(info.userName);
                viewHolder.wechat.setText(info.phone);
                viewHolder.register_time.setText("注册时间： "+TimeUtils.getStrTime(info.regDate + "", "yyyy-MM-dd"));
                viewHolder.take_cash.setText(info.totalWithdraw + "");


                viewHolder.wechat.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        TextView t = (TextView)view;
                        String str = t.getText().toString();
                        if (TextUtils.isEmpty(str)) {
                            return false;
                        }
                        // 复制邀请码
                        ClipboardManager myClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData myClip;
                        myClip = ClipData.newPlainText("text", str);//text是内容
                        myClipboard.setPrimaryClip(myClip);
                        Toast.makeText(MyTeamActivity1.this, "复制成功", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });


                return convertView;
            }
        };


        listView.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {
        if (v == contain_0) {
            setType(0);
        } else if (v == contain_1) {
            setType(1);
        } else if (v.equals(time_contain)) {

            if (sidx.equals("reg_date") && sord.equals("desc")) {
                sord = "asc";
                time_IV.setBackgroundResource(R.drawable.icon_arrow_up);
            } else if (sidx.equals("reg_date") && sord.equals("asc")) {
                sord = "desc";
                time_IV.setBackgroundResource(R.drawable.icon_arrow_down1);
            } else if (sidx.equals("totalWithdraw")) {
                sidx = "reg_date";
                sord = "desc";
                time_IV.setBackgroundResource(R.drawable.icon_arrow_down1);
            }
            getDataBySort();
        }
        else if (v.equals(cash_contain)) {

            if (sidx.equals("totalWithdraw") && sord.equals("desc")) {
                sord = "asc";
                cash_IV.setBackgroundResource(R.drawable.icon_arrow_up);
            } else if (sidx.equals("totalWithdraw") && sord.equals("asc")) {
                sord = "desc";
                cash_IV.setBackgroundResource(R.drawable.icon_arrow_down1);
            } else if (sidx.equals("reg_date")) {
                sidx = "totalWithdraw";
                sord = "desc";
                cash_IV.setBackgroundResource(R.drawable.icon_arrow_down1);
            }

            getDataBySort();
        } else if (v == searchImageView) {
            Intent intent = new Intent(this, SearchTeamActivity.class);
            intent.putExtra("type", currType);
            startActivity(intent);
        }

        // sidx 注册时间排序reg_date ，累计提现排序totalWithdraw
        // sord //降序desc，升序asc
    }

    private void getDataBySort() {
        if (currType == 0) {
            page1 = 1;
            aTypeDataArr.clear();
        } else if (currType == 1) {
            page2 = 1;
            bTypeDataArr.clear();
        }
        getData(currType);
    }

    private void setType(int type) {
        if (currType == type) {
            return;
        }
        currType = type;
        if (type == 0) {
            TextView tv1 = (TextView) findViewById(R.id.a_type);
            tv1.setTextColor(Color.parseColor("#fd5b68"));

            TextView tv2 = (TextView) findViewById(R.id.b_type);
            tv2.setTextColor(Color.parseColor("#666666"));
        } else if (type == 1) {
            TextView tv1 = (TextView) findViewById(R.id.a_type);
            tv1.setTextColor(Color.parseColor("#666666"));

            TextView tv2 = (TextView) findViewById(R.id.b_type);
            tv2.setTextColor(Color.parseColor("#fd5b68"));

            if (bTypeDataArr.size() == 0) {
                getData(currType);
            }
        }

        refreshListView();
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
        listView.refreshComplete(false, new Date().getTime());
    }

    @Override
    public void onLoadMore() {
        if (currType == 0) {
            page1 ++;
        } else if (currType == 1){
            page2 ++;
        }
        getData(currType);
    }

    public class ViewHolder {
        public TextView nick_name ;
        public TextView wechat ;
        public TextView register_time ;
        public TextView take_cash ;
        public ImageView profile_photo;
    }
}
