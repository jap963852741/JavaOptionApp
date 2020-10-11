package com.example.javaoptionapp.ui.Option;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.javaoptionapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OptionAdapter extends RecyclerView.Adapter<VH> {
    private List<String> dataList;
    private ViewGroup parentview;

    public OptionAdapter(List<String> dataList,ViewGroup container) {
        this.dataList = dataList;
        this.parentview = container;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false),parentview);

    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        String c = dataList.get(position);
        holder.tv1.setText(c);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}



class VH extends RecyclerView.ViewHolder {
    TextView tv1;
    public VH(@NonNull View itemView, View parent) {
        super(itemView);
        tv1 = itemView.findViewById(R.id.tv1);
    }
}