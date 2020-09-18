package wanggoo;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;
public abstract class StockUtil {
    private  String url;
    private OkHttpClient okHttpClient;
    private static JSONObject json_response_option;
    private Lock lock = new ReentrantLock();
    public void seturl(String url) {
        this.url = url;
    }
    public String geturl() {
        return url;
    }


    public static JSONObject get_json_response_option(){
        if (json_response_option == null){
            throw new ArithmeticException("try to get post() void first");
        }
        return json_response_option;
    }

    public void post(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15000, TimeUnit.MILLISECONDS)
                .readTimeout(15000, TimeUnit.MILLISECONDS);
        okHttpClient = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = null;
        try {
            //将请求添加到请求队列等待执行，并返回执行后的Response对象
            response = okHttpClient.newCall(request).execute();
            //获取Http Status Code.其中200表示成功
            if (response.code() == 200) {
                //这里需要注意，response.body().string()是获取返回的结果，此句话只能调用一次，再次调用获得不到结果。
                //所以先将结果使用result变量接收
                String result = response.body().string();
                result = result.replace("{","").replace("}","").replace("\"","");
                ArrayList  temp_list = new ArrayList();
                JSONObject json_result = new JSONObject();
                String[] list_result = result.split(",");
                for (String list : list_result){
                    String[] temp_list_result = list.split(":");
                    json_result.append(temp_list_result[0],temp_list_result[1]);
                }
//                System.out.println(result);
//                System.out.println(json_result);
                json_response_option = json_result;

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }

}
