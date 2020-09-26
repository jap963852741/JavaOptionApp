package com.example.javaoptionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.javaoptionapp.database.FeedReaderContract;
import com.example.javaoptionapp.database.FeedReaderDbHelper;
import com.example.javaoptionapp.room.AppDatabase;
import com.example.javaoptionapp.room.Date_Small_Taiwan_Feature;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;
import com.example.javaoptionapp.room.User;
import com.example.javaoptionapp.room.UserDao;
import com.example.javaoptionapp.ui.wanggoo.WangGooHistoryUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Long.sum;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
//    @Before
//    public void create_test(){
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(appContext);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        // Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "My Title");
//        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "子標題");
//        // Insert the new row, returning the primary key value of the new row
//        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
//        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
//        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
//    }
//    @Test
//    public void read_test(){
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(appContext);
//        Log.i("Now","read_test");
//        //Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {
//                BaseColumns._ID,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
//        };
//
//        // Filter results WHERE "title" = 'My Title'
//        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
//        String[] selectionArgs = { "My Title" };
//
//        // How you want the results sorted in the resulting Cursor
//        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";
//
//        Cursor cursor = db.query(
//                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
//                projection,             // The array of columns to return (pass null to get all)
//                selection,              // The columns for the WHERE clause
//                selectionArgs,          // The values for the WHERE clause
//                null,                   // don't group the rows
//                null,                   // don't filter by row groups
//                sortOrder               // The sort order
//        );
//
//        List itemIds = new ArrayList<>();
//        List subtitlearrays = new ArrayList<>();
//
//        while(cursor.moveToNext()) {
//            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
//            String subtitlearray = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE));
//            subtitlearrays.add(subtitlearray);
//            itemIds.add(itemId);
//        }
//        cursor.close();
//
//        System.out.println(itemIds);
//        System.out.println(subtitlearrays);
//
//
//        Cursor cursor_a = db.rawQuery("select * from entry",
//                null);
//        while (cursor_a.moveToNext()) {    //获取游标下移，作为循环，从而获得所有数据
//            String id = cursor_a.getString(0);
//            String name = cursor_a.getString(1);
//            String credit = cursor_a.getString(2);//获取credit学分
//            System.out.println("query--->" + id + "," + name + "," + credit);//输出数据
//        }
//
//        cursor_a.close();
//
//    }
//    @Test
//    public void delete_test(){
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(appContext);
//        Log.i("Now","create_test");
////        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
////        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(appContext);
//        // Gets the data repository in write mode
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        // Define 'where' part of query.
//        dbHelper.onUpgrade(db,0,1);
//
//    }



//
//    @Test
//    public void wghu_test(){
//        WangGooHistoryUtil wghu = new WangGooHistoryUtil();
//    }
    /**
     * MA 5 日線計算 單元測試
     *
     * */
    public static float get_Float_MA_5(List<String> LS){
        if(LS.size()>=5) {
            float sum = 0;
            for (String ds : LS) {
                sum += Float.parseFloat(ds);
            }
            return sum / 5;
        }else {
            return 0;
        }
    }
    @Test
    public void room_test(){

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FeatureDatabaseDao fdDao;
        FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
        fdDao = fdb.FeatureDatabaseDao();
//        //[time:1600963200000, tradeDate:1600963200000, open:12168.00000, high:12234.00000, low:12157.00000, close:12206.00000, volume:9865, millionAmount:0.00]
//        Date_Small_Taiwan_Feature dstf = new Date_Small_Taiwan_Feature("1600963200000","12168","12234","12157","12206","9865");
//        fdDao.insertAll(dstf);


        /**
        * 寫入及更新 DateIndexTable 的方法
        * [time:1600963200000, tradeDate:1600963200000, open:12168.00000, high:12234.00000, low:12157.00000, close:12206.00000, volume:9865, millionAmount:0.00]
        * */
        WangGooHistoryUtil wghu = new WangGooHistoryUtil();
        HashMap<String, HashMap<String,String>> hashmap_time_data = wghu.hashmap_time_data;
        System.out.println(hashmap_time_data);
        for (String key : hashmap_time_data.keySet()) {
            HashMap<String,String> temp_data = hashmap_time_data.get(key);
            String date = key;
            Date_Small_Taiwan_Feature dstf = new Date_Small_Taiwan_Feature(date,
                    temp_data.get("open").replace(".00000",""),
                    temp_data.get("high").replace(".00000",""),
                    temp_data.get("low").replace(".00000",""),
                    Float.parseFloat(temp_data.get("close")),
                    temp_data.get("volume"));
            Date_Small_Taiwan_Feature the_date_information = fdDao.get_Date_data(date);
//           沒有的才 insert
            if (the_date_information != null){
                fdDao.update(dstf);
            }else {
                fdDao.insertAll(dstf);
            }
        }




        /**
         * MA 5 日線計算 單元測試
         *當天收盤價也會計算在內
         * */
//        Float LS = fdDao.get_date_ma5("20200910");
//        System.out.println(LS);
        String ma5_begin_date = fdDao.get_ma5_begin_date();
        System.out.println("ma5開始計算日期"+ma5_begin_date);

        List<Date_Small_Taiwan_Feature> LD = fdDao.getAll();
        for (Date_Small_Taiwan_Feature d : LD){
            String date = d.date;
            if (Integer.parseInt(date) >= Integer.parseInt(ma5_begin_date)) { //從  ma5_begin_date 日開始更新
//                Float date_ma5 = fdDao.get_date_ma5(date);
//                fdDao.update_ma5(date, date_ma5);
                fdDao.update_all_ma5(date);
            }
        }

    }
}