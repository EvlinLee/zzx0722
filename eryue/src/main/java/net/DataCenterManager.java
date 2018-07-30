package net;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.eryue.BaseApplication;
import com.eryue.mine.ProductInfoDBHelper;

import java.util.ArrayList;
import net.InterfaceManager.SearchProductInfoEx;

public class DataCenterManager {

    private Context context;

    private static DataCenterManager dataCenterManager;

    private String pName = "zhuzhuxia";

    public String zfb;// 存储 支付宝账号
    public double balance;// 账户余额


    private ProductInfoDBHelper dbHelper;

    public static DataCenterManager Instance() {
        if (dataCenterManager == null) {
            dataCenterManager = new DataCenterManager();
            if (null == dataCenterManager.dbHelper) {
                dataCenterManager.dbHelper = new ProductInfoDBHelper(BaseApplication.getInstance().getApplicationContext(), "stu_db", null, 1);
            }
         }
        return dataCenterManager;
    }



    public void save(Context context, String key, String value) {
        if (key == null || value == null || context == null) {
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(pName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String get(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(pName, Context.MODE_PRIVATE);

        if (key == null) {
            return "";
        }

        String value = sharedPreferences.getString(key, "");
        return value;
    }


    // 记录 用户浏览历史
    public void saveProductInfo(InterfaceManager.SearchProductInfoEx productInfoEx) {
        if (delProductIfExist(productInfoEx.itemId)) {
            insertProductInfo(productInfoEx);
            return;
        }

        checkProductSum();
        insertProductInfo(productInfoEx);
    }

    public ArrayList<InterfaceManager.SearchProductInfoEx> getProductInfoList() {
        ArrayList<InterfaceManager.SearchProductInfoEx> resultArr = new ArrayList<>();
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                ProductInfoDBHelper.tableName,
                new String[]{
                        "itemId",
                        "itemTitle",
                        "jf",
                        "pictUrl",
                        "quanPrice",
                        "shortTitle",
                        "soldQuantity",
                        "searchFlag",
                        "video",
                        "isMall",
                        "discountPrice",
                        "afterQuan",
                        "couponStatus",
                        "productType"},
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            InterfaceManager interfaceManager = new InterfaceManager();
            InterfaceManager.SearchProductInfoEx productInfoEx = interfaceManager.new SearchProductInfoEx();
            productInfoEx.itemId = cursor.getString(cursor.getColumnIndex("itemId"));
            productInfoEx.afterQuan = cursor.getDouble(cursor.getColumnIndex("afterQuan"));
            productInfoEx.discountPrice = cursor.getDouble(cursor.getColumnIndex("discountPrice"));
            productInfoEx.isMall = cursor.getDouble(cursor.getColumnIndex("isMall"));
            productInfoEx.itemTitle = cursor.getString(cursor.getColumnIndex("itemTitle"));
            productInfoEx.jf = cursor.getDouble(cursor.getColumnIndex("jf"));
            productInfoEx.pictUrl = cursor.getString(cursor.getColumnIndex("pictUrl"));
            productInfoEx.quanPrice = cursor.getDouble(cursor.getColumnIndex("quanPrice"));
            productInfoEx.shortTitle = cursor.getString(cursor.getColumnIndex("shortTitle"));
            productInfoEx.soldQuantity = cursor.getInt(cursor.getColumnIndex("soldQuantity"));
//            productInfoEx.searchFlag = cursor.getString(cursor.getColumnIndex("searchFlag"));
//            productInfoEx.video = cursor.getString(cursor.getColumnIndex("video"));
            productInfoEx.couponStatus = cursor.getInt(cursor.getColumnIndex("couponStatus"));
            productInfoEx.productType = cursor.getString(cursor.getColumnIndex("productType"));

            resultArr.add(productInfoEx);
        }

        //关闭数据库
        db.close();

        return resultArr;
    }

    private boolean checkProductSum() {
        int maxSum = 50;
        int sum = 0;
        ArrayList<String> itemIdList = new ArrayList<>();
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                ProductInfoDBHelper.tableName,
                new String[]{"itemId"},
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            String itemId = cursor.getString(cursor.getColumnIndex("itemId"));
            itemIdList.add(itemId);
            sum ++;
            if (sum >= maxSum) {
                db.close();
                deleteProduct(itemIdList.get(0));
                return true;
            }
        }

        //关闭数据库
        db.close();

        return false;
    }

    private void insertProductInfo(InterfaceManager.SearchProductInfoEx productInfoEx) {
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //生成ContentValues对象 //key:列名，value:想插入的值
        ContentValues cv = new ContentValues();
        //往ContentValues对象存放数据，键-值对模式
        cv.put("itemId", productInfoEx.itemId);
        cv.put("itemTitle", productInfoEx.itemTitle);
        cv.put("jf", productInfoEx.jf);
        cv.put("pictUrl", productInfoEx.pictUrl);
        cv.put("quanPrice", productInfoEx.quanPrice);
        cv.put("shortTitle", productInfoEx.shortTitle);
        cv.put("soldQuantity", productInfoEx.soldQuantity);
//        cv.put("searchFlag", productInfoEx.searchFlag);
//        cv.put("video", productInfoEx.video);
        cv.put("isMall", productInfoEx.isMall);
        cv.put("discountPrice", productInfoEx.discountPrice);
        cv.put("afterQuan", productInfoEx.afterQuan);
        cv.put("couponStatus", productInfoEx.couponStatus);
        cv.put("productType", productInfoEx.productType);


        //调用insert方法，将数据插入数据库
        db.insert(ProductInfoDBHelper.tableName, null, cv);
        //关闭数据库
        db.close();
    }

    private boolean delProductIfExist(String itemId) {
        if (TextUtils.isEmpty(itemId)) {
            return false;
        }
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//参数1：表名
//参数2：要想显示的列
//参数3：where子句
//参数4：where子句对应的条件值
//参数5：分组方式
//参数6：having条件
//参数7：排序方式
        Cursor cursor = db.query(
                ProductInfoDBHelper.tableName,
                new String[]{"itemId"},
                    "itemId=?",
                    new String[]{itemId},
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            db.close();
            deleteProduct(itemId);
            return true;
        }

        //关闭数据库
        db.close();
        return false;
    }

    private void deleteProduct(String itemId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String whereClauses = "itemId=?";
        String[] whereArgs = {itemId};
        db.delete(ProductInfoDBHelper.tableName, whereClauses, whereArgs);
    }

    private void modifyProduct() {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("sage", "23");
////where 子句 "?"是占位符号，对应后面的"1",
//        String whereClause = "id=?";
//        String[] whereArgs = {String.valueOf(3)};
////参数1 是要更新的表名
////参数2 是一个ContentValeus对象
////参数3 是where子句
//        db.update(
//                tableName,
//                cv,
//                whereClause,
//                whereArgs);
    }


}
