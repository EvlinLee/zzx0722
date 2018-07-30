package com.eryue.mine;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.R;
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
 * Created by dazhou on 2018/5/20.
 * 团队搜索
 */

public class SearchTeamActivity extends BaseActivity {
    private EditText orderET;
    private ListView listView;
    private BaseAdapter adapter;
    private List dataArr;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationBar.setTitle("团队搜索");


        setContentView(R.layout.activity_search_team);

        type = getIntent().getIntExtra("type", 0);

        initViews();
    }

    private void initViews() {
        orderET = (EditText) findViewById(R.id.order);
        listView = (ListView) findViewById(R.id.listview);
        findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqTeam();
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
                    convertView = inflater.inflate(R.layout.cell_my_team_1, null);
                    viewHolder = new ViewHolder();
                    viewHolder.nick_name = (TextView) convertView.findViewById(R.id.nick_name);
                    viewHolder.wechat = (TextView) convertView.findViewById(R.id.wechat);
                    viewHolder.register_time = (TextView) convertView.findViewById(R.id.register_time);
                    viewHolder.take_cash = (TextView) convertView.findViewById(R.id.take_cash);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                MineInterface.TeamInfo info = (MineInterface.TeamInfo) getItem(position);
                if (null == info) {
                    return convertView;
                }


                viewHolder.nick_name.setText(info.userName);
                viewHolder.wechat.setText(info.wxName);
                viewHolder.register_time.setText(TimeUtils.getStrTime(info.regDate + "", "yyyy-MM-dd"));
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
                        Toast.makeText(SearchTeamActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });


                return convertView;
            }
        };

        listView.setAdapter(adapter);
    }


    // 关键字搜索
    private void reqTeam() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        int page = 1;

        String teamKey = orderET.getText().toString();
        if (TextUtils.isEmpty(teamKey)) {
            ToastTools.showShort(SearchTeamActivity.this, "请输入正确的内容进行搜索");
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.TeamReq callFunc = retrofit.create(MineInterface.TeamReq.class);
        Call<MineInterface.TeamRsp> call = callFunc.get(AccountUtil.getUID(), page, type, "reg_date", "desc", teamKey);
        call.enqueue(new Callback<MineInterface.TeamRsp>() {
            @Override
            public void onResponse(Call<MineInterface.TeamRsp> call, Response<MineInterface.TeamRsp> response) {

                if (response.body() == null || response.body().result == null|| response.body().result.size() == 0) {
                    ToastTools.showShort(SearchTeamActivity.this, "未查到相应用户");
                    return;
                }
                dataArr = response.body().result;
                refreshListView();
            }

            @Override
            public void onFailure(Call<MineInterface.TeamRsp> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(SearchTeamActivity.this, "请求失败");
            }
        });
    }


    private void refreshListView() {
        findViewById(R.id.title_header_con).setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView nick_name ;
        public TextView wechat ;
        public TextView register_time ;
        public TextView take_cash ;
    }
}
