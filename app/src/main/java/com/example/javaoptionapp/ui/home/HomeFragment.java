package com.example.javaoptionapp.ui.home;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
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
import com.example.javaoptionapp.Repository.bean.CMoneyBean;
import com.hdl.calendardialog.CalendarView;
import com.hdl.calendardialog.CalendarViewDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;
    private HomeAdapterNew homeAdapterNew;
    private ArrayList Market_Date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory()).get(HomeViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final RecyclerView recyclerView =  root.findViewById(R.id.re_view);
        Button choose_button = (Button) root.findViewById(R.id.choose_button);
        choose_button.setOnClickListener(this);
        Toolbar toolbar = root.findViewById(R.id.toolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        homeViewModel.getMarket_Date().observe(getViewLifecycleOwner(), new Observer<ArrayList>() {
            @Override
            public void onChanged(@Nullable ArrayList arrayList) {
                Market_Date = arrayList;
            }
        });


        homeViewModel.getCMoneyBean().observe(getViewLifecycleOwner(), new Observer<CMoneyBean>() {
            @Override
            public void onChanged(@Nullable CMoneyBean cMoneyBean) {
                homeAdapterNew = new HomeAdapterNew(cMoneyBean,container);
                recyclerView.setAdapter(homeAdapterNew);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            }
        });


        //设置日曆
        toolbar.setNavigationIcon(R.drawable.calendar);
        //设置日曆点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Long>  markDays = new ArrayList<>();
                @SuppressLint("SimpleDateFormat") final SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

                for (int i = 0; i < Market_Date.size(); i++) {
                    try {
                        Date d = sf.parse(String.valueOf(Market_Date.get(i)).replace("\"",""));
                        long milliseconds = d.getTime();
                        markDays.add(milliseconds);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                Log.d(TAG,"markDays" + markDays);

                CalendarViewDialog.getInstance()
                        .init(requireContext())
                        .addMarks(markDays)
                        .setLimitMonth(true)
                        .show(new CalendarView.OnCalendarClickListener() {
                            @Override
                            public void onDayClick(java.util.Calendar calendar) {
                                CalendarViewDialog.getInstance()
                                        .init(requireContext()).close();
                                String temp_date = sf.format(calendar.getTime());
                                homeAdapterNew.setData_index(homeViewModel.getDate_date(temp_date));
                            }
                            @Override
                            public void onDayNotMarkClick(java.util.Calendar daySelectedCalendar) {
                                super.onDayNotMarkClick(daySelectedCalendar);
                                Toast.makeText(getContext(), "此日無開盤/或是無資料" , Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        init();

        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, ActionBar_Menu_Num.date, Menu.NONE, R.string.date);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                List<Long> markDays = new ArrayList<>();
                @SuppressLint("SimpleDateFormat") final SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                for (int i = 0; i < Market_Date.size(); i++) {
                    try {
                        Date d = sf.parse(String.valueOf(Market_Date.get(i)).replace("\"",""));
                        long milliseconds = d.getTime();
                        markDays.add(milliseconds);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                CalendarViewDialog.getInstance()
                        .init(getContext())
                        .addMarks(markDays)
                        .setLimitMonth(true)
                        .show(new CalendarView.OnCalendarClickListener() {
                            @Override
                            public void onDayClick(java.util.Calendar calendar) {
                                CalendarViewDialog.getInstance().close();
                                String temp_date = sf.format(calendar.getTime());
                                Log.i("temp_date",temp_date);
//                                homeViewModel.post_tolken(temp_date);
                                homeAdapterNew.setData_index(homeViewModel.getDate_date(temp_date));
                            }

                            @Override
                            public void onDayNotMarkClick(java.util.Calendar daySelectedCalendar) {
                                super.onDayNotMarkClick(daySelectedCalendar);
                                Toast.makeText(getContext(), "此日無開盤/或是無資料" , Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    public interface ActionBar_Menu_Num{
        final Integer date = 1;
    }




    ///選彈式菜單
    @Override
    public void onClick(View v) {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(getContext(), v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.home_menu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(this);
        //显示(这一行代码不要忘记了)
        popup.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.BA123690a://大盤籌碼日報
                homeViewModel.item_schma = "BA1-23690a";
                homeViewModel.CMoneyTokenApi();
                break;
            case R.id.BA123695a://大盤漲跌家數
                homeViewModel.item_schma = "BA1-23695a";
                homeViewModel.CMoneyTokenApi();
                break;
            case R.id.BA123691a://大盤外資
                homeViewModel.item_schma = "BA1-23691a";
                homeViewModel.CMoneyTokenApi();
                break;
            case R.id.BA123692a://大盤主力
                homeViewModel.item_schma = "BA1-23692a";
                homeViewModel.CMoneyTokenApi();
                break;
            case R.id.BA123693a://大盤官股
                homeViewModel.item_schma = "BA1-23693a";
                homeViewModel.CMoneyTokenApi();
                break;
            case R.id.BA123694a://大盤資券
                homeViewModel.item_schma = "BA1-23694a";
                homeViewModel.CMoneyTokenApi();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeAdapterNew = null;
    }


    private void init(){
        homeViewModel.CMoneyTokenApi();
    }




}
