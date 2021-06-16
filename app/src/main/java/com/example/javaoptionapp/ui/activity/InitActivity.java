package com.example.javaoptionapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.javaoptionapp.R;
import com.example.javaoptionapp.databinding.ActivityInitBinding;
import com.example.javaoptionapp.databinding.ItemWanggooBinding;
import com.example.javaoptionapp.ui.common.ViewModelFactory;
import com.example.javaoptionapp.ui.wanggoo.WangGooViewModel;
import com.example.javaoptionapp.util.dialog.LoadingDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class InitActivity extends AppCompatActivity {
    private WangGooViewModel wangGooViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInitBinding binding = ActivityInitBinding.inflate(getLayoutInflater());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//Statusbar 轉為深色


        binding.loadingBar.setSmoothPercent(0.8f);

        wangGooViewModel =new ViewModelProvider(this,new ViewModelFactory(this)).get(WangGooViewModel.class);

        wangGooViewModel.getLoadingBarPercentLiveData().observe(this, loadingBarPercent -> {
            if(loadingBarPercent < 1f) {
                binding.loadingBar.setSmoothPercent(loadingBarPercent);
                binding.tvLoadingBar.setText(String.format(Locale.getDefault() ,"%f",loadingBarPercent));
            }else {
                // 创建需要启动的Activity对应的Intent
                Intent intent = new Intent(this,MainActivity.class);
                // 启动intent对应的Activity
                startActivity(intent);
                finish();
            }
        });


        View view = binding.getRoot();
        setContentView(view);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        wangGooViewModel.wangGooHistoryApiUpdateDb();
    }
}
