package com.example.javaoptionapp.ui.wanggoo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.javaoptionapp.R;
import com.example.javaoptionapp.Repository.bean.wangGoo.StrategyDataBean;
import com.example.javaoptionapp.databinding.FragmentWanggooBinding;
import com.example.javaoptionapp.databinding.ItemStrategyBuyApproachBinding;
import com.example.javaoptionapp.databinding.ItemStrategyBuyNoApproachBinding;
import com.example.javaoptionapp.databinding.ItemWanggooBinding;
import com.example.javaoptionapp.ui.common.ViewModelFactory;
import com.example.javaoptionapp.util.dialog.LoadingDialog;
import com.example.javaoptionapp.room.Table_Small_Taiwan_Feature;
import com.example.taiwanworkdaylib.APIUtil;
import com.example.taiwanworkdaylib.ApiResponse;


import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.max;
import static java.util.Collections.min;

public class WangGooFragment extends Fragment {

    private String TAG = "WangGooFragment";
    private FragmentWanggooBinding fragmentWanggooBinding;
    private WangGooViewModel wangGooViewModel;
    private ImageView image;
    private ViewStub strategyView;
    private Activity activity;
    private final int SHOW = 0;
    private final int HIDE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        activity = ((AppCompatActivity) getActivity());
        wangGooViewModel =new ViewModelProvider(this,new ViewModelFactory(activity)).get(WangGooViewModel.class);
        fragmentWanggooBinding = FragmentWanggooBinding.inflate(inflater, container, false);
        ItemWanggooBinding itemWanggooBinding =  ItemWanggooBinding.bind(fragmentWanggooBinding.getRoot());
        image = fragmentWanggooBinding.imageWanggoo;
        strategyView = fragmentWanggooBinding.strategy;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        image.getLayoutParams().height = screenHeight/4;

        Toolbar toolbar = fragmentWanggooBinding.toolBarWanggoo;
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_history));//把三個小點換掉
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        wangGooViewModel.getWangGooBean().observe(getViewLifecycleOwner(), responseWangGooBean -> {
            if(responseWangGooBean != null) {
                itemWanggooBinding.nowPrice.setText(responseWangGooBean.getClose());
                itemWanggooBinding.tradeDate.setText(responseWangGooBean.getTradeDateYYYYMMDDChinese());
                itemWanggooBinding.open.setText(responseWangGooBean.getOpen());
                itemWanggooBinding.highestPrice.setText(responseWangGooBean.getHigh());
                itemWanggooBinding.lowestPrice.setText(responseWangGooBean.getLow());
                itemWanggooBinding.closePrice.setText(responseWangGooBean.getClose());
                itemWanggooBinding.tradeVolume.setText(responseWangGooBean.getVolume());
            }
        });

        wangGooViewModel.getStrategyResultBean().observe(getViewLifecycleOwner(), responseStrategyResultBean -> {
            if(responseStrategyResultBean.getHundred_Data() == null) return;
            bitmap(responseStrategyResultBean.getHundred_Data());
            Log.e(TAG,responseStrategyResultBean.getHundred_Data().toString());
            if(responseStrategyResultBean.isBuyOrNot()) { //買進
                StrategyDataBean strategyDataBean = responseStrategyResultBean.getStrategyDataBean();
                if(strategyDataBean.isApproach()) { //持有日
                    strategyView.setLayoutResource(R.layout.item_strategy_buy_approach);
                    strategyView.inflate();
                    ItemStrategyBuyApproachBinding itemBinding = ItemStrategyBuyApproachBinding.bind(fragmentWanggooBinding.getRoot());
                    itemBinding.ApproachDate.setText(strategyDataBean.getApproachDate());
                    itemBinding.EntryPoint.setText(String.valueOf(strategyDataBean.getEntryPoint()));
                    itemBinding.ExitBenefitPoint.setText(String.valueOf(strategyDataBean.getExitBenefitPoint()));
                    itemBinding.LastTicketDay.setText(String.valueOf(strategyDataBean.getExpectedSellDate()));
                }else { //平倉日
                    strategyView.setLayoutResource(R.layout.item_strategy_buy_no_approach);
                    strategyView.inflate();
                    ItemStrategyBuyNoApproachBinding itemBinding = ItemStrategyBuyNoApproachBinding.bind(fragmentWanggooBinding.getRoot());
                    itemBinding.noApproachDate.setText(strategyDataBean.getApproachDate());
                    itemBinding.noApproachEntryPoint.setText(String.valueOf(strategyDataBean.getEntryPoint()));
                    itemBinding.noApproachExitPoint.setText(String.valueOf(strategyDataBean.getExitPoint()));
                    itemBinding.thisTimePerformance.setText(String.valueOf(strategyDataBean.getThisTimePerformance()));
                }
            }else {//觀察
                strategyView.setLayoutResource(R.layout.item_strategy_nothing);
                strategyView.inflate();
            }
        });

        wangGooViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if(isLoading){
                LoadingDialog.getInstance(getActivity().getApplicationContext()).show();
            }else{
                LoadingDialog.getInstance(getActivity().getApplicationContext()).hide();
            }
        });

        wangGooViewModel.getShowToast().observe(getViewLifecycleOwner(),toastContent ->{
            Toast.makeText(getContext(),toastContent,Toast.LENGTH_SHORT).show();
        });

        init();

        return fragmentWanggooBinding.getRoot();
    }

    private void init() {
        wangGooViewModel.wangGooApiGetStrategy();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, WangGooFragment.ActionBar_Menu_Num.update_two_year_data, Menu.NONE, R.string.update_two_year_data);
        menu.add(Menu.NONE, WangGooFragment.ActionBar_Menu_Num.update_all_year_data, Menu.NONE, R.string.update_all_year_data);
        menu.add(Menu.NONE, WangGooFragment.ActionBar_Menu_Num.update_date_data, Menu.NONE, R.string.update_date_data);
    }



    public interface ActionBar_Menu_Num{
        Integer update_two_year_data = 1;
        Integer update_all_year_data = 2;
        Integer update_date_data = 3;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                wangGooViewModel.wangGooHistoryApiUpdateDb();
                break;
            case 2:
                wangGooViewModel.update_all_history(activity);
                break;
            case 3:
                wangGooViewModel.updateDate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    private static float upsite_y(float y,float max_y,float min_y){
        return max_y-(y-min_y);
    }

    private void bitmap(List<Table_Small_Taiwan_Feature> Hundred_Data){
        float x_size = (float) image.getWidth() / Hundred_Data.size();
        float x_init = (float) image.getWidth() / (Hundred_Data.size()*2);
        float min_y ;
        float max_y ;
        float x_width = x_init/2;
        ArrayList<Float> min_y_list = new ArrayList<Float>();
        ArrayList<Float> max_y_list = new ArrayList<Float>();

        Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Paint paintRed = new Paint();
        paintRed.setColor(Color.RED);
        Paint paintRedWidth = new Paint();
        paintRedWidth.setColor(Color.RED);
        paintRedWidth.setStrokeWidth(x_width);
        Paint paintGreen = new Paint();
        paintGreen.setColor(Color.GREEN);
        Paint paintGreenWidth = new Paint();
        paintGreenWidth.setColor(Color.GREEN);
        paintGreenWidth.setStrokeWidth(x_width);

        Paint paintMA5 = new Paint();
        paintMA5.setColor(Color.BLUE);
        paintMA5.setStyle(Paint.Style.STROKE);
        Paint paintMA10 = new Paint();
        paintMA10.setColor(Color.rgb(255,97,0));
        paintMA10.setStyle(Paint.Style.STROKE);
        Canvas canvas = new Canvas(bitmap);
        if (Hundred_Data.size() > 0) {

            for(Table_Small_Taiwan_Feature Day_Data : Hundred_Data) {
                min_y_list.add(Day_Data.low);
                max_y_list.add(Day_Data.high);
            }
            min_y = min(min_y_list);
            max_y = max(max_y_list);


            Path ma5_path = new Path();
            Path ma10_path = new Path();
            float before_close = 0f;
                for (Table_Small_Taiwan_Feature Day_Data : Hundred_Data) {
                    Log.i("Hundred_Data", Day_Data.date + " 開盤: " +
                            Day_Data.open + " 最高: " + Day_Data.high +
                            " 最低 : " + Day_Data.low + " 收盤 : " + Day_Data.close + " " + Day_Data.MA_5 + " " + Day_Data.BIAS_5);


                    float startY = (upsite_y(Day_Data.low, max_y, min_y) - min_y) * image.getHeight() / (max_y - min_y);
                    float stopY = (upsite_y(Day_Data.high, max_y, min_y) - min_y) * image.getHeight() / (max_y - min_y);
                    float startY_width = (upsite_y(Day_Data.open, max_y, min_y) - min_y) * image.getHeight() / (max_y - min_y);
                    float stopY_width = (upsite_y(Day_Data.close, max_y, min_y) - min_y) * image.getHeight() / (max_y - min_y);
                    float ma5_Y = (upsite_y(Day_Data.MA_5, max_y, min_y) - min_y) * image.getHeight() / (max_y - min_y);
                    float ma10_Y = (upsite_y(Day_Data.MA_10, max_y, min_y) - min_y) * image.getHeight() / (max_y - min_y);

                    if (before_close < Day_Data.close) {
                        canvas.drawLine(x_init, startY, x_init, stopY, paintRed);
                        canvas.drawLine(x_init, startY_width, x_init, stopY_width, paintRedWidth);
                    } else {
                        canvas.drawLine(x_init, startY, x_init, stopY, paintGreen);
                        canvas.drawLine(x_init, startY_width, x_init, stopY_width, paintGreenWidth);
                    }
                    if (before_close == 0f) {
                        ma5_path.moveTo(x_init, ma5_Y);
                        ma10_path.moveTo(x_init, ma10_Y);
                    } else {
                        ma5_path.lineTo(x_init, ma5_Y);
                        ma10_path.lineTo(x_init, ma10_Y);
                    }


                    x_init += x_size;
                    before_close = Day_Data.close;
                }
                canvas.drawPath(ma5_path, paintMA5);
                canvas.drawPath(ma10_path, paintMA10);
                image.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onPause() { //solve leak problem
        super.onPause();
        image = null;
    }


}