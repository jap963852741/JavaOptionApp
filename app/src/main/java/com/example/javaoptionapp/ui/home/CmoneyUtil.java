package com.example.javaoptionapp.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CmoneyUtil {
    MarkeyInformation markeyInformation;
    private OkHttpClient okHttpClient;
    public final static long timeoutTime = 1000 * 15;
    private RedirectInterceptor mRedirectInterceptor = new RedirectInterceptor();

    private static JSONObject Market_information ;
    private static JSONObject token_json ;
    private static String token_string ;
    private static String Market_information_Title ;
    private static ArrayList Market_information_Title_Array ;
    private static ArrayList Market_information_NewestArray ;
    private static ArrayList Market_information_DataArray;
    private static ArrayList Title_information_Array;

    public interface MarkeyInformation {
        void answer(String info);
    }

    public void response(MarkeyInformation markeyInformation) {
        this.markeyInformation = markeyInformation;
        post_tolken();
    }

    public CmoneyUtil() {

    }



    public void post_tolken() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .sslSocketFactory(HomeViewModel.SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(HomeViewModel.SSLSocketClient.getHostnameVerifier())
                .addInterceptor(mRedirectInterceptor);
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
    //请求后的回调方法
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

    public void get(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .sslSocketFactory(HomeViewModel.SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(HomeViewModel.SSLSocketClient.getHostnameVerifier())
                .addInterceptor(mRedirectInterceptor);
        okHttpClient = builder.build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("Authorization","Bearer "+token_string);
                Request request = new Request.Builder()
                        .url("https://owl.cmoney.com.tw/OwlApi/api/v2/json/BA1-23689a")
                        .header("Authorization", "Bearer "+token_string)
                        .get()
                        .build();
                okHttpClient.newCall(request).enqueue(callback);
            }
        }).start();
    }
    //请求后的回调方法
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
                Market_information_DataArray = new ArrayList<String>(Arrays.asList(Market_information_DataArrayString.replaceAll("\\[" , "").split("]")));
                String Market_information_Newest_String = Market_information_DataArray.get(0).toString();
                Log.i("Market_information_Newest_String", Market_information_Newest_String);
                Market_information_NewestArray = new ArrayList<String>(Arrays.asList(Market_information_Newest_String.split(",")));


//
                Title_information_Array = new ArrayList<String>();
                for(int i = 0; i <= Market_information_Title_Array.size()-1; i++){
//                    Log.i("i=",String.valueOf(i));
//                    Log.i("Market_information_Title_Array.get(i).toString()",Market_information_Title_Array.get(i).toString());
//                    Log.i("Market_information_NewestArray.get(i).toString()",Market_information_NewestArray.get(i).toString());
                    String temp_word = Market_information_Title_Array.get(i).toString() + " : " + Market_information_NewestArray.get(i).toString();
                    Title_information_Array.add(temp_word);
                }


                if (null != markeyInformation) {
                        markeyInformation.answer(Title_information_Array.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };




}
