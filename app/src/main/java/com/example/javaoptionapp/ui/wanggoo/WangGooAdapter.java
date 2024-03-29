package com.example.javaoptionapp.ui.wanggoo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaoptionapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WangGooAdapter extends RecyclerView.Adapter<WangGooAdapter.VH> {

    public List<String> dataList;
    private ViewGroup parentview;

    static class VH extends RecyclerView.ViewHolder {
        TextView tv1;
        public VH(@NonNull View itemView,View parent) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
        }
    }
    public WangGooAdapter(List<String> dataList,ViewGroup container) {
        Log.i("WangGooAdapter",dataList.toString());


        for (int i = 0 ; i < dataList.size() ; i++ ){
            String temp_string = dataList.get(i);
            String[] temp_array = temp_string.split(":");
            if(temp_array[0].equals("time")){
                int time_index = i ;
                long millisecond = Long.parseLong(temp_array[1]);
                Date date = new Date(millisecond);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                String after_change_time = format.format(date);
                dataList.set(i,temp_string.replace("time","時間 ").replace(temp_array[1],after_change_time));
            }else if(temp_array[0].equals("tradeDate")){
                long millisecond = Long.parseLong(temp_array[1]);
                Date date = new Date(millisecond);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                String after_change_time = format.format(date);
                dataList.set(i,temp_string.replace("tradeDate","交易日期 ").replace(temp_array[1],after_change_time));
            }else if(temp_array[0].equals("open")){
                dataList.set(i,temp_string.replace("open","開盤 "));
            }else if(temp_array[0].equals("high")){
                dataList.set(i,temp_string.replace("high","最高價 "));
            }else if(temp_array[0].equals("low")){
                dataList.set(i,temp_string.replace("low","最低價 "));
            }else if(temp_array[0].equals("close")){
                dataList.set(0,temp_string.replace("close","目前 "));  //放第一筆
//                dataList.set(i,"");
            }else if(temp_array[0].equals("volume")){
                dataList.set(i,temp_string.replace("volume","交易量 "));
            }else if(temp_array[0].equals("millionAmount")){
//                dataList.set(i,"");
                dataList.remove(i);
            }
        }

        this.dataList = dataList;
        this.parentview = container;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false), parentview);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        String c = dataList.get(position);
        holder.tv1.setText(c);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        int deviceheight = displaymetrics.heightPixels;
        if(position == 0 || position > 7 ) {
            holder.tv1.setTextColor(Color.parseColor("#FF0000"));
            holder.tv1.getTextSize();
            holder.tv1.setTextSize(26);

            holder.tv1.getLayoutParams().height = deviceheight/5;
        }else {
            holder.tv1.getLayoutParams().height = deviceheight/10;
        }

    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}


