package com.example.javaoptionapp.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Table_Small_Taiwan_Feature.class,Table_Option.class}, version = 1)
public abstract class FeatureDatabase extends RoomDatabase {
    /**
     * 單例模式
     * volatile 確保線程安全
     * 線程安全意味著改對象會被許多線程使用
     * 可以被看作是一種 “程度較輕的 synchronized”
     */
    private static volatile FeatureDatabase INSTANCE;
    /**
     * 該方法由於獲得 DataBase 對象
     * abstract
     * @return
     */
    public abstract FeatureDatabaseDao FeatureDatabaseDao();

    public static FeatureDatabase getInstance(Context context) {
        // 若為空則進行實例化
        // 否則直接返回
        if (INSTANCE == null) {
            synchronized (FeatureDatabase.class) {
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FeatureDatabase.class, "database-name")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
