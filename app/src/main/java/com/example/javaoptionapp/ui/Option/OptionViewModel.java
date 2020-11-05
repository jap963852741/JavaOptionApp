package com.example.javaoptionapp.ui.Option;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OptionViewModel extends ViewModel {

    public static MutableLiveData<String> mText;

    public OptionViewModel() {
        mText = new MutableLiveData<>();
        OptionUtil otu = new OptionUtil(mText);
    }

//    public OptionViewModel(String Month) {
//        mText = new MutableLiveData<>();
//        OptionUtil otu = new OptionUtil(mText);
//    }



    public LiveData<String> getText() {
        return mText;
    }
}