package com.example.javaoptionapp.ui.wanggoo;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.util.Log;


import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javaoptionapp.MainActivity;
import com.example.javaoptionapp.room.AppDatabase;
import com.example.javaoptionapp.ui.home.HomeViewModel;
import org.json.JSONObject;
import static java.lang.Thread.sleep;


public class WangGooViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public WangGooViewModel() throws InterruptedException {
        mText = new MutableLiveData<>();
        WangGooUtil wgt = new WangGooUtil(mText);

    }

    public LiveData<String> getText() {
        return mText;
    }


}