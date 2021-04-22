package com.example.javaoptionapp.Repository.network.api.home;

import com.example.javaoptionapp.Repository.bean.CMoney.CMoneyBean;
import com.example.javaoptionapp.Repository.bean.CMoney.CMoneyTokenResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

public interface CMoneyService {

    @POST("auth?appId=20200904152146367&appSecret=629ec0a0928c11eb9bbf000c29beef84")
    @Headers("X-Parse-Application-Id:vqYuKPOkLQLYHhk4QTGsGKFwATT4mBIGREI2m8eD")
    Observable<CMoneyTokenResponse> cMoneyToken();

    @GET("api/v2/json/{serialNumber}")
    Observable<CMoneyBean> cMoneyInformation(@Header("Authorization") String token, @Path("serialNumber") String serialNumber);
}