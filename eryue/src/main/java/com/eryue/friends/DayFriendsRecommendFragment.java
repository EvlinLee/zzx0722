package com.eryue.friends;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eryue.AccountUtil;
import com.eryue.R;
import com.eryue.activity.BaseFragment;
import com.library.ui.dragrefresh.DragRefreshListView;
import com.library.ui.dragrefresh.ListViewFooter;

import net.InterfaceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 */

public class DayFriendsRecommendFragment extends BaseFragment implements DayFriendPresenter.IFriendsListener,DragRefreshListView.DragRefreshListViewListener,DayFriendPresenter.IFriendsExListener {

    private DragRefreshListView listview_dayfriends;
    private DayFriendsAdapter adapter;

    private DayFriendPresenter presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_dayfriends);
        initView();
        initData();

    }
    public static int getStatusBarHeight(Activity a) {
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initView(){
        listview_dayfriends = (DragRefreshListView) getView().findViewById(R.id.listview_dayfriends);
        adapter = new DayFriendsAdapter(getContext(),"Recommend");
        listview_dayfriends.setAdapter(adapter);
        listview_dayfriends.setFooterDividersEnabled(false);
        listview_dayfriends.setFooterViewState(ListViewFooter.STATE_HIDE);
        listview_dayfriends.setAutoLoadMore(true);
        listview_dayfriends.refreshComplete(true, new Date().getTime());

    }

    private void initData(){

        presenter = new DayFriendPresenter();
        presenter.setFriendsListener(this);
        presenter.setFriendsExListener(this);


//        List<InterfaceManager.TimeLineEx> dataList = new ArrayList<>();
//        dataList.add(new InterfaceManager.TimeLineEx());
//        dataList.add(new InterfaceManager.TimeLineEx());
//        dataList.add(new InterfaceManager.TimeLineEx());
//        adapter.setDataList(dataList);
//        adapter.notifyDataSetChanged();
        listview_dayfriends.setDragRefreshListViewListener(this);
        listview_dayfriends.setHeaderViewState();

        //初始化表情
//        Map<String,EmojiEntity> emojiMap = JsonParseUtil.parseEmojiMap(FileUtil.readAssetsFile(getContext(), "EmojiList.json"));

//        Log.d("libo","emojiList----"+emojiMap.size());

    }

    @Override
    public void onFriendsDataBack(final List<InterfaceManager.TimeLine> dataList) {

//        post(new Runnable() {
//            @Override
//            public void run() {
//                if (null!=adapter&&null!=dataList&&!dataList.isEmpty()){
//                    listview_dayfriends.refreshComplete(true, new Date().getTime());
//                    adapter.setDataList(dataList);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });


    }

    private int pageNo = 1;
    boolean isLastPage;
    private List<InterfaceManager.TimeLineEx> allDataList = new ArrayList<InterfaceManager.TimeLineEx>();

    @Override
    public void onFriendsDataError() {
        listview_dayfriends.refreshComplete(true, new Date().getTime());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null!=presenter){
            //切换ip，刷新数据
            if (!AccountUtil.getBaseIp().equals(presenter.getBaseIP())){
                refreshContent();
            }
        }
    }

    @Override
    public void onRefresh() {

        if (null!=presenter){
            isLastPage = false;
            pageNo = 1;
            presenter.requestTimeLineEx(pageNo);
        }

    }

    @Override
    public void onLoadMore() {
        if (isLastPage) {
            return;
        }

        pageNo++;
        //获取数据
//        presenter.searchProduct(pageNo, "");
        if (null!=presenter){
            showProgressMum();
            presenter.requestTimeLineEx(pageNo);
        }

    }

    public void refreshContent(){
        listview_dayfriends.setSelection(0);
        listview_dayfriends.setHeaderViewState();

    }

    @Override
    public void onFriendsDataExBack(final List<InterfaceManager.TimeLineEx> dataList) {
        hideProgressMum();
        post(new Runnable() {
            @Override
            public void run() {
                if (null==getActivity()||getActivity().isFinishing()){
                    return;
                }
                if (null!=listview_dayfriends){
                    listview_dayfriends.refreshComplete(true, new Date().getTime());
                }
                if (null==dataList||dataList.isEmpty()){
                    isLastPage = true;
                }else{
                    if (pageNo == 1) {
                        allDataList.clear();
                    }

                    allDataList.addAll(dataList);

                    if (null!=adapter&&null!=allDataList&&!allDataList.isEmpty()){
                        adapter.setDataList(allDataList);
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }

    @Override
    public void onFriendsDataExError() {
        hideProgressMum();
        listview_dayfriends.refreshComplete(true, new Date().getTime());
        isLastPage = true;

    }
}
