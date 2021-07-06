package com.example.javaoptionapp.util.dialog;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.javaoptionapp.R;

import androidx.annotation.NonNull;

public class LoadingDialog extends Dialog {

    private String TAG = "LoadingDialog";
    private static LoadingDialog uniqueInstance;
    private int SHOW = 0;
    private int HIDE = 1;
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
        //所以在代码里设置，不知道是不是小米手机的原因
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        WindowManager.LayoutParams params = this.getWindow().getAttributes();
//        /*設置匯系統窗口*/
//        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//6.0
//            getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//        }else {
//            getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
//        }

        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        setContentView(R.layout.loadingfragment);
        Log.i("LHD", "LoadingDialog onCreate");
        tv = (TextView) findViewById(R.id.tv);
        tv.setText("正在更新.....");
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }

    @Override
    public void hide() {

        String threadName = Thread.currentThread().getStackTrace()[3].getMethodName();
        Log.e(TAG,threadName);

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
        Log.e(TAG,threadName);
        super.show();
    }

}
