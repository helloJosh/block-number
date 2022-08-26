package com.example.blocknumber;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // 액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Handler handler = new Handler();
        handler.postDelayed(new SplashHandler(), 2000);
    }

    public class SplashHandler implements Runnable{
        public void run(){
            Intent intent = new Intent(getApplicationContext(), LoginWithGoogle.class);
            startActivity(intent);
            finish();
        }
    }
}