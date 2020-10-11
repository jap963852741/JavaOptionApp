package com.example.javaoptionapp.ui.Option;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaoptionapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionFragment extends Fragment {

    private OptionViewModel optionViewModel;
    private OptionAdapter OptionAdapter;

    public static Context option_context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        optionViewModel =new ViewModelProvider(this).get(OptionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_option, container, false);
        final RecyclerView recyclerView =  root.findViewById(R.id.re_view_option);
        option_context = getActivity().getApplicationContext();

        optionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                List<String> Option_data = new ArrayList<String>(Arrays.asList(s.split(",")));
                Log.i("Option_data",Option_data.toString());
                OptionAdapter = new OptionAdapter(Option_data, container);
                recyclerView.setAdapter(OptionAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            }
        });
        return root;
    }
}