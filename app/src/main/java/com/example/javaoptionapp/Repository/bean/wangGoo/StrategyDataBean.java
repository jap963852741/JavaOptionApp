package com.example.javaoptionapp.Repository.bean.wangGoo;

public class StrategyDataBean {

    private boolean Approach;

    private String ApproachDate;
    private Float EntryPoint;

    // 持有中
    private Float ExitBenefitPoint;
    private Float ExpectedSellDate;


    // 賣出日當日
    private Float ExitPoint;
    private Float thisTimePerformance;

    //持有中
    public StrategyDataBean(Boolean Approach,String ApproachDate,Float EntryPoint,Float ExitBenefitPoint,Float ExpectedSellDate){
        this.ApproachDate = ApproachDate;
        this.EntryPoint = EntryPoint;
        this.ExitBenefitPoint = ExitBenefitPoint;
        this.ExpectedSellDate = ExpectedSellDate;
    }


    //賣出日當日
    public StrategyDataBean(Boolean Approach,String ApproachDate,Float EntryPoint,Float ExitPoint){
        this.ApproachDate = ApproachDate;
        this.EntryPoint = EntryPoint;
        this.ExitPoint = ExitPoint;
        this.thisTimePerformance = EntryPoint - ExitPoint;
    }


    public boolean isApproach() {
        return Approach;
    }

    public Float getEntryPoint() {
        return EntryPoint;
    }

    public Float getExitBenefitPoint() {
        return ExitBenefitPoint;
    }

    public Float getExitPoint() {
        return ExitPoint;
    }

    public Float getExpectedSellDate() {
        return ExpectedSellDate;
    }

    public Float getThisTimePerformance() {
        return thisTimePerformance;
    }

    public String getApproachDate() {
        return ApproachDate;
    }
}
