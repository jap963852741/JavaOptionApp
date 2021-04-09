package com.example.javaoptionapp.Repository.network.api.option;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YahooOptionService {

    @POST("aa03?fumr=futurepart&opmr=optionpart&opcm=WTXO")
    Observable<ResponseBody> YahooOptionHtml(@Query("opym") String month);

}
