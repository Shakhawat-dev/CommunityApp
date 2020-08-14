package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.metacoders.communityapp.R;
import com.metacoders.communityapp.utils.SharedPrefManager;

public class SplashScreen extends AppCompatActivity {

    SharedPrefManager  manager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        manager = new SharedPrefManager( getApplicationContext()) ;

       Handler handler = new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {

               // to the activity
           }
       }, 1400);

    }
}