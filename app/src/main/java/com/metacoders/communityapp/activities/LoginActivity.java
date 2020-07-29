package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.RetrofitClient;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView registerTV;
    private TextInputEditText mUsername, mPassword;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializations();

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {

        String userName, password;
        userName = mUsername.getText().toString().trim();
        password = mPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {

            Call<LoginResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .login(userName, password);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.d(Utils.TAG, "onResponse: " + response.body().toString());
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d(Utils.TAG, "onResponse: " + t.toString());
                }
            });
        }

    }

    private void initializations() {
        registerTV = (TextView) findViewById(R.id.register_tv);
        mUsername = (TextInputEditText) findViewById(R.id.login_username);
        mPassword = (TextInputEditText) findViewById(R.id.login_password);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
    }
}