package com.example.javaoptionapp.ui.wanggoo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.javaoptionapp.room.Table_Small_Taiwan_Feature;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.taiwanworkdaylib.HolidayUtil;
import static com.example.javaoptionapp.ui.wanggoo.WangGooFragment.mUI_Handler;


public class StrategyUtil {

    private final String TAG = "StrategyUtil";
    public MutableLiveData<String> mText;
    public boolean Approach;
    public Float Entry_Point = null , Exit_Point = null, Exit_Benifit_Point = null ,Exit_Damage_Point = null;
    public int Day;
    public int Day_To_Stop_Loss;
    public String ApproachDate = "";
    public boolean Day_to_Stop;
    public static List<Table_Small_Taiwan_Feature> Hundred_Data;

    public StrategyUtil(MutableLiveData<String> mText){
        get_strategy();
        this.mText = mText;
    }



    void get_strategy(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                String need_to_add = "";
                Context appContext = WangGooFragment.strategyutil_context;
                FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
                FeatureDatabaseDao fdDao =fdb.FeatureDatabaseDao();
                HolidayUtil holidayutil = new HolidayUtil();
                WangGooFragment.mUI_Handler.sendEmptyMessage(WangGooFragment.MSG_UPLOAD_Begin);
//                update_db(fdDao);
                WangGooFragment.mUI_Handler.sendEmptyMessage(WangGooFragment.MSG_UPLOAD_Finish);

                Day_To_Stop_Loss = 4;//幾天內沒到停利點 就平倉
                Hundred_Data = fdDao.get_10_data_fromnow();
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
                fdb.close();
                need_to_add += ",,目前交易策略,";
                if(Approach){
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
                }else {
                    if(Day_to_Stop){
                        need_to_add +=  ","+"              入場日期 : "+ ApproachDate
                                + ","+"              入場點數 : "+ Entry_Point
                                + ","+"              平倉點數 : "+ Exit_Point
                                + ","+"              此次平倉績效:" + (Exit_Point - Entry_Point);

                    }else {
                        need_to_add += ",              空手看盤";
                    }
                }
                mText.postValue(mText.getValue() + need_to_add + ",,");
                WangGooFragment.mUI_Handler.sendEmptyMessage(WangGooFragment.UPDATE_IMAGE);
            }

        });

        thread.start();


    }




}
