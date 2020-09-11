package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.RetrofitClient;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.api.TokenInterceptor;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.News_List_Model;
import com.metacoders.communityapp.models.UserModel;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.StringGen;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView registerTV , forget_pass_tv ;
    private TextInputEditText mUsername, mPassword;
    private Button mLoginBtn;
    SharedPrefManager manager ;
    ProgressBar pbar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pbar = findViewById(R.id.progress_bar) ;
        pbar.setVisibility(View.GONE);
        manager = new SharedPrefManager( getApplicationContext() ) ;
        initializations();
        getSupportActionBar().hide();

        forget_pass_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });
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


    private void login() {

        String userName, password;

        if (!TextUtils.isEmpty(mUsername.getText().toString().trim()) && !TextUtils.isEmpty(mPassword.getText().toString().trim())) {

            pbar.setVisibility(View.VISIBLE);
            userName = mUsername.getText().toString().trim();
            password = mPassword.getText().toString().trim();

//            Call<LoginResponse> call = RetrofitClient
//                    .getInstance()
//                    .getApi()
//                    .login(userName, password);


//            sharedPrefManager = new SharedPrefManager(context) ;
//            String   accessTokens = sharedPrefManager.getUserToken();
//            Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

            NewsRmeApi api  = ServiceGenerator.createService(NewsRmeApi.class , "00") ;

            Call<LoginResponse> NetworkCall = api.login(userName, password) ;

            NetworkCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.d(Constants.TAG, "onResponse: " + response.body().toString());

                    LoginResponse res = response.body() ;

                    if(res.getError()){
                        // user pass or name wrong
                        pbar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext() , "Login Failed " , Toast.LENGTH_SHORT)
                                .show();
                    }
                    else {

                        UserModel userModel = new UserModel();
                        userModel = response.body().getUser();

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(userModel.getId(),
                                userModel.getUsername(),
                                userModel.getEmail(), userModel.getToken(),userModel.getRole(), userModel.getUserType() , userModel.getAvatar());


                        StringGen.token = userModel.getToken() ;
                        manager.saveUser(userModel.getEmail());
                        Log.d("TAG", "onResponse: " + userModel.getToken());
                     //   pbar.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
                        startActivity(intent);
                        finish();

                    }


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
        forget_pass_tv = findViewById(R.id.forget_passwordBtn);
    }
}