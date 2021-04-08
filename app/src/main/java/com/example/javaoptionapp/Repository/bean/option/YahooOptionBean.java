package com.example.javaoptionapp.Repository.bean.option;

import java.util.ArrayList;

public class YahooOptionBean {
    /**
     * 履約價 strikePrice
     * 成交價 dealPrice
     * 目標 goal
     *
     */
    private final String strikePrice;
    private final String dealPrice;
    private final String goal;

    public YahooOptionBean(String strikePrice ,String dealPrice ,String goal){
        this.strikePrice = strikePrice;
        this.dealPrice = dealPrice;
        this.goal = goal;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public String getGoal() {
        return goal;
    }

    public String getStrikePrice() {
        return strikePrice;
    }

}
