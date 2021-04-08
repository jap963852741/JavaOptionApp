package com.example.javaoptionapp.Repository;

import com.example.javaoptionapp.Repository.network.DataSource.CMoneyDataSource;
import com.example.javaoptionapp.Repository.network.DataSource.YahooOptionDataSource;

public class YahooOptionRepository {

    private final YahooOptionDataSource yahooOptionDataSource;

    public YahooOptionRepository(YahooOptionDataSource yahooOptionDataSource){
        this.yahooOptionDataSource = yahooOptionDataSource;
    }

    public YahooOptionDataSource getyahooOptionDataSource() {
        return yahooOptionDataSource;
    }
}
