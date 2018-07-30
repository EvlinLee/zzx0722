package com.eryue.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.DataCenterManager;
import net.InterfaceManager;

import java.util.List;

import base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.KeyFlag.INVITE_CODE_KEY;


// app激活量

public class AppActivatorActivity extends BaseActivity {

    private ListView listView;
    private BaseAdapter adapter;
    private List<InterfaceManager.SearchActivatorResponse.SearchActivatorInfo> dataArr;

    private String baseIP = AccountUtil.getBaseIp();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_activator);
        navigationBar.setTitle("APP激活量");
        setupViews();

        getData();
    }

    private void getData() {
        if (TextUtils.isEmpty(baseIP)) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceManager.SearchActivatorReq callFunc = retrofit.create(InterfaceManager.SearchActivatorReq.class);

        //邀请码
        String code = DataCenterManager.Instance().get(this,INVITE_CODE_KEY);
        if (TextUtils.isEmpty(code)){
            return;
        }
        Call<InterfaceManager.SearchActivatorResponse> call = callFunc.get(code);
        call.enqueue(new Callback<InterfaceManager.SearchActivatorResponse>() {
            @Override
            public void onResponse(Call<InterfaceManager.SearchActivatorResponse> call, Response<InterfaceManager.SearchActivatorResponse> response) {

                findViewById(R.id.no_data_hint).setVisibility(View.GONE);
                if (null!=response.body()&&response.body().status == 0) {
                    findViewById(R.id.no_data_hint).setVisibility(View.VISIBLE);
                } else {
                    dataArr = response.body().result;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<InterfaceManager.SearchActivatorResponse> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(getBaseContext(), "请求失败");
            }
        });
    }

    private void setupViews() {
        listView = (ListView) findViewById(R.id.listview);



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
                    view = inflater.inflate(R.layout.cell_app_activator, null);
                } else {
                    view = convertView;
                }

                TextView tv1 = (TextView) view.findViewById(R.id.time);
                TextView tv2 = (TextView) view.findViewById(R.id.total);
                TextView tv3 = (TextView) view.findViewById(R.id.ios);
                TextView tv4 = (TextView) view.findViewById(R.id.android);

                InterfaceManager.SearchActivatorResponse.SearchActivatorInfo info = dataArr.get(position);
                tv1.setText(info.createDate);
                tv2.setText(info.totalNum + "");
                tv3.setText(info.iosNum + "");
                tv3.setText(info.andriodNum + "");

                return view;
            }
        };

        listView.setAdapter(adapter);
    }
}
