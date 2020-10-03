package com.example.javaoptionapp.ui.wanggoo;

import android.os.Build;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WangGooUtil {
    private  String url;
    private OkHttpClient okHttpClient;
    public WangGooUtil(MutableLiveData  mld){
        this.seturl();
        post(mld);
    }
    public WangGooUtil() {

    }
    public void seturl() {
        int IntTime = 0;  //幾點幾分
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            IntTime = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm")));
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmm");// HH:mm:ss
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            IntTime = Integer.parseInt(simpleDateFormat.format(date));
        }
        Log.i("IntTime",String.valueOf(IntTime));
        if(IntTime > 830 && IntTime < 1455){
            url = "https://www.wantgoo.com/investrue/wtx&/daily-candlestick"; //早盤
        }else{
            url = "https://www.wantgoo.com/investrue/wtxp&/daily-candlestick"; //晚盤
        }
    }
    public void post(MutableLiveData mld){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15000, TimeUnit.MILLISECONDS)
                .readTimeout(15000, TimeUnit.MILLISECONDS);
        okHttpClient = builder.build();
        Request request = new Request.Builder().addHeader("User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                .url(url)
                .get()
                .build();

            //将请求添加到请求队列等待执行，并返回执行后的Response对象
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("responce",String.valueOf(response.code()));
                if (response.code() == 200) {
                    String result = null;
                    try {
                        result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    result = result.replace("{","").replace("}","").replace("\"","");
                    mld.postValue(mld.getValue()+result);
                }
            }
        }).start();




    }
}
