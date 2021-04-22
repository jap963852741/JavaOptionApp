package com.example.javaoptionapp;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.javaoptionapp.room.Table_Option;
import com.example.javaoptionapp.room.Table_Small_Taiwan_Feature;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;
import com.example.taiwanworkdaylib.APIUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void update_before_2014_feature() {

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
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
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

        ArrayList need_to_update_list = new ArrayList();
        ArrayList need_to_insert_list = new ArrayList();
        for (String key : hashmap_time_data.keySet()) {
            HashMap<String,String> temp_data = hashmap_time_data.get(key);
            String date = key;
            System.out.println(date +" " +temp_data.toString());
            Table_Small_Taiwan_Feature dstf = new Table_Small_Taiwan_Feature(date,
                    Float.parseFloat(temp_data.get("open")),
                    Float.parseFloat(temp_data.get("high")),
                    Float.parseFloat(temp_data.get("low")),
                    Float.parseFloat(temp_data.get("close")),
                    Float.parseFloat(temp_data.get("volume")));
            Table_Small_Taiwan_Feature the_date_information = fdDao.get_Date_data(date);
//           沒有的才 insert
            if (the_date_information != null){
                need_to_update_list.add(dstf);
            }else {
                need_to_insert_list.add(dstf);
            }
        }

        if (need_to_update_list != null) {
            fdDao.updateAllTable_Small_Taiwan_Feature(need_to_update_list);
        }
        if (need_to_insert_list != null) {
            fdDao.insertAllTable_Small_Taiwan_Feature(need_to_insert_list);
        }
}
    @Test
    public void update_2000_2019_option() {

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FeatureDatabaseDao fdDao;
        FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
        fdDao = fdb.FeatureDatabaseDao();
        InputStream inputStream = appContext.getResources().openRawResource(R.raw.option_data);
        HashMap<Integer, HashMap<String,String>> hashmap_time_data = new HashMap<Integer, HashMap<String,String>>();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuffer sb = new StringBuffer("");
            String result = null;
            int i = 0;

            while ((result = reader.readLine()) != null) {
//              資料轉換
                if (i>10000 && i % 1000 == 0){
                    System.out.println(result);
                }
                result = result.replace("[","").replace("]","").replace(" ","");
                result = result.replace("'","");
                String[] temp_list = result.split(",");

                HashMap<String, String> temp_map = new HashMap<String, String>();
                String temp_time = "";
                for (String bb : temp_list){
                    String[] final_list = bb.split(":");
                    if(final_list[0].equals("Date")){
                        temp_time = final_list[1];
                    }
                    temp_map.put(final_list[0],final_list[1]);
                }
                hashmap_time_data.put(i,temp_map);
                i+=1;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        for (Integer key : hashmap_time_data.keySet()) {
            HashMap<String,String> temp_data = hashmap_time_data.get(key);
            Table_Option table_option = new Table_Option(
                    temp_data.get("Date"),
                    temp_data.get("Maturity"),
                    temp_data.get("Strike_price"),
                    temp_data.get("CallPut"),
                    Float.parseFloat(temp_data.get("close")),
                    Integer.parseInt(temp_data.get("Day_To_Finish")));
            Table_Option the_date_information = fdDao.get_option_data(temp_data.get("Date"),temp_data.get("Maturity"),temp_data.get("Strike_price"),temp_data.get("CallPut"));
//           沒有的才 insert
            if (the_date_information != null){
//                fdDao.update_option(table_option);
                continue;
            }else {
                fdDao.insert_option(table_option);
            }
        }
        System.out.println("離開迴圈");
        fdb.close();
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
//        long year_time = TimeUnit.SECONDS.toMillis((long) (365.25*24*60*60));
//        String time = String.valueOf(System.currentTimeMillis());//- 2*year_time
//        WangGooHistoryUtil wghu = new WangGooHistoryUtil(time);
//        HashMap<String, HashMap<String,String>> hashmap_time_data = wghu.hashmap_time_data;
//        ArrayList need_to_update_list = new ArrayList();
//        ArrayList need_to_insert_list = new ArrayList();
//        for (String key : hashmap_time_data.keySet()) {
//            HashMap<String,String> temp_data = hashmap_time_data.get(key);
//            String date = key;
//            System.out.println(date +" " +temp_data.toString());
//            Table_Small_Taiwan_Feature dstf = new Table_Small_Taiwan_Feature(date,
//                    Float.parseFloat(temp_data.get("open")),
//                    Float.parseFloat(temp_data.get("high")),
//                    Float.parseFloat(temp_data.get("low")),
//                    Float.parseFloat(temp_data.get("close")),
//                    Float.parseFloat(temp_data.get("volume")));
//            Table_Small_Taiwan_Feature the_date_information = fdDao.get_Date_data(date);
////           沒有的才 insert
//            if (the_date_information != null){
//                need_to_update_list.add(dstf);
//            }else {
//                need_to_insert_list.add(dstf);
//            }
//        }
//
//        if (need_to_update_list != null) {
//            fdDao.updateAllTable_Small_Taiwan_Feature(need_to_update_list);
//        }
//        if (need_to_insert_list != null) {
//            fdDao.insertAllTable_Small_Taiwan_Feature(need_to_insert_list);
//        }

        /**
         * MA 5 日線計算 單元測試
         * MA 10 日線計算 單元測試
         * MA 15 日線計算 單元測試
         *當天收盤價也會計算在內
         * */
        fdDao.update_ALL_ma5(fdDao.get_ma5_begin_date());
        fdDao.update_ALL_ma10(fdDao.get_ma10_begin_date());
        fdDao.update_ALL_ma15(fdDao.get_ma15_begin_date());
        fdDao.update_ALL_ma30(fdDao.get_ma30_begin_date());
        fdDao.update_ALL_bias5(fdDao.get_ma5_begin_date());
        fdDao.update_ALL_before_5_days_average(fdDao.get_ma5_begin_date());

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
    public void option_long_test(){

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FeatureDatabaseDao fdDao;
        FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
        fdDao = fdb.FeatureDatabaseDao();
        List<Table_Small_Taiwan_Feature> All_Data = fdDao.getAll();
        boolean Approach = false;
        boolean have_option_information = false;
        String Long_Short = "";
        String Approach_Maturity = "";
        String Approach_Strike_price ="";

        Float Entry_Point = null ,
                Entry_Option_Point =null,
                Exit_Point = null,
                Exit_Option_Point = null,
                Exit_Benifit_Point = null ,
                Exit_Damage_Point = null;

        int Day_To_Stop_Loss = 4;


        Map<String, Float> Total_Performance = new LinkedHashMap();
        Map<String, Float> Total_Option_Performance = new LinkedHashMap();
        Map<String, Float> Win_Session = new LinkedHashMap();
        Map<String, Float> Total_Session = new LinkedHashMap();
        int Day = 0;
        for (Table_Small_Taiwan_Feature Day_Data : All_Data){
            System.out.printf("%s  開盤: %.0f  最高: %.0f  最低 : %.0f   收盤 : %.0f  %n", Day_Data.date,Day_Data.open,Day_Data.high,Day_Data.low,Day_Data.close);
            if(!Approach) {
                if(Day_Data.MA_5!=null&&Day_Data.MA_10!=null&&Day_Data.MA_15!=null&&Day_Data.MA_30!=null&&
                        Day_Data.close < Day_Data.MA_5
                        && Day_Data.high > Day_Data.MA_5
                ){ //收上引線不進場
                    Entry_Point = Day_Data.close; //進場點數
                    Exit_Benifit_Point =  Entry_Point * 1.2f; //停利點數
                    Approach = true;//進場
                    Long_Short = "Long"; //做多
                    Day = 1;//第0天
                    System.out.println(Day_Data.date + " 進場做多點數 : "+ Entry_Point +"停利點位:" + Exit_Benifit_Point);

                    Table_Option Approch_option = fdDao.get_Option_Date_Close_Settlement_data(Day_Data.date,30,1000,4);
                    if (Approch_option == null){
                        have_option_information = false;
                    }else {
                        have_option_information = true;
                    }
                    if(have_option_information) {
                        Entry_Option_Point = Approch_option.close;
                        Approach_Maturity = Approch_option.Maturity;
                        Approach_Strike_price = Approch_option.Strike_price;
                        System.out.println(Day_Data.date + "選擇權進場做多" +
                                "約 = " + Approach_Maturity +
                                " 履約價 = " + Approach_Strike_price +
                                " 收盤價 = " + Entry_Option_Point
                                + " 剩幾日到期 = " + Approch_option.Day_To_Finish
                        );
                    }
                }
            }
            else if(Approach && Long_Short.equals("Long")){//已進場做多 判斷停利停損
                boolean Win = false;
                if(Day_Data.high > Exit_Benifit_Point){  //Day_Data.close > Day_Data.MA_5 * 1.05 ||
                    Exit_Point =  Day_Data.close; //停利點數
                    Approach = false;

                    if(have_option_information) {
                        Table_Option Exit_option = fdDao.get_option_data(Day_Data.date, Approach_Maturity, Approach_Strike_price, "call");
                        Exit_Option_Point = Exit_option.close;
                        System.out.println("選擇權停利點數 : "+ Exit_Option_Point);
                    }
                    System.out.println("停利點數 : "+ Exit_Point);
                }
                else if(Day == Day_To_Stop_Loss){  //第三天沒漲超過100就算輸
                    Exit_Point = Day_Data.close; //停損點數
                    Approach = false;

                    if(have_option_information) {

                        Table_Option Exit_option = fdDao.get_option_data(Day_Data.date, Approach_Maturity, Approach_Strike_price, "call");
                        if( Exit_option == null) { //已歸零沒資料
                            Exit_Option_Point = 0f;
                        }else {
                            Exit_Option_Point = Exit_option.close;
                        }
                        System.out.println("選擇權停損點數 : " + Exit_Option_Point);

                    }

                    System.out.println("停損點數 : "+Exit_Point);

                }

                if(!Approach){//出場

                    String year = Day_Data.date.substring(0,4);
                    Float init_vale = 0f;
                    Float init_option_vale = 0f;
                    Float Denominator = 0f;
                    Float Numerator = 0f;
                    if (Total_Performance.containsKey(year)){
                        init_vale = Total_Performance.get(year);
                    }
                    if (Total_Option_Performance.containsKey(year)){
                        init_option_vale = Total_Option_Performance.get(year);
                    }
                    if (Win_Session.containsKey(year)){
                        Numerator = Win_Session.get(year);//出場一次就分子+1
                    }
                    if (Total_Session.containsKey(year)){
                        Denominator = Total_Session.get(year);//出場一次就分母+1
                    }
                    //買必put避險
                    Float change_value = Exit_Point - Entry_Point - 2;
                    if (change_value > 0f){
                        Win = true;
                    }

                    if(have_option_information) {
                        Float change_option_value = Exit_Option_Point - Entry_Option_Point - 2;
                        Total_Option_Performance.put(year , init_option_vale + change_option_value);
                        System.out.println("選擇權單次績效 : "+ change_option_value +"歷時天數 : " +Day);
                    }

                    Total_Performance.put(year , init_vale + change_value);
                    Total_Session.put(year , Denominator+1);
                    if (Win){
                        Win_Session.put(year , Numerator+1);
                    }
                    if (Math.abs(change_value) > 200){
                        System.out.println("-----------單次績效 : "+ String.valueOf(change_value) +"歷時天數 : " +String.valueOf(Day) );
                    }else {
                        System.out.println("單次績效 : "+ String.valueOf(change_value) +"歷時天數 : " +String.valueOf(Day) );
                    }
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

        AtomicReference<Float> total_option_v = new AtomicReference<>(0f);
        Total_Option_Performance.forEach((k, v) -> {
            System.out.println("選擇權年份: " + k + " 績效:" + v);
            total_option_v.updateAndGet(v1 -> v1 + v);
        });
        System.out.println("選擇權總績效 : "+ total_option_v);


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
            System.out.println("年份: " + k_1 + " 勝率:" + temp_win/v_1  +" " +temp_win +"/" + v_1);
            total_session.updateAndGet(v1_1 -> v1_1 + v_1);
        });
        System.out.println("總勝率 : "+ Float.valueOf(win_session.toString())/Float.valueOf(total_session.toString())
                +" " +win_session.toString().replace(".0","")
                +"/" + total_session.toString().replace(".0","") );
    }

    @Test
    public void delete_data_test(){
        java.util.Date time = new java.util.Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String day = format.format(time);
        System.out.println(day);
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FeatureDatabaseDao fdDao;
        FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
        fdDao = fdb.FeatureDatabaseDao();
        fdDao.Delete_after_day(day);


    }

    @Test
    public void Strategy_test(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FeatureDatabaseDao fdDao;
        FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
        fdDao = fdb.FeatureDatabaseDao();
        Table_Option option = fdDao.get_Option_Date_Close_Settlement_data("20011225",50,1000,2);
        System.out.println("Maturity = "+ option.Maturity+
                " close = "+option.close
                + " Day_To_Finish = " +option.Day_To_Finish
                +" Strike_price = "+option.Strike_price);
        Table_Option option_after = fdDao.get_option_data("20011227","200201","5300","call");
        System.out.println("Maturity = "+ option_after.Maturity+
                " close = "+option_after.close
                + " Day_To_Finish = " +option_after.Day_To_Finish
                +" Strike_price = "+option_after.Strike_price);
    }

    @Test
    public void get_day_unixtime(){

        DateFormat format = new SimpleDateFormat("yyyyMMddHHmm"); //定义日期格式化的格式
        String classDateString = "20201003"+"1400";//需要加减的字符串型日期
        Date classDate = null;//把字符串转化成指定格式的日期
        try {
            classDate = format.parse(classDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance(); //使用Calendar日历类对日期进行加减
        calendar.setTime(classDate);
        calendar.add(Calendar.DATE, +2);
        classDate = calendar.getTime();//获取加减以后的Date类型日期
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(classDate.getTime());
    }


//    @Test
//    public void get_now_option_data(){
//        OptionUtil OPU = new OptionUtil();
//        OPU.get_all_month();
//    }
//
//
//    @Test
//    public void canvas_test(){
//        OptionUtil OPU = new OptionUtil();
//        OPU.get_all_month();
//    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("com.example.taiwanworkday", appContext.getPackageName());
        new APIUtil().update();

        //等callback
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}