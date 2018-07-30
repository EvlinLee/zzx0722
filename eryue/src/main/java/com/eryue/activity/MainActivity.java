package com.eryue.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.BaseApplication;
import com.eryue.GoodsListFragment;
import com.eryue.GoodsListTabModel;
import com.eryue.R;
import com.eryue.RedRocketPopView;
import com.eryue.Update;
import com.eryue.friends.DayFriendsFragment;
import com.eryue.home.GoodsAllFragmentEx;
import com.eryue.home.InvitationDialog;
import com.eryue.home.MainPresenter;
import com.eryue.live.SearchLiveFragment;
import com.eryue.mine.AboutUsActivity;
import com.eryue.mine.MineFragment;
import com.eryue.mine.login.LoginActivity1;
import com.eryue.push.PushJumpModel;
import com.eryue.push.PushJumpUtil;
import com.eryue.search.GoodsSuperSearchFragment;
import com.eryue.util.Logger;
import com.eryue.util.StatusBarCompat;
import com.library.util.CommonData;
import com.library.util.UIScreen;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.DataCenterManager;
import net.KeyFlag;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import permission.LocalNotification;
import permission.PermissionUtil;

public class MainActivity extends BaseActivity implements OnClickListener {

    private boolean isExit = false;

    public static Context mContext;

    // private FrameLayout layoutMain;

    private LinearLayout layout1;

    private LinearLayout layout2;

    private LinearLayout layout3;

    private LinearLayout layout4;

    private LinearLayout layout5;

    private ImageView imgTab1;

    private ImageView imgTab2;

    private ImageView imgTab3;

    private ImageView imgTab4;

    private ImageView imgTab5;

    private TextView tvTab1;

    private TextView tvTab2;

    private TextView tvTab3;

    private TextView tvTab4;

    private TextView tvTab5;

    public static final int TAB_HOME = 0;
    public static final int TAB_SEARCH = 1;
    public static final int TAB_LIVE = 2;
    public static final int TAB_FRIEND = 3;
    public static final int TAB_MINE = 4;


    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            isExit = false;
        }
    };

    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(this, 0xFFFF1A40);
        super.onCreate(savedInstanceState);
        hideNavigationBar(true);
        Log.i("com.vshangxiu.vshangxiu", this.getClass().getSimpleName());
        mFragMgr = getSupportFragmentManager();
        mContext = this;
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        }
        setContentView(R.layout.activity_main);
        initHolder();
        bindListener();

        initViewPager();
        initData();

        GoodsListTabModel tabModel;
        for (int i = 0; i < resultTab.length; i++) {
            tabModel = new GoodsListTabModel(resultTab[i], tabValues[i]);
            tabList.add(tabModel);
        }

        PermissionUtil.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.PermissionListener() {
            @Override
            public void onPermissionGot() {
                LocalNotification.setInstallAlarm(MainActivity.this);
            }

            @Override
            public void onPermissionDenied(String permission) {
                finish();
            }

            @Override
            public void onPermissionDeniedAndNeverAskAgain() {
                finish();
            }
        }, getString(R.string.permission_rationale_storage));

        presenter = new MainPresenter();

        Update.getInstance(this).checkUpdate(AboutUsActivity.checkUpdateUrl);

        Update.getInstance(this).setUpdateListener(new Update.UpdateListener() {
            @Override
            public void onUpdateBack(boolean needUpdate) {
                // 检查版本号
                if (needUpdate) {
                    Update.getInstance(MainActivity.this).downLoadFile(true);
                }
            }

            @Override
            public void onUpdateError() {

            }
        });

        //猪猪侠2.1版本，不需要强制用户输入邀请码
//        String uid = AccountUtil.getUID();
//        if (TextUtils.isEmpty(uid)) {
//            presenter.getIpByCode(AccountUtil.DEFAULT_INVITECODE);
//            presenter.setInviteCodeListener(new MainPresenter.InviteCodeListener() {
//                @Override
//                public void onError() {
//
//                }
//
//                @Override
//                public void onSuccess() {
//                    if (null != goodsAllFragment) {
//                        goodsAllFragment.startRequest();
//                    }
//
//                    ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
//                        @Override
//                        public void handleMessage(Message msg) {
//                            PushJumpUtil.getInstance().registerBaiduPush(MainActivity.this);
//                        }
//                    }).sendEmptyMessageDelayed(200,1000);
//                }
//            });
//        }

    }

    private void initHolder() {
        // layoutMain = (FrameLayout) findViewById(R.id.containerBody);
        layout1 = (LinearLayout) findViewById(R.id.t1Layout);
        layout1.setTag(0);
        layout2 = (LinearLayout) findViewById(R.id.t2Layout);
        layout2.setTag(1);
        layout3 = (LinearLayout) findViewById(R.id.t3Layout);
        layout3.setTag(2);
        layout4 = (LinearLayout) findViewById(R.id.t4Layout);
        layout4.setTag(3);
        layout5 = (LinearLayout) findViewById(R.id.t5Layout);
        layout5.setTag(4);

        imgTab1 = (ImageView) findViewById(R.id.iv1);
        imgTab2 = (ImageView) findViewById(R.id.iv2);
        imgTab3 = (ImageView) findViewById(R.id.iv3);
        imgTab4 = (ImageView) findViewById(R.id.iv4);
        imgTab5 = (ImageView) findViewById(R.id.iv5);

        tvTab1 = (TextView) findViewById(R.id.text1);
        tvTab2 = (TextView) findViewById(R.id.text2);
        tvTab3 = (TextView) findViewById(R.id.text3);
        tvTab4 = (TextView) findViewById(R.id.text4);
        tvTab5 = (TextView) findViewById(R.id.text5);

    }

    public void showInvitationDialog() {
        if (null == invitationDialog) {
            invitationDialog = new InvitationDialog();
        }
        invitationDialog.setOnPopListener(new InvitationDialog.OnPopListener() {
            @Override
            public void onDismiss() {
                MainActivity.this.finish();
            }

            @Override
            public void onSuccess() {
                if (null != goodsAllFragment) {
                    goodsAllFragment.startRequest();
                }

                ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
                    @Override
                    public void handleMessage(Message msg) {
                        PushJumpUtil.getInstance().registerBaiduPush(MainActivity.this);
                    }
                }).sendEmptyMessageDelayed(200, 1000);

            }
        });

        if (null != invitationDialog && !invitationDialog.isAdded()) {
            invitationDialog.show(getSupportFragmentManager(), "invitation");
        }
    }

    InvitationDialog invitationDialog;


    @Override
    protected void onResume() {
        super.onResume();
        UIScreen.resetMainScreen(this);
        //如果没有邀请码，或者IP是空
        if (!MainActivity.this.isFinishing()) {
            //用户输入的邀请码
//            String inviteInputCode = DataCenterManager.Instance().get(MainActivity.this, KeyFlag.INVITE_CODE_KEY_INPUT);
//            //用户登录后返回的邀请码
//            String inviteCode = DataCenterManager.Instance().get(MainActivity.this, KeyFlag.INVITE_CODE_KEY);
//            if (TextUtils.isEmpty(inviteInputCode) && TextUtils.isEmpty(inviteCode)) {
//                showInvitationDialog();
//            } else {
//                PushJumpUtil.getInstance().registerBaiduPush(this);
//            }
            PushJumpUtil.getInstance().registerBaiduPush(this);

        }

//        boolean isLogin = AccountUtil.checkStatus();
//        if (!isLogin) {
//            AccountUtil.autoLogin();
//        }

        PushJumpModel model = PushJumpUtil.getInstance().getMessage();
        PushJumpUtil.getInstance().pushMsgJump(this, model);


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != invitationDialog && invitationDialog.isVisible()) {
            invitationDialog.dismiss();
        }
        //淘宝百川sdk，销毁
        AlibcTradeSDK.destory();
    }

    private List<Fragment> fragmentList;


    private int currentIndex;

    private void initViewPager() {

    }

    private void initData() {
        setCondition(currentIndex);
    }


    private void bindListener() {
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
    }


    private String[] resultTab = {"综合", "券后价", "销量", "超优惠"};
    String[] tabValues = {"updateTime", "afterQuan", "soldQuantity", "quanPrice"};

    ArrayList<GoodsListTabModel> tabList = new ArrayList<>();


    //    HomeFragment homeFragment;
    //首页改版(猪猪虾2期)
    GoodsAllFragmentEx goodsAllFragment;
    GoodsListFragment hotFragment;
    GoodsListFragment vedioFragment;
    //超级搜索
    GoodsSuperSearchFragment goodsSearchFramgent;

    //实时播
    SearchLiveFragment searchLiveFragment;
    DayFriendsFragment friendsFragment;
    MineFragment mineFragment;//个人中心

    private void setCondition(int index) {

        if (index == 3) {
            //如果没有登录就进入到登录页
            //点击消息、每日发圈、列表分享、详情分享、立即购买、联系客服,需要登录
            if (!AccountUtil.checkLogin(this)) {
                return;
            }
        }

        setStyle(index);
        switch (index) {
            case 0:
                if (goodsAllFragment == null) {
                    goodsAllFragment = new GoodsAllFragmentEx();
                } else {
                    goodsAllFragment.refreshView();
                }
                showFragment(goodsAllFragment);
                break;
            case 1:
//                if (hotFragment == null) {
//                    hotFragment = GoodsListFragment.newInstance(false);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("fragment_title",getResources().getString(R.string.string_bottom_tab02));
//                    bundle.putParcelableArrayList("tab",tabList);
//                    hotFragment.setArguments(bundle);
//                }else{
//                    hotFragment.refreshFragment();
//                }
//                showFragment(hotFragment);
                if (goodsSearchFramgent == null) {
                    goodsSearchFramgent = new GoodsSuperSearchFragment();
                } else {
                    goodsSearchFramgent.checkClip();
                }
                showFragment(goodsSearchFramgent);

                break;
            case 2:
//                if (vedioFragment == null) {
//                    vedioFragment = GoodsListFragment.newInstance(true);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("fragment_title",getResources().getString(R.string.string_bottom_tab03));
//                    bundle.putParcelableArrayList("tab",tabList);
//                    vedioFragment.setArguments(bundle);
//                }else{
//                    vedioFragment.refreshFragment();
//                }
//                showFragment(vedioFragment);

                if (searchLiveFragment == null) {
                    searchLiveFragment = new SearchLiveFragment();
                    String title = "实时播";
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    searchLiveFragment.setArguments(bundle);
                } else {
                    searchLiveFragment.refreshView();
                }
                showFragment(searchLiveFragment);
                break;
            case 3:
                if (null == friendsFragment) {
                    friendsFragment = new DayFriendsFragment();
                } else {
                    friendsFragment.refreshContent();
                }
                showFragment(friendsFragment);
                break;
            case 4:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                } else {
                    mineFragment.onResume();
                }
                showFragment(mineFragment);
                break;
            default:
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        currentIndex = intent.getIntExtra("selectIndex", 0);
        setCondition(currentIndex);
    }

    /**
     * 展示Fragment
     */
    protected void showFragment(BaseFragment fragment) {
        if (getFragment() != fragment && null != fragment) {
            FragmentTransaction transaction = mFragMgr.beginTransaction();
            if (null != getFragment()) {
                transaction.hide(getFragment());
            }
            setFragment(fragment);
            if (!fragment.isAdded()) {
                transaction.add(R.id.layout_content, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
    }

    private void exit() {
        Logger.getInstance(BaseApplication.getInstance()).writeLog_new("MainActivity", "MainActivity", "exit-----isExit=" + isExit);
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(),
                    getString(R.string.tip_exit), Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {

            CommonData.exit();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == layout1 || v == layout2 || v == layout3 || v == layout4
                || v == layout5) {
            setCondition(Integer.parseInt(v.getTag().toString()));
        }
    }

    @Override
    public void onBack() {
        exit();
    }

    RedRocketPopView redRocketPopView;


    @Override
    protected void onStop() {
        super.onStop();

        if (null != presenter) {
            presenter.setRedPacketListener(null);
        }

    }

    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {

            setStyle(arg0);
            currentIndex = arg0;
        }
    }

    private void setStyle(int index) {
        imgTab1.setImageResource(R.drawable.m_home_normal);
        imgTab2.setImageResource(R.drawable.m_goods_normal);
        imgTab3.setImageResource(R.drawable.m_video_normal);
        imgTab4.setImageResource(R.drawable.m_category_normal);
        imgTab5.setImageResource(R.drawable.m_mine_normal);

        tvTab1.setTextColor(getResources().getColor(R.color.main_text_color));
        tvTab2.setTextColor(getResources().getColor(R.color.main_text_color));
        tvTab3.setTextColor(getResources().getColor(R.color.main_text_color));
        tvTab4.setTextColor(getResources().getColor(R.color.main_text_color));
        tvTab5.setTextColor(getResources().getColor(R.color.main_text_color));
        if (index == 0) {
            imgTab1.setImageResource(R.drawable.m_home_selected);
            tvTab1.setTextColor(getResources().getColor(R.color.red));

        } else if (index == 1) {
            imgTab2.setImageResource(R.drawable.m_goods_selected);
            tvTab2.setTextColor(getResources().getColor(R.color.red));

        } else if (index == 2) {
            imgTab3.setImageResource(R.drawable.m_video_selected);
            tvTab3.setTextColor(getResources().getColor(R.color.red));

        } else if (index == 3) {
            imgTab4.setImageResource(R.drawable.m_category_selected);
            tvTab4.setTextColor(getResources().getColor(R.color.red));

        } else if (index == 4) {
            imgTab5.setImageResource(R.drawable.m_mine_selected);
            tvTab5.setTextColor(getResources().getColor(R.color.red));

        }
    }

    protected FragmentManager mFragMgr;

    /**
     * TAB之间切换请使用showFragments方法,各tab定义为fragment 2017-1-13
     *
     * @param fragment
     */
    public void showFragments(BaseFragment fragment) {
        if (fragment == getFragment()) {
            return;
        }

        setFragment(fragment);
        FragmentTransaction trans = mFragMgr.beginTransaction();
        trans.replace(R.id.main_body, fragment, fragment.getClass().getSimpleName());
        trans.addToBackStack(null); //来回切换tab，崩溃问题
        trans.commitAllowingStateLoss();

    }

    private BaseFragment fragment;

    public BaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(BaseFragment f) {
        this.fragment = f;
    }
}
