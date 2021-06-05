package com.example.javaoptionapp.ui.common;

import android.content.Context;

import com.example.javaoptionapp.Repository.CMoneyRepository;
import com.example.javaoptionapp.Repository.YahooOptionRepository;
import com.example.javaoptionapp.Repository.network.DataSource.CMoneyDataSource;
import com.example.javaoptionapp.Repository.network.DataSource.YahooOptionDataSource;
import com.example.javaoptionapp.ui.Option.OptionViewModel;
import com.example.javaoptionapp.ui.home.HomeViewModel;
import com.example.javaoptionapp.ui.wanggoo.WangGooViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {


    private Context context ;

    public ViewModelFactory(){
    }

    public ViewModelFactory(Context context){
        this.context = context;
    }


    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(new CMoneyRepository(new CMoneyDataSource()));
        }
        if (modelClass.isAssignableFrom(OptionViewModel.class)) {
            return (T) new OptionViewModel(new YahooOptionRepository(new YahooOptionDataSource()));
        }
        if (modelClass.isAssignableFrom(WangGooViewModel.class)) {
            return (T) new WangGooViewModel(context);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
