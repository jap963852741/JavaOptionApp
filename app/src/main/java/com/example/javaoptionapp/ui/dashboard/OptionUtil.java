package com.example.javaoptionapp.ui.dashboard;

import android.content.Context;
import android.util.Log;

import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;
import com.example.javaoptionapp.ui.wanggoo.StrategyUtil;
import com.example.javaoptionapp.ui.wanggoo.WangGooFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.room.Room;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OptionUtil {
    private  String url;
    private OkHttpClient okHttpClient;

    public OptionUtil(){
        seturl();
    }

    public void seturl() {
        url = "https://tw.screener.finance.yahoo.net/future/aa03?fumr=futurepart&opmr=optionpart&opcm=WTXO&opym=202011";
    }

    public void post(){
        Context appContext = OptionFragment.option_context;
//        FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
//        FeatureDatabaseDao fdDao =fdb.FeatureDatabaseDao();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15000, TimeUnit.MILLISECONDS)
                .readTimeout(15000, TimeUnit.MILLISECONDS);
        okHttpClient = builder.build();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .addHeader("Referrer"+"\t"+"Policy","strict-origin-when-cross-origin")
//                .addHeader("Remote Address","180.222.102.151:443")
                .url(url)
                .get()
                .build();
        //将请求添加到请求队列等待执行，并返回执行后的Response对象
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("responce","RUN");
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("responce",e.getMessage());
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
                    Log.i("option","result"+result);
                }
            }
        }).start();
    }


}
