package com.example.javaoptionapp;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.javaoptionapp.room.Date_Small_Taiwan_Feature;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;
import com.example.javaoptionapp.ui.wanggoo.WangGooHistoryUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

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

    @Test
    public void update_before_2014_db() {

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FeatureDatabaseDao fdDao;
        FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
        fdDao = fdb.FeatureDatabaseDao();

        InputStream inputStream = appContext.getResources().openRawResource(R.raw.all_data);
        HashMap<String, HashMap<String,String>> hashmap_time_data = new HashMap<String, HashMap<String,String>>();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuffer sb = new StringBuffer("");
            String result;
            while ((result = reader.readLine()) != null) {
//              資料轉換
                result = result.replace("[","").replace("]","").replace(" ","");
                result = result.replace("'","");
                String[] temp_list = result.split(",");

                HashMap<String, String> temp_map = new HashMap<String, String>();
                String temp_time = "";
                for (String bb : temp_list){
                    String[] final_list = bb.split(":");
                    if(final_list[0].equals("time")){
                        long millisecond = Long.parseLong(final_list[1]);
                        Date date = new Date(millisecond);
                        SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
                        temp_time = format.format(date);
                    }else{
                        temp_map.put(final_list[0],final_list[1]);
                    }
                }
                hashmap_time_data.put(temp_time,temp_map);
            }
            System.out.println(hashmap_time_data);
        } catch (Exception e1) {
            e1.printStackTrace();
        }


        for (String key : hashmap_time_data.keySet()) {
            HashMap<String,String> temp_data = hashmap_time_data.get(key);
            String date = key;
            System.out.println(temp_data.toString());
            Date_Small_Taiwan_Feature dstf = new Date_Small_Taiwan_Feature(date,
                    Float.parseFloat(temp_data.get("open")),
                    Float.parseFloat(temp_data.get("high")),
                    Float.parseFloat(temp_data.get("low")),
                    Float.parseFloat(temp_data.get("close")),
                    Float.parseFloat(temp_data.get("volume")));
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
             * MA 10 日線計算 單元測試
             * MA 15 日線計算 單元測試
             *當天收盤價也會計算在內
             * */
            String ma5_begin_date = fdDao.get_ma5_begin_date();
            String ma10_begin_date = fdDao.get_ma10_begin_date();
            String ma15_begin_date = fdDao.get_ma15_begin_date();
            List<Date_Small_Taiwan_Feature> LD = fdDao.getAll();
            for (Date_Small_Taiwan_Feature d : LD) {
                String date = d.date;
                if (Integer.parseInt(date) >= Integer.parseInt(ma5_begin_date)) { //從  ma5_begin_date 日開始更新
                    fdDao.update_ma5(date);
                }
                if (Integer.parseInt(date) >= Integer.parseInt(ma10_begin_date)) {
                    fdDao.update_ma10(date);
                }
                if (Integer.parseInt(date) >= Integer.parseInt(ma15_begin_date)) {
                    fdDao.update_ma15(date);
                }
            }


        }




    @Test
    public void room_test(){

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FeatureDatabaseDao fdDao;
        FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
        fdDao = fdb.FeatureDatabaseDao();
//        //[time:1600963200000, tradeDate:1600963200000, open:12168.00000, high:12234.00000, low:12157.00000, close:12206.00000, volume:9865, millionAmount:0.00]


        /**
        * 寫入及更新 DateIndexTable 的方法
        * [time:1600963200000, tradeDate:1600963200000, open:12168.00000, high:12234.00000, low:12157.00000, close:12206.00000, volume:9865, millionAmount:0.00]
        * */
        long year_time = TimeUnit.SECONDS.toMillis((long) (365.25*24*60*60));
        String time = String.valueOf(System.currentTimeMillis());//- 2*year_time
        WangGooHistoryUtil wghu = new WangGooHistoryUtil(time);
        HashMap<String, HashMap<String,String>> hashmap_time_data = wghu.hashmap_time_data;
        for (String key : hashmap_time_data.keySet()) {
            HashMap<String,String> temp_data = hashmap_time_data.get(key);
            String date = key;
            Date_Small_Taiwan_Feature dstf = new Date_Small_Taiwan_Feature(date,
                    Float.parseFloat(temp_data.get("open")),
                    Float.parseFloat(temp_data.get("high")),
                    Float.parseFloat(temp_data.get("low")),
                    Float.parseFloat(temp_data.get("close")),
                    Float.parseFloat(temp_data.get("volume")));
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
         * MA 10 日線計算 單元測試
         * MA 15 日線計算 單元測試
         *當天收盤價也會計算在內
         * */
        String ma5_begin_date = fdDao.get_ma5_begin_date();
        String ma10_begin_date = fdDao.get_ma10_begin_date();
        String ma15_begin_date = fdDao.get_ma15_begin_date();
        List<Date_Small_Taiwan_Feature> LD = fdDao.getAll();
        for (Date_Small_Taiwan_Feature d : LD){
            String date = d.date;
            if (Integer.parseInt(date) >= Integer.parseInt(ma5_begin_date)) { //從  ma5_begin_date 日開始更新
                fdDao.update_ma5(date);
            }
            if (Integer.parseInt(date) >= Integer.parseInt(ma10_begin_date)) {
                fdDao.update_ma10(date);
            }
            if (Integer.parseInt(date) >= Integer.parseInt(ma15_begin_date)) {
                fdDao.update_ma15(date);
            }
        }



        /**
        * 績效回測單元測試
         * 做多進場點
         * 1.close > MA15
         * 2.MA5 > MA10 X
         * 3.close < MA5
         *
         * 停利點
         * 1.高於 入場點 4%
         *
         * 停損點
         * 1.低於入場點 2%
         *
         * 做空進場點
        * */
        List<Date_Small_Taiwan_Feature> All_Data = fdDao.getAll();
        boolean Approach = false;
        String Long_Short = "";
        Float Entry_Point = null , Exit_Point = null, Exit_Benifit_Point = null ,Exit_Damage_Point = null;
        Map<String, Float> Total_Performance = new LinkedHashMap();
        int Day = 0;
        for (Date_Small_Taiwan_Feature Day_Data : All_Data){
            System.out.println(Day_Data.date + " 開盤: " + Day_Data.open + " 最高: "+Day_Data.high + " 最低 : "+Day_Data.low + " 收盤 : " +Day_Data.close);
            if(!Approach) {
                if(Day_Data.MA_5!=null &&Day_Data.MA_10!=null &&Day_Data.MA_15!=null   &&
                        Day_Data.MA_5 > Day_Data.MA_10 && Day_Data.close > Day_Data.MA_15 ){ //&&  Day_Data.close < Day_Data.MA_5
                    Entry_Point = Day_Data.close; //進場點數
                    Exit_Benifit_Point = Day_Data.close * 1.05f;
                    Exit_Damage_Point = Day_Data.close * 0.98f;
                    System.out.println("進場做多點數 : "+Day_Data.close +"停利點位:" + Exit_Benifit_Point + "停損點數 :" + Exit_Damage_Point);
                    Approach = true;//進場
                    Long_Short = "Long"; //做多
                    Day = 0;
                }

            }
            else if(Approach && Long_Short.equals("Long")){//已進場做多 判斷停利停損
               if(Day_Data.high > Exit_Benifit_Point){  //Day_Data.close > Day_Data.MA_5 * 1.05 ||
                    System.out.println("停利點數 : "+ Exit_Benifit_Point);
                    Exit_Point = Exit_Benifit_Point; //停利點數
                    Approach = false;
                }
                else if(Day_Data.close < Exit_Damage_Point){  //盤中如果有低於停損價位 關盤前要停損
                    System.out.println("停損點數 : "+Day_Data.close);
                    Exit_Point = Day_Data.close; //停損點數
                    Approach = false;
                }

                if(!Approach){//出場
                    String year = Day_Data.date.substring(0,4);
                    Float init_vale = 0f;
                    if (Total_Performance.containsKey(year)){
                        init_vale = Total_Performance.get(year);
                    }
                    //買必put避險
                    Float change_value = Exit_Point - Entry_Point - 2;
                    if (change_value < 0) {
                        change_value = -200f;
                    }else {
                        change_value -= 50;
                    }

                    Total_Performance.put(year , init_vale + change_value);
                    System.out.println("單次績效 : "+ String.valueOf(change_value) +"歷時天數 : " +String.valueOf(Day) );
                }else {
                    Day += 1;
                }
            }
        }
        AtomicReference<Float> total_v = new AtomicReference<>(0f);
        Total_Performance.forEach((k, v) -> {
            System.out.println("年份: " + k + " 績效:" + v);
            total_v.updateAndGet(v1 -> v1 + v);
        });
        System.out.println("總績效 : "+ total_v);





    }


    /**
     * 績效回測單元測試
     * 做多進場點
     * 1.close > MA15
     * 2.MA5 > MA10 X
     * 3.close < MA5
     *
     * 停利點
     * 1.高於 入場點 4%
     *
     * 停損點
     * 1.低於入場點 2%
     *
     * 做空進場點
     * */
    @Test
    public void option_test(){

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FeatureDatabaseDao fdDao;
        FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
        fdDao = fdb.FeatureDatabaseDao();
        List<Date_Small_Taiwan_Feature> All_Data = fdDao.getAll();
        boolean Approach = false;
        String Long_Short = "";
        Float Entry_Point = null , Exit_Point = null, Exit_Benifit_Point = null ,Exit_Damage_Point = null;
        Map<String, Float> Total_Performance = new LinkedHashMap();
        Map<String, Float> Win_Session = new LinkedHashMap();
        Map<String, Float> Total_Session = new LinkedHashMap();
        int Day = 0;
        for (Date_Small_Taiwan_Feature Day_Data : All_Data){
            System.out.println(Day_Data.date + " 開盤: " + Day_Data.open + " 最高: "+Day_Data.high + " 最低 : "+Day_Data.low + " 收盤 : " +Day_Data.close);
            if(!Approach) {
                if(Day_Data.MA_5!=null &&Day_Data.MA_10!=null &&Day_Data.MA_15!=null   &&
                        Day_Data.MA_5 > Day_Data.MA_10 && Day_Data.MA_10 > Day_Data.MA_15 ){ //&&  Day_Data.close < Day_Data.MA_5
                    Entry_Point = Day_Data.close; //進場點數
                    Exit_Benifit_Point =  Entry_Point + 100; //停利點數
                    Approach = true;//進場
                    Long_Short = "Long"; //做多
                    Day = 0;//第0天
                }

            }
            else if(Approach && Long_Short.equals("Long")){//已進場做多 判斷停利停損
                boolean Win = false;
                if(Day_Data.high > Exit_Benifit_Point){  //Day_Data.close > Day_Data.MA_5 * 1.05 ||
                    System.out.println("停利點數 : "+ Exit_Benifit_Point);
                    Exit_Point =  Exit_Benifit_Point; //停利點數
                    Approach = false;
                    Win = true;
                }
                else if(Day == 2){  //第三天沒漲超過100就算輸
                    System.out.println("停損點數 : "+Day_Data.close);
                    Exit_Point = Day_Data.close; //停損點數
                    Approach = false;
                }

                if(!Approach){//出場
                    String year = Day_Data.date.substring(0,4);
                    Float init_vale = 0f;
                    Float Denominator = 0f;
                    Float Numerator = 0f;
                    if (Total_Performance.containsKey(year)){
                        init_vale = Total_Performance.get(year);
                    }
                    if (Win_Session.containsKey(year)){
                        Numerator = Win_Session.get(year);//出場一次就分子+1
                    }
                    if (Total_Session.containsKey(year)){
                        Denominator = Total_Session.get(year);//出場一次就分母+1
                    }
                    //買必put避險
                    Float change_value = Exit_Point - Entry_Point - 2;


                    Total_Performance.put(year , init_vale + change_value);
                    Total_Session.put(year , Denominator+1);
                    if (Win){
                        Win_Session.put(year , Numerator+1);
                    }
                    System.out.println("單次績效 : "+ String.valueOf(change_value) +"歷時天數 : " +String.valueOf(Day) );
                }else {
                    Day += 1;
                }
            }
        }

        System.out.println("Win_Session : "+ Win_Session);
        System.out.println("Total_Session : "+ Total_Session);

        AtomicReference<Float> total_v = new AtomicReference<>(0f);
        Total_Performance.forEach((k, v) -> {
            System.out.println("年份: " + k + " 績效:" + v);
            total_v.updateAndGet(v1 -> v1 + v);
        });
        System.out.println("總績效 : "+ total_v);


        AtomicReference<Float> win_session = new AtomicReference<>(0f);
        Win_Session.forEach((k_0, v_0) -> {
            win_session.updateAndGet(v1_0 -> v1_0 + v_0);
        });

        AtomicReference<Float> total_session = new AtomicReference<>(0f);
        Total_Session.forEach((k_1, v_1) -> {
            Float temp_win =0f;
            if(Win_Session.containsKey(k_1)){
                temp_win = Win_Session.get(k_1);
            }
            System.out.println("年份: " + k_1 + " 勝率:" + temp_win/v_1);
            total_session.updateAndGet(v1_1 -> v1_1 + v_1);
        });
        System.out.println("總勝率 : "+ Integer.parseInt(win_session.toString())/Integer.parseInt(total_session.toString()));



    }





}