package com.example.javaoptionapp.Repository;

import com.example.javaoptionapp.Repository.network.DataSource.CMoneyDataSource;

public class CMoneyRepository {

    private final CMoneyDataSource cMoneyDataSource;

    public CMoneyRepository(CMoneyDataSource cMoneyDataSource){
        this.cMoneyDataSource = cMoneyDataSource;
    }

    public CMoneyDataSource getcMoneyDataSource() {
        return cMoneyDataSource;
    }
}
