package com.metacoders.communityapp.activities.payments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class WithdrawPayment extends AppCompatActivity {
    String bankType = "saving", cardType = "mastercard";
    RadioGroup bankTypeGroup, cardGroup;
    EditText b_nameET, bra_nameET, ac_nameET, ac_numberET, amt_bankET, card_numET, nameCardET, amt_cardET;
    NewsRmeApi api;
    MaterialButton button;
    String totalPoint = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_payment);
        button = findViewById(R.id.pointView);
        getSupportActionBar().hide();

        //   button.setText(SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getTotal_point() + "");

        bankTypeGroup = findViewById(R.id.bankGroup);
        cardGroup = findViewById(R.id.cardGroup);
        b_nameET = findViewById(R.id.bank_name);
        bra_nameET = findViewById(R.id.branch_name);
        ac_nameET = findViewById(R.id.ac_name);
        ac_numberET = findViewById(R.id.ac_number);
        amt_bankET = findViewById(R.id.transfer_amount_bank);
        nameCardET = findViewById(R.id.name_on_card);
        card_numET = findViewById(R.id.card_number);
        amt_cardET = findViewById(R.id.transfer_amount_card);

        api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
        bankTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.saving:
                    bankType = "saving";
                    break;
                case R.id.checking:
                    bankType = "checking";
                    break;
            }
        });
        cardGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.master:
                    cardType = "mastercard";
                    break;
                case R.id.visa:
                    cardType = "visa";
                    break;
            }
        });

        allClick();
        button.setOnClickListener(v -> {
            int point = 0;
            double money = 0.0;
            //Log.d("TAG", "onCreate: " );
            try {
                point = Integer.parseInt(totalPoint);
                money = 0.0000122 * point;
                button.setText("£ " + String.format("%.4f", money));

            } catch (Exception e) {
                Log.e("TAG", "onCreate: " + e.getMessage());
            }
        });
    }

    private void allClick() {

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        findViewById(R.id.bank_withdraw).setOnClickListener(v -> {

            getBankingData();

        });

        findViewById(R.id.card_withdraw).setOnClickListener(v -> {
            getCardDetais();
        });

    }

    private void getCardDetais() {
        String c_name, c_number, amt;
        double amt_int = 0;
        c_name = nameCardET.getText().toString();
        c_number = card_numET.getText().toString();
        amt = amt_cardET.getText().toString();

        if (c_name.isEmpty() || c_number.isEmpty() || amt.isEmpty()) {
            if (c_name.isEmpty()) {
                nameCardET.setError("Can't Be Empty");
            }
            if (c_number.isEmpty()) {
                card_numET.setError("Can't Be Empty");
            }
            if (amt.isEmpty()) {
                amt_cardET.setError("Can't Be Empty");
            }
            Toast.makeText(getApplicationContext(), "Error : Check All The fields", Toast.LENGTH_LONG).show();

        } else {
            amt_int = Double.parseDouble(amt);
            if (amt_int < 20) {
                amt_bankET.setError("Amount Must Be > 20 Pound");
                Toast.makeText(getApplicationContext(), "Error : Transfer Amount Can not be Less Than 20 Pound", Toast.LENGTH_LONG).show();
            } else {
                postWithdrawRequestCard(c_name, c_number, amt);
            }
        }


    }

    private void postWithdrawRequestCard(String c_name, String c_number, String amt) {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        Call<LoginResponse.forgetPassResponse> NetworkCall = api.withdrawToBank(
                cardType, c_name, c_number, amt
        );
        NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Msg : " + response.body().getMessage(), Toast.LENGTH_LONG).show();

                    if (response.body().getMessage().contains("successful")) {
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error : Try Again " + response.code(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error : Try Again " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getBankingData() {
        String b_name, bra_name, ac_name, ac_number, amt;
        double amt_int = 0;

        b_name = b_nameET.getText().toString();
        bra_name = bra_nameET.getText().toString();
        ac_name = ac_nameET.getText().toString();
        ac_number = ac_numberET.getText().toString();
        amt = amt_bankET.getText().toString();


        if (b_name.isEmpty() || bra_name.isEmpty() || ac_name.isEmpty() || ac_number.isEmpty() || amt.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Error : Check All The fields", Toast.LENGTH_LONG).show();
            if (b_name.isEmpty()) {
                b_nameET.setError("Can't Be Empty");
            }
            if (bra_name.isEmpty()) {
                bra_nameET.setError("Can't Be Empty");
            }
            if (ac_name.isEmpty()) {
                ac_nameET.setError("Can't Be Empty");
            }

            if (amt.isEmpty()) {
                amt_bankET.setError("Can't Be Empty");
            }
            if (ac_number.isEmpty()) {
                ac_numberET.setError("Can't Be Empty");
            }


        } else {
            amt_int = Double.parseDouble(amt);
            if (amt_int < 20) {
                amt_bankET.setError("Amount Must Be > 20 Pound");
                Toast.makeText(getApplicationContext(), "Error : Transfer Amount Can not be Less Than 20 Pound", Toast.LENGTH_LONG).show();
            } else {
                postWithDrawRequestBank(b_name, bra_name, ac_name, ac_number, amt);
            }
        }

    }

    private void postWithDrawRequestBank(String b_name, String bra_name, String ac_name, String ac_number, String amt) {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

        Call<LoginResponse.forgetPassResponse> NetworkCall = api.withdrawToBank(
                bankType, b_name, bra_name, ac_number, ac_name, amt
        );
        NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Msg : " + response.body().getMessage(), Toast.LENGTH_LONG).show();

                    if (response.body().getMessage().contains("successful")) {
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error : Try Again " + response.code(), Toast.LENGTH_LONG).show();

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

        ProgressDialog dialog = new ProgressDialog(WithdrawPayment.this);
        dialog.setMessage("Getting Your Details...");
        dialog.setCancelable(false);
        dialog.show();

        // findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
        Call<AuthorPostResponse> catCall = api.getAuthorPost(SharedPrefManager.getInstance(getApplicationContext()).getUser_ID() + "");

        catCall.enqueue(new Callback<AuthorPostResponse>() {
            @Override
            public void onResponse(Call<AuthorPostResponse> call, Response<AuthorPostResponse> response) {
                AuthorPostResponse ownListModelList = response.body();
                // findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                dialog.dismiss();
                if (response.code() == 200) {
                    ownListModelList = response.body();


                    // button.setText(ownListModelList.getAuthor().getTotal_point());
                    totalPoint = ownListModelList.getAuthor().getTotal_point();
                } else {
                    Toast.makeText(getApplicationContext(), "Error : Code " + response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<AuthorPostResponse> call, Throwable t) {
                //  findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error : Code " + t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUrPost();
    }
}