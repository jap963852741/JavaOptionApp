package com.example.javaoptionapp.ui.Option;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaoptionapp.R;
import com.example.javaoptionapp.Repository.bean.option.YahooOptionBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.javaoptionapp.ui.Option.OptionViewModel.mText;

public class OptionFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private OptionViewModel optionViewModel;
    private OptionAdapter OptionAdapter;
    private OptionAdapterNew OptionAdapterNew;
    public static String[] Option_Month;
    public static Context option_context;
    public static Button choose_option_month_button;
    public static final int UPDATE_BUTTON_TEXT =  1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        optionViewModel =new ViewModelProvider(this).get(OptionViewModel.class);
        optionViewModel =new ViewModelProvider(this,new OptionViewModelFactory()).get(OptionViewModel.class);


        View root = inflater.inflate(R.layout.fragment_option, container, false);
        final RecyclerView recyclerView =  root.findViewById(R.id.re_view_option);
        option_context = getActivity().getApplicationContext();
        choose_option_month_button = (Button) root.findViewById(R.id.choose_option_month_button);
        choose_option_month_button.setOnClickListener(this);

        optionViewModel.getlistYahooOptionBean().observe(getViewLifecycleOwner(), new Observer<ArrayList<YahooOptionBean>>() {
            @Override
            public void onChanged(@Nullable ArrayList<YahooOptionBean> listYahooOptionBean) {

                OptionAdapterNew = new OptionAdapterNew(listYahooOptionBean, container);
                recyclerView.setAdapter(OptionAdapterNew);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            }
        });




//        optionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                List<String> Option_data = new ArrayList<String>(Arrays.asList(s.split(",")));
//                Log.i("Option_data",Option_data.toString());
//                OptionAdapter = new OptionAdapter(Option_data, container);
//                recyclerView.setAdapter(OptionAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
//            }
//        });


        return root;
    }


    ///選彈式菜單
    @Override
    public void onClick(View v) {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(getContext(), v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        Menu final_menu = popup.getMenu();
        int itemid_index = 0;
        for (String month : Option_Month){ //選擇權動態
            final_menu.add(Menu.NONE,itemid_index,Menu.NONE,month);
            itemid_index ++;
        }
        //填充菜单
        inflater.inflate(R.menu.option_menu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(this);
        //显示(这一行代码不要忘记了)
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        new OptionUtil(mText,item.getItemId());
        return false;
    }
    ///選彈式菜單


    public static Handler mUI_Handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_BUTTON_TEXT:
                    choose_option_month_button.setText("到期日 : " + OptionUtil.option_month_text);
                    break;
            }
        }
    };



}