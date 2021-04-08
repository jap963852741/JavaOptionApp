package com.example.javaoptionapp;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.javaoptionapp.Repository.bean.CMoneyBean;
import com.example.javaoptionapp.Repository.bean.CMoneyTokenResponse;
import com.example.javaoptionapp.Repository.network.api.home.CMoneyService;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(AndroidJUnit4.class)
public class HomeRetrofitTest {

    @Test
    public void CMoneyTokenApi(String serialNumber){
        String TAG = "HomeRetrofitTest";
        String baseUrl = "https://owl.cmoney.com.tw/OwlApi/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        CMoneyService api = retrofit.create(CMoneyService.class);
        Observer<CMoneyTokenResponse> observer = new Observer<CMoneyTokenResponse>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull CMoneyTokenResponse cmoneyTolkenResponse) {
                Log.e(TAG,cmoneyTolkenResponse.getToken());
                CMoneyInformation(cmoneyTolkenResponse.getToken(),serialNumber);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
        api.cMoneyToken()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(observer);
        //等callback
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG,"finish");
    }

//    @Test
    public void CMoneyInformation(String token,String serialNumber){
        String TAG = "HomeRetrofitTest";
        String baseUrl = "https://owl.cmoney.com.tw/OwlApi/";
        Log.e(TAG,"Bearer "+token);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        CMoneyService api = retrofit.create(CMoneyService.class);
        Observer<CMoneyBean> observer = new Observer<CMoneyBean>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull CMoneyBean cmoneybean) {
                Log.e(TAG, cmoneybean.getTitle().toString());
                Log.e(TAG, cmoneybean.getData().toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
        api.cMoneyInformation("Bearer "+token,"BA1-23691a")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(observer);
        //等callback
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG,"finish");
    }

}
