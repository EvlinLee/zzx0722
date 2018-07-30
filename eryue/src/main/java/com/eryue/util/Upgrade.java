package com.eryue.util;

import com.library.util.CommonFunc;

import net.NetManager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Upgrade implements IHttpDataReceiver {

    private static int UPDATE_REQUEST = 91000024;
    private static String OSTYPE = "Android";//CommonFunc.filterString(System.getProperty("microedition.platform")," ");
    private IHttpDataListener receiver = null;
    private static int REQUEST_ID = HttpCommRequestId.UPGRADE_REQ_ID;
    private HttpCallbackData responseData;

    private Upgrade(IHttpDataListener _receiver) {
        receiver = _receiver;

        responseData = new HttpCallbackData();
        responseData.data = new ArrayList();
        responseData.requestID = -1;
    }

    private void ClearResponse() {
        responseData.isSucess = false;
        responseData.requestID = -1;
        if (responseData.data.size() > 0)
            responseData.data.clear();
    }

    public static int UpgradeRequest(String clientType, String SoftType, String Version, String sid, IHttpDataListener _receiver) {


        if (null == _receiver || null == clientType || null == SoftType || null == Version)
            return -1;
        String url = String.format("", UPDATE_REQUEST, OSTYPE, clientType, SoftType, Version);
        if (sid == null) {
            String str = new String(CommonFunc.readFromAssets(null,"sid"));
            if (str != null && !str.equals("")) {
                sid = str.substring(str.indexOf("#") + 1, str.indexOf("\r"));
            }
        }

        if (sid != null) {
            url += ("&sid=" + sid);
        }

        HttpRequestEvent event = new HttpRequestEvent(url, null, REQUEST_ID, 0, 0, new Upgrade(_receiver));
        HttpProcessor.getInstance().addEvent(event);
        return REQUEST_ID;
    }

    public static int UpgradeRequest(String clientType, String SoftType, String Version, IHttpDataListener _receiver) {
        return UpgradeRequest(clientType, SoftType, Version, null, _receiver);
    }

    public static int UpgradeRequest(String url, IHttpDataListener _receiver) {

        if (null == url || url.equals("") || null == _receiver)
            return -1;

        HttpRequestEvent event = new HttpRequestEvent(url, null, REQUEST_ID, 0, 0, new Upgrade(_receiver));
        HttpProcessor.getInstance().addEvent(event);

        return REQUEST_ID;
    }

    public void onHttpDataReceived(HttpResponseData data) {
        ClearResponse();

        if (data != null) {
            if (data.code == HttpConnection.HTTP_OK)
                responseData.isSucess = true;
            else
                responseData.isSucess = false;
            responseData.requestID = data.requestID;

            if (data.code != HttpConnection.HTTP_OK) {
                if (receiver != null)
                    receiver.OnHttpCallback(responseData);
                return;
            }

            UpgradeResData upgradedata = new UpgradeResData();
            DataInputStream reader = new DataInputStream(new ByteArrayInputStream(data.data));
            AdvanceReader reader2 = new AdvanceReader(reader);
            try {
                upgradedata.flag = reader2.readByte();
                upgradedata.serverTime = reader2.readString(reader2.readByte()); //��ȡ������ʱ��
                if (upgradedata.flag != 0) {
                    String url = reader2.readString();
                    if (null != url && url.indexOf(WindHttpClient_.HTTP_STR) == -1) {
                        url = WindHttpClient_.HTTP_PRE + url;
                    }
                    upgradedata.url = url;
                    upgradedata.info = reader2.readString();
                }
                responseData.data.add(upgradedata);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (receiver != null)
            receiver.OnHttpCallback(responseData);
    }


    //�ر�l��
    public static void closeConnection(int requestID) {
        try {
            HttpProcessor.getInstance().closeConnection(requestID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}