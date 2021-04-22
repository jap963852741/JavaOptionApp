package com.example.javaoptionapp.ui.wanggoo;

import android.os.Build;


import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static java.lang.Thread.sleep;


public class WangGooViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public WangGooViewModel() {
        mText = new MutableLiveData<>();
        WangGooUtil wgt = new WangGooUtil(mText);
    }

    public LiveData<String> getText() {
        return mText;
    }

}