package com.example.javaoptionapp.ui.home;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaoptionapp.R;
import com.example.javaoptionapp.Repository.bean.CMoneyBean;

import java.util.List;

public class HomeAdapterNew extends RecyclerView.Adapter<VH1> {
    private final String TAG = "HomeAdapterNew";
    private final CMoneyBean cMoneyBean;
    private final ViewGroup parentview;
    private int data_index = 0;

    public HomeAdapterNew(CMoneyBean cMoneyBean,ViewGroup container) {
        this.cMoneyBean = cMoneyBean;
        this.parentview = container;
    }
    public HomeAdapterNew(CMoneyBean cMoneyBean,ViewGroup container,int data_index) {
        this.cMoneyBean = cMoneyBean;
        this.parentview = container;
        this.data_index = data_index;
    }

    public void setData_index(int data_index){
        Log.e(TAG,"setData_index : " + data_index);
        this.data_index = data_index;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VH1(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false),parentview);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VH1 holder, int position) {
        String title = cMoneyBean.getTitle().get(position);
        String data = cMoneyBean.getData().get(data_index).get(position);

        holder.tv1.setText(title + " : " + data);
    }

    @Override
    public int getItemCount() {
        return cMoneyBean.getTitle().size();
    }

}


class VH1 extends RecyclerView.ViewHolder {
    TextView tv1;

    public VH1(@NonNull View itemView,View parent) {
        super(itemView);
        tv1 = itemView.findViewById(R.id.tv1);
        //parent 由 container傳來計算高度
        //item 的適應高度調整
        int parentHeight = parent.getHeight();
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.height =  parentHeight/12;
        //item 的適應高度調整
    }

}