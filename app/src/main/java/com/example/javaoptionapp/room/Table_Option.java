package com.example.javaoptionapp.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "OptionTable",primaryKeys = {"Date","Maturity","Strike_price","CallPut"})
public class Table_Option {
    public Table_Option(String Date,String Maturity,String Strike_price,String CallPut, Float close,Integer Day_To_Finish) {
        this.Date = Date;
        this.Maturity = Maturity;
        this.Strike_price = Strike_price;
        this.CallPut = CallPut;
        this.close = close;
        this.Day_To_Finish = Day_To_Finish;

    }
    //['Date:20020108', 'Maturity:200202', 'Strike_price:5000', 'CallPut:call', 'close:885', 'Day_To_Finish:24']
    @NonNull
    public String Date;
    @NonNull
    public String Maturity;
    @NonNull
    public String Strike_price;
    @NonNull
    public String CallPut;


    @ColumnInfo(name = "close")
    public Float close;

    @ColumnInfo(name = "Day_To_Finish")
    public Integer Day_To_Finish;
}
