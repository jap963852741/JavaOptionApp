package com.example.javaoptionapp.ui.Option;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaoptionapp.R;
import com.example.javaoptionapp.Repository.bean.option.YahooOptionBean;

import java.util.ArrayList;
import java.util.List;

public class OptionAdapterNew extends RecyclerView.Adapter<VH1> {
    private ArrayList<YahooOptionBean> dataList;

    public OptionAdapterNew(ArrayList<YahooOptionBean> dataList, ViewGroup container) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public VH1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH1(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull VH1 holder, int position) {
        YahooOptionBean yahooOptionBean = dataList.get(position);
//        holder.strikePrice.setText(c);
        holder.strikePrice.setText(yahooOptionBean.getStrikePrice());
        holder.dealPrice.setText(yahooOptionBean.getDealPrice());
        holder.goalPrice.setText(yahooOptionBean.getGoal());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}


class VH1 extends RecyclerView.ViewHolder {
    TextView strikePrice;
    TextView dealPrice;
    TextView goalPrice;

    public VH1(@NonNull View itemView) {
        super(itemView);
        strikePrice = itemView.findViewById(R.id.strikePrice);
        dealPrice = itemView.findViewById(R.id.dealPrice);
        goalPrice = itemView.findViewById(R.id.goalPrice);
    }
}