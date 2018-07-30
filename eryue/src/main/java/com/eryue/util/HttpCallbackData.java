package com.eryue.util;

import java.util.ArrayList;

public class HttpCallbackData {
    public boolean isSucess;
    public ArrayList data;
    public int requestID;
    public Object tag;

    public HttpCallbackData() {
        isSucess = false;
        data = null;
        requestID = -1;
    }

    public HttpCallbackData(boolean _isSucess, ArrayList _data, int _requestID) {
        isSucess = _isSucess;
        data = _data;
        requestID = _requestID;
    }
}
