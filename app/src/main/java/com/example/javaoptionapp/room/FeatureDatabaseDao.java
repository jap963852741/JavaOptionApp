package com.example.javaoptionapp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao// Database aceess object 访问数据库操作的接口
public interface FeatureDatabaseDao {
    @Query("SELECT * FROM DateIndexTable ORDER BY date ASC")
    List<Table_Small_Taiwan_Feature> getAll();

    @Query("SELECT * FROM (SELECT * FROM DateIndexTable ORDER BY date DESC limit 10) aa ORDER BY date ASC")
    List<Table_Small_Taiwan_Feature> get_10_data_fromnow();

    @Query("SELECT * FROM DateIndexTable WHERE date = :one_date"  )
    Table_Small_Taiwan_Feature get_Date_data(String one_date);

    @Query("SELECT * FROM OptionTable WHERE date = :one_date AND close > :small AND close < :big AND Day_To_Finish >= :D" +
            " ORDER BY close ASC LIMIT 1"  )
    Table_Option get_Option_Date_Close_Settlement_data(String one_date,Integer small,Integer big,Integer D);

    @Query("SELECT * FROM OptionTable WHERE Date = :one_date AND Maturity = :m AND Strike_price = :SP AND CallPut = :CP"  )
    Table_Option get_option_data(String one_date,String m,String SP ,String CP);

    @Query("SELECT date FROM DateIndexTable  ORDER BY date ASC limit 1 offset 4")//忽略4筆從第五筆開始拿
    String get_ma5_begin_date();

    @Query("SELECT date FROM DateIndexTable  ORDER BY date ASC limit 1 offset 9")
    String get_ma10_begin_date();

    @Query("SELECT date FROM DateIndexTable  ORDER BY date ASC limit 1 offset 14")
    String get_ma15_begin_date();

    @Query("SELECT date FROM DateIndexTable  ORDER BY date ASC limit 1 offset 29")
    String get_ma30_begin_date();

    @Query("UPDATE DateIndexTable SET MA_5 = (SELECT SUM(temp_table.close)/5 FROM (SELECT * FROM DateIndexTable WHERE date <= :one_date  ORDER BY date DESC limit 5) temp_table) Where date = :one_date")
    public void update_ma5(String one_date);

    @Query("UPDATE DateIndexTable SET MA_10 = (SELECT SUM(temp_table.close)/10 FROM (SELECT * FROM DateIndexTable WHERE date <= :one_date  ORDER BY date DESC limit 10) temp_table) Where date = :one_date")
    public void update_ma10(String one_date);

    @Query("UPDATE DateIndexTable SET MA_15 = (SELECT SUM(temp_table.close)/15 FROM (SELECT * FROM DateIndexTable WHERE date <= :one_date  ORDER BY date DESC limit 15) temp_table) Where date = :one_date")
    public void update_ma15(String one_date);

    @Query("UPDATE DateIndexTable SET MA_30 = (SELECT SUM(temp_table.close)/30 FROM (SELECT * FROM DateIndexTable WHERE date <= :one_date  ORDER BY date DESC limit 30) temp_table) Where date = :one_date")
    public void update_ma30(String one_date);

    @Query("UPDATE DateIndexTable SET BIAS_5 = (SELECT (temp_table.close-temp_table.MA_5)/temp_table.MA_5 FROM (SELECT * FROM DateIndexTable WHERE date = :one_date) temp_table) Where date = :one_date")
    public void update_bias5(String one_date);

    @Query("UPDATE DateIndexTable  SET before_5_days_average = " +
            "(SELECT SUM(temp_table.volume)/5 FROM " +
            "(SELECT * FROM DateIndexTable a WHERE a.date < :one_date  ORDER BY date DESC limit 5) " +
            "temp_table) Where date = :one_date")
    public void update_days_average(String one_date);

    @Query("UPDATE DateIndexTable  SET MA_5 = " +
            "(SELECT SUM(temp_table.close)/5 FROM " +
            "(SELECT * FROM DateIndexTable a WHERE a.date <= DateIndexTable.date  ORDER BY date DESC limit 5) " +
            "temp_table) Where date > :begin_day")
    public void update_ALL_ma5(String begin_day);

    @Query("UPDATE DateIndexTable  SET MA_10 = " +
            "(SELECT SUM(temp_table.close)/10 FROM " +
            "(SELECT * FROM DateIndexTable a WHERE a.date <= DateIndexTable.date  ORDER BY date DESC limit 10) " +
            "temp_table) Where date > :begin_day")
    public void update_ALL_ma10(String begin_day);

    @Query("UPDATE DateIndexTable  SET MA_15 = " +
            "(SELECT SUM(temp_table.close)/15 FROM " +
            "(SELECT * FROM DateIndexTable a WHERE a.date <= DateIndexTable.date  ORDER BY date DESC limit 15) " +
            "temp_table) Where date > :begin_day")
    public void update_ALL_ma15(String begin_day);

    @Query("UPDATE DateIndexTable  SET MA_30 = " +
            "(SELECT SUM(temp_table.close)/30 FROM " +
            "(SELECT * FROM DateIndexTable a WHERE a.date <= DateIndexTable.date  ORDER BY date DESC limit 30) " +
            "temp_table) Where date > :begin_day")
    public void update_ALL_ma30(String begin_day);


    @Query("UPDATE DateIndexTable SET BIAS_5 = " +
            "(SELECT (temp_table.close-temp_table.MA_5)/temp_table.MA_5 " +
            "FROM (SELECT * FROM DateIndexTable A WHERE A.date = DateIndexTable.date) temp_table) " +
            "Where date > :begin_day")
    public void update_ALL_bias5(String begin_day);

    @Query("UPDATE DateIndexTable  SET before_5_days_average = " +
            "(SELECT SUM(temp_table.volume)/5 FROM " +
            "(SELECT * FROM DateIndexTable a WHERE a.date < DateIndexTable.date  ORDER BY date DESC limit 5) " +
            "temp_table) Where date > :begin_day")
    public void update_ALL_before_5_days_average(String begin_day);

    @Query("SELECT * FROM DateIndexTable WHERE date IN (:tradedates)")
    List<Table_Small_Taiwan_Feature> loadAllByIds(int[] tradedates);


    @Query("DELETE FROM DateIndexTable WHERE date > :one_date")
    public void Delete_after_day(String one_date);



    @Insert
    void insertAll(Table_Small_Taiwan_Feature... data);

    @Insert
    void insertAllTable_Small_Taiwan_Feature(List<Table_Small_Taiwan_Feature> List_Table_Small_Taiwan_Feature);

    @Insert
    void insert_option(Table_Option... data);
    @Insert
    void insert_ALL_option(List<Table_Option> data);

    @Delete
    void delete(Table_Small_Taiwan_Feature data);

    @Update
    void update(Table_Small_Taiwan_Feature data);

    @Update
    void updateAllTable_Small_Taiwan_Feature(List<Table_Small_Taiwan_Feature> data);

    @Update
    void update_option(Table_Option data);
    @Update
    void update_ALL_option(List<Table_Option> data);
}
