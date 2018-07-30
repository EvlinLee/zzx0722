package com.library.startactivity;

import android.content.Intent;

/**
 * Created by sgli.Android on 2016/8/12.
 */
public abstract class StartActivityForResultAction {
    public abstract void handleResult(int resultCode, Intent data);
}
