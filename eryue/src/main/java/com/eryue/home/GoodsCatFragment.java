package com.eryue.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.eryue.search.GoodsSearchResultActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by enjoy on 2018/5/13.
 */

public class GoodsCatFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private GridView gridview_goodscat;

    private String[] from = {"image", "title"};
    private int[] to = {R.id.image, R.id.title};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setContentView(R.layout.fragment_goodscat);

        initView();
        showBack(true);
    }

    private void initView() {
        gridview_goodscat = getView().findViewById(R.id.gridview_goodscat);

        SimpleAdapter pictureAdapter = new SimpleAdapter(getContext(), getList(),
                R.layout.adapter_goodscat, from, to);

        gridview_goodscat.setAdapter(pictureAdapter);

        gridview_goodscat.setOnItemClickListener(this);
    }


    List<Map<String, Object>> list;
    public List<Map<String, Object>> getList() {
        list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

        String[] titles = new String[]{"食品", "男装","女装", "鞋包", "母婴", "美妆", "数码", "居家","全部"};
        String[] cats = new String[]{"shiping", "nanzhuang","nvzhuang", "xiebao", "muyin", "meizhuang", "sm", "jj",""};
        Integer[] images = {R.drawable.icon_food,R.drawable.icon_man,R.drawable.icon_girl,
                R.drawable.icon_xb,R.drawable.icon_my,R.drawable.icon_mz,R.drawable.icon_sm, R.drawable.icon_jj,
                 R.drawable.icon_allgoods};

        for (int i = 0; i < images.length; i++) {
            map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("title", titles[i]);
            map.put("cat", cats[i]);
            list.add(map);
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        if (null!=list&&position<list.size()){

            String title = (String) list.get(position).get("title");
            String cat = (String) list.get(position).get("cat");
            Intent intent = new Intent(getContext(), GoodsSearchResultActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("cat",cat);
            startActivity(intent);

        }

    }
}
