package com.example.javaoptionapp.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.javaoptionapp.R;

import androidx.annotation.NonNull;

public class LoadingDialog extends Dialog {

    private static LoadingDialog uniqueInstance;

    private LoadingDialog(@NonNull Context context) {
        super(context);
    }

    // 因為Constructor已經private，所以需要以下方法讓其他程式調用這個類別。
    public static LoadingDialog getInstance(Context context) {
        // 使用 synchronized 關鍵字避免多於一個Thread 進入
        if(uniqueInstance == null){
            synchronized(LoadingDialog.class){
                if(uniqueInstance == null){
                    uniqueInstance = new LoadingDialog(context);
                }
            }
        }         return uniqueInstance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView tv;

        super.onCreate(savedInstanceState);
        //去掉默认的title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉白色边角 我的小米手机在xml里设置 android:background="@android:color/transparent"居然不生效
        //所以在代码里设置，不知道是不是小米手机的原因
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        setContentView(R.layout.loadingfragment);
        Log.i("LHD", "LoadingDialog onCreate");
        tv = (TextView) findViewById(R.id.tv);
        tv.setText("正在更新.....");
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }

}
