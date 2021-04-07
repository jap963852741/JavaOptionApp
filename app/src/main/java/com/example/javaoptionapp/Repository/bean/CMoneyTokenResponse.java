package com.example.javaoptionapp.Repository.bean;

public class CMoneyTokenResponse {
    private String token;


    public CMoneyTokenResponse(String tolken) {
        this.token = tolken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String tolken) {
        this.token = tolken;
    }
}
