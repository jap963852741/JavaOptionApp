package com.example.javaoptionapp.ui.Option;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

public class OptionFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private OptionViewModel optionViewModel;
    private OptionAdapterNew OptionAdapterNew;
    private String[] Option_Month;
    private Button choose_option_month_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        optionViewModel =new ViewModelProvider(this,new OptionViewModelFactory()).get(OptionViewModel.class);


        View root = inflater.inflate(R.layout.fragment_option, container, false);
        final RecyclerView recyclerView =  root.findViewById(R.id.re_view_option);
        choose_option_month_button = (Button) root.findViewById(R.id.choose_option_month_button);
        choose_option_month_button.setOnClickListener(this);

        optionViewModel.getlistYahooOptionBean().observe(getViewLifecycleOwner(), new Observer<ArrayList<YahooOptionBean>>() {
            @Override
            public void onChanged(@Nullable ArrayList<YahooOptionBean> listYahooOptionBean) {

                OptionAdapterNew = new OptionAdapterNew(listYahooOptionBean);
                recyclerView.setAdapter(OptionAdapterNew);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            }
        });

        optionViewModel.getOption_Month().observe(getViewLifecycleOwner(), new Observer<String[]>() {
            @Override
            public void onChanged(@Nullable String[] option_Month) {
                Option_Month = option_Month;
            }
        });

        init("");//一開始無選擇月份

        return root;
    }

    public void init(String month){
        optionViewModel.yahooOptionApi(month);
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

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        init(Option_Month[item.getItemId()]);
        choose_option_month_button.setText("到期日 : " + Option_Month[item.getItemId()]);
        return false;
    }
    ///選彈式菜單






}