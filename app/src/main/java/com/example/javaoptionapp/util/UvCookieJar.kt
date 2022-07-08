package com.example.javaoptionapp.util

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient

object UvCookieJar : CookieJar {

    var cookies = mutableListOf<Cookie>()
    private set

    override fun saveFromResponse(url: HttpUrl, cookieList: List<Cookie>) {
        cookies.clear()
        cookies.addAll(cookieList)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> =
        cookies
}