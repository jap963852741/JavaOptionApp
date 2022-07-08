package com.example.javaoptionapp.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.javaoptionapp.R;

public class LoadingDialog extends Dialog {

    private String TAG = "LoadingDialog";
    private static volatile LoadingDialog uniqueInstance;

    private LoadingDialog(@NonNull Context context) {
        super(context);
    }

    // 因為Constructor已經private，所以需要以下方法讓其他程式調用這個類別。
    public static LoadingDialog getInstance(Context context) {
        // 使用 synchronized 關鍵字避免多於一個Thread 進入
        if (uniqueInstance == null) {
            synchronized (LoadingDialog.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LoadingDialog(context);
                }
            }
        }
        return uniqueInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView tv;

        super.onCreate(savedInstanceState);
        //去掉默认的title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        setContentView(R.layout.loadingfragment);
        tv = findViewById(R.id.tv);
        tv.setText("正在更新.....");
        LinearLayout linearLayout = this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }

    @Override
    public void hide() {

        String threadName = Thread.currentThread().getStackTrace()[3].getMethodName();
        Log.e(TAG, threadName);

        try {
            Thread.sleep(100);//延遲0.1秒再關閉 (使用者體驗)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.hide();
    }

    @Override
    public void show() {
        String threadName = Thread.currentThread().getStackTrace()[3].getMethodName();
        Log.e(TAG, threadName);
        super.show();
    }

}
