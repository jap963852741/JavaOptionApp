package com.example.javaoptionapp.ui.wanggoo;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.javaoptionapp.R;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;
import com.example.javaoptionapp.room.Table_Small_Taiwan_Feature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WangGooHistoryUtil extends WangGooUtil{
    private String url;
    private OkHttpClient okHttpClient;
    private String[] value;
    public HashMap<String, HashMap<String,String>> hashmap_time_data = new HashMap<String, HashMap<String,String>>();
    private FeatureDatabaseDao fdDao;
    public WangGooHistoryUtil(){
        super();
        this.seturl();
    }
    public WangGooHistoryUtil(String time){
        super();
//        WangGooFragment.mUI_Handler.sendEmptyMessage(WangGooFragment.MSG_LoadingDialog_Begin);
        this.seturl(time);
        post();
    }
    @Override
    public void seturl() {
//        System.out.println(System.currentTimeMillis());
        url = "https://www.wantgoo.com/investrue/wmt&/historical-daily-candlesticks?before="+System.currentTimeMillis()+"&top=490";
        System.out.println(url);
    }

    public void seturl(String time) {
//        System.out.println(System.currentTimeMillis());
        url = "https://www.wantgoo.com/investrue/wmt&/historical-daily-candlesticks?before="+time+"&top=490";
        System.out.println(url);
    }
    public void post(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                System.out.println("post run");
                Context appContext = WangGooFragment.strategyutil_context;
                FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
                fdDao =fdb.FeatureDatabaseDao();
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(15000, TimeUnit.MILLISECONDS)
                        .readTimeout(15000, TimeUnit.MILLISECONDS);
                okHttpClient = builder.build();
                Request request = new Request.Builder().addHeader("User-Agent",
                        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                        .url(url)
                        .get()
                        .build();

                //将请求添加到请求队列等待执行，并返回执行后的Response对象
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    System.out.println(e);

                    e.printStackTrace();
                }
//                Log.i("responce",String.valueOf(response.code()));
                System.out.println(response.code());
                if (response.code() == 200) {
                    String result = null;
                    try {
                        result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    result = result.replace("{", "").replace("[", "").replace("]", "");
                    System.out.println(result);
                    Log.i("WangGooHistoryUtil","result"+result);
                    value = result.split(Pattern.quote("},"));
                    System.out.println(result);
                    for (String aa : value) {
                        aa = aa.replace("\"", "");
                        String[] temp_list = aa.split(",");
                        HashMap<String, String> temp_map = new HashMap<String, String>();
                        String temp_time = "";
                        for (String bb : temp_list) {
                            String[] final_list = bb.split(":");
                            if (final_list[0].equals("time")) {
                                long millisecond = Long.parseLong(final_list[1]);
                                Date date = new Date(millisecond);
                                SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
                                temp_time = format.format(date);
                            } else if (final_list[0].equals("millionAmount")) {
                                hashmap_time_data.put(temp_time, temp_map);
                            } else {
                                temp_map.put(final_list[0], final_list[1]);
                            }
                        }
//                System.out.println("temp_map"+temp_map.toString());
                    }
                    update_db(hashmap_time_data);
                    fdb.close();
                }
            }
        });

        thread.start();
    }
    public void update_db(HashMap<String, HashMap<String,String>> hashmap_time_data){
        System.out.println("update_db");
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
        //如果資料太多 array 內存會爆  >>就要一筆一筆update
        if (need_to_update_list != null) {
            fdDao.updateAllTable_Small_Taiwan_Feature(need_to_update_list);
        }
        if (need_to_insert_list != null) {
            fdDao.insertAllTable_Small_Taiwan_Feature(need_to_insert_list);
        }

        fdDao.update_ALL_ma5(fdDao.get_ma5_begin_date());
        fdDao.update_ALL_ma10(fdDao.get_ma10_begin_date());
        fdDao.update_ALL_ma15(fdDao.get_ma15_begin_date());
        fdDao.update_ALL_ma30(fdDao.get_ma30_begin_date());
        fdDao.update_ALL_bias5(fdDao.get_ma5_begin_date());
        fdDao.update_ALL_before_5_days_average(fdDao.get_ma5_begin_date());

        java.util.Date today = new java.util.Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String day = format.format(today);
        fdDao.Delete_after_day(day);
//        WangGooFragment.mUI_Handler.sendEmptyMessage(WangGooFragment.MSG_LoadingDialog_Finish);
        WangGooFragment.loadingdialog.dismiss();
    }
    public void update_all_history(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                Context appContext = WangGooFragment.strategyutil_context;
                FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
                fdDao =fdb.FeatureDatabaseDao();
                InputStream inputStream = WangGooFragment.strategyutil_context.getResources().openRawResource(R.raw.all_data);
                HashMap<String, HashMap<String,String>> hashmap_time_data_all = new HashMap<String, HashMap<String,String>>();

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
                        hashmap_time_data_all.put(temp_time,temp_map);
                    }
                    System.out.println(hashmap_time_data_all);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                update_db(hashmap_time_data_all);
                fdb.close();

            }
        });
        thread.start();
    }

}
