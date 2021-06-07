package com.example.javaoptionapp;

import android.os.Bundle;
import android.view.View;

import com.example.javaoptionapp.ui.common.ViewModelFactory;
import com.example.javaoptionapp.ui.wanggoo.WangGooViewModel;
import com.example.javaoptionapp.util.dialog.LoadingDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private WangGooViewModel wangGooViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wangGooViewModel =new ViewModelProvider(this,new ViewModelFactory(this)).get(WangGooViewModel.class);
//        状态栏文字颜色只能在Android6.0以上版本才能自定义修改 ，
//        但只有两种选择： 白色 （0）和 灰色（View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR）
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//Statusbar 轉為深色
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_mystrategy,
                R.id.navigation_home,  R.id.navigation_dashboard).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        wangGooViewModel.getIsLoading().observe(this, isLoading -> {
            if(isLoading){
                LoadingDialog.getInstance(this).show();
            }else{
                LoadingDialog.getInstance(this).hide();
            }
        });

        init();
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        wangGooViewModel.wangGooHistoryApiUpdateDb();
    }
}