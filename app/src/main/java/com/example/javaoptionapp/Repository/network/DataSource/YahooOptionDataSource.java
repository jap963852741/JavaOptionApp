package com.example.javaoptionapp.Repository.network.DataSource;

import com.example.javaoptionapp.Repository.network.api.home.CMoneyService;
import com.example.javaoptionapp.Repository.network.api.option.YahooOptionService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class YahooOptionDataSource {

    public YahooOptionService getService(){
        String baseUrl = "https://tw.screener.finance.yahoo.net/future/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        YahooOptionService api = retrofit.create(YahooOptionService.class);
        return api ;
    }

}
