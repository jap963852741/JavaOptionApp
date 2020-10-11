package com.example.javaoptionapp.ui.Option;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OptionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OptionViewModel() {
        mText = new MutableLiveData<>();
        OptionUtil otu = new OptionUtil(mText);
        otu.post();
    }

    public LiveData<String> getText() {
        return mText;
    }
}