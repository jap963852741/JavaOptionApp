package com.example.javaoptionapp.ui.wanggoo;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;
import com.example.javaoptionapp.room.Table_Small_Taiwan_Feature;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class WangGooUtil {
    private  String url;
    private OkHttpClient okHttpClient;
    public HashMap<String, HashMap<String,String>> hashmap_time_data = new HashMap<String, HashMap<String,String>>();
    public WangGooUtil(MutableLiveData  mld){
        this.seturl();
        post(mld);
    }
    public WangGooUtil(){
    }
    public void seturl() {
        int IntTime = 0;  //幾點幾分
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            IntTime = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm")));
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmm");// HH:mm:ss
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            IntTime = Integer.parseInt(simpleDateFormat.format(date));
        }
        Log.i("IntTime",String.valueOf(IntTime));
        if(IntTime > 830 && IntTime < 1455){
            url = "https://www.wantgoo.com/investrue/wtx&/daily-candlestick"; //早盤
        }else{
            url = "https://www.wantgoo.com/investrue/wtxp&/daily-candlestick"; //晚盤
        }
    }
    public void post(MutableLiveData mld){
        Context appContext = WangGooFragment.strategyutil_context;
        FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
        FeatureDatabaseDao fdDao =fdb.FeatureDatabaseDao();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("responce",String.valueOf(response.code()));
                if (response.code() == 200) {
                    String result = null;
                    try {
                        result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    result = result.replace("{","").replace("}","").replace("\"","");
                    mld.postValue(mld.getValue()+result);
                    update_today_db(fdDao);
                    StrategyUtil stu = new StrategyUtil(mld);
                }
            }
        }).start();
    }
    public void update_today_db(FeatureDatabaseDao fdDao){
        HashMap<String, HashMap<String,String>> hashmap_time_data = this.hashmap_time_data;
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
                fdDao.update(dstf);
            }else {
                fdDao.insertAll(dstf);
            }
            fdDao.update_ma5(key);
            fdDao.update_days_average(key);
        }

        fdDao.update_ALL_before_5_days_average(fdDao.get_ma5_begin_date());

        java.util.Date today = new java.util.Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String day = format.format(today);
        fdDao.Delete_after_day(day);
    }
}
