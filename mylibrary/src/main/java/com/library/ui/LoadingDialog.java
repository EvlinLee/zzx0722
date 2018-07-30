package com.library.ui;

import android.app.Dialog;
import android.content.Context;

import com.library.R;


public class LoadingDialog extends Dialog {

    /**
     * <一句话功能简述>
     *
     * @param context
     */
    public LoadingDialog(Context context) {
        super(context);
        init(context);
    }

    public LoadingDialog(Context context, boolean cancelable,
                         OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

	/*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
		TextView imageView = (TextView) findViewById(R.id.progressBar1);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		if(hasFocus){
			animationDrawable.start();
		}else{
			animationDrawable.stop();
		}
	}*/

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.loading);
        setCanceledOnTouchOutside(true);
    }

    public void setOnTouchOutsideCancel(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
    }
}
