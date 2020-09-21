package com.example.javaoptionapp.ui.wanggoo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaoptionapp.R;
import com.example.javaoptionapp.ui.home.HomeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WangGooFragment extends Fragment {

    private WangGooViewModel wangGooViewModel;
    private WangGooAdapter wanggooAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        wangGooViewModel = new ViewModelProvider(this).get(WangGooViewModel.class);

        View root = inflater.inflate(R.layout.fragment_wanggoo, container, false);
        final RecyclerView recyclerView =  root.findViewById(R.id.re_view_wanggoo);
//        final TextView textView = root.findViewById(R.id.text_wanggoo);
        wangGooViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                Log.i("WangGoo_data",s);
                if (s != "This is wanggoo fragment") {
                    List<String> WangGoo_data = new ArrayList<String>(Arrays.asList(s
                            .split(",")));
                    wanggooAdapter = new WangGooAdapter(WangGoo_data, container);
                    recyclerView.setAdapter(wanggooAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                }
            }
        });
        return root;
    }
}