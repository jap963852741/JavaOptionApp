package com.example.javaoptionapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.javaoptionapp.R;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<VH> {

    private List<String> dataList;
    private ViewGroup parentview;
    public HomeAdapter(List<String> dataList,ViewGroup container) {
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
    //        TextView tv2;
    public VH(@NonNull View itemView,View parent) {
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