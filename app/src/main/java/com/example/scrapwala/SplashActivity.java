package com.example.scrapwala;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {
    private static int TIMEOUT = 5000;
    private GifImageView gifImageView;
    private TextView logo_text,logo_dec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        user_views();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);

                    startActivity(intent);
                    finish();

            }
        },TIMEOUT);

    }
    private void user_views()
    {
        gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        logo_text = (TextView)findViewById(R.id.Appname);

    }
}