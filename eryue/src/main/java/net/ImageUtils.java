package net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.library.util.StringUtils;
import com.library.util.UIScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dazhou on 2018/5/27.
 */

public class ImageUtils {
    public static Bitmap takeScreenShot(Activity activity) {
        if (null == activity)
            return null;

        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = null;
        b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas ca = new Canvas(b);
        ca.drawColor(activity.getResources().getColor(android.R.color.white));

        int nowDrawH = 0;
        ca.save();
        ca.translate(0, nowDrawH);
        ca.drawBitmap(b1, new Rect(0, statusBarHeight, width, height), new Rect(0, nowDrawH, width, nowDrawH + height - statusBarHeight), null);
        ca.restore();

        view.destroyDrawingCache();


        return b;
    }
}
