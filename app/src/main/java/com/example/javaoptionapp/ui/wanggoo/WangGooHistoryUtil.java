package com.example.javaoptionapp.ui.wanggoo;

import android.os.Build;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
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
    public WangGooHistoryUtil(){
        super();
        this.seturl();
        post();
    }
    @Override
    public void seturl() {
//        System.out.println(System.currentTimeMillis());
        url = "https://www.wantgoo.com/investrue/wmt&/historical-daily-candlesticks?before="+System.currentTimeMillis()+"&top=490";
        System.out.println(url);

    }
    public void post(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15000, TimeUnit.MILLISECONDS)
                .readTimeout(15000, TimeUnit.MILLISECONDS);
        okHttpClient = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        System.out.println("post");
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
            result = result.replace("{","").replace("[","").replace("]","");
            System.out.println(result);
            value = result.split(Pattern.quote("},"));
            System.out.println(result);
            for (String aa : value){
                aa = aa.replace("\"","");
                String[] temp_list = aa.split(",");
                HashMap<String, String> temp_map = new HashMap<String, String>();
                String temp_time = "";
                for (String bb : temp_list){
                    String[] final_list = bb.split(":");
                    if(final_list[0].equals("time")){
                        long millisecond = Long.parseLong(final_list[1]);
                        Date date = new Date(millisecond);
                        SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
                        temp_time = format.format(date);
                    }else if(final_list[0].equals("millionAmount")){
                        hashmap_time_data.put(temp_time,temp_map);
                    }else{
                        temp_map.put(final_list[0],final_list[1]);
                    }
                }
//                System.out.println("temp_map"+temp_map.toString());
            }
        }
    }
}
