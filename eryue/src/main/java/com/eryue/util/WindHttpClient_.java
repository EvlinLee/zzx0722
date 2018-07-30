package com.eryue.util;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.security.KeyStore;

public class WindHttpClient_ extends DefaultHttpClient {

    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 800;
    /**
     * 获取连接的最大等待时间
     */
    public final static int WAIT_TIMEOUT = 10000;
    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 400;
    /**
     * 连接超时时间
     */
    public final static int CONNECT_TIMEOUT = 6000;
    /**
     * 读取超时时间
     */
    public final static int SO_TIMEOUT = 10000;
    /**
     * http请求前缀
     */
    public final static String HTTP_STR = "http";
    /**
     * https请求前缀
     */
    public final static String HTTPS_STR = "https";

    /**
     * http请求前缀
     */
    public final static String HTTP_PRE = "http://";
    /**
     * https请求前缀
     */
    public final static String HTTPS_PRE = "https://";

    /**
     * https标准端口
     */
    public final static String HTTPS_PORT_STR = "443";

    /**
     * http标准端口
     */
    public final static String HTTP_PORT_STR = "80";


    private static WindHttpClient_ client;

    public static synchronized WindHttpClient_ getInstance() {
        if (client == null) {
            client = new WindHttpClient_();
        }
        return client;
    }

    private WindHttpClient_() {
        getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
        getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
    }

    public void setHttpTimeOut(int connectionTimeOut, int soTimeOut) {
        getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeOut);
        getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeOut);
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        KeyStore trustStore;
        SSLSocketFactory sf = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            sf = new SSLSocketFactoryEx(trustStore);
            // 允许所有主机的验证
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            e.printStackTrace();
            sf = null;
        }
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        registry.register(new Scheme("https", sf == null ? SSLSocketFactory
                .getSocketFactory() : sf, 443));
        HttpParams params = getParams();
        // 设置最大连接数
        ConnManagerParams.setMaxTotalConnections(params, MAX_TOTAL_CONNECTIONS);
        // 设置获取连接的最大等待时间
        ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);
        // 设置每个路由最大连接数
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(
                MAX_ROUTE_CONNECTIONS);
        ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
        // 设置连接超时时间
        HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
        // 设置读取超时时间
        HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
        return new ThreadSafeClientConnManager(params, registry);
    }

    /**
     * 没有http或者https前缀的url增加前缀
     *
     * @param url
     * @return
     */
    public static String changeUrlToHttp(String url) {
        if (null == url)
            return null;
        String url_l = url.toLowerCase();
        StringBuilder sb = new StringBuilder();
        if (!url_l.startsWith(HTTP_PRE) && !url_l.startsWith(HTTPS_PRE)) {
            //没有http或者https前缀
            if (url_l.contains(":" + HTTPS_PORT_STR + "/")) {
                //含有"：443/"端口
                sb.append(HTTPS_PRE);
            } else {
                sb.append(HTTP_PRE);
            }
            sb.append(url);
            //sky端口处已能保证443端口的https转化
//		}else if (url_l.contains(":" + HTTPS_PORT_STR + "/") && url_l.startsWith(HTTP_PRE)) {	
//			//有http://前缀，并且含有"：443/"字段的转化成https请求  
//			sb.append(HTTPS_STR);
//			sb.append(url.substring(HTTP_STR.length()));
//			
//			
//			sb.append(url);
        } else {
            return url;
        }
        return sb.toString();
    }


    public static boolean isHttpUrl(String url) {
        if (null == url)
            return false;
        String url_l = url.toLowerCase();
        if ((url_l.startsWith(WindHttpClient_.HTTP_PRE) || url_l
                .startsWith(WindHttpClient_.HTTPS_PRE))) {
            return true;
        }
        return false;
    }
}
