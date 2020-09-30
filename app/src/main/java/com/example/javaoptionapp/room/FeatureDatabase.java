package com.example.javaoptionapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Date_Small_Taiwan_Feature.class}, version = 1)
public abstract class FeatureDatabase extends RoomDatabase {
    public abstract FeatureDatabaseDao FeatureDatabaseDao();
}