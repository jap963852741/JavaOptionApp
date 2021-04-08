package com.example.javaoptionapp.Repository.network.api.option;

import com.example.javaoptionapp.Repository.bean.CMoneyTokenResponse;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface YahooOptionService {

    @POST("aa03?fumr=futurepart&opmr=optionpart&opcm=WTXO&opym=")
    Observable<ResponseBody> YahooOptionHtml();

}
