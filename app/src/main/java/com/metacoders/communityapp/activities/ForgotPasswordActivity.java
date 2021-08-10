package com.metacoders.communityapp.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextInputEditText username, email;
    String userName, eMail;
    Button confirmBtn;
    AlertDialog dialog;

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

        Call<JSONObject> Call = api.forget_password(eMail);

        Call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(retrofit2.Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful() || response.code() == 201) {


                }

            }

            @Override
            public void onFailure(retrofit2.Call<JSONObject> call, Throwable t) {

            }
        });

    }
}