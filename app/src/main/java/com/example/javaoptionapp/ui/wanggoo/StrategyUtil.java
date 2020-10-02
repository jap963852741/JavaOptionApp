package com.example.javaoptionapp.ui.wanggoo;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.example.javaoptionapp.room.Date_Small_Taiwan_Feature;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.room.Room;

public class StrategyUtil {
    private final ViewGroup container;
    public WangGooAdapter wgu;
    public boolean Approach;
    public Float Entry_Point = null , Exit_Point = null, Exit_Benifit_Point = null ,Exit_Damage_Point = null;
    public int Day;
    public String ApproachDate = "";
    public boolean Day_to_Stop;

    public StrategyUtil(ViewGroup container,WangGooAdapter wgu){
        get_strategy();
        this.container = container;
        this.wgu = wgu;
    }

    void get_strategy(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                Context appContext = container.getContext();
                FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
                FeatureDatabaseDao fdDao =fdb.FeatureDatabaseDao();
                List<Date_Small_Taiwan_Feature> Hundred_Data = fdDao.get_100_data_fromnow();
                Approach = false;
                for(Date_Small_Taiwan_Feature Day_Data : Hundred_Data){
                    Day_to_Stop = false;
                    Log.i("Hundred_Data", Day_Data.date + " 開盤: " + Day_Data.open + " 最高: "+Day_Data.high + " 最低 : "+Day_Data.low + " 收盤 : " +Day_Data.close + " "+ Day_Data.MA_5+ " "+ Day_Data.BIAS_5);
                    if(!Approach) {
                        if(Day_Data.MA_5!=null&&Day_Data.MA_10!=null&&Day_Data.MA_15!=null&&Day_Data.MA_30!=null&&
                                Day_Data.close < Day_Data.MA_5
                                && Day_Data.high > Day_Data.MA_5){
                            ApproachDate = Day_Data.date;
                            Entry_Point = Day_Data.close; //進場點數
                            Exit_Benifit_Point =  Entry_Point * 1.01f; //停利點數
                            Approach = true;//進場
                            Day = 1;//第0天
                            Log.i("Hundred_Data", Day_Data.date + " 進場做多點數 : "+ Entry_Point +"停利點位:" + Exit_Benifit_Point);

                        }
                    }else if(Approach ){//已進場做多 判斷停利停損
                        if(Day_Data.high > Exit_Benifit_Point){  //Day_Data.close > Day_Data.MA_5 * 1.05 ||
                            Day_to_Stop = true;
                            Exit_Point =  Day_Data.close; //停利點數
                            Approach = false;
                            System.out.println("停利點數 : "+ Exit_Point);
                        }
                        else if(Day == 2){  //第三天沒漲超過100就算輸
                            Day_to_Stop = true;
                            Exit_Point = Day_Data.close; //停損點數
                            Approach = false;
                            System.out.println("停損點數 : "+Exit_Point);
                        }
                        Day += 1;
                    }






                }

                wgu.dataList.add("");
                wgu.dataList.add("目前交易策略");
                wgu.dataList.add("");
                if(Approach){
                    wgu.dataList.add("              入場日期 : "+ ApproachDate);
                    wgu.dataList.add("              入場點數 : "+ Entry_Point);
                    wgu.dataList.add("              停利點數 : "+ Exit_Benifit_Point);

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
                    calendar.add(Calendar.DATE, +2);
                    classDate = calendar.getTime();//获取加减以后的Date类型日期
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    String formatted = format1.format(classDate.getTime());


                    wgu.dataList.add("              最後平倉日 : "+ formatted);
                    wgu.dataList.add("");
                    wgu.dataList.add("高點超過停利點 收盤平倉");
                }else {

                    if(Day_to_Stop){
                        wgu.dataList.add("              入場日期 : "+ ApproachDate);
                        wgu.dataList.add("              入場點數 : "+ Entry_Point);
                        wgu.dataList.add("              平倉點數 : "+ Exit_Point);
                        wgu.dataList.add("              此次平倉績效:" + (Exit_Point - Entry_Point));
                    }else {
                        wgu.dataList.add("              空手看盤");
                    }
                }
            }
        }


        );
        thread.start();


    }

}
