package com.example.javaoptionapp.wanggoo;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.javaoptionapp.Repository.WangGooRepository;
import com.example.javaoptionapp.Repository.bean.wangGoo.StrategyResultBean;
import com.example.javaoptionapp.Repository.bean.wangGoo.WangGooBean;
import com.example.javaoptionapp.Repository.network.DataSource.WangGooDataSource;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

@RunWith(AndroidJUnit4.class)
public class wangGooHistoryRetrofitTest {
    public static String TAG = "WangGooHistoryRetrofitTest";
    public static String response;

    @Test
    public void WangGooHistoryHtml() {
        Observer<ResponseBody> observer = new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
                try {
                    response = responseBody.string();
                    Log.e(TAG, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
        new WangGooRepository(new WangGooDataSource()).getWangGooDataSource().getHistoryService()
                .WangGooHistoryHtml(String.valueOf(System.currentTimeMillis()), "490")
//                .subscribeOn(Schedulers.)
                .observeOn(Schedulers.io())
                .subscribe(observer);
        //等callback
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "finish");
    }


    @Test
    public void WangGooHistoryBean() throws ParseException {
        Observer<List<WangGooBean>> observer = new Observer<List<WangGooBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull List<WangGooBean> wangGooBeanList) {
                Log.e(TAG, wangGooBeanList.toString());
                for (WangGooBean wangGooBean : wangGooBeanList) {
                    Log.e(TAG, wangGooBean.getTime());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
        Date todayDate = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(todayDate);
        c.add(Calendar.DAY_OF_WEEK,-5);
        todayDate = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 16:00:00");
        String todayString = formatter.format(todayDate);
        Date date = formatter.parse(todayString);
        long timestamp = date.getTime();
        Log.e("franktest", String.valueOf(timestamp));

        new WangGooRepository(new WangGooDataSource()).getWangGooDataSource().getHistoryService()
                .WangGooHistoryBeanArray(String.valueOf( timestamp), "490")
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


    @Test
    public void getStrategy() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FeatureDatabaseDao fdDao;
        FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
        fdDao = fdb.FeatureDatabaseDao();
        Observer<StrategyResultBean> observer = new Observer<StrategyResultBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull StrategyResultBean s) {
                Log.e(TAG, "123");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
        new WangGooDataSource().getStrategy(fdDao)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(observer);
        //等callback
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "finish");
    }


}
