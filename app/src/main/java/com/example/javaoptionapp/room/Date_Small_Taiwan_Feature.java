package com.example.javaoptionapp.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DateIndexTable")
public class Date_Small_Taiwan_Feature {
    public Date_Small_Taiwan_Feature() {
    }

//[time:1600963200000, tradeDate:1600963200000, open:12168.00000, high:12234.00000, low:12157.00000, close:12206.00000, volume:9865, millionAmount:0.00]

    public Date_Small_Taiwan_Feature(String date,String open,String high,String low,Float close,String volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public Date_Small_Taiwan_Feature(String date,String open,String high,String low,Float close,String volume,Float MA_5) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.MA_5 = MA_5;
    }
    @PrimaryKey // 设置主键，并且自动生长
    @NonNull
    public String date;

    @ColumnInfo(name = "open")
    public String open;

    @ColumnInfo(name = "high")
    public String high;

    @ColumnInfo(name = "low")
    public String low;

    @ColumnInfo(name = "close")
    public Float close;

    @ColumnInfo(name = "volume")
    public String volume;

    @ColumnInfo(name = "MA_5")
    public Float MA_5;
}
