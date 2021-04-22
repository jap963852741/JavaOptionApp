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

public class OptionAdapterNew extends RecyclerView.Adapter<OptionAdapterNew.optionViewHolder> {
    private ArrayList<YahooOptionBean> dataList;

    public OptionAdapterNew(ArrayList<YahooOptionBean> dataList) {
        this.dataList = dataList;
    }

    /**
     * static inner class 可以直接调用构造器构造，如下：
     * OptionAdapterNew.optionViewHolder vh = new OptionAdapterNew.optionViewHolder();
     *
     * non-static inner class 需要持有外部类实例的引用，所以无法直接调用构造器创建，需要先构造外部类实例，然后通过外部类实例调用构造方法创建，如下：
     * OptionAdapterNew OptionAdapterNew = new OptionAdapterNew();
     * OptionAdapterNew.optionViewHolder vh = OptionAdapterNew.new StartAndEndTime();
     */
    static class optionViewHolder extends RecyclerView.ViewHolder {
        TextView strikePrice;
        TextView dealPrice;
        TextView goalPrice;

        public optionViewHolder(@NonNull View itemView) {
            super(itemView);
            strikePrice = itemView.findViewById(R.id.strikePrice);
            dealPrice = itemView.findViewById(R.id.dealPrice);
            goalPrice = itemView.findViewById(R.id.goalPrice);
        }
    }



    @NonNull
    @Override
    public optionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new optionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull optionViewHolder holder, int position) {
        YahooOptionBean yahooOptionBean = dataList.get(position);
        holder.strikePrice.setText(yahooOptionBean.getStrikePrice());
        holder.dealPrice.setText(yahooOptionBean.getDealPrice());
        holder.goalPrice.setText(yahooOptionBean.getGoal());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}


