package com.example.javaoptionapp.ui.home;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.javaoptionapp.Repository.bean.CMoneyBean;
import com.example.javaoptionapp.Repository.bean.CMoneyTokenResponse;
import com.example.javaoptionapp.Repository.CMoneyRepository;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.ArrayList;


public class HomeViewModel extends ViewModel{
    private String TAG = "HomeViewModel";
    /**
     * BA1-23690a 大盤籌碼日報
     * BA1-23689a 大盤K線
     * BA1-23695a 大盤漲跌家數
     * BA1-23691a 大盤外資
     * BA1-23693a 大盤官股
     * */
    public String item_schma = "BA1-23689a";
    private static ArrayList Market_Date;//全域
    private static int date_index;
    private static CMoneyRepository cMoneyRepository;

    public static MutableLiveData<String>  mText;
    public static MutableLiveData<CMoneyBean>  cMoneyBeanMutableLiveData;
    public static MutableLiveData<ArrayList>  MarketDateMutableLiveData;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        cMoneyBeanMutableLiveData = new MutableLiveData<>();
        MarketDateMutableLiveData = new MutableLiveData<>();
        Log.i("Now","HomeViewModel()");
    }

    public HomeViewModel(CMoneyRepository homeCMoneyRepository) {
        mText = new MutableLiveData<>();
        cMoneyBeanMutableLiveData = new MutableLiveData<>();
        MarketDateMutableLiveData = new MutableLiveData<>();
        cMoneyRepository = homeCMoneyRepository;
        Log.i("Now","HomeViewModel()");
    }


    public void CMoneyTokenApi(){
        Observer<CMoneyTokenResponse> observer = new Observer<CMoneyTokenResponse>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull CMoneyTokenResponse cmoneyTolkenResponse) {
                Log.d(TAG,cmoneyTolkenResponse.getToken());
                CMoneyInformation(cmoneyTolkenResponse.getToken(),item_schma);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
        cMoneyRepository.getcMoneyDataSource().getService()
                .cMoneyToken()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(observer);
    }

    public void CMoneyInformation(String token,String serialNumber){
        Observer<CMoneyBean> observer = new Observer<CMoneyBean>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull CMoneyBean cmoneybean) {
                Log.d(TAG, cmoneybean.getTitle().toString());
                Log.d(TAG, cmoneybean.getData().toString());
                cMoneyBeanMutableLiveData.postValue(cmoneybean);
                Market_Date = new ArrayList<String>();
                for(int i = 0; i < cmoneybean.getTitle().size(); i++){
                    if (cmoneybean.getTitle().get(i).equals("日期")){
                        date_index = i;
                    }
                }
                for(int i = 0; i < cmoneybean.getData().size() ;i++){
                    Market_Date.add(cmoneybean.getData().get(i).get(date_index));
                }
                Log.d(TAG, "Market_Date = " + Market_Date.toString());
                MarketDateMutableLiveData.postValue(Market_Date);
            }
            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        };
        cMoneyRepository.getcMoneyDataSource().getService()
                .cMoneyInformation("Bearer "+token,serialNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(observer);
    }

    public int getDate_date(String date){
        for(int i = 0; i < Market_Date.size() ;i++){
            if (Market_Date.get(i).equals(date)){
//                Log.e(TAG,"getDate_date = "+i);
                return i;
            }
        }
        return 0;
    }

    public LiveData<CMoneyBean> getCMoneyBean(){
        return cMoneyBeanMutableLiveData;
    }

    public LiveData<ArrayList> getMarket_Date(){
        return MarketDateMutableLiveData;
    }

    public LiveData<String> getText() {
        return mText;
    }
}