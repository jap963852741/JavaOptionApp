package com.example.javaoptionapp.ui.wanggoo;

import android.content.Context;
import android.os.Build;
import android.util.Log;


import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.javaoptionapp.R;
import com.example.javaoptionapp.Repository.WangGooRepository;
import com.example.javaoptionapp.Repository.bean.wangGoo.StrategyResultBean;
import com.example.javaoptionapp.Repository.bean.wangGoo.WangGooBean;
import com.example.javaoptionapp.Repository.network.DataSource.WangGooDataSource;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;
import com.example.javaoptionapp.room.Table_Small_Taiwan_Feature;
import com.example.javaoptionapp.util.dialog.LoadingDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private Context myContext;
    private LoadingDialog loadingdialog;


    public WangGooViewModel(Context context){
        wangGooBeanMutableLiveData = new MutableLiveData<>();
        strategyResultBeanMutableLiveData = new MutableLiveData<>();
        fdDao = FeatureDatabase.getInstance(context).FeatureDatabaseDao();
        myContext = context;
        loadingdialog = LoadingDialog.getInstance(context);
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
        loadingdialog.show();
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
                loadingdialog.dismiss();
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onComplete() {
                loadingdialog.dismiss();
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

    public void update_all_history(){
        loadingdialog.show();
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                InputStream inputStream = myContext.getResources().openRawResource(R.raw.all_data);
                HashMap<String, HashMap<String,String>> hashmap_time_data_all = new HashMap<String, HashMap<String,String>>();

                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuffer sb = new StringBuffer("");
                    String result;
                    while ((result = reader.readLine()) != null) {
//              資料轉換
                        result = result.replace("[","").replace("]","").replace(" ","");
                        result = result.replace("'","");
                        String[] temp_list = result.split(",");

                        HashMap<String, String> temp_map = new HashMap<String, String>();
                        String temp_time = "";
                        for (String bb : temp_list){
                            String[] final_list = bb.split(":");
                            if(final_list[0].equals("time")){
                                long millisecond = Long.parseLong(final_list[1]);
                                Date date = new Date(millisecond);
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                                temp_time = format.format(date);
                            }else{
                                temp_map.put(final_list[0],final_list[1]);
                            }
                        }
                        hashmap_time_data_all.put(temp_time,temp_map);
                    }
                    System.out.println(hashmap_time_data_all);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                update_db(hashmap_time_data_all);
            }
        });
        thread.start();
    }

    public void update_db(HashMap<String, HashMap<String,String>> hashmap_time_data){
        ArrayList need_to_update_list = new ArrayList();
        ArrayList need_to_insert_list = new ArrayList();
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
        loadingdialog.dismiss();

    }

}