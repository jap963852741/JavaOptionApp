package com.example.javaoptionapp.ui.wanggoo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.javaoptionapp.Repository.CMoneyRepository;
import com.example.javaoptionapp.Repository.WangGooRepository;
import com.example.javaoptionapp.Repository.network.DataSource.CMoneyDataSource;
import com.example.javaoptionapp.Repository.network.DataSource.WangGooDataSource;
import com.example.javaoptionapp.ui.home.HomeViewModel;

public class WangGooModelFactory implements ViewModelProvider.Factory {
    private Context context ;

    public WangGooModelFactory(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WangGooViewModel.class)) {
            return (T) new WangGooViewModel(new WangGooRepository(new WangGooDataSource()), context);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
