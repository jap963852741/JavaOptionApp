package com.example.javaoptionapp.ui.Option;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.javaoptionapp.Repository.CMoneyRepository;
import com.example.javaoptionapp.Repository.YahooOptionRepository;
import com.example.javaoptionapp.Repository.network.DataSource.CMoneyDataSource;
import com.example.javaoptionapp.Repository.network.DataSource.YahooOptionDataSource;
import com.example.javaoptionapp.ui.home.HomeViewModel;

public class OptionViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OptionViewModel.class)) {
            return (T) new OptionViewModel(new YahooOptionRepository(new YahooOptionDataSource()));
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
