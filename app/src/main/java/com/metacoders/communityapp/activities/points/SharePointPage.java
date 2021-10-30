package com.metacoders.communityapp.activities.points;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.newModels.AuthorPostResponse;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharePointPage extends AppCompatActivity {
    EditText accountNum, pointAmt;
    Button shareBtn;
    MaterialButton button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_point_page);
        getSupportActionBar().hide();

        accountNum = findViewById(R.id.accunt_Number);
        pointAmt = findViewById(R.id.point_amt);
        shareBtn = findViewById(R.id.send);

        button = findViewById(R.id.pointView);

        button.setText(SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getTotal_point() + "");


        shareBtn.setOnClickListener(v -> {
            String ac_number = accountNum.getText().toString();
            String point = pointAmt.getText().toString();

            if (point.isEmpty() || ac_number.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Error : Can't Be Empty", Toast.LENGTH_LONG).show();
            } else {
                //send the point
                RequestForPoint(ac_number, point);
            }
        });

        findViewById(R.id.backBtn).setOnClickListener(v -> {
            finish();
        });


    }

    private void RequestForPoint(String ac_number, String point) {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
        Call<LoginResponse.forgetPassResponse> NetworkCall = api.sharePoint(ac_number, point);
        NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {

                if (response.isSuccessful()) {

                    accountNum.setText("");
                    pointAmt.setText("");
                    Toast.makeText(getApplicationContext(), "Msg : " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    loadUrPost();
                    if (response.body().getMessage().contains("successful")) {

                        //finish();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Error : Try Again " + response.code(), Toast.LENGTH_LONG).show();
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error : Try Again " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadUrPost() {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
        Call<AuthorPostResponse> catCall = api.getAuthorPost(SharedPrefManager.getInstance(getApplicationContext()).getUser_ID() + "");

        catCall.enqueue(new Callback<AuthorPostResponse>() {
            @Override
            public void onResponse(Call<AuthorPostResponse> call, Response<AuthorPostResponse> response) {
                AuthorPostResponse ownListModelList = response.body();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                if (response.code() == 200) {
                    ownListModelList = response.body();

                    //   followerCount.setText(ownListModelList.);
                    button.setText(ownListModelList.getAuthor().getTotal_point());

                } else {
                    Toast.makeText(getApplicationContext(), "Error : Code " + response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<AuthorPostResponse> call, Throwable t) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error : Code " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onResume() {
        loadUrPost();
        super.onResume();
    }
}