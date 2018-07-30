package com.library.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 *
 * @author wchen.wa
 * @version 1.0.0 2009/11/10
 */
public class CommonFunc {

    public static boolean isDigitalCash(String windcode) {
        if (!TextUtils.isEmpty(windcode) && windcode.toLowerCase().endsWith(".dc")) {
            return true;
        }
        return false;
    }
    public static float roundNumber(double number, int index) {
        BigDecimal b = new BigDecimal(number);
        return b.setScale(index, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 把数组用逗号分隔并保留指定小数点位数(四舍五入):1000.125 --> 1,000.13
     *
     * @return
     */
    public static String fixDoubleAndRound(double number, int count) {
        String patter = "###,##0.";
        if (count == 0) {
            patter = "###,##0";
        }
        for (int i = 0; i < count; i++) {
            patter += "0";
        }
        return new DecimalFormat(patter).format(number);
    }


    /**
     * 把数组用逗号分隔并保留指定小数点位数(四舍五入):1000.125 --> 1,000.13
     *
     * @return
     */
    public static String fixFormatStr(String numberStr, int count) {
        double value = StringUtils.valueOfDouble(numberStr, 0);
        if (value == 0) {
            return numberStr;
        }
        return fixDoubleAndRound(value, count);
    }

    /**
     * 创建byte数组
     *
     * @param size
     * @return
     */
    public static byte[] createByteArray(int size) {
//		System.gc();
//		int freeSize = (int)Runtime.getRuntime().freeMemory();
//		if(size > freeSize){
//			return null;
//		}else{
//			return new byte[size];
//		}
        return new byte[size];
    }

    /**
     * 判断手机是否安装某个应用
     * @param context
     * @param appPackageName  应用包名
     * @return   true：安装，false：未安装
     */
    public static boolean checkPackage(String appPackageName,Context context) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 计算date前多少时间或多少时间后的日期
     *
     * @param beforeMonth
     * @return
     */
    public static String getDateBefore(String dateStr, int beforeMonth) {
        if (beforeMonth == 0) {
            return dateStr;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return dateStr;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//      calendar.add(Calendar.DATE, -1);    //得到前一天
        calendar.add(Calendar.MONTH, -3);    //得到前一个月
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        return year * 10000 + month * 100 + day + "";

    }


    /**
     * 创建int数组
     *
     * @param size
     * @return
     */
    public static int[] createIntArray(int size) {
        return new int[size];
    }

    public static boolean isContain(int[] arr, int value) {
        int j = 0;
        for (; j < arr.length; j++) {
            if (arr[j] == value) {
                break;
            }
        }
        return j == arr.length ? false : true;
    }


    /**
     * 获取手机的IMEI
     *
     * @return IMEI序列号
     */
    public static String getIMEI() {
        String imei = System.getProperty("IMEI");
        if (imei == null) {
            imei = System.getProperty("phone.IMEI");
        }
        if (imei == null) {
            imei = System.getProperty("com.siemens.IMEI");
        }
        if (imei == null) {
            imei = System.getProperty("com.nokia.mid.imei");
        }
        if (imei == null) {
            imei = System.getProperty("com.sonyericsson.imei");
        }
        if (imei == null) {
            imei = System.getProperty("com.sonyericsson.IMEI");
        }
        return imei;
    }

    /**
     * 获取手机的IMSI
     *
     * @return IMSI序列号
     */
    public static String getIMSI() {
        String imsi = System.getProperty("imsi");
        if (imsi == null) {
            imsi = System.getProperty("phone.imsi");
        }
        if (imsi == null) {
            imsi = System.getProperty("com.siemens.imsi");
        }
        if (imsi == null) {
            imsi = System.getProperty("com.nokia.mid.imsi");
        }
        if (imsi == null) {
            imsi = System.getProperty("com.sonyericsson.imsi");
        }
        if (imsi == null) {
            imsi = System.getProperty("com.sonyericsson.sim.subscribernumber");
        }
        return imsi;
    }

    /**
     * 检测手机号码是否有效
     *
     * @param phoneCode 手机号码
     * @return true/false
     */
    public static boolean checkPhoneCodeValid(String phoneCode) {
        if (phoneCode == null || phoneCode.length() != 11)
            return false;
        // 手机号码的格式判断交由服务器负责，客户端只保证11位数
//		String prefix = phoneCode.substring(0, 2);
//		if (Assist.equalsIgnoreCase(prefix,"13") || Assist.equalsIgnoreCase(prefix,"15") || Assist.equalsIgnoreCase(prefix,"18")
//				|| Assist.equalsIgnoreCase(prefix,"14")) {
//			for (int i = 2; i < phoneCode.length(); i++) {
//				if (phoneCode.charAt(i) < '0' || phoneCode.charAt(i) > '9') {
//					return false;
//				}
//			}
//			return true;
//		} else
//			return false;
        return true;
    }


    /**
     * @param date
     * @return
     * @Description:
     */
    public static String FormartData(String date) {
        try {
            return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
                    + date.substring(6, 8);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * 修改日期
     *
     * @param date   日期 yyyyMMdd
     * @param offset 天数偏移量
     * @return 修改后的日期
     */
    public static int changeDate(int date, int offset) {
        int result = date;
        if (offset != 0) {
            int day = date % 100;
            int month = date % 10000 / 100;
            int year = date / 10000;
            day = getDayAtDate(year, month, day) + offset;
            while (day > getDayAtYear(year)) {
                year++;
                day -= getDayAtYear(year);
            }
            while (day <= 0) {
                year--;
                day += getDayAtYear(year);
            }
            result = getDateFromDay(year, day);
        }
        return result;
    }

    public static int changeTime(int time, int offset) {
        int result = time;
        if (offset != 0) {
            int sec = time % 100;
            int min = time % 10000 / 100;
            int hour = time / 10000;
            //100306
            sec += offset;
            if (sec >= 60) {
                int temp = sec / 60;
                sec %= 60;
                min += temp;
            }

            if (min >= 60) {
                int temp = min / 60;
                min %= 60;
                hour += temp;
            }

            if (hour >= 24) {
                hour /= 24;
            }

            return hour * 10000 + min * 100 + sec;
        }
        return result;
    }

    /**
     * 判断闰年
     *
     * @param year
     * @return
     */
    private static boolean isLeapYear(int year) {
//		return year % 4 == 0 || ((year % 100 == 0) && (year % 400 == 0));
        return year % 400 == 0 || year % 4 == 0 && year % 100 != 0;
    }

    /**
     * 根据指定的日期,获取在当年的天数
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private static int getDayAtDate(int year, int month, int day) {
        int totalDay = day;
        for (int i = 1; i < month; i++) {
            totalDay += getDayAtMonth(year, i);
        }
        return totalDay;
    }

    /**
     * 获取某月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDayAtMonth(int year, int month) {
        if (month < 9) {
            if (month == 2) {
                return isLeapYear(year) ? 29 : 28;
            } else {
                if (month % 2 != 0 || month == 8)
                    return 31;
                else
                    return 30;
            }
        } else {
            if (month % 2 != 0)
                return 30;
            else
                return 31;
        }
    }

    /**
     * 获取一年中的总天数
     *
     * @param year
     * @return
     */
    private static int getDayAtYear(int year) {
        return isLeapYear(year) ? 366 : 365;
    }

    /**
     * 根据当前年份和总天数，得到yyyyMMdd日期
     *
     * @param year
     * @param day
     * @return
     */
    private static int getDateFromDay(int year, int day) {
        int month = 1;
        int monthDay = 0;
        for (int i = 1; i <= 12; i++) {
            monthDay = getDayAtMonth(year, i);
            if (day <= monthDay) {
                month = i;
                break;
            }
            day -= monthDay;
        }
        return year * 10000 + month * 100 + day;
    }

    //获取年的最后一天
    public static int getDateFromDay(int year) {
        int day = getDayAtYear(year);
        int month = 1;
        int monthDay = 0;
        for (int i = 1; i <= 12; i++) {
            monthDay = getDayAtMonth(year, i);
            if (day <= monthDay) {
                month = i;
                break;
            }
            day -= monthDay;
        }
        return year * 10000 + month * 100 + day;
    }

    //获取季度的去年的季度值
    public static int getLastYearQuarter(String reportQuarter) {
        if (reportQuarter.length() < 8) {
            return StringUtils.valueOfInteger(reportQuarter, 0);

        }
        int lastYearQuarter = StringUtils.valueOfInteger(reportQuarter, 0) - 10000;
        return lastYearQuarter;
    }

    public static String getReportNews(String textReportSeason) {
        try {
            String reportSeason = textReportSeason;
            if (textReportSeason.contains("-")) {
                reportSeason = textReportSeason.replaceAll("-", "");
            }
            int date = StringUtils.valueOfInt(textReportSeason);
            int m = (date % 10000) / 100;
            String mText = "";
            if (m >= 0 && m <= 3) {
                mText = "一季报";
            } else if (m >= 4 && m <= 6) {
                mText = "中报";
            } else if (m >= 7 && m <= 9) {
                mText = "三季报";
            } else if (m >= 10 && m <= 12) {
                mText = "年报";
            }
            return (date / 10000) + mText;
        } catch (Exception e) {

        }
        return textReportSeason;
    }

    public static int getReportSeasonIndex(String textReportSeason) {
        try {
            int date = StringUtils.valueOfInt(textReportSeason);
            int m = (date % 10000) / 100;
            int index = -1;
            if (m >= 0 && m <= 3) {
                index = 1;
            } else if (m >= 4 && m <= 6) {
                index = 2;
            } else if (m >= 7 && m <= 9) {
                index = 3;
            } else if (m >= 10 && m <= 12) {
                index = 4;
            }
            return index;
        } catch (Exception e) {

        }
        return -1;
    }

    //获取季度的上一个季度值
    public static int getDateFromQuarter(String reportQuarter) {
        if (reportQuarter.length() < 8) {
            return 0;
        }
        int year = StringUtils.valueOfInteger(reportQuarter, 0) / 10000;
        int currentMon = StringUtils.valueOfInteger(reportQuarter.substring(4, 6), 0);
        int lastQuarter = currentMon - 3;
        if (lastQuarter == 0) {
            lastQuarter = 12;
            year = year - 1;
        }
        int monthDay = getDayAtMonth(year, lastQuarter);

        return year * 10000 + lastQuarter * 100 + monthDay;
    }

    //获取季度的上一个季度值
    public static int getDateFromQuarter(int quarterInt, String reportQuarter) {
        if (reportQuarter.length() < 8) {
            return 0;
        }
        int year = StringUtils.valueOfInteger(reportQuarter, 0) / 10000;
        int currentMon = StringUtils.valueOfInteger(reportQuarter.substring(4, 6), 0);
        int lastQuarter = currentMon + quarterInt;
        if (lastQuarter > 12) {
            lastQuarter = 3;
            year = year + 1;
        } else if (lastQuarter == 0) {
            lastQuarter = 12;
            year = year - 1;
        }
        int monthDay = getDayAtMonth(year, lastQuarter);

        return year * 10000 + lastQuarter * 100 + monthDay;
    }

    //获取某年的某月的最后一天
    public static String getMonthLastDay(String year, int month) {

        if (null == year || month < 0) {
            return "--";
        }

        int monthDay = getDayAtMonth(StringUtils.valueOfInteger(year, 0), month);

        return "" + StringUtils.valueOfInteger(year, 0) * 10000 + month * 100 + monthDay;

    }

    //#ifdef DEBUG
//@	/**
//@	 * 测试日期更改
//@	 * @param args
//@	 */
//@	public static void main(String[] args){
//@		//System.out.println(changeDate(20101201,0));
//@	}
    //#endif

    /**
     * 将Date对象格式化为String对象
     *
     * @param date
     * @param format
     * @param tz
     * @return
     */
    public static String dateTimeFormat(Date date, String format, TimeZone tz) {
        Calendar chinaCalendar = Calendar.getInstance(tz);
        chinaCalendar.setTime(date);
        int year = chinaCalendar.get(Calendar.YEAR);
        int month = chinaCalendar.get(Calendar.MONTH) + 1;
        int day = chinaCalendar.get(Calendar.DAY_OF_MONTH);
        int hou = chinaCalendar.get(Calendar.HOUR_OF_DAY);
        int min = chinaCalendar.get(Calendar.MINUTE);
        int sec = chinaCalendar.get(Calendar.SECOND);
        return replace(replace(replace(replace(replace(replace(format, "yyyy", Integer.toString(year)), "MM",
                CommonFunc.paddingZeroLeft(String.valueOf(month), 2)), "dd", CommonFunc.paddingZeroLeft(
                String.valueOf(day), 2)), "HH", CommonFunc.paddingZeroLeft(String.valueOf(hou), 2)),
                "mm", CommonFunc.paddingZeroLeft(String.valueOf(min), 2)), "ss", CommonFunc.paddingZeroLeft(
                String.valueOf(sec), 2));

    }

    /**
     * 得到今天的格式化日期
     *
     * @param tz
     * @return
     */
    public static String getToday(TimeZone tz) {
        return dateTimeFormat(Calendar.getInstance(tz).getTime(), "yyyyMMdd", tz);
    }

    public static String getFormatToday() {
        return dateTimeFormat(Calendar.getInstance(TimeZone.getDefault()).getTime(), "yyyy-MM-dd", TimeZone.getDefault());
    }

    public static String getYesterday() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
        return dateTimeFormat(date.getTime(), "yyyyMMdd", TimeZone.getDefault());
    }

    public static String getCurrentDate(String dateFormat) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
        return dateTimeFormat(date.getTime(), dateFormat, TimeZone.getDefault());
    }

    /**
     * 获取当前时间的int表示
     *
     * @return h*100+m
     */
    public static int getCurTime() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        return c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
    }

    /**
     * 将int类型转化为"yy:MM:hh"
     *
     * @param time
     * @return "yy:MM:hh"型字符串
     */
    public static String intToTime(int time) {
        String t = Integer.toString(time);
        t = paddingZeroLeft(t, 6);
        return t.substring(0, 2) + ":" + t.substring(2, 4) + ":" + t.substring(4, 6);
    }

    /**
     * int类型转化为Date
     *
     * @param iDate
     * @param tz
     * @return
     */
    public static Date intToDateTime(int iDate, TimeZone tz) {
        int y = iDate / 10000;
        int m = (iDate - y * 10000) / 100;
        int d = iDate % 100;
        return getDate(y, m, d, tz);
    }

    /**
     * 获得Date
     *
     * @param y
     * @param m
     * @param d
     * @param tz
     * @return Date
     */
    public static Date getDate(int y, int m, int d, TimeZone tz) {
        return getDate(y, m, d, 0, 0, 0, tz);
    }

    /**
     * 获得Date
     *
     * @param y
     * @param m
     * @param d
     * @param hou
     * @param min
     * @param sec
     * @param tz
     * @return Date
     */
    public static Date getDate(int y, int m, int d, int hou, int min, int sec, TimeZone tz) {
        Calendar c = Calendar.getInstance(tz);
        c.set(Calendar.YEAR, y);
        c.set(Calendar.MONTH, m - 1);
        c.set(Calendar.DATE, d);
        c.set(Calendar.HOUR_OF_DAY, hou);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, sec);
        return c.getTime();
    }

    /**
     * 解码EncodeType,得到股票是否流通,此方法废弃
     *
     * @param type
     * @return
     * @deprecated
     */
    public static boolean decodeStockInfo(short type) {
        if ((type & 0x04) == 4 || (type & 0x01) == 1) {
            return true;
        } else {
            return false;
        }
    }

//	public static StockInfoItem getSecurity(StockInfoItem item) {
//		StockInfoItem result = item;
//		decodeWindCodeInfo(item.EncodeType, result);
//		getWFTType(result);
//		return result;
//	}

//	public static void getWFTType(StockInfoItem stockInfoitem) {
//		StockInfoItem item = stockInfoitem;
//		item.WftType = WFTType.Unknown;
//
//		// "A" A�ɣ�"B" B��, "J" ���, "T" ����
//		if (item.SecType.equals("A") || item.SecType.equals("B") || item.SecType.equals("T")) {
//			item.WftType = WFTType.BaseHSStock;
//			if (item.SecType.equals("T")){
//				item.QuoteStatus = 3;
//				item.WftType = WFTType.Sanban;
//			}
//			else
//				item.QuoteStatus = 1;
//		} else if (item.SecType.equals("J")) {
//			if (item.WindCode.indexOf("NV") > 0) {
//				item.QuoteStatus = 2;
//				item.WftType = WFTType.BaseFundNetValue;
//			} else {
//				String prefix2 = item.WindCode.substring(0, 2);
//				item.QuoteStatus = 1;
//				if (prefix2.equals("16") || prefix2.equals("51")) {
//					if (Assist.equalsIgnoreCase(item.IsListed,"1")) {
//						if (item.StockName.indexOf("ETF") > 0) {
//							item.WftType = WFTType.ETFFoud;
//						} else {
//							item.WftType = WFTType.LoFFoud;
//						}
//					} else {
//						item.WftType = WFTType.BaseOpenFund;
//						item.QuoteStatus = 2;
//					}
//				} else {
//					item.WftType = WFTType.BaseCloseFund;
//				}
//			}
//		} else if (item.SecType.equals("QL")) {// Ȩ��
//			item.WftType = WFTType.BaseHSStock;
//			item.QuoteStatus = 2;
//		} else if (item.SecType.equals("Q")) {// ��ծ
//			if (item.WindCode.indexOf(".IB") > 0)
//				item.WftType = WFTType.InterBankBond;
//			else
//				item.WftType = WFTType.CorporateBond;
//			item.QuoteStatus = 1;
//		} else if (item.SecType.equals("K")) {// ��תծ
//			item.WftType = WFTType.ConvertibleBond;
//			item.QuoteStatus = 1;
//		} else if (item.SecType.equals("G") || item.SecType.equals("M")) // "G"
//			// ��ծ�ع�,"M"
//			// ���ʽ�ع�
//		{
//			if (item.WindCode.indexOf(".IB") > 0)
//				item.WftType = WFTType.InterBankBond;
//			else
//				item.WftType = WFTType.BaseHSStock;
//			item.QuoteStatus = 1;
//		} else if (item.SecType.equals("Z")) // ��ծ
//		{
//			if (item.WindCode.indexOf(".IB") > 0)
//				item.WftType = WFTType.InterBankBond;
//			else
//				item.WftType = WFTType.GovernmentBond;
//			item.QuoteStatus = 1;
//		} else if (item.SecType.equals("ZC")) // �ʲ�֧��֤ȯ
//		{
//			item.WftType = WFTType.MBS;
//			item.QuoteStatus = 3;
//		} else if (item.SecType.equals("F") || item.SecType.equals("P") || item.SecType.equals("DQ")) {
//			// "F" ����ծ,"P" ����Ʊ��, "DQ" ��������ȯ
//			item.WftType = WFTType.InterBankBond;
//			item.QuoteStatus = 1;
//		} else if (item.SecType.equals("IR")) {
//			item.WftType = WFTType.BaseInterestRate;
//			item.QuoteStatus = 2;
//		} else if (item.SecType.equals("D") || item.SecType.equals("M")) // "D"
//			// ��ծ�ع�,
//			// "M"
//			// ���ʽ�ع�
//		{
//			item.WftType = WFTType.BaseHSStock;
//		} else if (item.SecType.equals("O")) // "O"
//		{
//			if (item.WindCode.indexOf(".SG") > 0) {
//				item.WftType = WFTType.BaseSGXStock;
//				item.QuoteStatus = 1;
//			} else if(item.WindCode.toUpperCase().indexOf(".TW")>0){
//				item.WftType = WFTType.TWSTOCK;
//				item.QuoteStatus = 3;
//			} else{
//				item.WftType = WFTType.Unknown;
//				item.QuoteStatus = 3;
//			}
//		} else if (item.SecType.equals("OF")) // ����ʽ���
//		{
//			if (item.WindCode.indexOf("NV") > 0) {
//				item.WftType = WFTType.BaseFundNetValue; // ���ֵ
//				item.QuoteStatus = 2;
//
//			} else {
//				if (Assist.equalsIgnoreCase(item.IsListed,"1")) {
//					item.QuoteStatus = 1;
//					if (item.StockName.indexOf("ETF") > 0) {
//						item.WftType = WFTType.ETFFoud;
//					} else {
//						item.WftType = WFTType.LoFFoud;
//					}
//				} else {
//					item.WftType = WFTType.BaseOpenFund; // ����ʽ���
//					item.QuoteStatus = 2;
//				}
//			}
//		} else if (item.SecType.equals("S")) // ָ��
//		{
//			String exchangeCode = getExchangeCode(item.WindCode);
//			item.WftType = WFTType.BaseIndex;
//			item.QuoteStatus = 2;
//			if (exchangeCode.equals("SH") || exchangeCode.equals("SZ") || exchangeCode.equals("HI")) {
//				item.QuoteStatus = 1;
//				item.WftType = WFTType.BaseHSIndex;
//			} else if (exchangeCode.equals("SI")) // ����ָ��
//			{
//				item.WftType = WFTType.BaseSWIndex;
//				int codeInt = getNoSuffixCode(item.WindCode);
//				if ((codeInt % 10 == 0) || (codeInt >= 801001 && codeInt <= 801009)
//						|| (codeInt >= 801801 && codeInt <= 801899) || (codeInt >= 801901 && codeInt <= 801905))
//					item.QuoteStatus = 1;
//			} else if (exchangeCode.equals("XI")) {
//				int codeInt = getNoSuffixCode(item.WindCode);
//				if ((codeInt >= 830001 && codeInt <= 830099) || (codeInt >= 830101 && codeInt >= 830119)
//						|| (codeInt >= 834001 && codeInt <= 834999))
//					item.QuoteStatus = 1;
//			} else if (exchangeCode.equals("CI")) {
//				item.WftType = WFTType.BaseCIIndex;
//				int codeInt = getNoSuffixCode(item.WindCode);
//				if (codeInt != 816022 && codeInt != 816023 && codeInt != 816154 && codeInt != 816244
//						&& codeInt != 816253)
//					item.QuoteStatus = 1;
//			} else if (exchangeCode.equals("MI")) {
//				if (!item.WindCode.equals("133333.MI"))
//					item.QuoteStatus = 1;
//			} else if (exchangeCode.equals("HI")) {
//				item.QuoteStatus = 1;
//			} else if (getExchangeCode(item.WindCode).equals("WI")) {
//				item.QuoteStatus = 1;
//				item.WftType = WFTType.BaseWIIndex;
//			} else {
//				if (item.WindCode.indexOf("CRB") > 0)
//					item.QuoteStatus = 2;
//				else if (item.WindCode.substring(0, 2).equals("DJ") && (!item.WindCode.substring(0, 3).equals("DJI")))
//					item.QuoteStatus = 2;
//				else
//					item.QuoteStatus = 3;
//			}
//		} else if ((item.SecType.equals("X")) || (item.SecType.equals("XW"))) {
//			item.WftType = WFTType.BaseHKStock;
//			item.QuoteStatus = 1;
//		} else if (item.SecType.equals("EX")) {
//			item.WftType = WFTType.BaseFx;
//			item.QuoteStatus = 2;
//		} else if (item.SecType.equals("FX")) {
//			item.WftType = WFTType.BaseFx;
//			item.QuoteStatus = 1; // ������ʵʱ����
//		} else if ((item.SecType.equals("OL")) || (item.SecType.equals("MT")) || (item.SecType.equals("FT"))) {
//			item.WftType = WFTType.BaseFutures;
//			String exchangeCode = getExchangeCode(item.WindCode);
//			if (exchangeCode.equals("SHF") || exchangeCode.equals("CZC") || exchangeCode.equals("DCE"))
//				item.QuoteStatus = 1;
//			else if (exchangeCode.equals("CFF")) {
//				item.WftType = WFTType.IndexFutures;
//				item.QuoteStatus = 1;
//			}
//		} else if (item.SecType.equals("SG")) // �ƽ�
//		{
//			item.WftType = WFTType.BaseGold;
//			item.QuoteStatus = 1; // �ƽ����ʵʱ����
//		} else if ((item.SecType.equals("II")) || (item.SecType.equals("RB"))) {
//			item.WftType = WFTType.BaseIndex;
//			item.QuoteStatus = 2;
//		} else if (item.SecType.equals("QZ")) // Ȩ֤
//		{
//			item.WftType = WFTType.BaseWarrant;
//			item.QuoteStatus = 1;
//
//		} else if (item.SecType.equals("LC")) // ��Ʋ�Ʒ
//		{
//			item.WftType = WFTType.BaseLC;
//			item.QuoteStatus = 2;
//		}
//	}

    private static int getNoSuffixCode(String windCode) {
        int i = windCode.indexOf('.');
        int j = windCode.indexOf('_');
        if (j > 0)
            return StringUtils.valueOfInteger(windCode.substring(j + 1, i - j - 1), 0);
        else
            return StringUtils.valueOfInteger(windCode.substring(0, i), 0);
    }

    private static String byteToSecType(byte type) {
        String result = "";
        if (type == 0x01)
            result = "A";
        else if (type == 0x02)
            result = "B";
        else if (type == 0x03)
            result = "D"; // ��ծ�ع�
        else if (type == 0x04)
            result = "F"; // ����ծ
        else if (type == 0x05)
            result = "G"; // ��ծ�ع�
        else if (type == 0x06)
            result = "J"; // ���
        else if (type == 0x07)
            result = "K"; // ��ת��ȯծ
        else if (type == 0x08)
            result = "M"; // ���ʽ�ع�
        else if (type == 0x09)
            result = "N"; // Net/Staq��
        else if (type == 0x0A)
            result = "O";
        else if (type == 0x0B)
            result = "P"; // ����Ʊ��
        else if (type == 0x0C)
            result = "Q"; // ��ծ
        else if (type == 0x0D)
            result = "S"; // ָ��
        else if (type == 0x0E)
            result = "T"; // ����
        else if (type == 0x0F)
            result = "X"; // �۹�
        else if (type == 0x10)
            result = "Z"; // ��ծ
        else if (type == 0x11)
            result = "CJ"; // ͬҵ���
        else if (type == 0x12)
            result = "BX"; // ����
        else if (type == 0x13)
            result = "DA"; // ��A��
        else if (type == 0x14)
            result = "DQ"; // ��������ȯ
        else if (type == 0x15)
            result = "EX";
        else if (type == 0x16)
            result = "FT";
        else if (type == 0x17)
            result = "FX";
        else if (type == 0x18)
            result = "QL"; // Ȩ��
        else if (type == 0x19)
            result = "QZ"; // Ȩ֤
        else if (type == 0x1A)
            result = "II";
        else if (type == 0x1B)
            result = "IR";
        else if (type == 0x1C)
            result = "LC"; // �������
        else if (type == 0x1D)
            result = "MT";
        else if (type == 0x1E)
            result = "OF"; // ����ʽ���
        else if (type == 0x1F)
            result = "RB";
        else if (type == 0x20)
            result = "SG"; // �ƽ�
        else if (type == 0x21)
            result = "XT"; // ���в�Ʒ
        else if (type == 0x22)
            result = "XW"; // �۹�Ȩ֤
        else if (type == 0x23)
            result = "ZC"; // �ʲ�֧��֤ȯ
        else if (type == 0x24)
            result = "WSS"; // δ���й�˾
        else if (type == 0x25)
            result = "QFJJ"; // ���QFJJ

        return result;
    }

//	private static int getNoSuffixCode(String windCode) {
//		int i = windCode.indexOf('.');
//		int j = windCode.indexOf('_');
//		int intCode = 0;
//		try {
//			if (j > 0)
//				intCode = Integer.parseInt(windCode.substring(j + 1, i - j - 1));
//			else
//				intCode = Integer.parseInt(windCode.substring(0, i));
//		} catch (Exception e) {
//		}
//		return intCode;
//	}

    /**
     * 获取windcode的后缀
     *
     * @param windCode
     * @return
     */
    public static String getExchangeCode(String windCode) {
        int i = windCode.indexOf(".");
        if (i > 0)
            return windCode.substring(i + 1, windCode.length());
        else
            return "";
    }

    /**
     * 将4个字节数据转化为int类型
     *
     * @param temp
     * @return
     */
    public static int byte4ToJavaInt(byte[] temp) {
        return (int) (0xff000000 & (temp[3] << 24)) + (int) (0x00ff0000 & (temp[2] << 16))
                + (int) (0x0000ff00 & (temp[1] << 8)) + (int) (0x000000ff & (temp[0] << 0));
    }

    /**
     * 将2个字节数据转化为short类型
     *
     * @param temp
     * @return
     */
    public static short byte2ToJavaShort(byte[] temp) {
        return (short) ((short) (0xff00 & (temp[1] << 8)) + (short) (0x00ff & (temp[0] << 0)));
    }

    /**
     * 将2个字节数据转化为int类型
     *
     * @param temp
     * @return
     */
    public static int byte2ToJavaInt(byte[] temp) {
        return (int) ((int) (0x0000ff00 & (temp[1] << 8)) + (int) (0x000000ff & (temp[0] << 0)));
    }

    /**
     * 将int转化为len长度的字节数组
     *
     * @param source
     * @param len
     * @return
     */
    public static byte[] int2Byte(int source, int len) {
        byte[] bLocalArr = new byte[len];
        for (int i = 0; (i < 4) && (i < len); i++) {
            bLocalArr[i] = (byte) (source >> 8 * i & 0xFF);

        }
        return bLocalArr;
    }

    /**
     * 将byte数组转化为int
     *
     * @param byteArray
     * @return
     */
    public static int byte2Int(byte[] byteArray) {
        int iOutcome = 0;
        byte bLoop;

        for (int i = 0; i < 4; i++) {
            bLoop = byteArray[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }

        return iOutcome;
    }

    /**
     * 在字符串s的左边填充len长度的ch
     *
     * @param s
     * @param len
     * @param ch
     * @return
     */
    public static String paddingLeft(String s, int len, String ch) {
        String temp = s;
        while (temp.length() < len) {
            temp = ch + temp;
        }
        return temp;
    }

    /**
     * 在字符串s的左边填充len长度的"0"
     *
     * @param s
     * @param len
     * @return
     */
    public static String paddingZeroLeft(String s, int len) {
        return paddingLeft(s, len, "0");
    }

    /**
     * 在字符串str中将oldStr替换为newStr
     *
     * @param str
     * @param oldStr
     * @param newStr
     * @return
     */
    public static String replace(String str, String oldStr, String newStr) {
        int pos = str.indexOf(oldStr);
        if (pos >= 0) {
            return str.substring(0, pos) + newStr + str.substring(pos + oldStr.length());
        }
        return str;
    }

    /**
     * 计算x的power次方
     *
     * @param x
     * @param power
     * @return
     */
    public static float power(float x, int power) {
        float result = 1;
        int left = power;
        while (left > 0) {
            result = result * x;
            left--;
        }
        return result;
    }

    /**
     * 定制小数点位数,针对于float类型数据
     *
     * @param f
     * @param precise
     * @return
     */
    public static String floatFormat(float f, int precise) {
        String str = "";
        if (f < 0) {
            str = "-";
            f = -f;
        }
        float offset = 0.5f;
        if (precise == 0) {
            return str + Integer.toString((int) (f + offset));
        } else {
            int p = (int) power(10f, precise);
            offset = offset / p;
            long t = (long) ((f + offset) * p);
            if (t / p == 0)
                str += "0";
            else
                str += Long.toString(t / p);

            return str + "." + paddingZeroLeft(Long.toString(Math.abs(t) % p), precise);
        }
    }

    public static final String[] CREDIT_STR = {"", "A", "B", "C", "D", "+", "-", "1", "2", "3"};//0为空字符

    public static String creditFormat(float crexdit) {
        int x = (int) crexdit;
        int[] y = {0, 0, 0, 0};
        boolean dirty = false;
        boolean noValue = true;
        for (int i = 0; i < 4; i++) {
            y[i] = (x >> (12 - 4 * i)) & 0x0f;
            if (y[i] == 0) {
                continue;
            }
            if (y[i] > 9 || y[i] < 1) {
                dirty = true;
                break;
            }
            noValue = false;
        }
        if (dirty == true || true == noValue) {
            return "--";
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < y.length; i++) {
                sb.append(CREDIT_STR[y[i]]);
            }
            return sb.toString();
        }

    }

    public static String doubleToInt(String d) {
        try {
            Double dv = Double.valueOf(d);
            int di = dv.intValue();
            if (dv == di) {
                return di + "";
            } else {
                return d;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return d;
        }
    }

    public static String doubleFormat(String s, int precise, String defaultValue) {
        if (TextUtils.isEmpty(s)) {
            return defaultValue;
        }
        String result = s;

        try {
            Double d = Double.valueOf(s);
            result = doubleFormat(d, precise);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
        return result;
    }

    /**
     * 定制小数点位数,,针对于double类型数据
     *
     * @param d
     * @param precise
     * @return
     */
    public static String doubleFormat(double d, int precise) {
        String str = "";
        if (d < 0) {
            str = "-";
            d = -d;
        }
        double offset = 0.5d;
        if (precise == 0) {
            if (str.equals("-")) {
                return "-" + Long.toString((int) (d + offset));
            } else {
                return Long.toString((int) (d + offset));
            }
        } else {
            int p = (int) power(10, precise);
            offset = offset / p;
            long t = (long) ((d + offset) * p);
            if (t / p == 0)
                str += "0";
            else
                str += Long.toString(t / p);
            return str + "." + paddingZeroLeft(Long.toString(Math.abs(t) % p), precise);
        }
    }

    /**
     * 定制小数点位数,,针对于double类型数据
     *
     * @param d
     * @param precise
     * @param notZero 一直精确到数值不为0，最多精确到小数点后5位
     * @return
     */
    public static String doubleFormat(double d, int precise, boolean notZero) {
        double begin = d;
        String str = "";
        if (d < 0) {
            str = "-";
            d = -d;
        }
        double offset = 0.5d;
        if (precise == 0) {
            return Long.toString((int) (d + offset));
        } else {
            int p = (int) power(10, precise);
            offset = offset / p;
            long t = (long) ((d + offset) * p);
            if (t / p == 0) {
                str += "0";
            } else {
                str += Long.toString(t / p);
            }
            long n = Math.abs(t) % p;
            if (notZero && d != 0 && t / p == 0 && n == 0 && precise < 5) {
                return doubleFormat(begin, precise + 1, notZero);
            } else {
                return str + "." + paddingZeroLeft(Long.toString(Math.abs(t) % p), precise);
            }
        }
    }

    public static double rerurnTheCloseMax(double value) {
        double bb = 0;
        if (Math.abs(value) <= 1000) {
            bb = (int) value / 10;
            if (bb == 0) {
                bb = value + 0.5;
            } else {
                bb = bb * 10 + 1;
            }
        } else {
            bb = value / 10;
            bb = bb * 10 + 1;
        }
        return (double) bb;
    }

    public static double rerurnTheCloseMin(double value) {
        double bb = 0;
        if (Math.abs(value) <= 1000) {
            bb = (int) value / 10;
            if (bb == 0) {
                bb = value - 0.5;
            } else {
                bb = bb * 10 - 1;
            }
        } else {
            bb = value / 10;
            bb = bb * 10 - 1;
        }
        return (double) bb;
    }

    public static ArrayList getConditon(String s) {
        String[] s1 = s.split("\r\n");

        ArrayList<String> firstStrList = new ArrayList<String>();
        String[] s2;
        for (int i = 0; i < s1.length; i++) {
            s2 = s1[i].split(" ");
            if (s2.length >= 1) {
                if (s2[0] != null) {
                    firstStrList.add(s2[0]);
                }
            } else {
                firstStrList.add("暂无数据说明");
            }

        }
        return firstStrList;
    }

    public static ArrayList getConditonValue(String s) {
        String[] s1 = s.split("\r\n");
        ArrayList<String> secondStrList = new ArrayList<String>();
        String[] s2;
        for (int i = 0; i < s1.length; i++) {
            s2 = s1[i].split(" ");
            if (s2.length > 1) {

                secondStrList.add(s2[1]);

            } else if (s2.length == 1) {
                secondStrList.add(s2[0]);
            } else {
                secondStrList.add("暂无数据说明");

            }
        }
        return secondStrList;
    }

    public static String getMinValue(ArrayList list) {
        float temp;
        float min = 0;
        String ss = "--";

        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).toString();

//			if(!str.contains("%")){
//
//				return ss;
//			}else	{
            if (str.contains("%")) {
                temp = StringUtils.valueOfFloat(str.replace("%", ""), 0);
                if (min == 0) {
                    min = temp;
                } else if (temp < min) {
                    min = temp;
                }
                ss = min + "%";
            }
        }

//		}	
        return ss;
    }

    public static String getMaxValue(ArrayList list) {
        float temp;
        float max = 0;
        String ss = "--";

        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).toString();

            if (str.contains("%")) {
                temp = StringUtils.valueOfFloat(str.replace("%", ""), 0);
                if (max == 0) {
                    max = temp;
                }

                if (max < temp) {
                    max = temp;
                }
                ss = max + "%";
            }
        }
        return ss;
    }

    public static Float getMaxFloat(ArrayList list) {
        float temp;
        float max = 0;
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).toString();
            if (str.indexOf("%") != -1) {
                temp = StringUtils.valueOfFloat(str.replace("%", ""), 0);
                if (max == 0) {
                    max = temp;
                }

                if (max < temp) {
                    max = temp;
                }
            }
        }
        return max;
    }

    /**
     * 保留小数点后几位
     *
     * @param value - 数值
     * @param num   - 保留位数
     * @return
     */
    @Deprecated
    public static String DecimalFormat(String value, int num) {
        try {
            double d = Double.parseDouble(value);
            return DecimalFormat(d, num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 保留小数点后几位
     *
     * @param value - 数值
     * @param num   - 保留位数
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
     * 获得指定精度的float类型数据
     *
     * @param f
     * @param precise
     * @return
     */
    public static float fixFloat(float f, int precise) {
        int p = (int) power(10, precise + 1);
        int v = (int) (f * p);
        p = (int) power(10, precise);
        double temp = f;
//		if (v % 10 < 5) {
////			temp = f + 1f / p;
//			return (float)(v/10)/((float)p);
//		}
//		else
//			return (float)((v + 10)/10)/((float)p);
        return StringUtils.valueOfFloat(doubleFormat(temp, precise), 0);
    }

    /**
     * 获得指定精度的float类型数据
     *
     * @param f
     * @param precise
     * @return
     */
    public static float fixDoubleToFloat(double f, int precise) {
        int p = (int) power(10, precise + 1);
        int v = (int) (f * p);
        p = (int) power(10, precise);
        double temp = f;
//		if (v % 10 < 5) {
////			temp = f + 1f / p;
//			return (float)(v/10)/((float)p);
//		}
//		else
//			return (float)((v + 10)/10)/((float)p);
        return StringUtils.valueOfFloat(doubleFormat(temp, precise), 0);
    }

    public static double rerurnTheLargerTenth(double value) {

        return Math.ceil(value);
//		int bb = 0;
//		if(Math.abs(value)<=1000){
//			bb = (int)value/10;
//			if(bb == 0){
//				bb  = (int)value+5;
//			}else{		
//				bb = bb*10+10;
//			}
//		}else{
//			bb = (int)value/10;
//			bb = bb*10+30;
//		}
//		return (double)bb;
    }

    public static double rerurnTheMinTenth(double value) {
        int bb = 0;
        if (Math.abs(value) <= 1000) {
            bb = (int) value / 10;
            if (bb == 0) {
                bb = (int) value - 5;
            } else {
                bb = bb * 10 - 10;
            }
        } else {
            bb = (int) value / 10;
            bb = bb * 10 - 30;
        }
        return (double) bb;
    }

    /**
     * 在字符串s中过滤掉所有的字符串f
     *
     * @param s
     * @param f
     * @return
     */
    public static String filterString(String s, String f) {
        String result = "";
        if (s == null || s == "")
            return result;
        else {
            while (s.indexOf(f) > 0) {
                int index = s.indexOf(f);
                result += s.substring(0, index);
                s = s.substring(index + 1);
            }
            result += s;
        }

        return result;
    }

    static public ArrayList strSplit(String str, String split, boolean trimSplit, boolean allowEmpty, ArrayList strs) {
        int i, off = 0, len = split.length();
        while ((i = str.indexOf(split, off)) != -1) {
            if (allowEmpty || i > off)
                strs.add(str.substring(off, i));
            if (!trimSplit)
                strs.add(split);
            off = i + len;
        }
        if (off < str.length())
            strs.add(str.substring(off));
        return strs;
    }

    static public ArrayList chSplit(String str, String split, ArrayList strs, boolean trimSplit) {
        int i, off = 0, len = str.length();
        for (i = 0; i <= len; i++) {
            if (i == len || split.indexOf(str.charAt(i)) != -1) {
                if (i > off)
                    strs.add(str.substring(off, i));
                if (!trimSplit && i != len)
                    strs.add(str.substring(i, i + 1));
                off = i + 1;
            }
        }
        return strs;
    }

    public static boolean isFundNV(String AWindCode) {
        //boolean Result = false;
        int i = AWindCode.indexOf("NV.SH");
        if (i > -1) {
            return true;
        } else {
            i = AWindCode.indexOf("NV.SZ");
            if (i > -1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static int getMultiply(int x) {
        switch (x) {
            case 0:
                return 1;
            case 1:
                return 10;
            case 2:
                return 100;
            case 3:
                return 1000;
            case 4:
                return 10000;
            case 5:
                return 100000;
            case 6:
                return 1000000;
            case 7:
                return 10000000;
            case 8:
                return 100000000;
            case 9:
                return 1000000000;
            default:
                return 10;
        }
        //return result;
    }
//

    /**
     * 规格化
     *
     * @param wftType
     * @param multiplying
     * @return
     */
    public static void getConvertedData(int[] indicators,
                                        float[] value, int wftType, float multiplying) {
        //float multiplying
        try {
            for (int j = 0; j < indicators.length; j++) {
                if (indicators[j] == 4 || indicators[j] == 167
                        || (indicators[j] >= 11 && indicators[j] <= 50)) { // 前收价
                    value[j] /= multiplying;
                } else if (indicators[j] == 3 || indicators[j] == 5
                        || indicators[j] == 6 || indicators[j] == 80
                        || indicators[j] == 7 || indicators[j] == 75) {
                    value[j] /= multiplying;
                } else if (indicators[j] == 8) {
                    // if (wftType == WFTType.BaseStock) {
                    //
                    // }
                    value[j] /= 100;
                } else if (indicators[j] == 59 || indicators[j] == 188
                        || indicators[j] == 230 || indicators[j] == 232
                        || indicators[j] == 233 || indicators[j] == 243) {
                    value[j] /= 10000;
                } else if (indicators[j] == 190 || indicators[j] == 10
                        || indicators[j] == 189 || indicators[j] == 188
                        || indicators[j] == 82 || indicators[j] == 187
                        || indicators[j] == 231 || indicators[j] == 81
                        || indicators[j] == 234 || indicators[j] == 237
                        || indicators[j] == 238 || indicators[j] == 235
                        || indicators[j] == 236) {
                    value[j] /= 100;
                } else if (indicators[j] == 79) {
                    value[j] /= multiplying * 10;
                } else {
                }
            }
        } catch (Exception e) {
        }

    }

    public static String[] splitToArray(String content, String splitText) {
        String[] result = null;
        ArrayList v = split(content, splitText);
        result = new String[v.size()];
        if (v != null && v.size() > 0) {
            for (int i = 0; i < v.size(); i++) {
                result[i] = (String) v.get(i);
            }
        }
        return result;

    }

    public static ArrayList split(String content, String splitText) {
        if (content == null) return null;
        ArrayList lineContent = new ArrayList();
        int cStart = 0;
        int cEnd = 0;
        String cont = null;
        while (cStart < content.length()) {
            cont = null;
            cEnd = content.indexOf(splitText, cStart);
            while (cEnd >= 0) {
                // 两个相连分割符转换成一个使用符
                String endStr = content.substring(cEnd + splitText.length());
                if (endStr.startsWith(splitText)) {
                    cEnd = content.indexOf(splitText, cEnd + (splitText + splitText).length());
                } else {
                    break;
                }
            }
            if (cEnd >= 0) {
                cont = content.substring(cStart, cEnd);
                cStart = cEnd + splitText.length();
                lineContent.add(cont);
            } else {
                if (cStart < content.length()) {
                    cont = content.substring(cStart);
                    lineContent.add(cont);
                }
                break;
            }
        }
        lineContent.trimToSize();
        return lineContent;
    }


    public static int parseInt(String text) {
        try {
            if (text.startsWith("0x") || text.startsWith("0X")) {
                return Integer.parseInt(text.substring(2), 16);
            } else {
                return Integer.parseInt(text);
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static float parseFloat(String text) {
        try {
            return Float.parseFloat(text);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getSecType(int encodeType) {
        byte highByte = (byte) (encodeType >> 8);
        return byteToSecType(highByte);
    }

    public static boolean isIOPVCode(String ACode) {
        int codeValue = 0;
        int p = 0;

        boolean Result = false;
        if (ACode.indexOf("NV.") + 1 > 0) {
            return Result;
        }
        if (ACode.indexOf("V.SZ") + 1 > 0) {
            Result = true;
        } else if (ACode.indexOf(".SH") + 1 > 0) {
            p = ACode.indexOf(".");
            codeValue = StringUtils.valueOfInteger(ACode.substring(0, p - 1), 0);
            if ((((codeValue >= 159001) && (codeValue <= 159999)) || // ETF
                    ((codeValue >= 510001) && (codeValue <= 510999))) || ((codeValue >= 160000) && (codeValue <= 169999))) { //LOF
                if (isOdd(codeValue)) {
                    Result = true;
                }
            }
        }

        return Result;
    }

    //奇数
    public static boolean isEven(int a) {
        if ((a & 1) == 0) {
            return true;
        } else {
            return false;
        }
    }

    //偶数
    public static boolean isOdd(int a) {
        {
            return !isEven(a);
        }
    }

    public static String fixDate(int date) {
        int year = date / 10000;
        int month = (date % 10000) / 100;
        int day = (date % 100);
        return year + "-" + month + "-" + (day < 10 ? "0" + day : day);
    }

    public static String fixTime(int time) {
        int h = time / 10000;
        int m = (time % 10000) / 100;
//		int s = time % 100;
        if (h >= 24)
            h = h - 24;
        return (h < 10 ? "0" + h : "" + h) + ":" + (m < 10 ? "0" + m : "" + m);// + ":" + (s < 10 ? "0" + s : "" + s);
    }

    public static String fixTimeWithSecond(int time) {
        int h = time / 10000;
        int m = (time % 10000) / 100;
        int s = time % 100;
        return (h < 10 ? "0" + h : "" + h) + ":" + (m < 10 ? "0" + m : "" + m) + ":" + (s < 10 ? "0" + s : "" + s);
    }

    public static String fixTimeNOHour(int time) {
        int h = time / 10000;
        int m = (time % 10000) / 100;
        int s = time % 100;
        return (m < 10 ? "0" + m : "" + m) + ":" + (s < 10 ? "0" + s : "" + s);
    }

    public static String fixVolumn(double value) {
//		if (value > 100000000) {
//			value /= 100000000;
//			return CommonFunc.adjustFormat(CommonFunc.floatFormat(value, 1)) + "亿";
//		} else if (value > 10000) {
//			value /= 10000;
//			return CommonFunc.adjustFormat(CommonFunc.floatFormat(value, 1)) + "万";
//		} else {
//			return CommonFunc.adjustFormat(CommonFunc.floatFormat(value, 0));
//		}


        if (value > 100000000) {
            value /= 100000000;
            return CommonFunc.doubleFormat(value, 1) + "亿";
        } else if (value > 10000) {
            value /= 10000;
            return CommonFunc.doubleFormat(value, 1) + "万";
        } else {
            return CommonFunc.doubleFormat(value, 0);
        }

    }

    public static String fixText(double value) {
        if (value > 100000000) {
            value /= 100000000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "亿";
            return CommonFunc.doubleFormat(value, 1) + "亿";
        } else if (value > 10000) {
            value /= 10000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "万";
            return CommonFunc.doubleFormat(value, 1) + "万";
        } else {
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 0));
            return CommonFunc.doubleFormat(value, 0);
        }
    }

    public static String fixText(double value, int len) {
        if (value > 100000000) {
            value /= 100000000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "亿";
            return CommonFunc.doubleFormat(value, len) + "亿";
        } else if (value > 10000) {
            value /= 10000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "万";
            return CommonFunc.doubleFormat(value, len) + "万";
        } else {
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 0));
            return CommonFunc.doubleFormat(value, len);
        }
    }

    public static String fixTText(double f, int len) {
        double value = Math.abs(f);
        int length = len;
        // 负值同样需要保留小数点位
//		if(f<=0)
//			length =0;
        String text = f < 0 ? "-" : "";
        if (value > 100000000) {
            value /= 100000000;

//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "亿";
            return text + CommonFunc.doubleFormat(value, length) + "亿";
        } else if (value > 10000) {
            value /= 10000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "万";
            return text + CommonFunc.doubleFormat(value, length) + "万";
        } else {
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 0));
            return text + CommonFunc.doubleFormat(value, length);
        }
    }

    /**
     * 支持负数的四舍五入方法
     *
     * @param value
     * @param len
     * @return
     */
    public static String fixTextWithMark(double value, int len) {
        if (Math.abs(value) >= 100000000) {
            value /= 100000000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "亿";
            return CommonFunc.doubleFormat(value, len) + "亿";
        } else if (Math.abs(value) >= 10000) {
            value /= 10000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "万";
            return CommonFunc.doubleFormat(value, len) + "万";
        } else {
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 0));
            return CommonFunc.doubleFormat(value, len);
        }
    }

    /**
     * 支持负数的四舍五入方法
     *
     * @param value
     * @param len
     * @return
     */
    public static String fixTextWithMarkV2(double value, int len) {
        if (Math.abs(value) >= 100000000) {
            value /= 100000000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "亿";
            return CommonFunc.doubleFormat(value, 0) + "亿";
        } else if (Math.abs(value) >= 10000) {
            value /= 10000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "万";
            return CommonFunc.doubleFormat(value, 0) + "万";
        } else {
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 0));
            return CommonFunc.doubleFormat(value, len);
        }
    }


    /**
     * 支持负数的四舍五入方法
     *
     * @param value
     * @return
     */
    public static double fixDoubleWithMark(double value) {
        if (Math.abs(value) >= 100000000) {
            return value /= 100000000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "亿";
        } else if (Math.abs(value) >= 10000) {
            return value /= 10000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "万";
        } else {
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 0));
            return value;
        }
    }



    public static String fixTextEx(double value, int len) {
        String fu = "";
        double zValue = 0.0;
        if (value < 0) {
            fu = "-";
            zValue = Math.abs(value);
        } else {
            zValue = value;
        }
        if (zValue > 100000000) {
            zValue /= 100000000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "亿";
            return fu + CommonFunc.doubleFormat(zValue, len) + "亿";
        } else if (zValue > 10000) {
            zValue /= 10000;
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 1)) + "万";
            return fu + CommonFunc.doubleFormat(zValue, len) + "万";
        } else {
//			return CommonFunc.adjustFormat(CommonFunc.doubleFormat(value, 0));
            return fu + CommonFunc.doubleFormat(zValue, len);
        }
    }

    public static String fixDateTime(int dateTime) {
        int year = dateTime / 10000;
        int month = dateTime % 10000 / 100;
        int day = dateTime % 100;
        return ((year < 1000 ? "0" + year : "" + year) + "/" + (month < 10 ? "0" + month : "" + month) +
                "/" + (day < 10 ? "0" + day : "" + day));
    }

    public static boolean isCharOf(int ch, char[] chs) {
        for (int i = 0; i < chs.length; i++)
            if (chs[i] == ch)
                return true;
        return false;
    }

    public static String trimHeadTail(String str, boolean trimHead,
                                      boolean trimTail, char[] trimChs) {
        if (str == null)
            return null;
        int len = str.length(), start = 0, end = len;

        if (trimHead)
            for (; start < len; start++)
                if (!isCharOf(str.charAt(start), trimChs))
                    break;
        if (trimTail)
            for (end = len; end > 0; end--)
                if (!isCharOf(str.charAt(end - 1), trimChs))
                    break;
        if (start >= end)
            return "";
        return str.substring(start, end).toString();
    }

    public static String getWindCodeWithNoDot(String windCode) {
        if (windCode == null || windCode.indexOf(".") == -1)
            return windCode;
        return windCode.substring(0, windCode.indexOf("."));
    }


    public static byte[] readFromFile(String url) {
        if (url == null || url.equals(""))
            return null;
        else {
            ByteArrayOutputStream baos;
            byte[] returnByte = null;
            byte[] b = new byte[1];
            try {
                InputStream is = CommonFunc.class.getResourceAsStream(url);
                if (is != null) {
                    baos = new ByteArrayOutputStream();
                    while (is.available() > 0) {
                        is.read(b);
                        baos.write(b);
                    }
                    returnByte = baos.toByteArray();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return returnByte;
        }
    }

    public static byte[] readFromAssets(Context c, String path) {
        try {
            if (c == null) {
                return "".getBytes();
            }
            InputStream is = c.getAssets().open(path);
            byte[] b = new byte[is.available()];
            is.read(b);
            return b;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readFromAssets(String path, InputStream is) {
        try {
//			if(MainController.getMainController() == null){
//				return "".getBytes();
//			}
            //InputStream is = MainController.getMainController().getContext().getAssets().open(path);
            byte[] b = new byte[is.available()];
            is.read(b);
            return b;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //格式化字符串"20120331"格式化为"2012-03-31"
    public static String formatDateString(String dateStr) {
        if (dateStr.length() != 8) {
            return dateStr;
        }
        String result = "";
        int dateInt = StringUtils.valueOfInteger(dateStr, 0);
        int year = dateInt / 10000;
        result = result + year;
        int mon = (dateInt % 10000) / 100;
        if (mon < 10) {
            result = result + "-0" + mon;
        } else {
            result = result + "-" + mon;
        }
        int day = dateInt % 100;
        if (day < 10) {
            result = result + "-0" + day;
        } else {
            result = result + "-" + day;
        }
        return result;
    }

    /**
     * float相加操作防止精度丢失
     *
     * @param value1
     * @param value2
     * @return
     */
    public static float addFloat(float value1, float value2) {
        return new BigDecimal(Float.toString(value1)).add(new BigDecimal(Float.toString(value2))).floatValue();

    }


    /**
     * 字符串截取方法
     *
     * @param src        需要截取的字符创
     * @param startIndex 起始索引（从0开始）
     * @param length     结束索引(不包括)
     * @return 截取后字符创
     */
    public static String subString(String src, int startIndex, int length) {
        String dest = null;
        dest = src.substring(startIndex, startIndex + length);
        return dest;
    }

    /**
     * 用户是否为新手
     *
     * @return 首次登录返回 -1；超过60天返回 1；正常为 0；
     */
    public static int isBeginner(String thisTime, String lastTime) {
        if (null == lastTime) {
            return -1;
        } else if (getBetween_day(thisTime, lastTime) > 60) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 计算两个日期之间的时间差
     *
     * @param time1 当前日期
     * @param time2 上一次登录日期
     * @return 天数
     */
    public static long getBetween_day(String time1, String time2) {
        long quot = 0;
        long day = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        try {
            Date date1 = ft.parse(time1);
            Date date2 = ft.parse(time2);
            quot = date1.getTime() - date2.getTime();
            day = quot / 1000 / 60 / 60 / 24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 计算两个日期之间的时间差
     *
     * @param time1 当前日期
     * @param time2 上一次登录日期
     * @return 天数
     */
    public static long getQuot_day(String time1, String time2) {
        long quot = 0;
        long day = 0;
//		  long hour = 0;
//		  long min = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date1 = ft.parse(time1);
            Date date2 = ft.parse(time2);
            quot = date1.getTime() - date2.getTime();
            day = quot / 1000 / 60 / 60 / 24;
//		   hour=(quot/(60*60*1000)-day*24); 
//		   min=((quot/(60*1000))-day*24*60-hour*60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 计算两个日期之间的时间差
     *
     * @param time1 当前日期
     * @param time2 上一次登录日期
     * @return 小时
     */
    public static long getQuot_hour(String time1, String time2) {
        long quot = 0;
        long day = 0;
        long hour = 0;
//		  long min = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date1 = ft.parse(time1);
            Date date2 = ft.parse(time2);
            quot = date1.getTime() - date2.getTime();
            day = quot / 1000 / 60 / 60 / 24;
            hour = (quot / (60 * 60 * 1000) - day * 24);
//		   min=((quot/(60*1000))-day*24*60-hour*60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hour;
    }

    /**
     * 计算两个日期之间的时间差
     *
     * @param time1 当前日期
     * @param time2 上一次登录日期
     * @return 分
     */
    public static long getQuot_minute(String time1, String time2) {
        long quot = 0;
        long day = 0;
        long hour = 0;
        long min = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date1 = ft.parse(time1);
            Date date2 = ft.parse(time2);
            quot = date1.getTime() - date2.getTime();
            day = quot / 1000 / 60 / 60 / 24;
            hour = (quot / (60 * 60 * 1000) - day * 24);
            min = ((quot / (60 * 1000)) - day * 24 * 60 - hour * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return min;
    }

    public static final String default_Value = "";

    public static final String default_Value_another = "--";

    //计算百分数
    public static String paraStringToFloatRate(String text, int length) {
        try {
            if (text != null && text.length() > 0) {
                float fr = StringUtils.valueOfFloat(text, 0);
                String reText = fixTextWithMark(fr, length);
                if (reText.equalsIgnoreCase("92233720368547758.07亿")) {
                    return default_Value;
                }
                return reText + "%";
            } else {
                return default_Value;
            }
        } catch (Exception e) {
            if (text != null)
                return text;
        }
        return default_Value;
    }

    public static String paraStringToFloat(String text, int length) {
        try {
            if (text != null && text.length() > 0) {
                Float.parseFloat(text);
                float fr = StringUtils.valueOfFloat(text, 0);
                String reText = fixTextWithMark(fr, length);
                if (reText.equalsIgnoreCase("92233720368547758.07亿"))
                    reText = default_Value;
                return reText;
            } else {
                return default_Value;
            }
        } catch (Exception e) {
            if (text != null)
                return text;
        }
        return default_Value;
    }

    //查找数值在数组中的位置
    public static int indexOf(Object object, Object[] array) {
        Object[] a = array;
        if (null == array || array.length == 0) {
            return -1;
        }
        int s = array.length;
        if (object != null) {
            for (int i = 0; i < s; i++) {
                if (object.equals(a[i])) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < s; i++) {
                if (a[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    //查找数值在数组中的位置
    public static int indexOf(int object, int[] array, int spaceNum) {
        if (null == array || array.length == 0) {
            return -1;
        }
        int s = array.length;
        for (int i = 0; i < s; i++) {
            if (object == array[i]) {
                return i - spaceNum;
            }
        }
        return -1;
    }

    //double的四舍五入
    public static double doubleRound(double value, int size) {
        BigDecimal b = new BigDecimal(value);
        b = b.setScale(size, BigDecimal.ROUND_HALF_UP);
        return b.doubleValue();
    }

    //double的四舍五入
    public static float floatRound(float value, int size) {
        BigDecimal b = new BigDecimal(value);
        b = b.setScale(size, BigDecimal.ROUND_HALF_UP);
        return b.floatValue();
    }


    /**
     * 取得月最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得月第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }


    /**
     * 取得季度最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDateOfSeason(Date date) {
        return getLastDateOfMonth(getSeasonDate(date)[2]);
    }


    /**
     * 取得季度月
     *
     * @param date
     * @return
     */
    public static Date[] getSeasonDate(Date date) {
        Date[] season = new Date[3];

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int nSeason = getSeason(date);
        if (nSeason == 1) {// 第一季度
            c.set(Calendar.MONTH, Calendar.JANUARY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.FEBRUARY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MARCH);
            season[2] = c.getTime();
        } else if (nSeason == 2) {// 第二季度
            c.set(Calendar.MONTH, Calendar.APRIL);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MAY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.JUNE);
            season[2] = c.getTime();
        } else if (nSeason == 3) {// 第三季度
            c.set(Calendar.MONTH, Calendar.JULY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.AUGUST);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);
            season[2] = c.getTime();
        } else if (nSeason == 4) {// 第四季度
            c.set(Calendar.MONTH, Calendar.OCTOBER);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.NOVEMBER);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.DECEMBER);
            season[2] = c.getTime();
        } else {// 第一季度
            c.set(Calendar.MONTH, Calendar.JANUARY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.FEBRUARY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MARCH);
            season[2] = c.getTime();
        }
        return season;
    }

    /**
     * 根据年和季度取得该季度最后一天的日期
     *
     * @return
     */
    public static String getDateStrOfSeason(int season, String year, String format) {
        String seasonStr;
        Date seasonDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        SimpleDateFormat desSdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(year);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            if (season == 1) {// 第一季度
                c.set(Calendar.MONTH, Calendar.MARCH);
            } else if (season == 2) {// 第二季度
                c.set(Calendar.MONTH, Calendar.JUNE);
            } else if (season == 3) {// 第三季度
                c.set(Calendar.MONTH, Calendar.SEPTEMBER);
            } else if (season == 4) {// 第四季度
                c.set(Calendar.MONTH, Calendar.DECEMBER);
            } else {
                c.set(Calendar.MONTH, Calendar.MARCH);
            }
            c.set(Calendar.DAY_OF_MONTH, 1);

            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DAY_OF_MONTH, -1);
            seasonDate = c.getTime();
            return desSdf.format(seasonDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static int getSeason(String dateStr, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (null == date) {
            return -1;
        }
        return getSeason(date);

    }

    /**
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @param date
     * @return
     */
    public static int getSeason(Date date) {

        int season = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;

    }


    public static final int[] seasons = {Calendar.MARCH, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.DECEMBER};

    public static List<String> getSeasonDate(int period, String formatStr) {


        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

        List<String> resultList = new ArrayList<>();

        //计算当年的季度


        int seasonIndex = getSeason(c.getTime()) - 1;


        int tmpSeason = seasonIndex;

        for (int i = 0; i < period; i++) {


            tmpSeason = tmpSeason - 1;
            if (tmpSeason < 0) {

                c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
                tmpSeason = seasons.length - 1;
            }


            c.set(Calendar.MONTH, seasons[tmpSeason]);
            c.set(Calendar.DAY_OF_MONTH, 1);

            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DAY_OF_MONTH, -1);
            resultList.add(sdf.format(c.getTime()));


        }

        return resultList;

    }


    /**
     * 获取endDate向前的连续报告期
     *
     * @param period    获取报告期的数量
     * @param endDate   截止的报告期
     * @param formatStr 格式化字符串 例如“yyyyMMdd”
     * @return
     */
    public static List<String> getSeasonDate(int period, String endDate, String formatStr) {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

        if (!TextUtils.isEmpty(endDate)) {
            Date date;
            try {
                date = sdf.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            c.setTime(date);
        }

        List<String> resultList = new ArrayList<>();

        //计算当年的季度
        int seasonIndex = getSeason(c.getTime());
        int tmpSeason = seasonIndex;

        for (int i = 0; i < period; i++) {


            tmpSeason = tmpSeason - 1;
            if (tmpSeason < 0) {

                c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
                tmpSeason = seasons.length - 1;
            }


            c.set(Calendar.MONTH, seasons[tmpSeason]);
            c.set(Calendar.DAY_OF_MONTH, 1);

            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DAY_OF_MONTH, -1);
            resultList.add(sdf.format(c.getTime()));


        }

        return resultList;

    }

    /**
     * 获取endDate向前的连续报告期及报告期名称，例如：2017年报 2017三季报
     *
     * @param period    获取报告期的数量
     * @param endDate   截止的报告期
     * @param formatStr 格式化字符串 例如“yyyyMMdd”
     * @return
     */
    public static List<List<String>> getSeasonDateStr(int period, String endDate, String formatStr) {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

        if (!TextUtils.isEmpty(endDate)) {
            Date date;
            try {
                date = sdf.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            c.setTime(date);
        }

        SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");
        List<List<String>> resultList = new ArrayList<>();
        List<String> reportDateList = new ArrayList<>();
        List<String> reportStrList = new ArrayList<>();

        //计算当年的季度
        int seasonIndex = getSeason(c.getTime());
        int tmpSeason = seasonIndex;

        for (int i = 0; i < period; i++) {


            tmpSeason = tmpSeason - 1;
            if (tmpSeason < 0) {

                c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
                tmpSeason = seasons.length - 1;
            }
            c.set(Calendar.MONTH, seasons[tmpSeason]);
            c.set(Calendar.DAY_OF_MONTH, 1);

            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DAY_OF_MONTH, -1);
            reportDateList.add(sdf.format(c.getTime()));
            reportStrList.add(yearSdf.format(c.getTime()) + REPORTSEASONSTR[tmpSeason]);
        }

        resultList.add(reportDateList);
        resultList.add(reportStrList);
        return resultList;

    }


    /**
     * 获取startDate与endDate之间的连续报告期及报告期名称，例如：2017年报 2017三季报
     *
     * @param startDate 起始时间
     * @param endDate   截止的报告期
     * @param formatStr 格式化字符串 例如“yyyyMMdd”
     * @return
     */
    public static List<List<String>> getSeasonDateStr(String startDate, String endDate, String formatStr) {
        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date date;
        try {
            date = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        c.setTime(date);

        Calendar startc = Calendar.getInstance();
        try {
            date = sdf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        startc.setTime(date);


        SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");
        List<List<String>> resultList = new ArrayList<>();
        List<String> reportDateList = new ArrayList<>();
        List<String> reportStrList = new ArrayList<>();

        //计算当年的季度
        int seasonIndex = getSeason(c.getTime());
        int tmpSeason = seasonIndex;

        while (c.compareTo(startc) != -1) {
            tmpSeason = tmpSeason - 1;
            if (tmpSeason < 0) {

                c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
                tmpSeason = seasons.length - 1;
            }
            c.set(Calendar.MONTH, seasons[tmpSeason]);
            c.set(Calendar.DAY_OF_MONTH, 1);

            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DAY_OF_MONTH, -1);
            if(c.compareTo(startc) == 1 || c.compareTo(startc) == 0) {
                reportDateList.add(sdf.format(c.getTime()));
                reportStrList.add(yearSdf.format(c.getTime()) + REPORTSEASONSTR[tmpSeason]);
            }
        }

        resultList.add(reportDateList);
        resultList.add(reportStrList);
        return resultList;

    }

    public static final String[] REPORTSEASONSTR = {"一季报", "中报", "三季报", "年报"};


    public static List<String> getSeasonDate(int period, int monthIndex, String formatStr) {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

        List<String> resultList = new ArrayList<>();


        int seasonIndex = getSeason(c.getTime());


        if (seasonIndex <= monthIndex) {

            c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
        }

        int season = seasons[monthIndex - 1];

        for (int i = 0; i < period; i++) {

            c.set(Calendar.MONTH, season);
            c.set(Calendar.DAY_OF_MONTH, 1);

            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DAY_OF_MONTH, -1);

            resultList.add(sdf.format(c.getTime()));

            c.add(Calendar.YEAR, -1);
        }

        return resultList;

    }

    public static List<String> getSeasonDate(int period, int monthIndex, String endDate, String formatStr) {


        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

        if (!TextUtils.isEmpty(endDate)) {
            Date date;
            try {
                date = sdf.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            c.setTime(date);
        }


        List<String> resultList = new ArrayList<>();


        int seasonIndex = getSeason(c.getTime());


//		if(seasonIndex<=monthIndex){
//
//			c.set(Calendar.YEAR, c.get(Calendar.YEAR)-1);
//		}

        int season = seasons[monthIndex - 1];

        for (int i = 0; i < period; i++) {

            c.set(Calendar.MONTH, season);
            c.set(Calendar.DAY_OF_MONTH, 1);

            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DAY_OF_MONTH, -1);

            resultList.add(sdf.format(c.getTime()));

            c.add(Calendar.YEAR, -1);
        }

        return resultList;

    }

    public static String getDateFrom(int spaceNum, String dateStr, String formatStr) {

        SimpleDateFormat dateformat = new SimpleDateFormat(formatStr, Locale.getDefault());
        Date date;
        try {
            date = dateformat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

        calendar.add(Calendar.DAY_OF_MONTH, spaceNum);


        return sdf.format(calendar.getTime());

    }

    public static String getDateFromMonth(int spaceNum, String dateStr, String formatStr) {

        SimpleDateFormat dateformat = new SimpleDateFormat(formatStr, Locale.getDefault());
        Date date;
        try {
            date = dateformat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

        calendar.add(Calendar.MONTH, spaceNum);


        return sdf.format(calendar.getTime());

    }

    public static String getDateFromYear(int spaceNum, String dateStr, String formatStr) {

        SimpleDateFormat dateformat = new SimpleDateFormat(formatStr, Locale.getDefault());
        Date date;
        try {
            date = dateformat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        calendar.add(Calendar.YEAR, spaceNum);

        return sdf.format(calendar.getTime());

    }


    public static String getDateOfYearFirst(String formatStr) {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return sdf.format(calendar.getTime());

    }

    /**
     * 比较两个日期
     * @param date1
     * @param date2
     * @param formatStr
     * @return
     */
    public static int compareTime(String date1, String date2,String formatStr) {


        DateFormat df = new SimpleDateFormat(formatStr);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                //dt1 在dt2前
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //dt1在dt2后
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 比较两个日期
     * @param date1
     * @param date2
     * @param df
     * @return
     */
    public static int compareTime(String date1, String date2,DateFormat df) {

        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                //dt1 在dt2前
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //dt1在dt2后
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 比较两个日期(返回天数)
     * @param date1
     * @param date2
     * @param df
     * @return
     */
    public static long compareTimeDay(String date1, String date2,DateFormat df) {

        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            long time = dt1.getTime()-dt2.getTime();
            long day = time/(24*3600*1000);
            return day;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取几年之前的时间
     * @param dateStr
     * @param num
     * @return
     */
    public static String getYearFrom( int num,String dateStr,String formatStr) {
        SimpleDateFormat dateformat = new SimpleDateFormat(formatStr, Locale.getDefault());
        Date date;
        try {
            date = dateformat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, num);

        return dateformat.format(calendar.getTime());
    }

    /**
     * 根据最大值、最小值、期望的坐标数，求最终的坐标数据
     * @param cormax 最大值
     * @param cormin 最小值
     * @param cornumber 期望的坐标数
     * @return 坐标值的集合
     */
    public static ArrayList<Double> standard(double cormax, double cormin, double cornumber) {
        double realmaxmoney, corstep, tmpstep, tmpnumber, temp, extranumber;
        realmaxmoney = Double.valueOf(cormax);
        if (cormax <= cormin) {
            return new ArrayList<>();
        }
        corstep = (cormax - cormin) / cornumber;
        if (Math.pow(10, (int) (Math.log(corstep) / Math.log(10))) == corstep) {
            temp = Math.pow(10, (int) (Math.log(corstep) / Math.log(10)));
        } else {
            temp = Math.pow(10, ((int) (Math.log(corstep) / Math.log(10)) + 1));
        }
        tmpstep = corstep / temp;
        //选取规范步长
        if (tmpstep >= 0 && tmpstep <= 0.01) {
            tmpstep = 0.01;
        }else if (tmpstep >= 0.010001 && tmpstep <= 0.02) {
            tmpstep = 0.02;
        } else if (tmpstep >= 0.020001 && tmpstep <= 0.025) {
            tmpstep = 0.025;
        }else if (tmpstep >= 0.025001 && tmpstep <= 0.05) {
            tmpstep = 0.05;
        }else if (tmpstep >= 0.050001 && tmpstep <= 0.1) {
            tmpstep = 0.1;
        } else if (tmpstep >= 0.100001 && tmpstep <= 0.2) {
            tmpstep = 0.2;
        } else if (tmpstep >= 0.200001 && tmpstep <= 0.25) {
            tmpstep = 0.25;
        } else if (tmpstep >= 0.250001 && tmpstep <= 0.5) {
            tmpstep = 0.5;
        } else {
            tmpstep = 1;
        }

        tmpstep = tmpstep * temp;
        if ((int) (cormin / tmpstep) != (cormin / tmpstep)) {
            if (cormin < 0) {
                cormin = (-1) * Math.ceil(Math.abs(cormin / tmpstep)) * tmpstep;
            } else {
                cormin = (int) (Math.abs(cormin / tmpstep)) * tmpstep;
            }

        }
        if ((int) (cormax / tmpstep) != (cormax / tmpstep)) {
            cormax = (int) (cormax / tmpstep + 1) * tmpstep;
        }
        tmpnumber = (cormax - cormin) / tmpstep;
        if (tmpnumber < cornumber) {
            extranumber = cornumber - tmpnumber;
            tmpnumber = cornumber;
            if (extranumber % 2 == 0) {
                cormax = cormax + tmpstep * (int) (extranumber / 2);
            } else {
                cormax = cormax + tmpstep * (int) (extranumber / 2 + 1);
            }
            cormin = cormin - tmpstep * (int) (extranumber / 2);
        }
        cornumber = tmpnumber;

        double nummoney = 0;
        ArrayList<Double> resultArr  = new ArrayList<>();
        double keduwidth = (cormax - cormin) / cornumber;
        BigDecimal keduwidth1 = new BigDecimal(Double.toString(keduwidth));
        BigDecimal cormin1 = new BigDecimal(Double.toString(cormin));

        if (realmaxmoney <= 0) {
            nummoney = cormin;
            for (int i = 0; nummoney < cormax; i++) {
                BigDecimal i1 = new BigDecimal(Double.toString(i));
                nummoney = keduwidth1.multiply(i1).add(cormin1).doubleValue();
                resultArr.add(nummoney);
            }
        } else {
            for (int i = 0; nummoney < realmaxmoney; i++) {
                BigDecimal i1 = new BigDecimal(Double.toString(i));
                nummoney = keduwidth1.multiply(i1).add(cormin1).doubleValue();
                resultArr.add(nummoney);
            }
        }


        return  resultArr;
    }

    /**
     * 根据传入的文本获取double值
     * @param text
     * @return
     */
    public static Double getDoubleValue(String text) {
        if (TextUtils.isEmpty(text)) {
            return 0.0;
        }
        try {
            Double d = Double.valueOf(text);
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * 取数据的floor值
     */
    public static final int DOUBLE_FLOOR = 0;
    /**
     * 取数据的ceil值
     */
    public static final int DOUBLE_CEIL = 1;

    /**
     * 取整
     *
     * @param data
     * @param type 是ceil还是floor
     * @return
     */
    public static double getIntPartOfDouble(double data, int type) {
        try {
            double tmpDouble = 0;
            String dataStr = new DecimalFormat("#.##").format(data);
            if (Math.abs(data) > 0 && Math.abs(data) < 1) {

                for (int i = 0; i < dataStr.length(); i++) {

                    tmpDouble = data * Math.pow(10, i);
                    if (Math.abs(tmpDouble) > 10) {
                        if (type == 1) {
                            data = Math.ceil(tmpDouble) / Math.pow(10, i);
                        } else {
                            data = Math.floor(tmpDouble) / Math.pow(10, i);
                        }
                        break;
                    }

                }

            } else if (data != 0) {

                if (dataStr.indexOf(".") != -1) {
                    dataStr = dataStr.substring(0, dataStr.indexOf("."));
                }

                if (dataStr.length() > 1) {

                    tmpDouble = data / Math.pow(10, (dataStr.length() - 2));

                    if (type == 1) {
                        data = Math.ceil(tmpDouble) * Math.pow(10, (dataStr.length() - 2));
                    } else {
                        data = Math.floor(tmpDouble) * Math.pow(10, (dataStr.length() - 2));
                    }

                } else {

                    if (type == 1) {
                        data = Math.ceil(data);
                    } else {
                        data = Math.floor(data);
                    }
                }

            }

        } catch (Exception e) {

        }
        return data;

    }

    /**
     * 获取指定的循环色
     * @param index
     * @return
     */
    public static int getCommonColor(int index) {
        int[] COLORS = new int[]{
                Color.parseColor("#e0525d"),
                Color.parseColor("#fdb91c"),
                Color.parseColor("#37d3c5"),
                Color.parseColor("#449ed3"),
                Color.parseColor("#ff761b"),
                Color.parseColor("#7367b0"),
                Color.parseColor("#88abda"),
                Color.parseColor("#ffff10"),
                Color.parseColor("#f29c9f"),
                Color.parseColor("#ca873b"),
        };
        if (index >= COLORS.length) {
            return 0;
        }
        return COLORS[index];
    }

    /**
     * 校验字符串格式是否满足要求
     *
     * @param formatStr 校验的格式
     * @param str       被校验的字符串
     * @return
     */
    public static boolean isValidStr(String formatStr, String str) {

        Pattern p = Pattern.compile(formatStr);
        Matcher m = p.matcher(str);

        return m.matches();

    }


    public static boolean checkHasInstalledApp(@NonNull Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(pkgName, PackageManager.GET_GIDS);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        } catch (RuntimeException e) {
            app_installed = false;
        }
        return app_installed;
    }


}
