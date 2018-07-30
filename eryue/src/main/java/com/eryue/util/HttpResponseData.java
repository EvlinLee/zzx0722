package com.eryue.util;

public class HttpResponseData {
    public int code;
    public int requestID;
    public byte[] data;

    public HttpResponseData() {
        code = -1;
        requestID = -1;
        data = null;
    }

    public HttpResponseData(int _code, int _requestID, byte[] _data) {
        code = _code;
        requestID = _requestID;
        data = _data;
    }
}
