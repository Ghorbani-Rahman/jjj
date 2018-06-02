package com.delaroystodios.metakar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class LoadingActivity extends AppCompatActivity {



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);


        setStatusBarTransparent();

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        Thread splash = new Thread(){
            @Override
            public void run() {
                try
                {
                    sleep(2000);
                }catch (InterruptedException whatIsTheProblem)
                {
                    whatIsTheProblem.printStackTrace();
                }finally {
                    Intent intent = new Intent(LoadingActivity.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        splash.start();

    }

    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }

}
