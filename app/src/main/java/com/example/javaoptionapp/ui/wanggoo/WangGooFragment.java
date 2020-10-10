package com.example.javaoptionapp.ui.wanggoo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaoptionapp.R;
import com.example.javaoptionapp.dialog.LoadingDialog;
import com.example.javaoptionapp.dialog.LoadingDialogFragment;
import com.example.javaoptionapp.ui.home.HomeAdapter;
import com.example.javaoptionapp.ui.home.HomeFragment;
import com.hdl.calendardialog.CalendarView;
import com.hdl.calendardialog.CalendarViewDialog;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WangGooFragment extends Fragment {

    private WangGooViewModel wangGooViewModel;
    private WangGooAdapter wanggooAdapter;
    public static final int MSG_UPLOAD_Begin =  1;
    public static final int MSG_UPLOAD_Finish = 2;
    private static AVLoadingIndicatorView avi;
    public static Context strategyutil_context;
    public static LoadingDialog loadingdialog;
    public WangGooHistoryUtil wghu;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        strategyutil_context = getActivity().getApplicationContext();
        wangGooViewModel = new ViewModelProvider(this).get(WangGooViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wanggoo, container, false);
        final RecyclerView recyclerView =  root.findViewById(R.id.re_view_wanggoo);
        avi = root.findViewById(R.id.avi);
        Toolbar toolbar = root.findViewById(R.id.toolBar_wanggoo);
//        toolbar.setNavigationIcon(R.drawable.ic_history);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_history));//把三個小點換掉
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        loadingdialog = new LoadingDialog(getContext());
        //仅点击外部不可取消
        loadingdialog.setCanceledOnTouchOutside(false);
        //点击返回键和外部都不可取消
        loadingdialog.setCancelable(false);
        wghu = new WangGooHistoryUtil();
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add(Menu.NONE, WangGooFragment.ActionBar_Menu_Num.update_two_year_data, Menu.NONE, R.string.update_two_year_data);
        menu.add(Menu.NONE, WangGooFragment.ActionBar_Menu_Num.update_all_year_data, Menu.NONE, R.string.update_all_year_data);
    }

    public interface ActionBar_Menu_Num{
        final Integer update_two_year_data = 1;
        final Integer update_all_year_data = 2;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                wghu.post();
                loadingdialog.show();
                break;
            case 2:
                wghu.update_all_history();
                loadingdialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
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