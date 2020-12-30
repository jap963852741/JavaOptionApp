package com.example.javaoptionapp.ui.wanggoo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaoptionapp.R;
import com.example.javaoptionapp.dialog.LoadingDialog;
import com.example.javaoptionapp.dialog.LoadingDialogFragment;
import com.example.javaoptionapp.room.Table_Small_Taiwan_Feature;
import com.example.javaoptionapp.ui.home.HomeAdapter;
import com.example.javaoptionapp.ui.home.HomeFragment;
import com.example.taiwanworkdaylib.APIUtil;
import com.hdl.calendardialog.CalendarView;
import com.hdl.calendardialog.CalendarViewDialog;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Collections.max;
import static java.util.Collections.min;

public class WangGooFragment extends Fragment {

    private WangGooViewModel wangGooViewModel;
    private WangGooAdapter wanggooAdapter;
    public static final int MSG_UPLOAD_Begin =  1;
    public static final int MSG_UPLOAD_Finish = 2;
    public static final int UPDATE_IMAGE = 3;


    private static AVLoadingIndicatorView avi;
    public static Context strategyutil_context;
    public static LoadingDialog loadingdialog;
    public WangGooHistoryUtil wghu;
    public static Activity activity;
    public static ImageView image;
    public static Bitmap bitmap;
    public static Canvas canvas;
    public static DisplayMetrics displayMetrics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i("status","onCreateView");

        strategyutil_context = getActivity().getApplicationContext();
        activity = ((AppCompatActivity) getActivity());

        wangGooViewModel = new ViewModelProvider(this).get(WangGooViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wanggoo, container, false);
        final RecyclerView recyclerView =  root.findViewById(R.id.re_view_wanggoo);
        avi = root.findViewById(R.id.avi);
        image = root.findViewById(R.id.image_wanggoo);
        displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
//        int screenWidth = displayMetrics.widthPixels;
        image.getLayoutParams().height = screenHeight/4;
//        image.getLayoutParams().width = screenWidth/3;

        Toolbar toolbar = root.findViewById(R.id.toolBar_wanggoo);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_history));//把三個小點換掉
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        loadingdialog = new LoadingDialog(getContext());
        //仅点击外部不可取消
        loadingdialog.setCanceledOnTouchOutside(false);
        //点击返回键和外部都不可取消
        loadingdialog.setCancelable(false);
        wghu = new WangGooHistoryUtil();
        wghu.post();
        loadingdialog.show();
        wangGooViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                Log.i("WangGoo_data",s);

                List<String> WangGoo_data = new ArrayList<String>(Arrays.asList(s.split(",")));
                wanggooAdapter = new WangGooAdapter(WangGoo_data, container);
                recyclerView.setAdapter(wanggooAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

            }
        });
        Log.i("status","onCreateView");

        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, WangGooFragment.ActionBar_Menu_Num.update_two_year_data, Menu.NONE, R.string.update_two_year_data);
        menu.add(Menu.NONE, WangGooFragment.ActionBar_Menu_Num.update_all_year_data, Menu.NONE, R.string.update_all_year_data);
        menu.add(Menu.NONE, WangGooFragment.ActionBar_Menu_Num.update_date_data, Menu.NONE, R.string.update_date_data);
    }

    public interface ActionBar_Menu_Num{
        final Integer update_two_year_data = 1;
        final Integer update_all_year_data = 2;
        final Integer update_date_data = 3;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                wghu.post();
                loadingdialog.show();
                bitmap();
                break;
            case 2:
                wghu.update_all_history();
                loadingdialog.show();
                break;
            case 3:
                loadingdialog.show();
                new APIUtil().update();
                loadingdialog.hide();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    public static Handler mUI_Handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPLOAD_Begin:
                    startAnim();
                    break;
                case MSG_UPLOAD_Finish:
                    stopAnim();
                    break;
                case UPDATE_IMAGE:
                    bitmap();
                    break;
            }
        }
    };

    static void startAnim(){
        avi.show();
        // or avi.smoothToShow();
    }

    static void stopAnim(){
        avi.hide();
        // or avi.smoothToHide();
    }

    private static float upsite_y(float y,float max_y,float min_y){
        return max_y-(y-min_y);
    }
    static void bitmap(){
        float x_size = (float) image.getWidth() / StrategyUtil.Hundred_Data.size();
        float x_init = (float) image.getWidth() / (StrategyUtil.Hundred_Data.size()*2);
        float min_y ;
        float max_y ;
        float x_width = x_init/2;
        ArrayList<Float> min_y_list = new ArrayList<Float>();
        ArrayList<Float> max_y_list = new ArrayList<Float>();

        bitmap = Bitmap.createBitmap(image.getWidth(),image.getHeight(),Bitmap.Config.ARGB_8888);
        Paint paintRed = new Paint();
        paintRed.setColor(Color.RED);
        Paint paintRedWidth = new Paint();
        paintRedWidth.setColor(Color.RED);
        paintRedWidth.setStrokeWidth(x_width);
//        paintRed.setStyle(Paint.Style.STROKE);
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
        canvas = new Canvas(bitmap);

        for(Table_Small_Taiwan_Feature Day_Data : StrategyUtil.Hundred_Data) {
            min_y_list.add(Day_Data.low);
            max_y_list.add(Day_Data.high);
        }
        min_y = min(min_y_list);
        max_y = max(max_y_list);


        Path ma5_path = new Path();
        Path ma10_path = new Path();
        float before_close = 0f;
        for(Table_Small_Taiwan_Feature Day_Data : StrategyUtil.Hundred_Data) {
            Log.i("Hundred_Data", Day_Data.date + " 開盤: " +
                    Day_Data.open + " 最高: " + Day_Data.high +
                    " 最低 : " + Day_Data.low + " 收盤 : " + Day_Data.close + " " + Day_Data.MA_5 + " " + Day_Data.BIAS_5);


            float startY = (upsite_y(Day_Data.low,max_y,min_y) - min_y) * image.getHeight()/(max_y-min_y);
            float stopY = (upsite_y(Day_Data.high,max_y,min_y) - min_y) * image.getHeight()/(max_y-min_y);
            float startY_width = (upsite_y(Day_Data.open,max_y,min_y) - min_y) * image.getHeight()/(max_y-min_y);
            float stopY_width = (upsite_y(Day_Data.close,max_y,min_y) - min_y) * image.getHeight()/(max_y-min_y);
            float ma5_Y = (upsite_y(Day_Data.MA_5,max_y,min_y) - min_y) * image.getHeight()/(max_y-min_y);
            float ma10_Y = (upsite_y(Day_Data.MA_10,max_y,min_y) - min_y) * image.getHeight()/(max_y-min_y);

            if(before_close < Day_Data.close){
                canvas.drawLine(x_init, startY, x_init, stopY, paintRed);
                canvas.drawLine(x_init, startY_width, x_init, stopY_width, paintRedWidth);
            }else {
                canvas.drawLine(x_init, startY, x_init, stopY, paintGreen);
                canvas.drawLine(x_init, startY_width, x_init, stopY_width, paintGreenWidth);
            }
            if (before_close == 0f){
                ma5_path.moveTo(x_init,ma5_Y);
                ma10_path.moveTo(x_init,ma10_Y);
            }else {
                ma5_path.lineTo(x_init,ma5_Y);
                ma10_path.lineTo(x_init,ma10_Y);
            }


            x_init += x_size;
            before_close = Day_Data.close;
        }
        canvas.drawPath(ma5_path, paintMA5);
        canvas.drawPath(ma10_path, paintMA10);
        image.setImageBitmap(bitmap);
    }

}