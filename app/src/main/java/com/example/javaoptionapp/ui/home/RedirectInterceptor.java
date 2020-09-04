package com.example.javaoptionapp.ui.home;

import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp重定向存在两个缺陷：
 * 1.okhttp处理301,302重定向时，会把请求方式设置为GET
 * 这样会丢失原来Post请求中的参数。
 *
 * 2.okhttp默认不支持跨协议的重定向，比如http重定向到https
 */

class RedirectInterceptor implements Interceptor {
    private final String TAG = "RedirectInterceptor";
    private Map<String, String> urlsMap = new HashMap<String, String>();

    private void putUrlsMap(String url){
        try {
            String domain = new URI(url).getHost();
            urlsMap.put(domain, url);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public String getNewBaseUrl(String baseUrl){
        String newBaseUrl = "";
        try {
            String domainKey = new URI(baseUrl).getHost();
            if(urlsMap.containsKey(domainKey)){
                newBaseUrl = urlsMap.get(domainKey);
                if(newBaseUrl != null && !newBaseUrl.isEmpty()){
                    int endIndex = newBaseUrl.indexOf("/", newBaseUrl.indexOf("://")+3);
                    if(endIndex > "http://".length() && endIndex > newBaseUrl.indexOf(".")){
                        newBaseUrl = newBaseUrl.substring(0, endIndex);
                    }else{
                        newBaseUrl = "";
                    }
                }else{
                    newBaseUrl = "";
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return newBaseUrl;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        int code = response.code();
        HttpUrl beforeUrl = request.url();
        HttpUrl afterUrl = response.request().url();

//        Headers headers = response.headers();
        String headerUrl = ""; // 获取重定向的Url
        if (response.header("Location") != null) {
            headerUrl = response.header("Location");
        }
//        HttpUrl headerUrl = HttpUrl.get(response.header("Location"));
//        Log.i("___", "headers = " + headers+"\nheaderUrl = "+headerUrl);

//        String headerScheme = "";
        if (headerUrl != null && headerUrl.contains("://")) {
//            headerScheme = headerUrl.substring(0, headerUrl.indexOf("://"));

            //1.根据url判断是否是重定向
//            if(!beforeUrl.equals(afterUrl) || !beforeUrl.toString().equals(headerUrl)) {
//                Log.d(TAG, "有「轉導」網址" +
//                        "\nresponseCode = " + code +
//                        "\nbeforeUrl = " + beforeUrl +
//                        "\nafterUrl = " + afterUrl +
//                        "\nheaderUrl = " + headerUrl);

                //处理两种情况 1、跨协议 2、原先不是GET请求。
//                if (!beforeUrl.scheme().equals(afterUrl.scheme()) || !beforeUrl.scheme().equals(headerScheme) || !request.method().equals("GET")) {
//                    Log.d(TAG, "有跨協議Scheme！重新請求到新的網址");

            /**
             * 與IT討論，目前只針對http status code 307 和 308 做判斷
             * 目前IT判斷機制狀況有三，都為http status code 307：
             * 1.域名原為http：若當下測線域名為 http:// 但為預設(80)port號 >> 則會跳轉導頁至 http:// (9991)port號
             * 2.域名原為https：若當下測線域名為 https:// 但為預設(443)port號 >> 則會跳轉導頁至 https:// (9016)port號
             * 3.域名原為https：當下測線域名為 http:// 預設(80)port號 >> 會先跳轉導頁至 https:// (443)port號後 >> 再跳轉導頁至 https:// (9016)port號
             * 註：若一開始測線域名就帶port號，則不會做跳轉導頁的動作
             */
                if (code == 307 || code == 308) {
                    Log.d(TAG, "(HTTP_REDIRECT) ResponseCode="+code+"！重新請求到新的網址" +
                            "\nbeforeUrl = " + beforeUrl +
                            "\nafterUrl = " + afterUrl +
                            "\nheaderUrl = " + headerUrl);

                    //重新构建请求
                    Request newRequest;
                    if (!beforeUrl.toString().equals(headerUrl)) {
                        putUrlsMap(headerUrl);
                        newRequest = request.newBuilder().url(headerUrl).build();
                    } else {
                        putUrlsMap(afterUrl.toString());
                        newRequest = request.newBuilder().url(afterUrl).build();
                    }
                    response = chain.proceed(newRequest);
                }
//            }
        }
        return response;
    }
}
