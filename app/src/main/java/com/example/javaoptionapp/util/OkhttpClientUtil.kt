package com.example.javaoptionapp.util

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun OkHttpClient.Builder.addLogIntercept(): OkHttpClient.Builder {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    this.addInterceptor(logging)
    return this
}

fun OkHttpClient.Builder.addCookieInHeader(): OkHttpClient.Builder {
    val logging = RequestTokenInterceptor()
    this.addInterceptor(logging)
    return this
}


fun OkHttpClient.Builder.addCookie(): OkHttpClient.Builder {
    this.cookieJar(UvCookieJar)
    return this
}