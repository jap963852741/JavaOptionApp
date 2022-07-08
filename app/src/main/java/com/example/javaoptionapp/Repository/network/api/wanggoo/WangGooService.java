package com.example.javaoptionapp.Repository.network.api.wanggoo;

import com.example.javaoptionapp.Repository.bean.wangGoo.WangGooBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WangGooService {

    @Headers({"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)"})
    @GET("daily-candlestick")
    Observable<ResponseBody> WangGooHtml();

    @Headers({"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36",
            "referer: https://www.wantgoo.com/futures/wtx&",
            "X-Client-Signature: 21e14d4e2096fa362356d71fe7ce29df885246c64909e014029c3b1d4361dcae",
            "Host: www.wantgoo.com",
            "cookie: BID=00F096B0-FA07-4862-A62C-42D940679904; client_fingerprint=866b46b8db587d00128c98ffd434c11bbbfafcf2599ca3969aea3ea99d7ac8d3; _gid=GA1.2.212648619.1657295589; _gcl_au=1.1.1900224183.1657295589; __cf_bm=MwJkwnKuSOGSwJiUk9Fh6qFm45JMUrkW5UYlIVpk2LI-1657295589-0-AUhZSbhc6zJYIeyPoSJH/9rP+s/1IvJYOrMSXxfB2gO5UgVQv1GBZq4oNc8+LYbMda9JzZwxeZ3CZH+bG7tStqIaVmK6ccNcXKII4wW2ejK/v8nZCd9mAnl+KKwJCkU+NQ==; wcsid=eX4grS5Kd5ahiFe03h7B70HA6A0rKFba; hblid=MI6L6onQ7O4i7g183h7B70H06BoaADbA; _okdetect=%7B%22token%22%3A%2216572955898140%22%2C%22proto%22%3A%22about%3A%22%2C%22host%22%3A%22%22%7D; olfsk=olfsk8897134870505712; _okbk=cd4%3Dtrue%2Cvi5%3D0%2Cvi4%3D1657295589991%2Cvi3%3Dactive%2Cvi2%3Dfalse%2Cvi1%3Dfalse%2Ccd8%3Dchat%2Ccd6%3D0%2Ccd5%3Daway%2Ccd3%3Dfalse%2Ccd2%3D0%2Ccd1%3D0%2C; _ok=8391-691-10-7433; _hjid=4befbb79-c4df-49cc-b107-b125ca36efc6; _hjSessionUser_827061=eyJpZCI6ImQ2YzZiNGM4LTJkYzUtNTZlZC05OWU3LTcxMTQ4Yzc5NTE4NCIsImNyZWF0ZWQiOjE2NTcyOTU1OTE0ODksImV4aXN0aW5nIjp0cnVlfQ==; _hjIncludedInSessionSample=0; _hjSession_827061=eyJpZCI6IjRhMWMyODQ1LTI5YzItNDAzMy1iNTJmLTVkZDRiZmEyNjAxOCIsImNyZWF0ZWQiOjE2NTcyOTU1OTE1OTUsImluU2FtcGxlIjpmYWxzZX0=; _hjIncludedInPageviewSample=1; _hjAbsoluteSessionInProgress=0; __gpi=UID=00000780d5674a42:T=1657295612:RT=1657295612:S=ALNI_MaMeOSxKp5kMJeastpl-YIM5HmlkQ; _gat_gtag_UA_6993262_2=1; __gads=ID=93dfeb0147c17b3a-22f418b41ad5000a:T=1657295612:RT=1657295773:S=ALNI_MagzFvSaWuPSjb9ADpf7xcyPANipQ; _oklv=1657295788477%2CeX4grS5Kd5ahiFe03h7B70HA6A0rKFba; _ga_FCVGHSWXEQ=GS1.1.1657295588.1.1.1657295809.0; _ga=GA1.1.1409341239.1657295588"})
    @GET("daily-candlestick")
    Observable<WangGooBean> WangGooBean();

    @Headers({"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)", "Referer: https://www.wantgoo.com/futures/wmt&"})
    @GET("historical-daily-candlesticks")
    Observable<ResponseBody> WangGooHistoryHtml(@Query("before") String before, @Query("top") String top);
    //  有變動邏輯 before 改為上星期的 16:00
//    "https://www.wantgoo.com/investrue/wmt&/historical-daily-candlesticks?before="+System.currentTimeMillis()+"&top=490"

    @Headers({"User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)"})
    @GET("historical-daily-candlesticks")
    Observable<List<WangGooBean>> WangGooHistoryBeanArray(@Query("before") String before, @Query("top") String top);
    //https://www.wantgoo.com/investrue/wmt&/historical-daily-candlesticks?before=1632758400000&top=490
}
