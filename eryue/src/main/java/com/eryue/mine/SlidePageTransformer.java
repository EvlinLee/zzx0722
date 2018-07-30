package com.eryue.mine;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;


public class SlidePageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE=0.60f;
    private static final float CURR_SCALE = 0.7f;

    @Override
    public void transformPage(View page, float position) {
        if(position<-1.0f) {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        }
        else if (position <= 0.0f) {
            page.setTranslationX(-page.getWidth() * position / 4);
            float scale = MIN_SCALE + (CURR_SCALE - MIN_SCALE) * (position + 1.0f);
            page.setScaleX(scale);
            page.setScaleY(scale);
        }
        else if(position <= 1.0f) {
            page.setTranslationX(-page.getWidth() * position / 4);
            float scale = MIN_SCALE + (CURR_SCALE - MIN_SCALE) * (1.0f - position);
            page.setScaleX(scale);
            page.setScaleY(scale);
        }
        else {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        }
    }
}
