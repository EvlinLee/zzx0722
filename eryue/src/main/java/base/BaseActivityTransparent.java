package base;

import android.graphics.Color;
import android.os.Bundle;

import com.eryue.util.StatusBarCompat;

/**
 * Created by enjoy on 2018/7/20.
 */

public class BaseActivityTransparent extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(this,  Color.TRANSPARENT, Color.TRANSPARENT);
        super.onCreate(savedInstanceState);
    }
}
