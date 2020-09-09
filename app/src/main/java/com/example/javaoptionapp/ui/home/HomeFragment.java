package com.example.javaoptionapp.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaoptionapp.MainActivity;
import com.example.javaoptionapp.R;
import com.hdl.calendardialog.CalendarView;
import com.hdl.calendardialog.CalendarViewDialog;
import com.hdl.calendardialog.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

//controller
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private HomeAdapter homeAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, ActionBar_Menu_Num.date, Menu.NONE, R.string.date);
//        date.setVisible(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
//                final Calendar calendar = Calendar.getInstance();
                List<Long>  markDays = new ArrayList<>();
                ArrayList market_date_array = CmoneyUtil.Market_Date;
                final SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                for (int i = 0; i < market_date_array.size()-1; i++) {
                    try {
                        Date d = sf.parse(String.valueOf(market_date_array.get(i)).replace("\"",""));
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
                                CmoneyUtil cu = new CmoneyUtil();
                                cu.post_tolken(temp_date);

                            }

                            @Override
                            public void onDayNotMarkClick(java.util.Calendar daySelectedCalendar) {
                                super.onDayNotMarkClick(daySelectedCalendar);
                                Toast.makeText(getContext(), "此日無開盤" , Toast.LENGTH_SHORT).show();

                            }

                        });





                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface ActionBar_Menu_Num{
        final Integer date = 1;
    }


}
