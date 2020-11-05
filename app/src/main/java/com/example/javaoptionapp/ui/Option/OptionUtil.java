package com.example.javaoptionapp.ui.Option;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import androidx.lifecycle.MutableLiveData;

import com.example.javaoptionapp.ui.wanggoo.WangGooFragment;

import static com.example.javaoptionapp.ui.Option.OptionFragment.UPDATE_BUTTON_TEXT;
import static com.example.javaoptionapp.ui.Option.OptionFragment.choose_option_month_button;
import static com.example.javaoptionapp.ui.Option.OptionFragment.mUI_Handler;

public class OptionUtil {
    private  int month_index;
    public static String option_month_text;
    private  String url;
    public static LinkedHashMap<String,String> option_strike_price  = new LinkedHashMap<String,String>();;
    public MutableLiveData<String> mText;

    public OptionUtil(MutableLiveData<String> mText){
        this.mText = mText;
        seturl(0);
        get_all_month();
    }
    public OptionUtil(MutableLiveData<String> mText,int month){
        this.mText = mText;
        this.month_index = month;
        seturl(month);
        post();
    }
    public OptionUtil(){
        seturl(0);
    }

    public void seturl(int month) {
        if (OptionFragment.Option_Month != null) {
            option_month_text = OptionFragment.Option_Month[month];
            url = "https://tw.screener.finance.yahoo.net/future/aa03?fumr=futurepart&opmr=optionpart&opcm=WTXO&opym=" + OptionFragment.Option_Month[month];
        }else {
            url = "https://tw.screener.finance.yahoo.net/future/aa03?fumr=futurepart&opmr=optionpart&opcm=WTXO&opym=";
        }
    }
    public void post(){
        Thread thread = new my_thread();
        thread.start();
    }
    class my_thread extends Thread{
        @Override
        public void run() {
            try {
                option_strike_price.clear();
                OptionFragment.mUI_Handler.sendEmptyMessage(UPDATE_BUTTON_TEXT);
                System.out.println(url);
                Document doc = Jsoup.connect(url).get();
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
                            if (temp_index == Href_Num.Deal_price){
                                option_strike_price.put(sp,text);
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
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
            }
            mText.postValue(Text);
        }
    }
    public interface Href_Num{
        final Integer Buy_price = 1;
        final Integer Sell_price = 2;
        final Integer Deal_price = 3;
        final Integer Up_Down = 4;
        final Integer Open_position = 5;
    }

    class get_month_thread extends Thread{
        @Override
        public void run() {
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
                Element month_value = doc.getElementById("itemselect");
                OptionFragment.Option_Month = month_value.text().split(" ");
                seturl(0);
                post();
                Log.i("month_value",month_value.text());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void get_all_month(){
        Thread get_month_thread = new get_month_thread();
        get_month_thread.start();
    }

}
