package com.example.javaoptionapp.Repository;

import com.example.javaoptionapp.Repository.network.DataSource.WangGooDataSource;
import com.example.javaoptionapp.Repository.network.DataSource.YahooOptionDataSource;

public class WangGooRepository {

    private final WangGooDataSource wangGooDataSource;

    public WangGooRepository(WangGooDataSource wangGooDataSource){
        this.wangGooDataSource = wangGooDataSource;
    }

    public WangGooDataSource getWangGooDataSource() {
        return wangGooDataSource;
    }
}
