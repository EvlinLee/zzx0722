package com.eryue.mine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dazhou on 2018/5/20.
 */

public class ProductInfoDBHelper extends SQLiteOpenHelper {
    public static String tableName = "product_info_table";

    private static final String TAG = "TestSQLite";
    public static final int VERSION = 1;


    public ProductInfoDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + tableName + "(" +
                "itemId varchar(50) primary key," +
                "itemTitle varchar(200)," +
                "jf double," +
                "pictUrl varchar(200)," +
                "quanPrice double," +
                "shortTitle varchar(200)," +
                "soldQuantity int," +
                "searchFlag varchar(200)," +
                "video varchar(200)," +
                "isMall double," +
                "discountPrice double," +
                "afterQuan double," +
                "couponStatus int," +
                "productType varchar(200))";
        //输出创建数据库的日志信息
        Log.i(TAG, "create Database------------->" + sql);
        //execSQL函数用于执行SQL语句
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //输出更新数据库的日志信息
        Log.i(TAG, "update Database------------->");
    }
}
