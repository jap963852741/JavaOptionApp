package com.example.taiwanworkdaylib;

import java.net.MalformedURLException;

public interface ApiResponse {
    void success();
    void fail(Exception e);
}
