package com.example.javaoptionapp.database;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public static class FeedHistoryMarketData implements BaseColumns {
        public static final String TABLE_NAME = "HistoryMarketData";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_VOLUME = "volume";
        public static final String COLUMN_NAME_HIGH = "high";
        public static final String COLUMN_NAME_LOW = "low";
        public static final String COLUMN_NAME_CLOSE = "close";
        public static final String COLUMN_NAME_OPEN = "open";
    }
    public static final String SQL_CREATE_HistoryMarketData =
            "CREATE TABLE " + FeedHistoryMarketData.TABLE_NAME + " (" +
                    FeedHistoryMarketData._ID + " INTEGER PRIMARY KEY," +
                    FeedHistoryMarketData.COLUMN_NAME_TIME + " TEXT," +
                    FeedHistoryMarketData.COLUMN_NAME_VOLUME + " TEXT," +
                    FeedHistoryMarketData.COLUMN_NAME_HIGH + " TEXT," +
                    FeedHistoryMarketData.COLUMN_NAME_LOW + " TEXT," +
                    FeedHistoryMarketData.COLUMN_NAME_CLOSE + " TEXT," +
                    FeedHistoryMarketData.COLUMN_NAME_OPEN + " TEXT)";
    public static final String SQL_DELETE_HistoryMarketData =
            "DROP TABLE IF EXISTS " + FeedHistoryMarketData.TABLE_NAME;

}