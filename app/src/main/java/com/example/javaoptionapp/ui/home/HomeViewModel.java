package com.example.javaoptionapp.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//MVVM是Model-View-ViewModel的简写，同样也是分为三部分：
//
//Model：代表你的基本业务逻辑
//
//View：显示内容的视图
//
//ViewModel：将前面两者联系在一起的对象
public class HomeViewModel extends ViewModel implements CmoneyUtil {
    public OkHttpClient okHttpClient;
    public final static long timeoutTime = 1000 * 15;
//    public RedirectInterceptor mRedirectInterceptor = new RedirectInterceptor();
    public String get_url = "https://owl.cmoney.com.tw/OwlApi/api/v2/json/";
    public String item_schma = "BA1-23689a";
    //BA1-23690a 大盤籌碼日報
    // BA1-23689a 大盤K線
//    BA1-23695a 大盤漲跌家數
//BA1-23691a 大盤外資
    private static JSONObject Market_information ;
    private static JSONObject token_json ;
    private static String token_string ;
    private static String Market_information_Title ;
    private static ArrayList Market_information_Title_Array ;
    private static ArrayList Market_information_NewestArray ;
    private static ArrayList Market_information_DataArray;
    private static ArrayList Title_information_Array;
    static ArrayList Market_Date;//全域
    private static int date_index;

    private static String choose_date;
    public static MutableLiveData<String>  mText;

    public static class SSLSocketClient {
        //获取这个SSLSocketFactory
        public static SSLSocketFactory getSSLSocketFactory() {
            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, getTrustManager(), new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //获取TrustManager
        private static TrustManager[] getTrustManager() {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };
            return trustAllCerts;
        }

        //获取HostnameVerifier
        public static HostnameVerifier getHostnameVerifier() {
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };
            return hostnameVerifier;
        }
    }
//    public MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        Log.i("Now","HomeViewModel()");
    }

    @Override
    public void post_tolken() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .sslSocketFactory(HomeViewModel.SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(HomeViewModel.SSLSocketClient.getHostnameVerifier());
//                .addInterceptor(mRedirectInterceptor);
        okHttpClient = builder.build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("appId", "20200904152146367")
                .addFormDataPart("appSecret", "4a56a0f0ee7f11ea93fb000c2932e359")
                .build();
        Request request = new Request.Builder()
                .url("https://owl.cmoney.com.tw/OwlApi/auth?appId=20200904152146367&appSecret=4a56a0f0ee7f11ea93fb000c2932e359")
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(callback_token);

    }
    private Callback callback_token = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.i("get_tolken", "onFailure");
            Log.i("get_tolken", e.toString());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.i("get_tolken", "onResponse");
            final StringBuffer sb = new StringBuffer(response.body().string());

            try {
                token_json = new JSONObject(String.valueOf(sb));
                token_string = token_json.getString("token");
                Log.i("token_string", token_string);
                get();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void get(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .sslSocketFactory(HomeViewModel.SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(HomeViewModel.SSLSocketClient.getHostnameVerifier());
//                .addInterceptor(mRedirectInterceptor);
        okHttpClient = builder.build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("Authorization","Bearer "+token_string);
                Request request = new Request.Builder()
                        .url(get_url+item_schma)
                        .header("Authorization", "Bearer "+token_string)
                        .get()
                        .build();
                okHttpClient.newCall(request).enqueue(callback);
            }
        }).start();
    }
    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.i("get", "onFailure");
            Log.i("get", e.toString());
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.i("get", "onResponse");
            final StringBuffer sb = new StringBuffer(response.body().string());
            Log.i("get", sb.toString());
            try {
                Market_information =new JSONObject(String.valueOf(sb));
                Market_information_Title = Market_information.getString("Title");
                Market_information_Title_Array = new ArrayList<String>(Arrays.asList(Market_information_Title.replaceAll("\\[" , "").replaceAll("]" , "").split(",")));
                String Market_information_DataArrayString = Market_information.getString("Data");
                Market_information_DataArray = new ArrayList<String>(Arrays.asList(Market_information_DataArrayString.replaceAll("\\[" , "").split("],")));
                String Market_information_Newest_String = Market_information_DataArray.get(0).toString();
                Log.i("Market_information_Newest_String", Market_information_Newest_String);
                Market_information_NewestArray = new ArrayList<String>(Arrays.asList(Market_information_Newest_String.split(",")));

//
                Title_information_Array = new ArrayList<String>();
                //取得第一筆資料的Array
                for(int i = 0; i <= Market_information_Title_Array.size()-1; i++){
                    if (Market_information_Title_Array.get(i).toString() == "日期"){
                        date_index = i;
                    }
                    String temp_word = Market_information_Title_Array.get(i).toString() + " : " + Market_information_NewestArray.get(i).toString();
                    Title_information_Array.add(temp_word);
                }
                Log.i("Market_information_DataArray",Market_information_DataArray.toString());
                Market_Date = new ArrayList<String>();
                for(int i = 0; i <= Market_information_DataArray.size()-1;i++){
                    String Market_information_String = Market_information_DataArray.get(i).toString();
                    ArrayList Market_information_TempArray = new ArrayList<String>(Arrays.asList(Market_information_String.split(",")));
                    Market_Date.add(Market_information_TempArray.get(date_index));
                }

                HomeViewModel.mText.postValue(Title_information_Array.toString());
                Log.i("Market_Date",Market_Date.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void post_tolken(String date) {
        choose_date = date;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .sslSocketFactory(HomeViewModel.SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(HomeViewModel.SSLSocketClient.getHostnameVerifier());
//                .addInterceptor(mRedirectInterceptor);
        okHttpClient = builder.build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("appId", "20200904152146367")
                .addFormDataPart("appSecret", "4a56a0f0ee7f11ea93fb000c2932e359")
                .build();
        Request request = new Request.Builder()
                .url("https://owl.cmoney.com.tw/OwlApi/auth?appId=20200904152146367&appSecret=4a56a0f0ee7f11ea93fb000c2932e359")
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(callback_token_date);
    }
    private Callback callback_token_date = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.i("get_tolken", "onFailure");
            Log.i("get_tolken", e.toString());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.i("get_tolken", "onResponse");
            final StringBuffer sb = new StringBuffer(response.body().string());

            try {
                token_json = new JSONObject(String.valueOf(sb));
                token_string = token_json.getString("token");
                Log.i("token_string", token_string);
                get(choose_date);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void get(String date) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .sslSocketFactory(HomeViewModel.SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(HomeViewModel.SSLSocketClient.getHostnameVerifier());
//                .addInterceptor(mRedirectInterceptor);
        okHttpClient = builder.build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("Authorization","Bearer "+token_string);
                Request request = new Request.Builder()
                        .url(get_url+item_schma)
                        .header("Authorization", "Bearer "+token_string)
                        .get()
                        .build();
                okHttpClient.newCall(request).enqueue(callback_date);
            }
        }).start();
    }
    //请求后的回调方法
    private Callback callback_date = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.i("get", e.toString());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.i("get", "onResponse");
            final StringBuffer sb = new StringBuffer(response.body().string());

            for(int i = 0; i <= Market_information_Title_Array.size()-1; i++){
                if (Market_information_Title_Array.get(i).toString() == "日期"){
                    date_index = i;
                }
            }
            Log.i("choose_date",choose_date);
            for(int i = 0; i <= Market_information_DataArray.size()-1;i++){
                String temp_String = Market_information_DataArray.get(i).toString();
                ArrayList temp_array = new ArrayList<String>(Arrays.asList(temp_String.split(",")));
                if (temp_array.get(date_index).toString().replace("\"","").equals(choose_date)){
                    Market_information_NewestArray = temp_array;
                    break;
                }
            }


            Title_information_Array = new ArrayList<String>();
            //取得第一筆資料的Array
            for(int i = 0; i <= Market_information_Title_Array.size()-1; i++){
                String temp_word = Market_information_Title_Array.get(i).toString() + " : " + Market_information_NewestArray.get(i).toString();
                Title_information_Array.add(temp_word);
            }

            Market_Date = new ArrayList<String>();
            for(int i = 0; i <= Market_information_DataArray.size()-1;i++){
                String Market_information_String = Market_information_DataArray.get(i).toString();
                ArrayList Market_information_TempArray = new ArrayList<String>(Arrays.asList(Market_information_String.split(",")));
                Market_Date.add(Market_information_TempArray.get(date_index));
            }

            Log.i("Title_information_Array",Title_information_Array.toString());
            HomeViewModel.mText.postValue(Title_information_Array.toString());

        }
    };



    public LiveData<String> getText() {
        return mText;
    }


}