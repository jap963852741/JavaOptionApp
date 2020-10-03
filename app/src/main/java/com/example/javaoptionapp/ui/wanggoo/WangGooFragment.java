package com.example.javaoptionapp.ui.wanggoo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WangGooFragment extends Fragment {

    private WangGooViewModel wangGooViewModel;
    private WangGooAdapter wanggooAdapter;
    public static ViewGroup container;
    public static View root;
    public static final int MSG_UPLOAD_Begin =  1;
    public static final int MSG_UPLOAD_Finish = 2;
    private static AVLoadingIndicatorView avi;
//    private Handler mUI_Handler = new Handler();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup fcontainer, Bundle savedInstanceState) {
        wangGooViewModel = new ViewModelProvider(this).get(WangGooViewModel.class);
        container = fcontainer;
        root = inflater.inflate(R.layout.fragment_wanggoo, container, false);
        final RecyclerView recyclerView =  root.findViewById(R.id.re_view_wanggoo);
        avi = WangGooFragment.root.findViewById(R.id.avi);



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




    public static Handler mUI_Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPLOAD_Begin:
                    startAnim();
                    break;
                case MSG_UPLOAD_Finish:
                    stopAnim();
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

}