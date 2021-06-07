package com.example.javaoptionapp.Repository.network.DataSource;

import android.os.Environment;
import android.util.Log;

import androidx.room.Room;

import com.example.javaoptionapp.Repository.bean.wangGoo.StrategyDataBean;
import com.example.javaoptionapp.Repository.bean.wangGoo.StrategyResultBean;
import com.example.javaoptionapp.Repository.network.api.option.YahooOptionService;
import com.example.javaoptionapp.Repository.network.api.wanggoo.WangGooService;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;
import com.example.javaoptionapp.room.Table_Option;
import com.example.javaoptionapp.room.Table_Small_Taiwan_Feature;
import com.example.javaoptionapp.ui.wanggoo.WangGooFragment;
import com.example.taiwanworkdaylib.HolidayUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WangGooDataSource implements FeatureDatabaseDao {

    private String TAG = "WangGooDataSource";
    private FeatureDatabaseDao featureDatabaseDao;

    public WangGooService getService(){

        String baseUrl;
        int IntTime = 0;  //幾點幾分
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            IntTime = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm")));
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmm");// HH:mm:ss
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            IntTime = Integer.parseInt(simpleDateFormat.format(date));
        }
        if(IntTime > 830 && IntTime < 1455){
            baseUrl = "https://www.wantgoo.com/investrue/wtx&/"; //早盤
        }else{
            baseUrl = "https://www.wantgoo.com/investrue/wtxp&/"; //晚盤
        }

        Log.i(TAG, "baseUrl = " + baseUrl);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        return retrofit.create(WangGooService.class);
    }

    public WangGooService getHistoryService(){
        String baseUrl;
        baseUrl = "https://www.wantgoo.com/investrue/wmt&/";
        Log.i(TAG, "baseUrl = " + baseUrl);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        return retrofit.create(WangGooService.class);
    }

    public Observable<StrategyResultBean> getStrategy(FeatureDatabaseDao fdDao){ //StrategyBean

        Observable<StrategyResultBean> myObservable = Observable.create(subscriber -> {
            featureDatabaseDao = fdDao;
            StrategyResultBean strategyResultBean = new StrategyResultBean();
            HolidayUtil holidayutil = new HolidayUtil();
            String need_to_add = "";
            boolean Approach;
            Float Entry_Point = null , Exit_Point = null, Exit_Benifit_Point = null ,Exit_Damage_Point = null;
            int Day = 0;
            int Day_To_Stop_Loss;
            String ApproachDate = "";
            boolean Day_to_Stop = false;
            List<Table_Small_Taiwan_Feature> Hundred_Data;

            Day_To_Stop_Loss = 4;//幾天內沒到停利點 就平倉
            Hundred_Data = get_10_data_fromnow();
            strategyResultBean.setHundred_Data(Hundred_Data);
            Log.e(TAG,"Hundred_Data = "+ Hundred_Data.toString());
            Approach = false;
            for(Table_Small_Taiwan_Feature Day_Data : Hundred_Data){
                Day_to_Stop = false;
                Log.i("Hundred_Data", Day_Data.date + " 開盤: " + Day_Data.open + " 最高: "+Day_Data.high + " 最低 : "+Day_Data.low + " 收盤 : " +Day_Data.close + " "+ Day_Data.MA_5+ " "+ Day_Data.BIAS_5);
                if(!Approach) {
                    if(Day_Data.MA_5!=null&&Day_Data.MA_10!=null&&Day_Data.MA_15!=null&&Day_Data.MA_30!=null&&
                            Day_Data.close < Day_Data.MA_5
                            && Day_Data.high > Day_Data.MA_5
                            && Day_Data.volume > Day_Data.before_5_days_average*.86){
                        ApproachDate = Day_Data.date;
                        Entry_Point = Day_Data.close; //進場點數
                        Exit_Benifit_Point =  Entry_Point * 1.2f; //停利點數
                        Approach = true;//進場
                        Day = 1;//第0天
//                            Log.i("Hundred_Data", Day_Data.date + " 進場做多點數 : "+ Entry_Point +"停利點位:" + Exit_Benifit_Point);
                    }
                }else if(Approach){//已進場做多 判斷停利停損
                    if(Day_Data.high > Exit_Benifit_Point){  //Day_Data.close > Day_Data.MA_5 * 1.05 ||
                        Day_to_Stop = true;
                        Exit_Point =  Day_Data.close; //停利點數
                        Approach = false;
//                            System.out.println("停利點數 : "+ Exit_Point);
                    }
                    else if(Day == Day_To_Stop_Loss){  //第四天沒漲超過100就算輸
                        Day_to_Stop = true;
                        Exit_Point = Day_Data.close; //停損點數
                        Approach = false;
//                            System.out.println("停損點數 : "+Exit_Point);
                    }
                    Day += 1;
                }
            }

            need_to_add += ",,目前交易策略,";
            if(Approach){//進場
                need_to_add +=  ","+"              入場日期 : "+ ApproachDate
                        +","+"              入場點數 : "+ Entry_Point
                        + ","+"              停利點數 : "+ Exit_Benifit_Point;
                DateFormat format = new SimpleDateFormat("yyyyMMdd"); //定义日期格式化的格式
                String classDateString = ApproachDate;//需要加减的字符串型日期
                Date classDate = null;//把字符串转化成指定格式的日期
                try {
                    classDate = format.parse(classDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance(); //使用Calendar日历类对日期进行加减
                calendar.setTime(classDate);
                System.out.println(calendar.get(calendar.DAY_OF_WEEK));

//                    calendar.add(Calendar.DATE, +Day_To_Stop_Loss);
                int add_day = 0;

                Log.i("calculate", Environment.getDataDirectory().getPath());
                while (add_day < Day_To_Stop_Loss) {
                    classDate = calendar.getTime();
                    SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
                    holidayutil.set_date(String.valueOf(fm.format(classDate.getTime())));
                    calendar.add(Calendar.DATE, +1);
                    if (!holidayutil.isHoliday()){
                        add_day += 1;
                    }
                }

                classDate = calendar.getTime();//获取加减以后的Date类型日期
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String formatted = format1.format(classDate.getTime());

                need_to_add +=  ","+"              最後平倉日 : "+ formatted
                        + ","
                        + ","+"高點超過停利點 收盤平倉";
                Log.e(TAG,"Exit_Benifit_Point = " + Exit_Benifit_Point.toString());
                strategyResultBean.setBuyOrNot(true);
                strategyResultBean.setStrategyDataBean(
                        new StrategyDataBean(
                                true,
                                ApproachDate,
                                Entry_Point,
                                Exit_Benifit_Point,
                                formatted
                        )
                );

            }else {
                if(Day_to_Stop){
//                    need_to_add +=  ","+"              入場日期 : "+ ApproachDate
//                            + ","+"              入場點數 : "+ Entry_Point
//                            + ","+"              平倉點數 : "+ Exit_Point
//                            + ","+"              此次平倉績效:" + (Exit_Point - Entry_Point);

                    strategyResultBean.setBuyOrNot(true);
                    strategyResultBean.setStrategyDataBean(
                            new StrategyDataBean(
                                    false,
                                    ApproachDate,
                                    Entry_Point,
                                    Exit_Point
                            )
                    );

                }else {
                    strategyResultBean.setBuyOrNot(false);
                }
            }

            subscriber.onNext(strategyResultBean);
            subscriber.onComplete();
        });



        return myObservable;
    }

    @Override
    public List<Table_Small_Taiwan_Feature> getAll() {
        return featureDatabaseDao.getAll();
    }

    @Override
    public List<Table_Small_Taiwan_Feature> get_10_data_fromnow() {
        return featureDatabaseDao.get_10_data_fromnow();
    }

    @Override
    public Table_Small_Taiwan_Feature get_Date_data(String one_date) {
        return featureDatabaseDao.get_Date_data(one_date);
    }

    @Override
    public Table_Option get_Option_Date_Close_Settlement_data(String one_date, Integer small, Integer big, Integer D) {
        return featureDatabaseDao.get_Option_Date_Close_Settlement_data(one_date,small,big,D);
    }

    @Override
    public Table_Option get_option_data(String one_date, String m, String SP, String CP) {
        return featureDatabaseDao.get_option_data(one_date,m,SP,CP);
    }

    @Override
    public String get_ma5_begin_date() {
        return featureDatabaseDao.get_ma5_begin_date();
    }

    @Override
    public String get_ma10_begin_date() {
        return featureDatabaseDao.get_ma10_begin_date();
    }

    @Override
    public String get_ma15_begin_date() {
        return featureDatabaseDao.get_ma15_begin_date();
    }

    @Override
    public String get_ma30_begin_date() {
        return featureDatabaseDao.get_ma15_begin_date();
    }

    @Override
    public void update_ma5(String one_date) {

    }

    @Override
    public void update_ma10(String one_date) {

    }

    @Override
    public void update_ma15(String one_date) {

    }

    @Override
    public void update_ma30(String one_date) {

    }

    @Override
    public void update_bias5(String one_date) {

    }

    @Override
    public void update_days_average(String one_date) {

    }

    @Override
    public void update_ALL_ma5(String begin_day) {

    }

    @Override
    public void update_ALL_ma10(String begin_day) {

    }

    @Override
    public void update_ALL_ma15(String begin_day) {

    }

    @Override
    public void update_ALL_ma30(String begin_day) {

    }

    @Override
    public void update_ALL_bias5(String begin_day) {

    }

    @Override
    public void update_ALL_before_5_days_average(String begin_day) {

    }

    @Override
    public List<Table_Small_Taiwan_Feature> loadAllByIds(int[] tradedates) {
        return null;
    }

    @Override
    public void Delete_after_day(String one_date) {

    }

    @Override
    public void insertAll(Table_Small_Taiwan_Feature... data) {

    }

    @Override
    public void insertAllTable_Small_Taiwan_Feature(List<Table_Small_Taiwan_Feature> List_Table_Small_Taiwan_Feature) {

    }

    @Override
    public void insert_option(Table_Option... data) {

    }

    @Override
    public void insert_ALL_option(List<Table_Option> data) {

    }

    @Override
    public void delete(Table_Small_Taiwan_Feature data) {

    }

    @Override
    public void update(Table_Small_Taiwan_Feature data) {

    }

    @Override
    public void updateAllTable_Small_Taiwan_Feature(List<Table_Small_Taiwan_Feature> data) {

    }

    @Override
    public void update_option(Table_Option data) {

    }

    @Override
    public void update_ALL_option(List<Table_Option> data) {

    }
}
