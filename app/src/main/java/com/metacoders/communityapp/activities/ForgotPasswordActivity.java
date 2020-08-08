package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextInputEditText username, email;
    String userName, eMail;
    Button confirmBtn;
    AlertDialog dialog  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        dialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();

        username = findViewById(R.id.username_et);
        email = findViewById(R.id.email_et);
        confirmBtn = findViewById(R.id.confirm_button);


        confirmBtn.setOnClickListener(v -> {

            userName = username.getText().toString();
            eMail = email.getText().toString();

            sendResponse();

        });


    }

    private void sendResponse() {

        //forgetPassResponse

        //   Call netCall =


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

        Call<LoginResponse.forgetPassResponse> Call = api.forget_password(userName, eMail);

        Call.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(retrofit2.Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {
                if (response.isSuccessful() || response.code() == 201) {
                    LoginResponse.forgetPassResponse model = response.body();

                    if (!model.getError()) {
                        // response ok . password sent
                        dialog.setTitle("Success...") ;
                        dialog.setMessage("" + model.getMessage());

                    }
                    else {
                        dialog.setTitle("Error!!") ;
                        dialog.setMessage("Please Try Again");
                    }
                    dialog.setCancelable(true);
                    dialog.show();


                }

            }

            @Override
            public void onFailure(retrofit2.Call<LoginResponse.forgetPassResponse> call, Throwable t) {

            }
        });

    }
}