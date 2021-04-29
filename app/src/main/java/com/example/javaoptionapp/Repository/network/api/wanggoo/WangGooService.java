package com.example.javaoptionapp.Repository.network.api.wanggoo;

import com.example.javaoptionapp.Repository.bean.wangGoo.WangGooBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WangGooService {

    @Headers({"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)"})
    @GET("daily-candlestick")
    Observable<ResponseBody> WangGooHtml();

    @Headers({"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)"})
    @GET("daily-candlestick")
    Observable<WangGooBean> WangGooBean();

    @Headers({"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)"})
    @GET("historical-daily-candlesticks?")
    Observable<ResponseBody> WangGooHistoryHtml(@Query("before") String before,@Query("top") String top);
//    "https://www.wantgoo.com/investrue/wmt&/historical-daily-candlesticks?before="+System.currentTimeMillis()+"&top=490"

    @Headers({"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)"})
    @GET("historical-daily-candlesticks?")
    Observable<List<WangGooBean>> WangGooHistoryBeanArray(@Query("before") String before, @Query("top") String top);
}
