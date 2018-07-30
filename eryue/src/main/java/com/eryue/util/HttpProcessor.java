package com.eryue.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;


public class HttpProcessor implements Runnable, IHttpProcessorListener, IThread {
    private static HttpProcessor _httpProcessor;
    private final int MAX_CONCURRENT_COUNT = 5;
    private Vector _eventQueue;
    private int _maxConcurrentCount = MAX_CONCURRENT_COUNT;
    ;
    private int _processingCount = 0;
    private boolean _cancel = false;
    private boolean _paused = false;
    //	private ThreadPool _threadPool;
    private Threading _pasuedObj = new Threading();
    private Threading _lockObj = new Threading();
    private Threading _lockEvent = new Threading();
    private Threading _lockReq = new Threading();

    public HttpProcessor() {
        _eventQueue = new Vector();
//		_threadPool = ThreadPool.instance();

        start();
    }

    public static HttpProcessor getInstance() {
        if (_httpProcessor == null) {
            _httpProcessor = new HttpProcessor();
        }
        return _httpProcessor;
    }

    public void setMaxCount(int count) {
        if (count < 1)
            _maxConcurrentCount = 1;
        else
            _maxConcurrentCount = count;
    }

    public void addEvent(HttpRequestEvent evt) {
        synchronized (_lockEvent) {
            if (!_eventQueue.contains(evt))
                _eventQueue.addElement(evt);
        }
        synchronized (_lockObj) {
            _lockObj.notify();
        }
    }

    private void removeEvent(HttpRequestEvent evt) {
        synchronized (_lockEvent) {
            _eventQueue.remove(evt);
        }
    }

    public void removeReceiver(IHttpDataReceiver reveiver) {
        synchronized (_lockEvent) {
            for (int i = _eventQueue.size() - 1; i >= 0; i--) {
                HttpRequestEvent e = (HttpRequestEvent) _eventQueue.elementAt(i);
                if (e.receiver == reveiver) {
                    _eventQueue.remove(i);
                }
            }
        }
    }

    private HttpRequestEvent getEvent(String url) {
        synchronized (_lockEvent) {
            for (int i = _eventQueue.size() - 1; i >= 0; i--) {
                HttpRequestEvent e = (HttpRequestEvent) _eventQueue.elementAt(i);
                if (null != (e.url) && e.url.equals(url)) {
                    return e;
                }
            }
            return null;
        }
    }

    private HttpRequestEvent getFirstEvent() {
        synchronized (_lockEvent) {
            for (int i = _eventQueue.size() - 1; i >= 0; i--) {
                HttpRequestEvent e = (HttpRequestEvent) _eventQueue.elementAt(i);
                if (!e.getRequested()) {
                    return e;
                }
            }
            return null;
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public void cancel() {
        _cancel = true;
        _eventQueue.removeAllElements();
//		_threadPool.shutdown();
        synchronized (_lockObj) {
            Assist.notify(_lockObj);
        }
        synchronized (_pasuedObj) {
            Assist.notify(_pasuedObj);
        }
    }

    public void setPause(boolean paused) {
        synchronized (_pasuedObj) {
            _paused = paused;
            Assist.notify(_pasuedObj);
        }
    }

    //ͬ��http����
    public HttpResponseData synHttpRequest(HttpRequestEvent data) {
        if (null != data)
            data.receiver = null;
        HttpRequest httpRequest = new HttpRequest(data);
        //ApacheHttp httpRequest = new ApacheHttp(data);

        httpRequest.startRequest();

        return httpRequest.getHttpResponseData();
    }

    //�쳣http����
    public void asyHttpRequest(HttpRequestEvent data) {
        data.setStandard(true);
        addEvent(data);
    }

    public void OnHttpProcessorReceived(HttpRequestEvent data) {
        synchronized (_lockObj) {
            _processingCount--;
            Assist.notify(_lockObj);
        }

        try {
            HttpRequestEvent event = getEvent(data.url);
            if (event != null) {
                removeEvent(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while (!_cancel) {
            synchronized (_pasuedObj) {
                if (_paused) {
                    try {
                        Assist.wait(_pasuedObj);
                    } catch (InterruptedException e) {
                    }
                }
            }
            synchronized (_lockObj) {
                HttpRequestEvent event;
                if (_processingCount < _maxConcurrentCount && (event = getFirstEvent()) != null) {
                    event.setRequested(true);

                    HttpRequest httpRequest = new HttpRequest(event, this);
                    //ApacheHttp httpRequest = new ApacheHttp(event,this);
                    //��������httpͨ��
                    HttpRequestData data = new HttpRequestData(httpRequest, event, event.requestID);
                    synchronized (_lockReq) {
                        reqManager.add(data);
                    }

                    //�����߳��첽ִ�У�֧�ֶ�����󲢷�
                    try {
//						_threadPool.instance().start(httpRequest);
                        new Thread(httpRequest).start();
                    } catch (Exception e) {
                    }
                    _processingCount++;

                } else {
                    try {
                        Assist.wait(_lockObj);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }
    }

    ArrayList reqManager = new ArrayList();

    public void closeConnection(int requestID) throws IOException {
        synchronized (_lockEvent) {
            for (int i = 0; i < _eventQueue.size(); i++) {
                if (((HttpRequestEvent) _eventQueue.elementAt(i)).requestID == requestID) {
                    _eventQueue.remove(i);
                }
            }
        }
        if (reqManager.size() == 0)
            return;
        synchronized (_lockReq) {
            for (int i = 0; i < reqManager.size(); i++) {
                if (((HttpRequestData) reqManager.get(i)).getRequestID() == requestID) {
                    if (((HttpRequestData) reqManager.get(i)).getHttp() != null)
                        ((HttpRequestData) reqManager.get(i)).getHttp().closeConnection();
                    if (reqManager.size() != 0)
                        reqManager.remove(i);
                }
            }
        }

    }

}


class HttpRequestData {
    private HttpRequest http;
    private HttpRequestEvent event;
    private int requestID;

    public HttpRequestData(HttpRequest _http, HttpRequestEvent _event, int _requestID) {
        http = _http;
        event = _event;
        requestID = _requestID;
    }

    public int getRequestID() {
        return requestID;
    }

    public HttpRequest getHttp() {
        return http;
    }

    public HttpRequestEvent getEvent() {
        return event;
    }
}
