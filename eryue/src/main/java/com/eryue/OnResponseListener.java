package com.eryue;

/**
 * Created by bli.Jason on 2018/3/1.
 */

public interface OnResponseListener {
    void onSuccess();

    void onCancel();

    void onFail(String message);
}
