package com.example.javaoptionapp.ui.wanggoo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javaoptionapp.ui.home.HomeViewModel;

import org.json.JSONObject;


public class WangGooViewModel extends ViewModel {
//implements WangGooUtil.get_resource
    private MutableLiveData<String> mText;

    public WangGooViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is wanggoo fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

//    @Override
//    public void get_wanggoo_jeon() {
//        JSONObject wgu_json = WangGooUtil.get_json_response_option();
//        mText.setValue(wgu_json.toString());
//    }
}