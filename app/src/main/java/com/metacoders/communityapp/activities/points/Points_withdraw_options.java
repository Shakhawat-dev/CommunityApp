package com.metacoders.communityapp.activities.points;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.payments.WithdrawPayment;
import com.metacoders.communityapp.utils.SharedPrefManager;

public class Points_withdraw_options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_withdraw_options);


        getSupportActionBar().hide();

        SharedPrefManager.getInstance(getApplicationContext())

        findViewById(R.id.withdraw_money).setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), WithdrawPayment.class));
        });


    }
}