package com.eryue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dialog.ShareDialog;
import com.eryue.AccountUtil;
import com.eryue.ActivityHandler;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.WXShare;
import com.eryue.jd.SearchJDFragment;
import com.eryue.jd.SearchJDPresenter;
import com.eryue.jd.SearchJDPresenter.SearchJDShareListener;
import com.eryue.mine.StorageUtils;
import com.eryue.share.GoodsSharePresenter;
import com.eryue.util.SharedPreferencesUtil;
import com.eryue.util.StatusBarCompat;
import com.eryue.widget.ImageGridView;
import com.eryue.widget.ShareContentView;
import com.library.util.ImageUtils;
import com.library.util.StringUtils;
import com.library.util.ToastTools;

import net.InterfaceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;


public class CreateShareActivityEx extends BaseActivity implements View.OnClickListener,
        ShareContentView.OnShareClickListener, GoodsSharePresenter.IGoodsShareDetailExListener,SearchJDShareListener {

    /**
     * 返回
     */
    private ImageView iv_back;

    private TextView tv_copy;

    private EditText et_sharecontent;

    //淘口令
    private RelativeLayout layout_tkl;
    private EditText et_tkl;

    private TextView tv_copytkl;

    /**
     * 分享
     */
    private ShareContentView shareview;

    private WXShare wxShare;

    /**
     * 添加自定义小尾巴
     */
    private TextView tv_selfend;

    private String uid = AccountUtil.getUID();

    private ImageGridView gridview_img;

    /**
     * 添加右滑返回操作
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(this,0xFFFF1A40);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_share);
        hideNavigationBar(true);
        wxShare = new WXShare(this);

        initViews();
        testImageScrollView();

        initData();

    }

    private void initViews() {
        gridview_img = findViewById(R.id.gridview_img);
        tv_copy = (TextView) findViewById(R.id.tv_copy);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_sharecontent = (EditText) findViewById(R.id.et_sharecontent);
        tv_selfend = (TextView) findViewById(R.id.tv_selfend);

        tv_copy.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_selfend.setOnClickListener(this);


        shareview = (ShareContentView) findViewById(R.id.shareview);
        shareview.setCancelVisiblity(false);
        shareview.setOnShareClickListener(this);

        layout_tkl = findViewById(R.id.layout_tkl);
        et_tkl = findViewById(R.id.et_tkl);
        tv_copytkl = findViewById(R.id.tv_copytkl);
        tv_copytkl.setOnClickListener(this);

    }


    private GoodsSharePresenter sharePresenter;

    private SearchJDPresenter jdPresenter;

    String itemId;

    private void initData() {

        itemId = getIntent().getStringExtra("itemId");
        String searchFlag = getIntent().getStringExtra("searchFlag");
        if (TextUtils.isEmpty(itemId)) {
            return;
        }

        sharePresenter = new GoodsSharePresenter();
        sharePresenter.setShareDetailExListener(this);

        jdPresenter = new SearchJDPresenter();
        jdPresenter.setJdShareListener(this);

        String type = getIntent().getStringExtra("type");

        String productType = getIntent().getStringExtra("productType");

        String jdType = getIntent().getStringExtra("jdtype");
        showProgressMum();
            if (!TextUtils.isEmpty(jdType) && jdType.equals(SearchJDFragment.JDFREE)) {
                //京东免单
                jdPresenter.requesJDShareDetail(itemId,uid);


            } else {
                sharePresenter.requestWholeAppShareDetail(itemId, uid, productType, searchFlag);
            }

//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                clearLocalPic();
//                return null;
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

//    ImageScrollView imageScrollView;

    private void testImageScrollView() {
//        ArrayList arrayList = new ArrayList();
//        arrayList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336901046&di=3421d4583323f2fb3f7ce821b450947c&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Flarge%2F887790fagw1f4qhnk6bjsj20sg0lcad4.jpg");
//        arrayList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336900958&di=d0ae72a20a0632b308658c4a2e84dfc1&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fbaike%2Fpic%2Fitem%2F91ef76c6a7efce1b27893518a451f3deb58f6546.jpg");
//        arrayList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336901046&di=55315e57ce1709c4921588edfadcbbc9&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201207%2F11%2F20120711194953_LyCFr.jpeg");
//        arrayList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336901046&di=90969c98ab9874408c3662382d846a19&imgtype=0&src=http%3A%2F%2Ff10.topitme.com%2Fo%2F201012%2F27%2F12934577228027.jpg");
//        arrayList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336901046&di=ac97c72576d61df7cb1f6cd4251b6841&imgtype=0&src=http%3A%2F%2Fimage13.m1905.cn%2Fuploadfile%2Fs2010%2F0205%2F20100205083613575.jpg");
//		arrayList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336901046&di=bb4c520867e4a3d780879c10e90b6755&imgtype=0&src=http%3A%2F%2Fu.thsi.cn%2Ffileupload%2Fdata%2FInput%2F2015%2Fbea5b49873d52a763df69a354ded6130.jpg");
//		arrayList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336901046&di=fe77aa61a643061fedc9c2afeb8e9d40&imgtype=0&src=http%3A%2F%2Fp9.qhmsg.com%2Ft01c4a4176628fc23c2.jpg");
//		arrayList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336901045&di=70a82cbf6bdc4622bb6ede90538d40e1&imgtype=0&src=http%3A%2F%2Fimg2.mtime.com%2Fmg%2F2008%2F21%2F9f6c56ed-de37-4b95-b078-fab2ae55c92d.jpg");
//		arrayList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336901045&di=e34069b766432576001ac0d8e345a89e&imgtype=0&src=http%3A%2F%2Fwx1.sinaimg.cn%2Flarge%2Fbc5711b9ly1fe2rqaugddj20pe0wwalz.jpg");
//		arrayList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1518336901045&di=993160d4adf5d583d4bdea3e75c8dcd1&imgtype=0&src=http%3A%2F%2Fhimg2.huanqiu.com%2Fattachment2010%2F2017%2F0122%2F20170122093516442.jpg");
//        imageScrollView.setData(arrayList);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_copy) {
            if (null != et_sharecontent) {
                String content = et_sharecontent.getText().toString();
                //文案
                if (!TextUtils.isEmpty(content)) {
                    StringUtils.copyToClipBoard(content,this);
                }
            }

        } else if (v == tv_copytkl) {
            if (null != et_tkl) {
                //淘口令
                String tklString = et_tkl.getText().toString();
                if (!TextUtils.isEmpty(tklString)) {
                    StringUtils.copyToClipBoard(tklString,this);
                }
            }
        } else if (v == iv_back) {
            this.finish();
        } else if (v == tv_selfend) {
            startActivity(new Intent(this, SelfEndActivity.class));
        }
    }

    InterfaceManager.WholeAppShareDetailInfo detailInfoEx;

    //    InterfaceManager.ShareProductDetailInfo detailInfo;
    List<ImageGridView.ImageModel> imgList;

    @Override
    public void onShareClick(final int tag) {

        if (null != wxShare) {

//            if(tag == ShareContentView.FLAG_WEIXIN){
//                //微信好友：分享小程序
//                String serverURL = AccountUtil.getServerURL();
//                String url = serverURL+"appShare.do?itemId=%s&uid=%s";
//                url = String.format(url,itemId,uid);
//                if (null!=wxShare){
//                    wxShare.sendMiniApps("",detailInfoEx.content,detailInfoEx.content,url);
//                }
//
//            }else{
            if (null == imgList || imgList.isEmpty()) {
                return;
            }

            //组装图片
            final List<String> urlList = new ArrayList<>();
            for (int i = 0; i < imgList.size(); i++) {
                if (imgList.get(i).isChecked) {
                    urlList.add(imgList.get(i).url);
                }
            }

            if (null == urlList || urlList.isEmpty()){
                ToastTools.showShort(this,"请选择分享的图片");
                return;

            }

            //下载后图片
            //分享微信或者朋友圈的图片
            final List<File> shareImgFiles = new ArrayList<>();
            ImageUtils.getInstance(this).setPicPath(GoodsContants.PATH_SHARE);
            ImageUtils.getInstance(this).setScaleSize(300);
            ImageUtils.getInstance(this).setOnDownLoadListener(new ImageUtils.OnDownLoadListener() {
                @Override
                public void onDownLoadBack(final File file) {
                    shareImgFiles.add(file);

                    if (null != urlList && urlList.size() == shareImgFiles.size()) {
                        if (CreateShareActivityEx.this.isFinishing()) {
                            return;
                        }
                        CreateShareActivityEx.this.hideProgressMum();
                        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {

                            @Override
                            public void handleMessage(Message msg) {
                                if (null != shareImgFiles && !shareImgFiles.isEmpty()) {
                                    if (tag == ShareContentView.FLAG_WEIXIN) {
                                        if(null!=wxShare){
                                            wxShare.shareMultiplePictureToFriend(CreateShareActivityEx.this, shareImgFiles);
                                        }
//                                        //复制内容到剪贴板
                                        StringUtils.copyToClipBoard(et_sharecontent.getText().toString(),CreateShareActivityEx.this);

                                    } else if (tag == ShareContentView.FLAG_WEIXIN_TIMELINE) {
                                        if (null != wxShare) {
                                            wxShare.shareMultiplePictureToTimeLine(CreateShareActivityEx.this, detailInfoEx.content, shareImgFiles);
                                        }
                                        //复制内容到剪贴板
                                        StringUtils.copyToClipBoard(et_sharecontent.getText().toString(),CreateShareActivityEx.this);

                                    }
                                }


                            }
                        }).sendEmptyMessage(0);


                    }
                }

                @Override
                public void onDownLoadError() {
                    if (CreateShareActivityEx.this.isFinishing()) {
                        return;
                    }
                    CreateShareActivityEx.this.hideProgressMum();


                }
            });
            ImageUtils.getInstance(CreateShareActivityEx.this).download(urlList);
        }


//        }

    }


    /**
     * 清空本地图片
     */
    private void clearLocalPic() {
        File file_path = StorageUtils.getOwnCacheDirectory(getApplicationContext(), GoodsContants.PATH_SHARE);
        if (null != file_path && file_path.isDirectory()) {
            // 删除文件夹中的图片
            File[] files = file_path.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 删除子文件
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageUtils.getInstance(CreateShareActivityEx.this).setOnDownLoadListener(null);
    }

    @Override
    public void onShareDetailExBack(InterfaceManager.WholeAppShareDetailInfo shareDetailInfo) {
        hideProgressMum();
        refreshView(shareDetailInfo);

    }

    @Override
    public void onShareDetailExError() {
        hideProgressMum();

    }

    @Override
    public void onSearchJDShareBack(InterfaceManager.WholeAppShareDetailInfo shareDetailInfo) {
        hideProgressMum();
        refreshView(shareDetailInfo);
    }

    @Override
    public void onSearchJDShareError() {
        hideProgressMum();

    }

    private void refreshView(final InterfaceManager.WholeAppShareDetailInfo shareDetailInfo){
        if (null == shareDetailInfo) {
            return;
        }
        this.detailInfoEx = shareDetailInfo;

        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (null != shareDetailInfo && null != shareDetailInfo.pics) {

                    imgList = new ArrayList<>();
                    ImageGridView.ImageModel imageModel;
                    for (int i = 0; i < shareDetailInfo.pics.size(); i++) {

                        if (!TextUtils.isEmpty(shareDetailInfo.pics.get(i))) {

                            imageModel = new ImageGridView.ImageModel();
                            imageModel.url = shareDetailInfo.pics.get(i);
                            if (i == 0) {
                                imageModel.isChecked = true;
                            }
                            imgList.add(imageModel);
                        }

                    }
                    gridview_img.setData(imgList);

                }

                if (!TextUtils.isEmpty(shareDetailInfo.content)) {

                    String content = shareDetailInfo.content;
                    //自定义小尾巴
                    String selfend = SharedPreferencesUtil.getInstance().getString(SelfEndActivity.key_selfend);
                    if (null != content && !TextUtils.isEmpty(selfend)) {
                        content = content + selfend;
                    }
                    et_sharecontent.setText(content);
                    et_sharecontent.setSelection(content.length());
                }

                //淘口令
                if (!TextUtils.isEmpty(detailInfoEx.tkl)&&!detailInfoEx.tkl.equalsIgnoreCase("error")) {

                    String tkl = shareDetailInfo.tkl;
                    //自定义小尾巴
                    et_tkl.setText(tkl);
                    et_tkl.setSelection(tkl.length());
                } else {
                    layout_tkl.setVisibility(View.GONE);
                }
            }
        }).sendEmptyMessage(0);

    }
}
