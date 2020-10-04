package com.example.javaoptionapp.ui.wanggoo;

import android.content.Context;
import android.util.Log;

import com.example.javaoptionapp.room.Table_Small_Taiwan_Feature;
import com.example.javaoptionapp.room.FeatureDatabase;
import com.example.javaoptionapp.room.FeatureDatabaseDao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.wang.avi.AVLoadingIndicatorView;

public class StrategyUtil {
    public MutableLiveData<String> mText;
    public boolean Approach;
    public Float Entry_Point = null , Exit_Point = null, Exit_Benifit_Point = null ,Exit_Damage_Point = null;
    public int Day;
    public String ApproachDate = "";
    public boolean Day_to_Stop;
    public AVLoadingIndicatorView avi;

    public StrategyUtil(MutableLiveData<String> mText){
        get_strategy(mText);
        this.mText = mText;

    }


    public void update_db(FeatureDatabaseDao fdDao){
        String time = String.valueOf(System.currentTimeMillis());//- 2*year_time
        WangGooHistoryUtil wghu = new WangGooHistoryUtil(time);
        HashMap<String, HashMap<String,String>> hashmap_time_data = wghu.hashmap_time_data;
        for (String key : hashmap_time_data.keySet()) {
            HashMap<String,String> temp_data = hashmap_time_data.get(key);
            String date = key;
            Table_Small_Taiwan_Feature dstf = new Table_Small_Taiwan_Feature(date,
                    Float.parseFloat(temp_data.get("open")),
                    Float.parseFloat(temp_data.get("high")),
                    Float.parseFloat(temp_data.get("low")),
                    Float.parseFloat(temp_data.get("close")),
                    Float.parseFloat(temp_data.get("volume")));
            Table_Small_Taiwan_Feature the_date_information = fdDao.get_Date_data(date);
//           沒有的才 insert
            if (the_date_information != null){
                fdDao.update(dstf);
            }else {
                fdDao.insertAll(dstf);
            }
        }

        String ma5_begin_date = fdDao.get_ma5_begin_date();
        String ma10_begin_date = fdDao.get_ma10_begin_date();
        String ma15_begin_date = fdDao.get_ma15_begin_date();
        String ma30_begin_date = fdDao.get_ma30_begin_date();
        List<Table_Small_Taiwan_Feature> LD = fdDao.getAll();
        for (Table_Small_Taiwan_Feature d : LD){
            String date = d.date;
            if (Integer.parseInt(date) >= Integer.parseInt(ma5_begin_date)) { //從  ma5_begin_date 日開始更新
                fdDao.update_ma5(date);
                fdDao.update_bias5(date);
            }
            if (Integer.parseInt(date) >= Integer.parseInt(ma10_begin_date)) {
                fdDao.update_ma10(date);
            }
            if (Integer.parseInt(date) >= Integer.parseInt(ma15_begin_date)) {
                fdDao.update_ma15(date);
            }
            if (Integer.parseInt(date) >= Integer.parseInt(ma30_begin_date)) {
                fdDao.update_ma30(date);
            }
        }
    }


    void get_strategy(MutableLiveData<String> mText){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                String need_to_add = "";
                Context appContext = WangGooFragment.container.getContext();
                FeatureDatabase fdb = Room.databaseBuilder(appContext, FeatureDatabase.class, "database-name").build();
                FeatureDatabaseDao fdDao =fdb.FeatureDatabaseDao();
                WangGooFragment.mUI_Handler.sendEmptyMessage(WangGooFragment.MSG_UPLOAD_Begin);
                update_db(fdDao);
                WangGooFragment.mUI_Handler.sendEmptyMessage(WangGooFragment.MSG_UPLOAD_Finish);

                List<Table_Small_Taiwan_Feature> Hundred_Data = fdDao.get_30_data_fromnow();
                Approach = false;
                for(Table_Small_Taiwan_Feature Day_Data : Hundred_Data){
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
                    calendar.add(Calendar.DATE, +2);
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
            }
        });

        thread.start();


    }




}
