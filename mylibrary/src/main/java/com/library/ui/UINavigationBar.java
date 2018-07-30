package com.library.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.library.R;


/**
 * ****************************************************************
 * 文件名称	: UINavigationBar.java
 * 作    者	: Aofei.Wang
 * 创建时间	: 2012-11-30 上午10:45:40
 * 文件描述	: 导航条，每个页面最上方
 * 修改历史	: 2012-11-30 1.00 初始版本
 *****************************************************************
 */
public class UINavigationBar extends RelativeLayout implements OnTouchListener{
	
	public static final int DEF_BAR_HEIGHT = 50;
	public static final int DEF_BAR_BG_RES_ID = R.color.windstock_toolbar_bg;

	private Context context;

	private TextView titleView;
	private ImageView backImageView;
	
	/**
	 * @param context
	 * @param attrs
	 */
	public UINavigationBar(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        inflater.inflate(R.layout.navigationbar, this);

		setupViews();
	}
	
	public UINavigationBar(Context context) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        inflater.inflate(R.layout.navigationbar, this);
		setupViews();
	}

	private void setupViews() {
		titleView = (TextView)findViewById(R.id.title);

		backImageView = (ImageView)findViewById(R.id.back);
		backImageView.setOnTouchListener(this);
	}


	public void setTitle(String text){
		titleView.setVisibility(View.VISIBLE);
		titleView.setText(text);
	}

	public void setHidden(boolean hide) {
		if (hide == true)
			setVisibility(View.GONE);
		else
			setVisibility(View.VISIBLE);
	}


	
	/**
	 * key event callback 
	 * @param sender
	 * @param event
	 */
	public boolean onTouch(View sender, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_UP){
			if (sender == backImageView) {
				Log.d("1", "1");
				onBack();
			}
		}
		return true;
	}
	
	public interface OnBackListener {
		public void onBack();
	}

	private OnBackListener onBackListener;

	public void setOnBackListener(OnBackListener onBackListener) {
		this.onBackListener = onBackListener;
	}

	private void onBack() {
		if (onBackListener != null) {
			onBackListener.onBack();
		}
	}

}
