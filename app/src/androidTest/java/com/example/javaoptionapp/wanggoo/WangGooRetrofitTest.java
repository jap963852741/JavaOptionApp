package com.example.javaoptionapp.wanggoo;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.javaoptionapp.Repository.WangGooRepository;
import com.example.javaoptionapp.Repository.bean.wangGoo.WangGooBean;
import com.example.javaoptionapp.Repository.network.DataSource.WangGooDataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.LinkedHashMap;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

@RunWith(AndroidJUnit4.class)
public class WangGooRetrofitTest {
    public static String TAG = "WangGooRetrofitTest";
    public static String response;

    @Test
    public void WangGooHtml(){
        Observer<ResponseBody> observer = new Observer<ResponseBody>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
                try {
                    response = responseBody.string();
                    Log.e(TAG,response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
        new WangGooRepository(new WangGooDataSource()).getWangGooDataSource().getService()
                .WangGooHtml()
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


    @Test
    public void WangGooBean(){
        Observer<WangGooBean> observer = new Observer<WangGooBean>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull WangGooBean wangGooBean) {
                Log.e(TAG,wangGooBean.toString());
                Log.e(TAG,wangGooBean.getTime());
            }
            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
        new WangGooRepository(new WangGooDataSource()).getWangGooDataSource().getService()
                .WangGooBean()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(observer);
        //等callback
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
