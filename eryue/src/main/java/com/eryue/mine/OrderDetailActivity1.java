package com.eryue.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.WXShare;
import com.eryue.home.SharePopView;
import com.eryue.widget.ShareContentView;
import com.eryue.widget.StockDatePopModel;
import com.eryue.widget.StockDatePopView;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;
import com.library.util.ImageUtils;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.InterfaceManager;
import net.MineInterface;
import net.TimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import base.BaseActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.eryue.mine.SearchOrderActivity.SearchType;

/**
 * Created by dazhou on 2018/5/3.
 * 订单详情，第二版
 */

public class OrderDetailActivity1 extends BaseActivity implements DragRefreshListView.DragRefreshListViewListener, View.OnClickListener {

    private final String[] hintStr0 = {"今日", "七天", "本月", "上月", "全部"};
    private final int[] hintValue0 = {1, 2, 3, 4, 0};
    private int hintIndex0 = 0;

    private final String[] hintStr1 = {"全部订单", "我的订单", "代理订单"};
    private final int[] hintValue1 = {2, 0, 1};
    private int hintIndex1 = 0;

    private final String[] hintStr2 = {"全部积分", "预估积分", "有效积分"};
    private final int[] hintValue2 = {2, 0, 1};
    private int hintIndex2 = 0;

    private final String[] hintStr3 = {"全部", "淘宝", "京东", "京东免单", "蘑菇街", "拼多多", "苏宁"};
    private final int[] hintValue3 = {0, 1, 2, 3, 4, 5, 6};
    private int hintIndex3 = 0;


    private StockDatePopView popView0;
    private StockDatePopView popView1;
    private StockDatePopView popView2;
    private StockDatePopView popView3;

    private DragRefreshListView listView;
    private BaseAdapter adapter;

    private ArrayList dataArr;

    private int page  = 1;

    private TextView yugujifen;
    private TextView youxiaojifen;
    private TextView dingdanshuliang;


    //分享
    SharePopView sharePopView;
    private View shareView;
    private WXShare wxShare;

    private int dd;// 订单数量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_1);

        navigationBar.setTitle("订单详情");
        navigationBar.searchImageView.setVisibility(View.VISIBLE);
        navigationBar.searchImageView.setOnClickListener(this);

        setupViews();

        getData();
        getData1();
    }

    private void setupViews() {
        initPopView();
        initListView();

        yugujifen = findViewById(R.id.yugujifen);
        youxiaojifen = findViewById(R.id.youxiaojifen);
        dingdanshuliang = findViewById(R.id.dingdanshuliang);

        shareView = findViewById(R.id.share_img);
        shareView.setOnClickListener(this);
    }


    private void initListView() {
        listView = (DragRefreshListView) findViewById(R.id.listview);
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
                    convertView = inflater.inflate(R.layout.cell_order_detail_1, null);
                    viewHolder = new ViewHolder();
                    viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                    viewHolder.picture = (ImageView) convertView.findViewById(R.id.picture);
                    viewHolder.dingdanhao_value = (TextView) convertView.findViewById(R.id.dingdanhao_value);
                    viewHolder.pay_value = (TextView) convertView.findViewById(R.id.pay_value);
                    viewHolder.jifen_value = (TextView) convertView.findViewById(R.id.jifen_value);
                    viewHolder.jifen = (TextView) convertView.findViewById(R.id.jifen);
                    viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                    viewHolder.offer = (TextView) convertView.findViewById(R.id.offer);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                MineInterface.OrderInfo info = (MineInterface.OrderInfo) getItem(position);
                if (null == info) {
                    return convertView;
                }


                Glide.with(OrderDetailActivity1.this).load(info.picUrl).into(viewHolder.picture);
                viewHolder.title.setText(info.auctionTitle);
                viewHolder.dingdanhao_value.setText(info.order_number);
                viewHolder.pay_value.setText(info.realPay + "");
                viewHolder.time.setText(info.orderDateStr);
                viewHolder.offer.setText("由" + info.integralPrividor + "提供");

                if (hintIndex2 == 2) {
                    viewHolder.jifen.setText("有效积分");
                    viewHolder.jifen_value.setText(info.realIntegral + "");
                } else {
                    viewHolder.jifen.setText("预估积分");
                    viewHolder.jifen_value.setText(info.integral + "");
                }


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

        showProgressMum();

        int flag = hintValue1[hintIndex1];
        int orderType = hintValue3[hintIndex3];
        int day = hintValue0[hintIndex0];
        int integralStatus = hintValue2[hintIndex2];


        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(60, TimeUnit.SECONDS).
                readTimeout(60, TimeUnit.SECONDS).
                writeTimeout(60, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseip)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
        MineInterface.SearchOrderReq callFunc = retrofit.create(MineInterface.SearchOrderReq.class);
        Call<MineInterface.SearchOrderRsp> call = callFunc.get(
                page,
                "",
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
                }

                try {
                    if (1 == page) {
                        dataArr.clear();
                    }

                    if (response.body() == null) {
                        showNoMoreData();
                        return;
                    }

                    dd = response.body().dd;

                    if (response.body().rows == null || response.body().rows.size() == 0) {
                        showNoMoreData();
                        return;
                    }

                    dataArr.addAll(response.body().rows);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                refreshListView();
            }

            @Override
            public void onFailure(Call<MineInterface.SearchOrderRsp> call, Throwable t) {
                hideProgressMum();
                t.printStackTrace();
                ToastTools.showShort(OrderDetailActivity1.this, t.toString());
            }
        });
    }

    // 获取 积分 数据
    public void getData1() {
        String baseip = AccountUtil.getBaseIp();
        if (TextUtils.isEmpty(baseip)) {
            return;
        }

        int flag = hintValue1[hintIndex1];
        int orderType = hintValue3[hintIndex3];
        int day = hintValue0[hintIndex0];
        int integralStatus = hintValue2[hintIndex2];

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MineInterface.SearchOrderIntegralReq callFunc = retrofit.create(MineInterface.SearchOrderIntegralReq.class);
        Call<MineInterface.SearchOrderIntegralRsp> call = callFunc.get(
                "",
                flag,
                orderType,
                day,
                integralStatus,
                AccountUtil.getUID());
        call.enqueue(new Callback<MineInterface.SearchOrderIntegralRsp>() {
            @Override
            public void onResponse(Call<MineInterface.SearchOrderIntegralRsp> call, final Response<MineInterface.SearchOrderIntegralRsp> response) {

                if (response.body() == null) {
                    ToastTools.showShort(OrderDetailActivity1.this, "暂无积分数据");
                    return;
                }

                ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.d("1", response.body().toString());
                        yugujifen.setText(response.body().integral + "");
                        youxiaojifen.setText(response.body().realIntegral + "");
                    }
                }).sendEmptyMessageDelayed(0, 0);
            }

            @Override
            public void onFailure(Call<MineInterface.SearchOrderIntegralRsp> call, Throwable t) {
                t.printStackTrace();
                ToastTools.showShort(OrderDetailActivity1.this, "积分请求失败");
            }
        });
    }


    private void refreshListView() {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                dingdanshuliang.setText(dd + "");
                adapter.notifyDataSetChanged();
                listView.setFooterViewState(ListViewFooter.STATE_NORMAL);
            }
        }).sendEmptyMessageDelayed(0,0);
    }

    private void showNoMoreData() {
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                dingdanshuliang.setText(dd + "");
                adapter.notifyDataSetChanged();
                listView.setFooterViewState(ListViewFooter.STATE_RESULT, "无更多数据", Color.GRAY);
            }
        }).sendEmptyMessageDelayed(0,0);
    }


    private void getDataByChangeType() {
        page = 1;
        getData();
        getData1();
    }


    private void initPopView() {
        // 今日
        findViewById(R.id.hint0_con).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != popView0) {
                    popView0.showPopView(v);
                }
            }
        });
        popView0 = new StockDatePopView(this);
        popView0.setData(hintStr0, hintValue0);
        popView0.setOnDateClickListener(new StockDatePopView.OnDateClickListener() {
            @Override
            public void onDateItemClick(StockDatePopModel model) {
                TextView textView = (TextView) findViewById(R.id.hint0);
                textView.setText(model.getShowString());
                hintIndex0 = model.getIndex();
                getDataByChangeType();
            }
        });

        popView0.setSelectOption(hintIndex0);


        // 订单
        findViewById(R.id.hint1_con).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != popView1) {
                    popView1.showPopView(v);
                }
            }
        });
        popView1 = new StockDatePopView(this);
        popView1.setData(hintStr1, hintValue1);
        popView1.setOnDateClickListener(new StockDatePopView.OnDateClickListener() {
            @Override
            public void onDateItemClick(StockDatePopModel model) {
                TextView textView = (TextView) findViewById(R.id.hint1);
                textView.setText(model.getShowString());
                hintIndex1 = model.getIndex();
                getDataByChangeType();

            }
        });

        popView1.setSelectOption(hintIndex1);

        // 积分
        findViewById(R.id.hint2_con).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != popView2) {
                    popView2.showPopView(v);
                }
            }
        });
        popView2 = new StockDatePopView(this);
        popView2.setData(hintStr2, hintValue2);
        popView2.setOnDateClickListener(new StockDatePopView.OnDateClickListener() {
            @Override
            public void onDateItemClick(StockDatePopModel model) {
                TextView textView = (TextView) findViewById(R.id.hint2);
                textView.setText(model.getShowString());
                hintIndex2 = model.getIndex();
                getDataByChangeType();

            }
        });

        popView2.setSelectOption(hintIndex2);


        // 全部
        findViewById(R.id.hint3_con).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != popView3) {
                    popView3.showPopView(v);
                }
            }
        });
        popView3 = new StockDatePopView(this);
        popView3.setData(hintStr3, hintValue3);
        popView3.setOnDateClickListener(new StockDatePopView.OnDateClickListener() {
            @Override
            public void onDateItemClick(StockDatePopModel model) {
                TextView textView = (TextView) findViewById(R.id.hint3);
                textView.setText(model.getShowString());
                hintIndex3 = model.getIndex();
                getDataByChangeType();

            }
        });

        popView3.setSelectOption(hintIndex3);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        page ++;
        getData();
    }

    @Override
    public void onClick(View v) {
        if (v == navigationBar.searchImageView) {
            Intent intent = new Intent(this, SearchOrderActivity.class);
            intent.putExtra(SearchType, 1);
            startActivity(intent);
        }
        else if (v == shareView) {
            if (null == sharePopView){
                sharePopView = new SharePopView(OrderDetailActivity1.this);
                sharePopView.setOnShareClickListener(new ShareContentView.OnShareClickListener() {
                    @Override
                    public void onShareClick(int tag) {
                        shareImage(tag);
                    }
                });
                wxShare = new WXShare(OrderDetailActivity1.this);

            }

            sharePopView.showPopView();
        }
    }

    private void shareImage(int tag) {
        Bitmap bitmap = net.ImageUtils.takeScreenShot(this);

        if (bitmap == null) {
            ToastTools.showShort(OrderDetailActivity1.this, "图片有异常，稍后重试");
            return;
        }
        if (wxShare == null) {
            wxShare = new WXShare(OrderDetailActivity1.this);
        }
        wxShare.shareImage(bitmap, tag);
    }

    public class ViewHolder {
        public TextView title ;
        public ImageView picture ;
        public TextView dingdanhao_value ;
        public TextView pay_value ;
        public TextView jifen;
        public TextView jifen_value;
        public TextView time;
        public TextView offer;
    }


    private void savePicture(Bitmap image) {
        try {
            //压缩图片
            ImageUtils.getInstance(this).setScaleSize(900);
            byte[] compressByte = ImageUtils.getInstance(this).compressedimgToBase64(image);
            File file_path = StorageUtils.getOwnCacheDirectory(getApplicationContext(), GoodsContants.PATH_DOWNIMG);
            File file = new File(file_path, "erWeiMa.png");
            FileOutputStream fOut = new FileOutputStream(file);

            fOut.write(compressByte);
            fOut.flush();
            fOut.close();

            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
            scanPhotos(file, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanPhotos(File file, Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
