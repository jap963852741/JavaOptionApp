package com.example.javaoptionapp.Repository.bean.wangGoo;

import com.example.javaoptionapp.room.Table_Small_Taiwan_Feature;

import java.util.List;

public class StrategyResultBean {

    private boolean buyOrNot;

    private StrategyDataBean strategyDataBean;

    private List<Table_Small_Taiwan_Feature> Hundred_Data;

    public boolean isBuyOrNot() {
        return buyOrNot;
    }

    public void setBuyOrNot(boolean buyOrNot) {
        this.buyOrNot = buyOrNot;
    }

    public void setHundred_Data(List<Table_Small_Taiwan_Feature> hundred_Data) {
        Hundred_Data = hundred_Data;
    }

    public List<Table_Small_Taiwan_Feature> getHundred_Data() {
        return Hundred_Data;
    }

    public StrategyDataBean getStrategyDataBean() {
        return strategyDataBean;
    }

    public void setStrategyDataBean(StrategyDataBean strategyDataBean) {
        this.strategyDataBean = strategyDataBean;
    }
}
