package com.example.javaoptionapp.ui.home;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaoptionapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//controller
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private HomeAdapter homeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
//      homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);  已被官方棄用
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final RecyclerView recyclerView =  root.findViewById(R.id.re_view);


        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                List<String> Market_data = new ArrayList<>();
                Market_data = new ArrayList<String>(Arrays.asList(s.replaceAll("\\[" , "").replaceAll("]","").replaceAll("\""," ").split(",")));
                homeAdapter = new HomeAdapter(Market_data,container);
                recyclerView.setAdapter(homeAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

            }
        });
        return root;
    }






}
