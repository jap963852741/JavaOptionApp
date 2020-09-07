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

//Model层只用来产生数据
public class HomeViewModel extends ViewModel implements CmoneyUtil.MarkeyInformation {
    private RedirectInterceptor mRedirectInterceptor = new RedirectInterceptor();
    public final static long timeoutTime = 1000 * 15;



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
    private OkHttpClient okHttpClient;
    private MutableLiveData<String> mText;
    public static final MediaType JSONType = MediaType.parse("application/json; charset=utf-8");
    private static JSONObject Market_information ;
    private static JSONObject token_json ;
    private static String token_string ;
    private static String Market_information_Title ;
    private static ArrayList Market_information_NewestArray ;
    private static ArrayList Market_information_DataArray;





    public void post_tolken() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutTime, TimeUnit.MILLISECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
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
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
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
                String Market_information_DataArrayString = Market_information.getString("Data");
                Market_information_DataArray = new ArrayList<String>(Arrays.asList(Market_information_DataArrayString.replaceAll("\\[" , "").split("]")));
                String Market_information_Newest_String = Market_information_DataArray.get(0).toString();
                Log.i("Market_information_Newest_String", Market_information_Newest_String);
                Market_information_NewestArray = new ArrayList<String>(Arrays.asList(Market_information_Newest_String.split(",")));



            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("sb",sb.toString());
            Log.i("Market_information",Market_information.toString());
            Log.i("Market_information_Title",Market_information_Title.toString());
            Log.i("Market_information_DataArray",Market_information_DataArray.get(0).toString());
            Log.i("Market_information_NewestArray",Market_information_NewestArray.toString());

        }
    };

    public HomeViewModel() {
        mText = new MutableLiveData<>();
//        post_tolken();
        CmoneyUtil cmoneyUtil = new CmoneyUtil();
        cmoneyUtil.response(this);
        Log.i("Now","HomeViewModel()");


    }

    public LiveData<String> getText() {
        return mText;
    }


    @Override
    public void answer(String info) {
        Log.i("Now","answer");
        mText.postValue("目前大盤"+ info);
    }
}