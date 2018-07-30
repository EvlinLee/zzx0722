package com.eryue.util;

/**
 * @author hwyu
 * @date 2010.10.21 14:23
 */

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.library.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Assist {

    public static Object addKeyAndValue(Hashtable map, Object key, Object value) {
        return map.put(key, value);
    }

    public static Object removeByKey(Hashtable map, Object key) {
        return map.remove(key);
    }

    public static String GetGuid() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    public static void addKeyAndValue(Hashtable map, Object key, int value) {
        map.put(key, value);
    }

    public static void addKeyAndValue(Hashtable map, int key, int value) {
        map.put(key, value);
    }

    public static Object addKeyAndValue(Hashtable map, int key, Object value) {
        return map.put(key, value);
    }

    public static Object getValueByKey(Hashtable map, Object key) {
        return map.get(key);
    }

    public static Object getValueByKey(Hashtable map, int key) {
        return map.get(key);
    }

    public static boolean containsKey(Hashtable map, Object key) {
        return map.containsKey(key);
    }

    public static boolean add(Vector targetVector, Object value) {
        return targetVector.add(value);
    }

    public static boolean contains(Vector targetVector, Object value) {
        return targetVector.contains(value);
    }

    public static void putAllToHashtable(Hashtable map, Hashtable target) {
        map.putAll(target);
    }


    public static void removeHashtableByValue(Hashtable map, Object value) {
        if (null == map || null == value)
            return;
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            if (value.equals(it.next())) {
                it.remove();
            }
        }
    }

    public static boolean addAll(Vector targetVector, Vector sourceVector) {
        if (targetVector == null || sourceVector == null) {
            return false;
        }
        return targetVector.addAll(sourceVector);
    }

    /**
     * �б����������Ƿ���ͬһ��, 20110101
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeek(int date1, int date2) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date(date1 / 10000, date1 % 10000 / 100, date1 % 100));
        int dayOfWeek1 = ca.get(Calendar.DAY_OF_WEEK);
        long millis1 = ca.getTimeInMillis();

        ca.setTime(new Date(date2 / 10000, date2 % 10000 / 100, date2 % 100));
        long millis2 = ca.getTimeInMillis();

        long offset1 = (dayOfWeek1) * 24 * 3600 * 1000;
        long offset2 = (7 - dayOfWeek1) * 24 * 3600 * 1000;

        long offset = Math.abs(millis2 - millis1);

        return offset <= offset1 || offset <= offset2;
    }

    public static String getCurDate(String dateFormat) {
        //"yyyyMMdd HHmmss"
//		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
//		TimeZone.setDefault(tz);
        SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return df.format(Calendar.getInstance().getTime());

    }

    /**
     * Author:xsong
     * String
     *
     * @param time       ʱ���ַ�
     * @param srcFormat  Դʱ���ʽ
     * @param destFormat Ŀ��ʱ���ʽ
     * @return ��ʽ�����ʱ���ַ�
     */
    public static String getDateByFormat(String time, String srcFormat, String destFormat) {
        //空指针判断
        if (TextUtils.isEmpty(time) || TextUtils.isEmpty(srcFormat) || TextUtils.isEmpty(destFormat)) {
            return time;
        }
        if (time.length() < srcFormat.length()) {
            return time;
        }
        SimpleDateFormat df = new SimpleDateFormat(srcFormat, Locale.getDefault());
        try {
            Date date = df.parse(time);
            SimpleDateFormat stringformat = new SimpleDateFormat(destFormat, Locale.getDefault());
            return stringformat.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return time;
        }
    }

    /**
     * Author:xsong
     * String
     *
     * @param time       ʱ���ַ�
     * @param srcFormat  Դʱ���ʽ
     * @param destFormat Ŀ��ʱ���ʽ
     * @return ��ʽ�����ʱ���ַ�
     */
    public static String getDateByFormat2(String time, String srcFormat, String destFormat) {
        if (null == time) {
            return time;
        }
        SimpleDateFormat df = new SimpleDateFormat(srcFormat, Locale.getDefault());
        try {
            Date date = df.parse(time);
            SimpleDateFormat stringformat = new SimpleDateFormat(destFormat, Locale.getDefault());
            return stringformat.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return time;
        }
    }

    public static String getDateByFormatNews(String time, String srcFormat, String destFormat) {
        if (null == time || time.length() < 10) {
            return time;
        }
        SimpleDateFormat df = new SimpleDateFormat(srcFormat, Locale.getDefault());
        try {
            Date date = df.parse(time);
            SimpleDateFormat stringformat = new SimpleDateFormat(destFormat, Locale.getDefault());
            return stringformat.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return time;
        }
    }

    public static String replace(String src, String target, String replace) {
        if (src == null || target == null || replace == null) {
            return src;
        }

        return src.replace(target, replace);
    }

    public static String replace(String src, char target, char replace) {
        if (src == null) {
            return src;
        }
        return src.replace(target, replace);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        if ((str1 == null) && (str2 == null)) {
            return true;
        } else if ((str1 == null && str2 != null) || (str2 == null && str1 != null)) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    public static String string(byte[] data, String encoding) throws UnsupportedEncodingException {
        return new String(data, encoding);
    }

    public static String[] Split(String srcString, String regex) {
        if (srcString != null)
            return srcString.split(regex);
        return null;
    }

    public static String ReplaceStringByPattern(String pattern, String originalString, String replaceString) {
        try {
            Pattern localPattern = Pattern.compile(pattern);

            replaceString = replaceString.replaceAll("\\\\", "\\\\\\\\");
            Matcher localMatcher = localPattern.matcher(originalString);
            if (localMatcher.find())

            {

                originalString = localMatcher.replaceAll(replaceString);

            }
            return originalString;

        } catch (Exception ex)

        {

        }

        return null;

    }

    public static String GetAbsolutePath() {
        return "file:///android_asset/drawable";
    }


    private static String documentSavePath;
    private static String customfaceSavePath;
    private static String document_path = "windPad/download/document/";
    private static String customface_path = "windPad/download/customface/";

//	public static String GetRecvFilePath(int transferFileType){
//		if(transferFileType == 1)
//			return "file:///sdcard/"+document_path;
//		else
//			return "file:///sdcard/"+customface_path;   
//	}

//	public static String GetRecvFileSavePath(int transferFileType) {
//		if(transferFileType == 1){
//			if (documentSavePath == null)
//				createRecvFileSDPath(1);
//			return documentSavePath;
//		}else{
//			if (customfaceSavePath == null)
//				createRecvFileSDPath(2);
//			return customfaceSavePath;
//		}
//	}
//	
//	private static void createRecvFileSDPath(int transferFileType) {
//		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//			File sdcardDir = Environment.getExternalStorageDirectory();
//			String path = sdcardDir.getParent() + File.separator + sdcardDir.getName();
//			if(transferFileType == 1){
//				documentSavePath = path + File.separator + document_path;
//				File file = new File(documentSavePath);
//				if (!file.exists()) {
//					file.mkdirs();
//				}
//			}else{
//				customfaceSavePath = path + File.separator + customface_path;
//				File file = new File(customface_path);
//				if (!file.exists()) {
//					file.mkdirs();
//				}
//			}
//		}
//	}

    public static String GetPlatform() {

        return "Android";

    }

    public static String GetPatternString(String pattern, String originalString) {
        try {
            Pattern localPattern = Pattern.compile(pattern);
            Matcher localMatcher = localPattern.matcher(originalString);
            if (localMatcher.find()) {
                return localMatcher.group();
            } else {
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static Vector GetPatternStringList(String pattern, String originalString) {
        Vector stringList = new Vector();
        try {
            Pattern localPattern = Pattern.compile(pattern);
            Matcher localMatcher = localPattern.matcher(originalString);
            while (localMatcher.find()) {
                stringList.add(localMatcher.group());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return stringList;
    }


    public static String string(byte[] data, int off, int len, String encoding) throws UnsupportedEncodingException {
        return new String(data, off, len, encoding);
    }

    public static byte[] bytes(String str, String encoding) throws UnsupportedEncodingException {
        return str.getBytes(encoding);
    }

    public static double longBitsToDouble(long bits) {
        return Double.longBitsToDouble(bits);
    }

    public static float intBitsToFloat(int bits) {
        return Float.intBitsToFloat(bits);
    }

    public static String DoubleToString(String data) {
        BigDecimal bd = new BigDecimal(data);
        return bd.toPlainString();
    }

    public static int floatToIntBits(float value) {
        return Float.floatToIntBits(value);
    }

    public static long doubleToLongBits(double value) {
        return Double.doubleToLongBits(value);
    }

	/*//�漰���͵��ຯ����Ҫָ��׼ȷ���õĺ���ԭ��
    public static void insertElementAt(Vector v, Object obj, int index){
		v.insertElementAt(obj, index);
	}*/

    public static int lastIndexOf(Vector v, Object object) {
        return v.lastIndexOf(object);
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static void sleep(int milliseconds) throws InterruptedException {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        }
    }

    public static void wait(IThread obj, long milliseconds) throws InterruptedException {
        try {
            obj.wait(milliseconds);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        }
    }

    public static void wait(IThread obj) throws InterruptedException {
        try {
            obj.wait();
        } catch (InterruptedException e) {
            throw new InterruptedException();
        }
    }

    public static void notifyAll(IThread obj) {
        obj.notifyAll();
    }

    public static void notify(IThread obj) {
        obj.notify();
    }

    /**
     * ��ȡ����mac��ַ
     * <p/>
     * ��Ҫ��AndroidManifest.xml����ӣ�
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @param activity
     * @return
     */
    public static String getLocalMacAddress(Context activity) {
        WifiManager wifi = (WifiManager) activity
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * ��ȡ����ip��ַ
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (java.util.Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (java.util.Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("WifiPreference IpAddress: " + ex.toString());
        }
        return null;
    }

    /**
     * ��ȡ����IMEI��
     * <p/>
     * ��Ҫ��AndroidManifest.xml����ӣ�
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     *
     * @return
     */
//	public static String getIMEI(Activity activity) {
//		String str = "";
//		try {
//			str = ((TelephonyManager) activity
//					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println("getIMEI(),ERROR");
//			e.printStackTrace();
//		}
//		return str;
//	}
    public static String getRandomString(int length) {
        StringBuilder _str = new StringBuilder();
        for (int i = 0; i < length; i++)
            _str.append((int) (Math.random() * 10));
        return _str.toString();
    }

    /**
     * ���ֶ�
     *
     * @param str1
     * @param str2
     * @return
     */

    public static boolean StringContains(String str1, String str2) {
        return str1.contains(str2);
    }

    /**
     * ��double���ͱ���ָ����С���λ���ת��ΪString���ͷ���
     *
     * @param num
     * @return
     */
    public static String DecimalFormat(double value, int num) {
        StringBuffer formatString = null;
        DecimalFormat df;
        if (num == 0)
            formatString = new StringBuffer("0");
        else if (num == -1) {
            formatString = new StringBuffer("");
        } else
            formatString = new StringBuffer("0.");
        for (int i = 0; i < num; i++) {
            formatString.append('0');
        }
        df = new DecimalFormat(formatString.toString());
        return df.format(value);
    }

    /**
     * ��ȡHashtable��Key���б�(ת��c#�޷�����)
     *
     * @param table
     * @return
     */
    public static Object[] KeyArray(Hashtable table) {
        return table == null ? null : table.keySet().toArray();
    }

    private static final long TICKS_AT_EPOCH = 621355968000000000l; //1970���ticks
    private static final long TICKS_PER_MILLISECOND = 10000;

    /**
     * ��ȡϵͳ��ǰ����yyyyMMdd
     *
     * @return
     */
    public static String getCurrentDate() {
        return Assist.getCurDate("yyyy-MM-dd");
    }

    /**
     * ��ȡϵͳ��ǰʱ��,ʱ���ʽHH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        return Assist.getCurDate("HHmmss");
    }

    /**
     * ��ȡϵͳ��ǰʱ��,ʱ���ʽyyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return Assist.getCurDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * ��������֮���������أ�endDate��beginDate������
     *
     * @param beginDate ��ʼ����
     * @param endDate   ��������
     * @return�����������ʼ��������������
     */
    public static int CalculateDays(String beginDate, String endDate) {
        if (beginDate.length() > 10 && endDate.length() > 10) {
            beginDate = beginDate.substring(0, 10);
            endDate = endDate.substring(0, 10);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar d1 = new GregorianCalendar();
        Calendar d2 = new GregorianCalendar();

        long days = 0;
        try {
            Date bDate = format.parse(beginDate);
            Date eDate = format.parse(endDate);
            days = eDate.getTime() - bDate.getTime();
            days = days / 1000 / 60 / 60 / 24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) days;
    }

    /**
     * ��������ں͵�ǰ���ڱȽ�
     *
     * @param date Ҫ�뵱ǰ���ڽ��бȽϵ�����
     * @return ��ǰ���ڱȸ����ڶ����������
     */
    public static int ComToday(String date) {
        return CalculateDays(getCurrentDate(), date);
    }

    /**
     * ���ո�ʽ��ȡ����
     *
     * @param formate
     * @return �����ַ�
     */
    public static String getDateFormate(String formate, String date) {
        String dateStrs[] = date.split("/");
        if ("yyyy/mm".equalsIgnoreCase(formate) && dateStrs.length > 1) {
            String month = dateStrs[1].length() == 2 ? dateStrs[1] : "0" + dateStrs[1];
            return dateStrs[0] + "/" + month;
        }
        return date;
    }

    /**
     * �Ƚϻ�ȡ�죬������ͳ�·��򷵻��죬���·�����ʾ��MM/D��
     *
     * @return �� �� ��/��
     */
    public static String getDayOfMonth(String lastDate, String date) {
        String lastDateStrs[] = lastDate.split("/");
        String dateStrs[] = date.split("/");
        if (lastDateStrs.length > 2 && dateStrs.length > 2) {
            String lastMonth = lastDateStrs[1].length() == 2 ? lastDateStrs[1] : "0" + lastDateStrs[1];
            String thisMonth = dateStrs[1].length() == 2 ? dateStrs[1] : "0" + dateStrs[1];
            if (lastMonth != null && lastMonth.equals(thisMonth)) {
                return dateStrs[2];
            } else {
                return thisMonth + "/" + dateStrs[2];
            }
        }
        return date;
    }

    /**
     * ����1899-12-30���days������
     *
     * @param _days
     * @return
     */
    public static String daysIntToString(int _days) {
        //1899-12-30 ��2000-1-1��36526 ��
        int space = 36526;
        int days = _days - space;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        //2000-1-1
        Date datefrom = new Date(100, 0, 1);
        //today
        Date dateto = new Date();

        long d = dateto.getTime() - datefrom.getTime();
        long count = d / (long) (1000 * 60 * 60 * 24);

        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, days - (int) count);
        Date myDate = ca.getTime();
        String myString = format.format(myDate);
        if (myString.substring(0, myString.indexOf("-")).equals("2079")) {
            //System.out.println("_days="+_days);
            //System.out.println("Integer.MAX_VALUE="+Integer.MAX_VALUE);
        }

        return myString;
    }

    /**
     * �����������ڵ������Ǽ���
     *
     * @param year
     * @param mon
     * @param day
     * @return
     */
    public static int GetTomorrowDate(int year, int mon, int day) {

        int[] mons = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        Calendar calendar = Calendar.getInstance();
        //�ж�����  
        boolean mIsLeapYear = ((GregorianCalendar) calendar).isLeapYear(year);

        if (mIsLeapYear && mon == 1 && day == 28) {
            return 29;
        }
        if (mIsLeapYear && mon == 1 && day == 29) {
            return 1;
        } else if (day == mons[mon]) {
            return 1;
        } else {
            return day + 1;
        }
    }

    public static long CalTick(int y, int m, int d) {
        Date buydate = new Date(y - 1900, m - 1, d, 12, 0, 0);
        long tick = buydate.getTime();

        return TICKS_PER_MILLISECOND * tick + TICKS_AT_EPOCH;
    }

    public static String TicksToDate(long ticks) {
        long tic = (ticks - TICKS_AT_EPOCH) / TICKS_PER_MILLISECOND;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date myDate = new Date(tic);
        String myString = format.format(myDate);

        return myString;
    }

    public static int GetBytesLen(String text, String Encode) {
        int len = 0;
        try {
            len = text.getBytes(Encode).length;
        } catch (UnsupportedEncodingException e) {
        }

        return len;
    }

    public static boolean IsMatch(String str, String regex) // ƥ��������ʽ
    {
        return str.matches(regex);
    }

    public static String ChangeBytesToGBK(byte[] bytes, String encoding) throws UnsupportedEncodingException {
        return Assist.string(bytes, encoding);
    }

    public static boolean contrastDate(String strData, String strCorrent) {
        int data = StringUtils.valueOfInteger(strData, 0);
        int corrent = StringUtils.valueOfInteger(strCorrent, 0);
        return (corrent - data < 0);
    }


    /**
     *
     * @param bankName
     * @return
     */
    public static char GetShortName(String bankName) {
        if (bankName == null || bankName.length() == 0) return ' ';

        String shortName = String2Alpha(bankName);
        return shortName.charAt(0);
    }

    /**
     * ������ĸ�����������������顿
     */
    private static final char[] alphatable = {'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    /**
     * ����Χ�����������������顿
     */
    private static final int[] table = new int[]{45217, 45253, 45761, 46318,
            46826, 47010, 47297, 47614, 47614, 48119, 49062, 49324, 49896,
            50371, 50614, 50622, 50906, 51387, 51446, 52218, 52218, 52218,
            52698, 52980, 53689, 54481, 55289};

    /**
     * ������, �����ַ�, �õ������ĸ, Ӣ����ĸ���ض�Ӧ�Ĵ�д��ĸ ����Ǽ��庺�ַ��� '*'��������������
     */
    public static char Char2Alpha(char ch) {
        if (ch >= 'a' && ch <= 'z')
            return (char) (ch - 'a' + 'A');
        if (ch >= 'A' && ch <= 'Z')
            return ch;
        int gb = gbValue(ch);
        if (gb < table[0])
            return '*';
        for (int i = 0; i < 26; ++i) {
            if (match(i, gb)) {
                if (i >= 26)
                    return '*';
                else
                    return alphatable[i];
            }
        }
        return '*';
    }

    /**
     * ���һ�����ֵ��ַ���һ������ƴ������ĸ���ַ�������������
     */
    public static String String2Alpha(String str) {
        String Result = "";
        try {
            for (int i = 0; i < str.length(); i++) {
                Result += Char2Alpha(str.charAt(i));
            }
        } catch (Exception e) {
            Result = " ";
        }
        return Result;
    }

    /**
     * ��������������
     */
    private static boolean match(int i, int gb) {
        if (gb < table[i])
            return false;
        int j = i + 1;
        // 	��ĸZʹ����������ǩ
        while (j < 26 && (table[j] == table[i]))
            ++j;
        if (j == 26)
            return gb <= table[j];
        else
            return gb < table[j];
    }

    /**
     * ȡ�����뺺�ֵı��롾������������
     */
    private static int gbValue(char ch) {
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < 2)
                return 0;
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return '*';
        }
    }

    /**
     * 保留unit位小数
     *
     * @param f
     * @param unit
     * @return
     */
    public static String formatPrice(String f, int unit) {
        float t = StringUtils.valueOfFloat(f, 0);
        if (t == 0) {
            return f;
        }
        return String.format("%." + unit + "f", t);
    }

    /**
     * 保留unit位小数
     *
     * @param f
     * @param unit
     * @return
     */
    public static String formatPrice(float f, int unit) {
        String result = String.format("%." + unit + "f", f);
        return result;
    }



}
