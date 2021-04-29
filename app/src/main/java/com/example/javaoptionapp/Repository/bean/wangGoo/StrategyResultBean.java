package com.example.javaoptionapp.Repository.bean.wangGoo;

public class StrategyResultBean {

    private boolean buyOrNot;

    private StrategyDataBean strategyDataBean;

    public boolean isBuyOrNot() {
        return buyOrNot;
    }

    public void setBuyOrNot(boolean buyOrNot) {
        this.buyOrNot = buyOrNot;
    }

    public StrategyDataBean getStrategyDataBean() {
        return strategyDataBean;
    }

    public void setStrategyDataBean(StrategyDataBean strategyDataBean) {
        this.strategyDataBean = strategyDataBean;
    }
}
