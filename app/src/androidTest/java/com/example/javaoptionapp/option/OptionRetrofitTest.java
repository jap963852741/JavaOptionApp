package com.example.javaoptionapp.option;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.javaoptionapp.Repository.YahooOptionRepository;
import com.example.javaoptionapp.Repository.bean.option.YahooOptionBean;
import com.example.javaoptionapp.Repository.network.DataSource.YahooOptionDataSource;
import com.example.javaoptionapp.Repository.network.api.option.YahooOptionService;
import com.example.javaoptionapp.ui.Option.OptionUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(AndroidJUnit4.class)
public class OptionRetrofitTest {
    public static String TAG = "OptionRetrofitTest";
    public static String response;

    static String my_response(String my_response) {
        return my_response;
    }

    @Test
    public void optionHtml(){
        Observer<ResponseBody> observer = new Observer<ResponseBody>(){
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
                try {
                    response = responseBody.string();
                    LinkedHashMap<String,String> option_strike_price  = new LinkedHashMap<String,String>();
                    ArrayList<YahooOptionBean> listYahooOptionBean = new ArrayList<YahooOptionBean>();

                    Document doc = Jsoup.parse(response);
                    Elements Strike_prices = doc.getElementsByClass("ext-big-tb-center");
                    Elements links = doc.select("a");

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
                                if (temp_index == OptionUtil.Href_Num.Deal_price){
                                    option_strike_price.put(sp,text);
                                }
                            }
                        }
                    }

                    String Text = "履約價 成交價  目標,";
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

                        Text += key +
                                "  " + price+
                                "  " + target
                                + ",";

                    }


                    for (YahooOptionBean yahooOptionBean : listYahooOptionBean){
                        Log.e(TAG,"yahooOptionBean = "+ yahooOptionBean.getStrikePrice() + yahooOptionBean.getDealPrice()+yahooOptionBean.getGoal());

                    }


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
        new YahooOptionRepository(new YahooOptionDataSource()).getyahooOptionDataSource().getService()
                .YahooOptionHtml()
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
    public void HTML_parser(){
        LinkedHashMap<String,String> option_strike_price  = new LinkedHashMap<String,String>();;
        Document doc = Jsoup.parse(response);
        Elements Strike_prices = doc.getElementsByClass("ext-big-tb-center");
        Elements links = doc.select("a");

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
                    if (temp_index == OptionUtil.Href_Num.Deal_price){
                        option_strike_price.put(sp,text);
                    }
                }
            }
        }

        String Text = "履約價 成交價  目標,";
        for (String key : option_strike_price.keySet()){
        String target = " ";
        String price = "";
        if (!option_strike_price.get(key).equals("--")){
            target = String.valueOf(Integer.parseInt(key)+Float.parseFloat(option_strike_price.get(key).replace(".00","")));
            price = String.format(" %6s",option_strike_price.get(key));
        }else {
            price = String.format(" %6s","--");
        }
        Text += key +
                "  " + price+
                "  " + target
                + ",";
        Log.e(TAG,"Text = "+ Text);

        }

    }
}
