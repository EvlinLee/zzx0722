package com.eryue.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

public class GoodsCatFragment extends BaseFragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    private GridView gridview_goodscat;

    /**
     * 分类小标题
     */
    private TextView classify_list_title;

    /**
     * 存放gridview中数据
     */
    private List<Map<String, Object>> list;

    private String[] from = {"image", "title"};
    private int[] to = {R.id.image, R.id.title};
    private String[] titles = new String[]{"女装","男装","鞋品","箱包","母婴","美妆","内衣","配饰","美食","数码家电","家装家纺","居家日用","文娱用品","户外运动"};
    private String[] femaleClothing = new String[]{"T恤","裤子","衬衫","外套","卫衣","大衣","棉服","毛衣","羽绒服"};
    private String[] maleClothing = new String[]{"T恤","裤子","衬衫","外套","卫衣","大衣","棉服","毛衣","羽绒服"};
    private String[] shoes = new String[]{"凉鞋", "拖鞋", "单鞋", "冬鞋", "平底鞋", "高跟鞋", "休闲鞋", "运动鞋", "皮鞋"};
    private String[] bags = new String[]{"斜挎包", "手提包", "双肩包", "男/女钱包", "拉杆箱", "实用大包", "商务公文包", "腰包", "旅行包"};
    private String[] motherBabySupplies = new String[]{"童装","童鞋","奶粉辅食","纸尿裤","玩具","洗护喂养","妈咪专区","宝宝寝居","文具"};
    private String[] makeup = new String[]{"眼部护理","面部护理","唇部护理","洁面/卸妆","口红","气垫/粉饼","香水","妆前/隔离","美妆工具"};
    private String[] underwear = new String[]{"文胸","女士睡衣","文胸套装","女士内裤","男士睡衣","男士内裤","男女袜子","丝袜/裤袜","打底裤"};
    private String[] accessories = new String[]{"项链","耳饰","银饰","发饰","戒指","手镯/链","胸针","木饰","文玩","手表"};
    private String[] foods = new String[]{"糕点","方便面/粉","糖果","饼干","巧克力","坚果","饮料","奶粉/麦片","茶叶"};
    private String[] gitalProduct = new String[]{"手机","电脑","游戏机","数码单反","空调","冰箱","平板电视","电饭煲","洗衣机"};
    private String[] decoration = new String[]{"灯饰五金","客厅家具","卧室家具","餐厅家具","储物家具","家饰软装","纸品清洁","床上套件","晾晒用品"};
    private String[] lifeProduct = new String[]{"家具拖鞋", "毛浴巾", "个人用品","浴室用品","拖把","扫把/簸箕","清洁刷","宠物日品","一次性用品"};
    private String[] entertainmentProduct = new String[]{"文房用品","工艺品","文具","书法绘画","乐器","扑克/麻将","兵乓/羽毛/篮球","球拍","象棋/跳棋/围棋"};
    private String[] sports = new String[]{"跑步鞋","运动休闲鞋","球鞋","徒步/登山鞋","T恤/POLO","运动裤","泳装","运动套装","防晒皮肤衣"};


    private Integer[] femaleClothingImg = {R.drawable.img_women_01,R.drawable.img_women_02,R.drawable.img_women_03,R.drawable.img_women_04,R.drawable.img_women_05,R.drawable.img_women_06,
            R.drawable.img_women_07,R.drawable.img_women_08,R.drawable.img_women_09};
    private Integer[] maleClothingImg = {R.drawable.img_men_01,R.drawable.img_men_02,R.drawable.img_men_03,R.drawable.img_men_04,R.drawable.img_men_05,R.drawable.img_men_06,
            R.drawable.img_men_07,R.drawable.img_men_08,R.drawable.img_men_09};
    private Integer[] shoesImg = {R.drawable.img_shoes_01, R.drawable.img_shoes_02, R.drawable.img_shoes_03, R.drawable.img_shoes_04, R.drawable.img_shoes_05, R.drawable.img_shoes_06, R.drawable.img_shoes_07,
            R.drawable.img_shoes_08, R.drawable.img_shoes_09};
    private Integer[] bagsImg = {R.drawable.img_bag_01,R.drawable.img_bag_02,R.drawable.img_bag_03,R.drawable.img_bag_04,R.drawable.img_bag_05,R.drawable.img_bag_06,R.drawable.img_bag_07,R.drawable.img_bag_08,
            R.drawable.img_bag_09};
    private Integer[] motherBabySuppliesImg = {R.drawable.img_baby_01,R.drawable.img_baby_02,R.drawable.img_baby_03,R.drawable.img_baby_04,R.drawable.img_baby_05,R.drawable.img_baby_06,R.drawable.img_baby_07,
            R.drawable.img_baby_08,R.drawable.img_baby_09};
    private Integer[] makeupImg = {R.drawable.img_makeup_01, R.drawable.img_makeup_02, R.drawable.img_makeup_03, R.drawable.img_makeup_04, R.drawable.img_makeup_05, R.drawable.img_makeup_06,
            R.drawable.img_makeup_07, R.drawable.img_makeup_08, R.drawable.img_makeup_09};
    private Integer[] underwearImg = {R.drawable.img_underwear_01,R.drawable.img_underwear_02,R.drawable.img_underwear_04,R.drawable.img_underwear_03,R.drawable.img_underwear_05,R.drawable.img_underwear_06,R.drawable.img_underwear_07,
            R.drawable.img_underwear_08,R.drawable.img_underwear_09};
    private Integer[] accessoriesImg = {R.drawable.img_ornament_01, R.drawable.img_ornament_02, R.drawable.img_ornament_03, R.drawable.img_ornament_05, R.drawable.img_ornament_04, R.drawable.img_ornament_06
            , R.drawable.img_ornament_07, R.drawable.img_ornament_08, R.drawable.img_ornament_09};
    private Integer[] foodsImg = {R.drawable.img_food_01, R.drawable.img_food_02, R.drawable.img_food_03, R.drawable.img_food_04, R.drawable.img_food_05, R.drawable.img_food_06, R.drawable.img_food_07, R.drawable.img_food_08,
            R.drawable.img_food_09};
    private Integer[] gitalProductImg = {R.drawable.img_ha_01,R.drawable.img_ha_02,R.drawable.img_ha_03,R.drawable.img_ha_04,R.drawable.img_ha_05,R.drawable.img_ha_06,R.drawable.img_ha_07,
            R.drawable.img_ha_08,R.drawable.img_ha_09};
    private Integer[] decorationImg = {R.drawable.img_hd_01, R.drawable.img_hd_02, R.drawable.img_hd_03, R.drawable.img_hd_04, R.drawable.img_hd_05, R.drawable.img_hd_06, R.drawable.img_hd_07, R.drawable.img_hd_08,
            R.drawable.img_hd_09};
    private Integer[] lifeProductImg = {R.drawable.img_dailygoods_01,R.drawable.img_dailygoods_02,R.drawable.img_dailygoods_03,R.drawable.img_dailygoods_04,R.drawable.img_dailygoods_05,R.drawable.img_dailygoods_06,
            R.drawable.img_dailygoods_07,R.drawable.img_dailygoods_08,R.drawable.img_dailygoods_09};
    private Integer[] entertainmentProductImg = {R.drawable.img_rg_01, R.drawable.img_rg_02, R.drawable.img_rg_03, R.drawable.img_rg_04, R.drawable.img_rg_05, R.drawable.img_rg_06, R.drawable.img_rg_07,
            R.drawable.img_rg_08, R.drawable.img_rg_09};
    private Integer[] sportsImg = {R.drawable.img_outdoors_01, R.drawable.img_outdoors_02, R.drawable.img_outdoors_03, R.drawable.img_outdoors_04, R.drawable.img_outdoors_05, R.drawable.img_outdoors_06,
            R.drawable.img_outdoors_07, R.drawable.img_outdoors_08, R.drawable.img_outdoors_09};

    private TextView[] titleTextView = new TextView[titles.length];

    /**
     *     存放检索内容
     */
    private ArrayList<String[]> titleText = new ArrayList<>();

    /**
     * 存放图片
     * @param savedInstanceState
     */
    private ArrayList<Integer[]> imgArray = new ArrayList<>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setContentView(R.layout.fragment_goodscat);

        initView();
        initData();
        showBack(true);
    }
    private void initData() {
        titleText.add(femaleClothing);
        titleText.add(maleClothing);
        titleText.add(shoes);
        titleText.add(bags);
        titleText.add(motherBabySupplies);
        titleText.add(makeup);
        titleText.add(underwear);
        titleText.add(accessories);
        titleText.add(foods);
        titleText.add(gitalProduct);
        titleText.add(decoration);
        titleText.add(lifeProduct);
        titleText.add(entertainmentProduct);
        titleText.add(sports);

        imgArray.add(femaleClothingImg);
        imgArray.add(maleClothingImg);
        imgArray.add(shoesImg);
        imgArray.add(bagsImg);
        imgArray.add(motherBabySuppliesImg);
        imgArray.add(makeupImg);
        imgArray.add(underwearImg);
        imgArray.add(accessoriesImg);
        imgArray.add(foodsImg);
        imgArray.add(gitalProductImg);
        imgArray.add(decorationImg);
        imgArray.add(lifeProductImg);
        imgArray.add(entertainmentProductImg);
        imgArray.add(sportsImg);

    }

    private void initView() {

        classify_list_title = getView().findViewById(R.id.classify_list_title);

        gridview_goodscat = getView().findViewById(R.id.gridview_goodscat);

        SimpleAdapter pictureAdapter = new SimpleAdapter(getContext(), getList1(),
                R.layout.adapter_goodscat, from, to);

        gridview_goodscat.setAdapter(pictureAdapter);

        gridview_goodscat.setOnItemClickListener(this);

        LinearLayout mainLinerLayout = (LinearLayout)getView().findViewById(R.id.title_linearLayout);



        //添加标题栏数据 设置格式
       for (int i = 0;i<titles.length;i++) {
           titleTextView[i]=new TextView(getContext());
           titleTextView[i].setBackground(getResources().getDrawable(R.drawable.classify_item_selector));
           titleTextView[i].setTextSize(16);
           titleTextView[i].setTextColor(Color.parseColor("#666666"));
           titleTextView[i].setGravity(Gravity.CENTER);
           titleTextView[i].setPadding(10,10,10,10);
           mainLinerLayout.addView(titleTextView[i]);
           titleTextView[i].setText(titles[i]);
           titleTextView[i].setOnClickListener(this);
       }
        setTextViewSelected();
        titleTextView[0].setSelected(true);
        titleTextView[0].setTextColor(Color.parseColor("#fd5b67"));
    }
    private void setTextViewSelected(){
        for (int i = 0;i<titles.length;i++) {
            titleTextView[i].setSelected(false);
            titleTextView[i].setTextColor(Color.parseColor("#666666"));
        }
    }

    public List<Map<String, Object>> getList1() {
        list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

        String[] cats = new String[]{"shiping", "nanzhuang","nvzhuang", "xiebao", "muyin", "meizhuang", "sm", "jj",""};
        Integer[] images = {R.drawable.img_women_01,R.drawable.img_women_02,R.drawable.img_women_03,R.drawable.img_women_04,R.drawable.img_women_05,R.drawable.img_women_06,
                R.drawable.img_women_07,R.drawable.img_women_08,R.drawable.img_women_09};

        for (int i = 0; i < images.length; i++) {
            map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("title", femaleClothing[i]);
            map.put("cat", cats[i]);
            list.add(map);
        }
        return list;
    }
    public List<Map<String, Object>> getList2(int b) {
        list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

//        String[] cats = new String[]{"shiping", "nanzhuang","nvzhuang", "xiebao", "muyin", "meizhuang", "sm", "jj",""};
  /*      Integer[] images = {R.drawable.icon_food,R.drawable.icon_food,R.drawable.icon_food,
                R.drawable.icon_food,R.drawable.icon_food,R.drawable.icon_food,R.drawable.icon_food, R.drawable.icon_food,
                R.drawable.icon_food};
*/

        String [] content =titleText.get(b);
        Integer[] images = imgArray.get(b);

        for (int i = 0; i < images.length; i++) {
            map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("title", content[i]);
//            map.put("cat", cats[i]);
            list.add(map);
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        if (null!=list&&position<list.size()){

            String title = classify_list_title.getText().toString()+"+"+(String) list.get(position).get("title");
            String cat = (String) list.get(position).get("cat");
            Intent intent = new Intent(getContext(), GoodsSearchResultActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("cat",cat);
            startActivity(intent);

        }

    }

    @Override
    public void onClick(View v) {
        setTextViewSelected();
        for (int i = 0;i<titles.length;i++) {
            if (v == titleTextView[i]) {
                titleTextView[i].setSelected(true);
                titleTextView[i].setTextColor(Color.parseColor("#fd5b67"));
                SimpleAdapter pictureAdapter = new SimpleAdapter(getContext(), getList2(i),
                        R.layout.adapter_goodscat, from, to);

                gridview_goodscat.setAdapter(pictureAdapter);
                //设置分类小标题
                classify_list_title.setText(titleTextView[i].getText().toString());
            }
        }

    }
}
