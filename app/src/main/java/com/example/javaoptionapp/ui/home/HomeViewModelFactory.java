package com.example.javaoptionapp.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.javaoptionapp.Repository.network.CMoneyRepository;
import com.example.javaoptionapp.Repository.network.DataSource.CMoneyDataSource;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(new CMoneyRepository(new CMoneyDataSource()));
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
