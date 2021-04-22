package com.example.javaoptionapp.Repository.bean.wangGoo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//{"time":1619020800000,"tradeDate":1619020800000,"open":17258.00000,"high":17369.00000,"low":17053.00000,"close":17109.00000,"volume":189631,"millionAmount":0.00}
public class WangGooBean {

    private String time;
    private String tradeDate;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private String millionAmount;


    public String getClose() {
        return close;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getMillionAmount() {
        return millionAmount;
    }

    public String getOpen() {
        return open;
    }

    public String getTime() {
        long millisecond = Long.parseLong(time);
        Date date = new Date(millisecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    public String getTradeDate() {
        long millisecond = Long.parseLong(tradeDate);
        Date date = new Date(millisecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    public String getVolume() {
        return volume;
    }
}
