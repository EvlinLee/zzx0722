package com.eryue.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class HttpRequest implements Runnable {
    private HttpRequestEvent _requestData;
    private HttpResponseData _responseData;
    //private HttpConnectionImpl _httpCon;
    private WindHttpClient_ _httpClient;
    private IHttpProcessorListener _receive;

    private boolean exitHttp = false;

    //�ر�l��
    public void closeConnection() throws IOException {
        exitHttp = true;
//		if(_httpCon!=null)
//			_httpCon.close();
//		onHttpDataReceived();
        _requestData = null;
        _receive = null;
    }

    public HttpRequest(HttpRequestEvent data) {
        _requestData = data;

        _responseData = new HttpResponseData();
        _responseData.code = -1;
        _responseData.requestID = _requestData.requestID;
        _responseData.data = null;

    }

    public HttpRequest(HttpRequestEvent data, IHttpProcessorListener receive) {
        _requestData = data;
        _receive = receive;

        _responseData = new HttpResponseData();
        _responseData.code = -1;
        _responseData.requestID = _requestData.requestID;
        _responseData.data = null;
    }

    public HttpResponseData getHttpResponseData() {
        return _responseData;
    }

    private void DealData(InputStream _is, long length) {
        if (null == _is)
            return;
        try {
            // Read data

            //�˳�l��
            if (exitHttp) {
                if (_is != null)
                    _is.close();
                return;
            }

            if (_requestData.postData != null || _requestData.getStandard()) {
                byte[] buf = new byte[1024];
                ByteArrayOutputStream mstrem = new ByteArrayOutputStream();
                int len;
                while ((len = _is.read(buf, 0, buf.length)) > 0) {

                    if (exitHttp) {
                        if (_is != null)
                            _is.close();
                        return;
                    }
                    mstrem.write(buf, 0, len);
                }
                _responseData.data = mstrem.toByteArray();
                mstrem.close();
                mstrem = null;

                _is.close();
                _is = null;
                return;
            } else {
                long dataLen = length;
                if (dataLen == 2) {
                    _responseData.data = null;
//					onHttpDataReceived();
                } else {
                    byte[] responseData = null;
                    if (dataLen > 1) {
                        responseData = new byte[(int) dataLen - 2];
                    } else
                        responseData = new byte[1];
                    int dataReaded = 0;
                    int totalReaded = 0;

                    dataReaded = _is.read(responseData, 0, 1);
                    if (dataReaded == 1) {
                        while (responseData[0] == 0) {

                            if (exitHttp) {
                                if (_is != null)
                                    _is.close();
                                return;
                            }

                            dataReaded = _is.read(responseData, 0, 1);
                            Assist.sleep(100);
                        }
                        if (responseData[0] == 126) {// �ɹ�
                            dataReaded = 0;
                            dataReaded = _is.read(responseData, 0, 1);
                            boolean bNeedGzip = false;
                            if (responseData[0] == 1)
                                bNeedGzip = true;
                            do {
                                dataReaded = _is.read(responseData, totalReaded,
                                        (responseData.length - totalReaded) < 1024 ? (responseData.length - totalReaded) : 1024);
                                totalReaded += dataReaded;
                            } while (dataReaded > 0 && totalReaded < dataLen - 2);
                            if (bNeedGzip)
                                responseData = GzipOperate.inflate(responseData);

                            _responseData.data = responseData;
//								onHttpDataReceived();
                        } else {
                            _responseData.data = null;

                        }
                    }
                }
                _is.close();
                _is = null;
            }
        } catch (Exception e) {
            if (_is != null) {
                try {
                    _is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
            _responseData.data = null;
        }
    }

    public void startRequest() {
        try {

            for (; _requestData.retry >= 0; _requestData.retry--) {
                // �˳�l��
                if (exitHttp) {
                    // if(_httpCon!=null)
                    // _httpCon.close();
                    return;
                }
                _responseData.data = null;
                HttpUriRequest httpRequest = null;
                HttpResponse httpResponse = null;
                if (_requestData.postData == null) {
                    httpRequest = new HttpGet(_requestData.url);// 建立http
                    // get联机
                    httpRequest.addHeader("Content-Length", String.valueOf(0));
                    httpResponse = _httpClient.execute(httpRequest);
                } else {
                    httpRequest = new HttpPost(_requestData.url); // 建立HTTP
                    // POST联机
                    httpRequest.addHeader("Content-Length",
                            String.valueOf(0));
                    if (null != _requestData.postData) {
                        UrlEncodedFormEntity form = new UrlEncodedFormEntity(
                                _requestData.postData);
                        ((HttpPost) httpRequest).setEntity(form);
                    }
                    httpResponse = _httpClient.execute(httpRequest);
                }
                _responseData.code = httpResponse.getStatusLine()
                        .getStatusCode();
                if (_responseData.code == HttpConnection.HTTP_OK) {
                    InputStream is = httpResponse.getEntity().getContent();
                    long length = httpResponse.getEntity().getContentLength();
                    DealData(is, length);
                    // �˳�l��
                    if (exitHttp) {
                        // if(_httpCon!=null)
                        // _httpCon.close();
                        return;
                    }
                    // _httpCon.close();
                    break;
                }
                // _httpCon.close();
            }
            onHttpDataReceived();
        } catch (Exception e) {
            e.printStackTrace();
            _responseData.data = null;
            onHttpDataReceived();
        }
    }

    private void onHttpDataReceived() {

        try {
            if (null != _requestData.receiver) {
                _requestData.receiver.onHttpDataReceived(_responseData);
            }

            if (null != _receive) {
                _receive.OnHttpProcessorReceived(_requestData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        if (null == _requestData || null == _requestData.url)
            return;
        _httpClient = WindHttpClient_.getInstance();
//		if(null != _requestData)
//			_httpClient.setHttpTimeOut(_requestData.timeout, _requestData.timeout);
        startRequest();
    }

}
