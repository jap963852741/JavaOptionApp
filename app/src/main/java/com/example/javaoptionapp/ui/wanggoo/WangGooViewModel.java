package com.example.javaoptionapp.ui.wanggoo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WangGooViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WangGooViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is wanggoo fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}