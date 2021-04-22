package com.example.javaoptionapp.Repository.network.api.wanggoo;

import com.example.javaoptionapp.Repository.bean.wangGoo.WangGooBean;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WangGooService {

    @Headers({"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)"})
    @GET("daily-candlestick")
    Observable<ResponseBody> WangGooHtml();

    @Headers({"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)"})
    @GET("daily-candlestick")
    Observable<WangGooBean> WangGooBean();

}
