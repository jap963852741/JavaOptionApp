package com.example.javaoptionapp.ui.Option;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javaoptionapp.Repository.CMoneyRepository;
import com.example.javaoptionapp.Repository.YahooOptionRepository;
import com.example.javaoptionapp.Repository.bean.option.YahooOptionBean;
import com.example.javaoptionapp.Repository.network.DataSource.YahooOptionDataSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class OptionViewModel extends ViewModel {

    private String TAG = "OptionViewModel";
    private YahooOptionRepository yahooOptionRepository;
    public static MutableLiveData<ArrayList<YahooOptionBean>> mlistYahooOptionBean;
    public static MutableLiveData<String[]> mOption_Month;

    public OptionViewModel(YahooOptionRepository yahooOptionRepository) {
        this.yahooOptionRepository = yahooOptionRepository;
        mlistYahooOptionBean = new MutableLiveData<>();
        mOption_Month = new MutableLiveData<>();
    }

    public LiveData<ArrayList<YahooOptionBean>> getlistYahooOptionBean() {
        return mlistYahooOptionBean;
    }
    public LiveData<String[]> getOption_Month() {
        return mOption_Month;
    }
    public void yahooOptionApi(String month){
        Log.e(TAG,"yahooOptionApi ---> " + month);
        Observer<ResponseBody> observer = new Observer<ResponseBody>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
                try {
                    responseToListYahooOptionBean(responseBody.string());
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
        yahooOptionRepository.getyahooOptionDataSource().getService()
                .YahooOptionHtml(month)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(observer);
    }

    public void responseToListYahooOptionBean(String response){
        Log.e(TAG,response);
        final Integer Buy_price = 1;
        final Integer Sell_price = 2;
        final Integer Deal_price = 3;
        final Integer Up_Down = 4;
        final Integer Open_position = 5;

        LinkedHashMap<String,String> option_strike_price  = new LinkedHashMap<String,String>();
        ArrayList<YahooOptionBean> listYahooOptionBean = new ArrayList<YahooOptionBean>();
        listYahooOptionBean.add(new YahooOptionBean("履約價","成交價","目標"));

        Document doc = Jsoup.parse(response);
        Elements Strike_prices = doc.getElementsByClass("ext-big-tb-center");
        Elements links = doc.select("a");
        Element month_value = doc.getElementById("itemselect");
        mOption_Month.postValue(month_value.text().split(" "));

        for (Element element : Strike_prices) {
            String sp = element.text();
            String new_sp;
            new_sp = "B"+sp+"C";
            int temp_index = 0;
            for (Element link : links){
                String relHref = link.attr("href");
                String text = link.text();
                if (relHref.contains(new_sp)){
                    temp_index++;
                    if (temp_index == Deal_price){
                        option_strike_price.put(sp,text);
                    }
                }
            }
        }

        for (String key : option_strike_price.keySet()){
            String target = " ";
            String price = "";
            if (!option_strike_price.get(key).equals("--")){
                target = String.valueOf(Integer.parseInt(key)+Float.parseFloat(option_strike_price.get(key).replace(".00","")));
                price = String.format(" %6s",option_strike_price.get(key));
            }else {
                price = String.format(" %6s","--");
            }

            listYahooOptionBean.add(new YahooOptionBean(key,price,target));

        }

        mlistYahooOptionBean.postValue(listYahooOptionBean);
    }

}