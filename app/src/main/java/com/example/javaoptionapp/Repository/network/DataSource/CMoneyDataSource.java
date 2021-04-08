package com.example.javaoptionapp.Repository.network.DataSource;

import com.example.javaoptionapp.Repository.network.api.CMoneyService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CMoneyDataSource {

    public CMoneyService getService(){
        String baseUrl = "https://owl.cmoney.com.tw/OwlApi/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        CMoneyService api = retrofit.create(CMoneyService.class);
        return api ;
    }

}
