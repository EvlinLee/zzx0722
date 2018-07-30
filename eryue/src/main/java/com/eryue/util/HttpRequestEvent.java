package com.eryue.util;


import org.apache.http.NameValuePair;

import java.util.List;

public class HttpRequestEvent {
    private final int HTTP_TIME_OUT = 60000;
    private final int MAX_RETRY_COUNT = 3;
    public String url;
    public List<NameValuePair> postData;
    public int requestID;
    public int timeout = HTTP_TIME_OUT;
    public int retry;
    private boolean _isRequested;
    private boolean _standard;
    public IHttpDataReceiver receiver;

    public HttpRequestEvent() {
        url = null;
        postData = null;
        requestID = -1;
        timeout = HTTP_TIME_OUT;
        retry = 0;
        receiver = null;
        _standard = false;
        _isRequested = false;
    }

    public HttpRequestEvent(String _url, List<NameValuePair> _postData, int _requestID, int _timeout, int _retry, IHttpDataReceiver _receiver) {
        url = _url;
        postData = _postData;
        requestID = _requestID;
        if (_timeout != 0)
            timeout = _timeout;
        else
            timeout = 15 * 1000;
        if (_retry > MAX_RETRY_COUNT || _retry < 0)
            retry = 0;
        else
            retry = _retry;
        receiver = _receiver;
        _standard = false;
        _isRequested = false;
    }

    public boolean getRequested() {
        return _isRequested;
    }

    public void setRequested(boolean requested) {
        _isRequested = requested;
    }

    public boolean getStandard() {
        return _standard;
    }

    public void setStandard(boolean flag) {
        _standard = flag;
    }
}
