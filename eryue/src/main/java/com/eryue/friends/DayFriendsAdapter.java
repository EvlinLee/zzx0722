package com.eryue.friends;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eryue.ActivityHandler;
import com.eryue.GoodsContants;
import com.eryue.R;
import com.eryue.WXShare;
import com.eryue.activity.ImageBrowserActivity;
import com.eryue.home.SharePopView;
import com.eryue.ui.ImageCacheHelper;
import com.eryue.ui.Images;
import com.eryue.ui.NineViewGroup;
import com.eryue.widget.ShareContentView;
import com.library.util.ImageUtils;
import com.library.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import net.InterfaceManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import base.BaseActivity;

/**
 * Created by bli.Jason on 2018/2/27.
 */

public class DayFriendsAdapter extends BaseAdapter implements ShareContentView.OnShareClickListener {


    private List<InterfaceManager.TimeLineEx> dataList;
    private Context context;

    //分享左边按钮
    Drawable shareDrawable;

    //分享
    SharePopView sharePopView;
    private WXShare wxShare;

    private InterfaceManager.TimeLineEx shareTimeLine;

    public DayFriendsAdapter(Context context) {
        this.context = context;

        sharePopView = new SharePopView(context);
        sharePopView.setOnShareClickListener(this);
        wxShare = new WXShare(context);
        shareDrawable = context.getResources().getDrawable(R.drawable.icon_moments_share);
        //width即为你需要设置的图片宽度，height即为你设置的图片的高度
        shareDrawable.setBounds(0, 0, StringUtils.dipToPx(20), StringUtils.dipToPx(20));
    }

    public void setDataList(List<InterfaceManager.TimeLineEx> dataList) {
        this.dataList = dataList;
    }

    public void clearData(){
        if (null!=this.dataList){
            this.dataList.clear();
        }
    }

    @Override
    public int getCount() {
        if (null == dataList) {
            return 0;
        } else {
            return dataList.size();
        }
    }

    @Override
    public InterfaceManager.TimeLineEx getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayFriendsViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_dayfriends, null);
            viewHolder = new DayFriendsViewHolder();
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_sendtitle = (TextView) convertView.findViewById(R.id.tv_sendtitle);
            viewHolder.iv_share = (ImageView) convertView.findViewById(R.id.iv_share);
            viewHolder.tv_sendcontent = (TextView) convertView.findViewById(R.id.tv_sendcontent);
            viewHolder.nineView = (NineViewGroup) convertView.findViewById(R.id.nineView);
            viewHolder.tv_sendtime = (TextView) convertView.findViewById(R.id.tv_sendtime);
            viewHolder.layout_comment = (LinearLayout) convertView.findViewById(R.id.layout_comment);
            viewHolder.tv_copycomment = convertView.findViewById(R.id.tv_copycomment);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DayFriendsViewHolder) convertView.getTag();
        }

        final InterfaceManager.TimeLineEx timeLine = dataList.get(position);
        if (null != timeLine) {
            viewHolder.tv_sendtitle.setText("猪猪虾推荐");
            viewHolder.tv_sendcontent.setText(timeLine.title);

            List<String> imgList = new ArrayList<>();
            imgList = timeLine.picts;
//            imgList.add(baseIP + "newTheme/img/nian-01.jpg");
//            imgList.add(baseIP + "newTheme/img/nian-02.jpg");
//            imgList.add(baseIP + "newTheme/img/nian-03.jpg");
//            imgList.add(baseIP + "newTheme/img/nian-04.jpg");
//            imgList.add(baseIP + "newTheme/img/nian-01.jpg");
//            imgList.add(baseIP + "newTheme/img/nian-02.jpg");
//            imgList.add(baseIP + "newTheme/img/nian-03.jpg");
//            imgList.add(baseIP + "newTheme/img/nian-04.jpg");


            if (null != imgList && imgList.size() > 0) {
                viewHolder.nineView.setVisibility(View.VISIBLE);
                Images[] images = new Images[imgList.size()];
                for (int i = 0; i < images.length; i++) {
                    Images image = new Images();
                    image.picAdd = imgList.get(i);
                    image.smallPicAdd = imgList.get(i);
                    if (images.length > 1) {
                        image.spicSideRate = 2f;
                    } else {
                        image.spicSideRate = 1f;
                    }

                    images[i] = image;
                }
                displayImage(viewHolder.nineView, images, (ArrayList<String>) imgList);
            } else {
                viewHolder.nineView.setVisibility(View.GONE);
            }

            String sendTIme = convertTimesToDate(StringUtils.valueOfLong(timeLine.sendTime));
            if (!TextUtils.isEmpty(sendTIme)){
                viewHolder.tv_sendtime.setText("建议发送时间 "+sendTIme);
            }

            viewHolder.layout_comment.removeAllViews();
            //评论
            List<String> commentList = new ArrayList<>();
//            commentList.add("复制这条消息,￥s1dXoNWEjv5￥,快打开手机淘宝,即可领券下单！手慢无~");
//            commentList.add("复制这条消息,￥s1dXoNWEjv5￥,快打开手机淘宝,即可领券下单！手慢无~");
            if (!TextUtils.isEmpty(timeLine.content)) {
                commentList.add(timeLine.content);
            }
            final int textSize = 15;
            viewHolder.tv_copycomment.setVisibility(View.GONE);
            if (null != commentList && !commentList.isEmpty()) {
                viewHolder.layout_comment.setVisibility(View.VISIBLE);

                int count = commentList.size();

                View itemLayout;
                TextView title;
                TextView tv_copy;
                LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, StringUtils.dipToPx(4));


                String commentContent = null;
                for (int i = 0; i < count; i++) {
                    commentContent = commentList.get(i);
                    if (TextUtils.isEmpty(commentContent)) {
                        continue;
                    }
                    if (i > 0) {
                        viewHolder.layout_comment.addView(new View(context), lineLp);
                    }

                    itemLayout = LayoutInflater.from(context).inflate(R.layout.view_comment, null);
                    title = (TextView) itemLayout.findViewById(R.id.tv_comment);
                    tv_copy = (TextView) itemLayout.findViewById(R.id.tv_copy);
                    Drawable icon_comment = context.getResources().getDrawable(R.drawable.icon_comment);
                    icon_comment.setBounds(0, 0, StringUtils.dipToPx(16), StringUtils.dipToPx(16));//对图片进行压缩
                    title.setCompoundDrawables(icon_comment,null,null,null);

                    title.setText(commentContent);


                    final String finalCommentContent = commentContent;
                    itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StringUtils.copyToClipBoard(finalCommentContent,context);
                        }
                    });
                    viewHolder.layout_comment.addView(itemLayout);
                }

                viewHolder.tv_copycomment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringUtils.copyToClipBoard(timeLine.content,context);
                    }
                });

            } else {
                viewHolder.layout_comment.setVisibility(View.GONE);
            }


            viewHolder.iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareTimeLine = timeLine;
                    if (null != sharePopView) {
                        sharePopView.showPopView();
                    }
                }
            });
        }

        return convertView;
    }


    DisplayImageOptions optionsSmall = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .showImageOnLoading(R.drawable.img_default_contract)
            .showImageOnFail(R.drawable.img_default_contract)
            .considerExifParams(true).build();

    private void displayImage(NineViewGroup nineView, Images[] images, final ArrayList<String> bigPics) {

        int count = 0;
        if (images != null) {
            count = images.length;
        }

        //nineView.setSingleScale(2f / 3);
        //nineView.setMultiScale(7f / 8);
        nineView.setLimit(true);

        nineView.setDataCount(count);

        for (int i = 0; i < count; i++) {
            String imageUrl = images[i].smallPicAdd;
            if (TextUtils.isEmpty(imageUrl)) {
                imageUrl = "";
            }
            NineViewGroup.ScaleImageView imageView = nineView.getImageView(i);
            imageView.setSpicSideRate(images[i].spicSideRate);
            imageView.setMinRate(1f);
            //imageView.setBackgroundColor(0xFFFF0000);
            Glide.with(context).load(imageUrl).placeholder(R.drawable.img_default_contract).into(imageView);
//            ImageCacheHelper.getInstance().loadSmallImage(imageView, imageUrl);
//            ImageCacheHelper.getInstance().setOptionsSmall(optionsSmall);

            if (bigPics != null) {
                final int finalI = i;
                nineView.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ImageBrowserActivity.class);
                        intent.putExtra(ImageBrowserActivity.KEY_INDEX, finalI);
                        intent.putStringArrayListExtra(ImageBrowserActivity.KEY_URL, bigPics);
                        context.startActivity(intent);
                    }
                });
            }
        }

        for (int i = count; i < nineView.getChildCount(); i++) {
            ImageCacheHelper.getInstance().loadSmallImage(
                    nineView.getChildAt(i), null);
            nineView.getChildAt(i).setOnClickListener(null);
        }
    }


    List<File> shareImgFiles;

    @Override
    public void onShareClick(final int tag) {

        if (null != wxShare) {


            if (context instanceof BaseActivity) {

                ((BaseActivity) context).showProgressMum();
            }
            shareImgFiles = new ArrayList<>();

            ImageUtils.getInstance(context).setPicPath(GoodsContants.PATH_SHARE);
            ImageUtils.getInstance(context).setScaleSize(300);
            ImageUtils.getInstance(context).setOnDownLoadListener(new ImageUtils.OnDownLoadListener() {
                @Override
                public void onDownLoadBack(final File file) {
                    shareImgFiles.add(file);

                    if (null != shareTimeLine.picts && shareImgFiles.size() == shareTimeLine.picts.size()) {
                        if (((BaseActivity) context).isFinishing()){
                            return;
                        }
                        if (context instanceof BaseActivity) {
                            ((BaseActivity) context).hideProgressMum();
                        }
                        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {

                            @Override
                            public void handleMessage(Message msg) {
                                if (null != sharePopView) {
                                    sharePopView.dimiss();
                                }
                                if (null != shareImgFiles && !shareImgFiles.isEmpty()) {
                                    if (tag == ShareContentView.FLAG_WEIXIN) {

                                        wxShare.shareMultiplePictureToFriend(context, shareImgFiles);
                                        //复制内容到剪贴板
                                        StringUtils.copyToClipBoard(shareTimeLine.title,context);
                                    } else if (tag == ShareContentView.FLAG_WEIXIN_TIMELINE) {
                                        wxShare.shareMultiplePictureToTimeLine(context, shareTimeLine.title, shareImgFiles);
                                        //复制内容到剪贴板
                                        StringUtils.copyToClipBoard(shareTimeLine.title,context);
                                    }
                                }


                            }
                        }).sendEmptyMessage(0);


                    }
                }

                @Override
                public void onDownLoadError() {
                    if (((BaseActivity) context).isFinishing()){
                        return;
                    }
                    if (context instanceof BaseActivity) {

                        ((BaseActivity) context).hideProgressMum();
                    }
                    if (null != sharePopView) {
                        sharePopView.dimiss();
                    }

                }
            });
            ImageUtils.getInstance(context).download(shareTimeLine.picts);


        }

    }

    public class DayFriendsViewHolder {
        public ImageView iv_icon;
        public TextView tv_sendtitle;
        public ImageView iv_share;
        public TextView tv_sendcontent;
        public NineViewGroup nineView;
        public TextView tv_sendtime;
        public LinearLayout layout_comment;

        public TextView tv_copycomment;//复制评论
    }

    //把毫秒数转换为日期
    public String convertTimesToDate(long second){
        if (second == 0){
            return "";
        }
        try{
            //时间转换为东八区时间
            TimeZone tz = TimeZone.getTimeZone("GMT+8");
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
            format.setTimeZone(tz);
            Date myDate = new Date();
            myDate.setTime(second);
            return format.format(myDate);
        }catch (Exception e){

        }

        return "";
    }


}
