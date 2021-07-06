package com.example.javaoptionapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
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
                String percent = String.format(Locale.getDefault() ,"%.0f",loadingBarPercent*100) + " %";
                binding.loadingBar.setSmoothPercent(loadingBarPercent);
                binding.tvLoadingBar.setText(percent);
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
        // Android6.0 就是API 23之后。APP需要动态获取权限。
        //java.io.FileNotFoundException: ./sdcard/data/data.txt (Permission denied)
        final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},EXTERNAL_STORAGE_REQ_CODE);
        }

        wangGooViewModel.wangGooHistoryApiUpdateDb();
    }
}
