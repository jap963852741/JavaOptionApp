package com.example.javaoptionapp.Repository.network.DataSource;

import android.util.Log;

import com.example.javaoptionapp.Repository.network.api.option.YahooOptionService;
import com.example.javaoptionapp.Repository.network.api.wanggoo.WangGooService;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WangGooDataSource {

    private String TAG = "WangGooDataSource";

    public WangGooService getService(){

        String baseUrl;
        int IntTime = 0;  //幾點幾分
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            IntTime = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm")));
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmm");// HH:mm:ss
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            IntTime = Integer.parseInt(simpleDateFormat.format(date));
        }
        if(IntTime > 830 && IntTime < 1455){
            baseUrl = "https://www.wantgoo.com/investrue/wtx&/"; //早盤
        }else{
            baseUrl = "https://www.wantgoo.com/investrue/wtxp&/"; //晚盤
        }

        Log.i(TAG, "baseUrl = " + baseUrl);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        return retrofit.create(WangGooService.class);
    }

}
