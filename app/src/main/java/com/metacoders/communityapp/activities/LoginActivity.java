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
import com.metacoders.communityapp.api.TokenInterceptor;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.UserModel;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.metacoders.communityapp.utils.Constants;

import java.io.IOException;

import okhttp3.ResponseBody;
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
//                changePassword();
            }
        });
    }

    //TODO: Checking Purpose, functionality will be changed
    private void changePassword() {

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .changePassword("123456", "sms123456");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null ) {
                    try {
                        Log.d(Constants.TAG, "onResponse: change pass " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(Constants.TAG, "onResponse: change pass " + t.toString());
            }
        });
    }

    private void login() {

        String userName, password;

        if (!TextUtils.isEmpty(mUsername.getText().toString().trim()) && !TextUtils.isEmpty(mPassword.getText().toString().trim())) {

            userName = mUsername.getText().toString().trim();
            password = mPassword.getText().toString().trim();

            Call<LoginResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .login(userName, password);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.d(Constants.TAG, "onResponse: " + response.body().toString());

                    UserModel userModel = new UserModel();
                    userModel = response.body().getUser();

                    SharedPrefManager.getInstance(getApplicationContext())
                            .userLogin(userModel.getId(), userModel.getUsername(), userModel.getEmail(), userModel.getToken(), userModel.getRole(), userModel.getUserType());

                    Intent intent = new Intent(LoginActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d(Constants.TAG, "onResponse: " + t.toString());
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