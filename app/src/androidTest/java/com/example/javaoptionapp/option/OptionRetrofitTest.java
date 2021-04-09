package com.example.javaoptionapp.option;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.javaoptionapp.Repository.YahooOptionRepository;
import com.example.javaoptionapp.Repository.bean.option.YahooOptionBean;
import com.example.javaoptionapp.Repository.network.DataSource.YahooOptionDataSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

@RunWith(AndroidJUnit4.class)
public class OptionRetrofitTest {
    public static String TAG = "OptionRetrofitTest";
    public static String response;

    static String my_response(String my_response) {
        return my_response;
    }

//    @Test
//    public void optionHtml(){
//        Observer<ResponseBody> observer = new Observer<ResponseBody>(){
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//            }
//
//            @Override
//            public void onNext(@NonNull ResponseBody responseBody) {
//                try {
//                    response = responseBody.string();
//                    LinkedHashMap<String,String> option_strike_price  = new LinkedHashMap<String,String>();
//                    ArrayList<YahooOptionBean> listYahooOptionBean = new ArrayList<YahooOptionBean>();
//
//
//
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onError(@NonNull Throwable e) {
//                Log.e(TAG,e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//            }
//        };
//        new YahooOptionRepository(new YahooOptionDataSource()).getyahooOptionDataSource().getService()
//                .YahooOptionHtml()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(Schedulers.io())
//                .subscribe(observer);
//        //等callback
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.e(TAG,"finish");
//    }

    @Test
    public void HTML_parser(){
        LinkedHashMap<String,String> option_strike_price  = new LinkedHashMap<String,String>();;
        Document doc = Jsoup.parse(response);
        Elements Strike_prices = doc.getElementsByClass("ext-big-tb-center");
        Elements links = doc.select("a");

    }
}
