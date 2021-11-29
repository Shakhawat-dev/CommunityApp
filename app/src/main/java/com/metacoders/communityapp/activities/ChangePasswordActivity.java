package com.metacoders.communityapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    String oldpass, newPass;
    TextInputEditText oldPASS, newPASS, confirmPass;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPASS = findViewById(R.id.previous_password_field);
        newPASS = findViewById(R.id.new_password_field);
        confirmPass = findViewById(R.id.confirm_new_password_field);

        dialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
        findViewById(R.id.change_pass_btn).setOnClickListener(v -> {


            // getting data
            oldpass = oldPASS.getText().toString();
            newPass = newPASS.getText().toString();

            if (newPass.equals(confirmPass.getText().toString())) {

                changePassword(oldpass, newPass);

            } else {

                Toast.makeText(getApplicationContext(), "Both Passwords Didn't Match", Toast.LENGTH_LONG).show();

            }


        });

    }

    private void changePassword(String oldPass, String newPass) {
//
//        Call<ResponseBody> call = RetrofitClient
//                .getInstance()
//                .getApi()
//                .changePassword(oldPass, newPass);


        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));

        Call<LoginResponse.forgetPassResponse> call = api.changePassword(newPass, newPass, AppPreferences.getAccessToken(getApplicationContext()));

        call.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {

                LoginResponse.forgetPassResponse model = response.body();

               try{
                   if (model.getError()) {
                       dialog.setTitle("Error !!!");
                       dialog.setMessage("" + model.getMessage());
                       dialog.setCancelable(false);
                       dialog.setButton("ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                               dialog.dismiss();

                           }
                       });

                   }
                   else {
                       dialog.setTitle("Success !!!");
                       dialog.setMessage("" + model.getMessage());
                       dialog.setCancelable(false);
                       dialog.setButton("ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                               dialog.dismiss();
                               finish();

                           }
                       });

                   }
               }catch (Exception e ){

                   dialog.setTitle("Error !!!");
                   dialog.setMessage("" + model.getMessage());
                   dialog.setCancelable(false);
                   dialog.setButton("ok", (dialog, which) -> dialog.dismiss());
               }
                dialog.show();

            }

            @Override
            public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                Log.d(Constants.TAG, "onResponse: change pass " + t.toString());
            }
        });
    }
}