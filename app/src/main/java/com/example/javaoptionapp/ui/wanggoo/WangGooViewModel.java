package com.example.javaoptionapp.ui.wanggoo;

import android.content.Context;
import android.os.Build;
import android.util.Log;


import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.javaoptionapp.Repository.WangGooRepository;
import com.example.javaoptionapp.Repository.bean.wangGoo.StrategyResultBean;
import com.example.javaoptionapp.Repository.bean.wangGoo.WangGooBean;
import com.example.javaoptionapp.Repository.network.DataSource.WangGooDataSource;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;
import com.example.javaoptionapp.room.Table_Small_Taiwan_Feature;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static java.lang.Thread.sleep;


public class WangGooViewModel extends ViewModel {

    private String TAG = "WangGooViewModel";
    private MutableLiveData<String> mText;
    private MutableLiveData<WangGooBean> wangGooBeanMutableLiveData;
    private MutableLiveData<StrategyResultBean> strategyResultBeanMutableLiveData;
    private FeatureDatabaseDao fdDao;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public WangGooViewModel() {
        mText = new MutableLiveData<>();
        WangGooUtil wgt = new WangGooUtil(mText);
    }

    public WangGooViewModel(Context context){
        wangGooBeanMutableLiveData = new MutableLiveData<>();
        strategyResultBeanMutableLiveData = new MutableLiveData<>();
        fdDao = FeatureDatabase.getInstance(context).FeatureDatabaseDao();
        wangGooApi();
        wangGooHistoryApiUpdateDb();
        getStrategy();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<WangGooBean> getWangGooBean() {
        return wangGooBeanMutableLiveData;
    }
    public LiveData<StrategyResultBean> getStrategyResultBean() {return strategyResultBeanMutableLiveData;}

    public void wangGooApi(){
        Observer<WangGooBean> observer = new Observer<WangGooBean>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @Override
            public void onNext(@NonNull WangGooBean wangGooBean) {
                wangGooBeanMutableLiveData.postValue(wangGooBean);
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
    }

    public void wangGooHistoryApiUpdateDb(){
        Observer<List<WangGooBean>> observer = new Observer<List<WangGooBean>>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @Override
            public void onNext(@NonNull List<WangGooBean> wangGooBeanList) {
                ArrayList need_to_update_list = new ArrayList();
                ArrayList need_to_insert_list = new ArrayList();
                for (WangGooBean wangGooBean : wangGooBeanList) {
                    Table_Small_Taiwan_Feature dstf = new Table_Small_Taiwan_Feature(
                            wangGooBean.getTimeYYYYMMDD(),
                            Float.parseFloat(wangGooBean.getOpen()),
                            Float.parseFloat(wangGooBean.getHigh()),
                            Float.parseFloat(wangGooBean.getLow()),
                            Float.parseFloat(wangGooBean.getClose()),
                            Float.parseFloat(wangGooBean.getVolume()));

                    Table_Small_Taiwan_Feature the_date_information = fdDao.get_Date_data(wangGooBean.getTradeDateYYYYMMDD());
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

            }
            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
        new WangGooRepository(new WangGooDataSource()).getWangGooDataSource().getHistoryService()
                .WangGooHistoryBeanArray(String.valueOf(System.currentTimeMillis()),"490")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(observer);
    }

    public void getStrategy(){
        Observer<StrategyResultBean> observer = new Observer<StrategyResultBean>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @Override
            public void onNext(@NonNull StrategyResultBean s) {
                strategyResultBeanMutableLiveData.postValue(s);
                Log.e(TAG,s.toString());
            }
            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
        new WangGooDataSource().getStrategy(fdDao)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(observer);
    }



}