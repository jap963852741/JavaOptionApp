package com.example.javaoptionapp.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javaoptionapp.Repository.bean.CMoneyBean;
import com.example.javaoptionapp.Repository.bean.CMoneyTokenResponse;
import com.example.javaoptionapp.Repository.network.api.CMoneyService;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;


public class HomeViewModel extends ViewModel{
    private String TAG = "HomeViewModel";
    public String get_url = "https://owl.cmoney.com.tw/OwlApi/api/v2/json/";
    /**
     * BA1-23690a 大盤籌碼日報
     * BA1-23689a 大盤K線
     * BA1-23695a 大盤漲跌家數
     * BA1-23691a 大盤外資
     * BA1-23693a 大盤官股
     *
     *
     * */
    public String item_schma = "BA1-23689a";
    private static ArrayList Market_Date;//全域
    private static int date_index;

    public static MutableLiveData<String>  mText;
    public static MutableLiveData<CMoneyBean>  cMoneyBeanMutableLiveData;
    public static MutableLiveData<ArrayList>  MarketDateMutableLiveData;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        cMoneyBeanMutableLiveData = new MutableLiveData<>();
        MarketDateMutableLiveData = new MutableLiveData<>();
        Log.i("Now","HomeViewModel()");
    }

//    @Override
//    public void post_tolken() {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(timeoutTime, TimeUnit.MILLISECONDS)
//                .readTimeout(timeoutTime, TimeUnit.MILLISECONDS)
//                .sslSocketFactory(HomeViewModel.SSLSocketClient.getSSLSocketFactory())
//                .hostnameVerifier(HomeViewModel.SSLSocketClient.getHostnameVerifier());
////                .addInterceptor(mRedirectInterceptor);
//        okHttpClient = builder.build();
//
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("appId", "20200904152146367")
//                .addFormDataPart("appSecret", "4a56a0f0ee7f11ea93fb000c2932e359")
//                .build();
//        Request request = new Request.Builder()
//                .url("https://owl.cmoney.com.tw/OwlApi/auth?appId=20200904152146367&appSecret=4a56a0f0ee7f11ea93fb000c2932e359")
//                .post(requestBody)
//                .build();
//
//        okHttpClient.newCall(request).enqueue(callback_token);
//
//    }
//    private Callback callback_token = new Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//            Log.i("get_tolken", e.toString());
//        }
//
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            Log.i("get_tolken", "onResponse");
//            final StringBuffer sb = new StringBuffer(response.body().string());
//
//            try {
//                token_json = new JSONObject(String.valueOf(sb));
//                token_string = token_json.getString("token");
//                Log.i("token_string", token_string);
//                get();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    };
//
//    @Override
//    public void get(){
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(timeoutTime, TimeUnit.MILLISECONDS)
//                .readTimeout(timeoutTime, TimeUnit.MILLISECONDS)
//                .sslSocketFactory(HomeViewModel.SSLSocketClient.getSSLSocketFactory())
//                .hostnameVerifier(HomeViewModel.SSLSocketClient.getHostnameVerifier());
////                .addInterceptor(mRedirectInterceptor);
//        okHttpClient = builder.build();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("Authorization","Bearer "+token_string);
//                Request request = new Request.Builder()
//                        .url(get_url+item_schma)
//                        .header("Authorization", "Bearer "+token_string)
//                        .get()
//                        .build();
//                okHttpClient.newCall(request).enqueue(callback);
//            }
//        }).start();
//    }
//    private Callback callback = new Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//            Log.i("get", "onFailure");
//            Log.i("get", e.toString());
//        }
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            Log.i("get", "onResponse");
//            final StringBuffer sb = new StringBuffer(response.body().string());
//            Log.i("get", sb.toString());
//            try {
//                Market_information =new JSONObject(String.valueOf(sb));
//                Market_information_Title = Market_information.getString("Title");
//                Market_information_Title_Array = new ArrayList<String>(Arrays.asList(Market_information_Title.replaceAll("\\[" , "").replaceAll("]" , "").split(",")));
//                String Market_information_DataArrayString = Market_information.getString("Data");
//                Market_information_DataArray = new ArrayList<String>(Arrays.asList(Market_information_DataArrayString.replaceAll("\\[" , "").split("],")));
//                String Market_information_Newest_String = Market_information_DataArray.get(0).toString();
//                Log.i("Market_information_Newest_String", Market_information_Newest_String);
//                Market_information_NewestArray = new ArrayList<String>(Arrays.asList(Market_information_Newest_String.split(",")));
//
////
//                Title_information_Array = new ArrayList<String>();
//                //取得第一筆資料的Array
//                for(int i = 0; i <= Market_information_Title_Array.size()-1; i++){
//                    if (Market_information_Title_Array.get(i).toString() == "日期"){
//                        date_index = i;
//                    }
//                    String temp_word = Market_information_Title_Array.get(i).toString() + " : " + Market_information_NewestArray.get(i).toString();
//                    Title_information_Array.add(temp_word);
//                }
//                Log.i("Market_information_DataArray",Market_information_DataArray.toString());
////                Market_Date = new ArrayList<String>();
////                for(int i = 0; i <= Market_information_DataArray.size()-1;i++){
////                    String Market_information_String = Market_information_DataArray.get(i).toString();
////                    ArrayList Market_information_TempArray = new ArrayList<String>(Arrays.asList(Market_information_String.split(",")));
////                    Market_Date.add(Market_information_TempArray.get(date_index));
////                }
//
//                HomeViewModel.mText.postValue(Title_information_Array.toString());
//                Log.i("Market_Date",Market_Date.toString());
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//
//    @Override
//    public void post_tolken(String date) {
//        choose_date = date;
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(timeoutTime, TimeUnit.MILLISECONDS)
//                .readTimeout(timeoutTime, TimeUnit.MILLISECONDS)
//                .sslSocketFactory(HomeViewModel.SSLSocketClient.getSSLSocketFactory())
//                .hostnameVerifier(HomeViewModel.SSLSocketClient.getHostnameVerifier());
////                .addInterceptor(mRedirectInterceptor);
//        okHttpClient = builder.build();
//
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("appId", "20200904152146367")
//                .addFormDataPart("appSecret", "4a56a0f0ee7f11ea93fb000c2932e359")
//                .build();
//        Request request = new Request.Builder()
//                .url("https://owl.cmoney.com.tw/OwlApi/auth?appId=20200904152146367&appSecret=4a56a0f0ee7f11ea93fb000c2932e359")
//                .post(requestBody)
//                .build();
//
//        okHttpClient.newCall(request).enqueue(callback_token_date);
//    }
//    private Callback callback_token_date = new Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//            Log.i("get_tolken", "onFailure");
//            Log.i("get_tolken", e.toString());
//        }
//
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            Log.i("get_tolken", "onResponse");
//            final StringBuffer sb = new StringBuffer(response.body().string());
//
//            try {
//                token_json = new JSONObject(String.valueOf(sb));
//                token_string = token_json.getString("token");
//                Log.i("token_string", token_string);
//                get(choose_date);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    };
//
//    @Override
//    public void get(String date) {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(timeoutTime, TimeUnit.MILLISECONDS)
//                .readTimeout(timeoutTime, TimeUnit.MILLISECONDS)
//                .sslSocketFactory(HomeViewModel.SSLSocketClient.getSSLSocketFactory())
//                .hostnameVerifier(HomeViewModel.SSLSocketClient.getHostnameVerifier());
////                .addInterceptor(mRedirectInterceptor);
//        okHttpClient = builder.build();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("Authorization","Bearer "+token_string);
//                Request request = new Request.Builder()
//                        .url(get_url+item_schma)
//                        .header("Authorization", "Bearer "+token_string)
//                        .get()
//                        .build();
//                okHttpClient.newCall(request).enqueue(callback_date);
//            }
//        }).start();
//    }
//    //请求后的回调方法
//    private Callback callback_date = new Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//            Log.i("get", e.toString());
//        }
//
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            Log.i("get", "onResponse");
//            final StringBuffer sb = new StringBuffer(response.body().string());
//
//            for(int i = 0; i <= Market_information_Title_Array.size()-1; i++){
//                if (Market_information_Title_Array.get(i).toString() == "日期"){
//                    date_index = i;
//                }
//            }
//            Log.i("choose_date",choose_date);
//            for(int i = 0; i <= Market_information_DataArray.size()-1;i++){
//                String temp_String = Market_information_DataArray.get(i).toString();
//                ArrayList temp_array = new ArrayList<String>(Arrays.asList(temp_String.split(",")));
//                if (temp_array.get(date_index).toString().replace("\"","").equals(choose_date)){
//                    Market_information_NewestArray = temp_array;
//                    break;
//                }
//            }
//
//
//            Title_information_Array = new ArrayList<String>();
//            //取得第一筆資料的Array
//            for(int i = 0; i <= Market_information_Title_Array.size()-1; i++){
//                String temp_word = Market_information_Title_Array.get(i).toString() + " : " + Market_information_NewestArray.get(i).toString();
//                Title_information_Array.add(temp_word);
//            }
//
////            Market_Date = new ArrayList<String>();
////            for(int i = 0; i <= Market_information_DataArray.size()-1;i++){
////                String Market_information_String = Market_information_DataArray.get(i).toString();
////                ArrayList Market_information_TempArray = new ArrayList<String>(Arrays.asList(Market_information_String.split(",")));
////                Market_Date.add(Market_information_TempArray.get(date_index));
////            }
//
//            Log.i("Title_information_Array",Title_information_Array.toString());
//            HomeViewModel.mText.postValue(Title_information_Array.toString());
//
//        }
//    };


    public void CMoneyTokenApi(){
        String baseUrl = "https://owl.cmoney.com.tw/OwlApi/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        CMoneyService api = retrofit.create(CMoneyService.class);
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
        api.cMoneyToken()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(observer);
    }

    public void CMoneyInformation(String token,String serialNumber){
        String baseUrl = "https://owl.cmoney.com.tw/OwlApi/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        CMoneyService api = retrofit.create(CMoneyService.class);
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
        api.cMoneyInformation("Bearer "+token,serialNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(observer);
    }

    public int getDate_date(String date){
        for(int i = 0; i < Market_Date.size() ;i++){
            if (Market_Date.get(i).equals(date)){
                Log.e(TAG,"getDate_date = "+i);
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