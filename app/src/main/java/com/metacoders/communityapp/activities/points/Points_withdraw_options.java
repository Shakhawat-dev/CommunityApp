package com.metacoders.communityapp.activities.points;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.payments.WithdrawPayment;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.AuthorPostResponse;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Points_withdraw_options extends AppCompatActivity {
    MaterialButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_withdraw_options);

        getSupportActionBar().hide();
        button = findViewById(R.id.pointView);

        button.setText(SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getTotal_point() + "");

        findViewById(R.id.backBtn).setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.withdraw_money).setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), WithdrawPayment.class));
        });

        findViewById(R.id.share_point).setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SharePointPage.class));
        });
//        button.setOnClickListener(v -> {
//            int point = 0 ;
//            double money = 0.0 ;
//            //Log.d("TAG", "onCreate: " );
//            try{
//            point  = Integer.parseInt(button.getText().toString() );
//            money = 0.0000122 * point ;
//            button.setText("£ " + String.format("%.4f" , money));
//
//            }catch (Exception e){
//                Log.e("TAG", "onCreate: " + e.getMessage());
//            }
//        });


    }

    public void loadUrPost() {
        // findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
        Call<AuthorPostResponse> catCall = api.getAuthorPost(SharedPrefManager.getInstance(getApplicationContext()).getUser_ID() + "");

        catCall.enqueue(new Callback<AuthorPostResponse>() {
            @Override
            public void onResponse(Call<AuthorPostResponse> call, Response<AuthorPostResponse> response) {
                AuthorPostResponse ownListModelList = response.body();

                if (response.code() == 200) {
                    ownListModelList = response.body();

                    button.setText(ownListModelList.getAuthor().getTotal_point());

                } else {
                    Toast.makeText(getApplicationContext(), "Error : Code " + response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<AuthorPostResponse> call, Throwable t) {
                //  findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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