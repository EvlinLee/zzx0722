package com.eryue.home;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eryue.R;

import java.util.ArrayList;

/**
 * @author zhangxiaocheng
 *
 * Created by Administrator on 2018/7/30.
 */

public class GoodsTopCardViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    ArrayList<ArrayList> mlist;
    TextView tv1;
    TextView tv2;
    ImageView iv;
    CardView cv;

    public GoodsTopCardViewAdapter(Context context, ArrayList<ArrayList> list) {
        this.mContext = context;
        this.mlist=list;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragments_goods_top,parent,false);
        cv = (CardView)view.findViewById(R.id.cardView);
        tv1 = (TextView) view.findViewById(R.id.textView1);
        tv2 = (TextView) view.findViewById(R.id.textView2);
        iv = (ImageView) view.findViewById(R.id.image1);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view){};
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        tv1.setText((String)mlist.get(position).get(0));
        tv2.setText((String)mlist.get(position).get(1));
        iv.setImageResource((int)mlist.get(position).get(2));
    }
}