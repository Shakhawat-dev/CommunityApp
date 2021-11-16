package com.metacoders.communityapp.activities.contactPage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.newModels.UserModel;
import com.metacoders.communityapp.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactPage extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1000;
    CheckBox isCehcked;
    Button googleBtn, fbBtn, vk;
    GoogleSignInOptions gso;
    Spinner genderSpinner;

    CallbackManager callbackManager;
    private TextInputEditText mName, mEmail, mPassword, mPhone, mMsg;
    private Button mSignUpBtn;
    private String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);
        initView();
    }

    private void initView() {
        mName = (TextInputEditText) findViewById(R.id.regi_name);
        mEmail = (TextInputEditText) findViewById(R.id.regi_email);
        mMsg = (TextInputEditText) findViewById(R.id.regi_pass);
        mSignUpBtn = (Button) findViewById(R.id.signUPBtn);
        mPhone = findViewById(R.id.phone);


        UserModel model = SharedPrefManager.getInstance(getApplicationContext()).getUserModel();

        mName.setText(model.getName());
        mEmail.setText(model.getEmail());
        mPhone.setText(model.getPhone());


        findViewById(R.id.signUPBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContact();
            }
        });
    }

    private void createContact() {

        String name, email, msg, phone;

        if (!TextUtils.isEmpty(mEmail.getText().toString().trim()) && !TextUtils.isEmpty(mMsg.getText().toString().trim())) {

            name = mName.getText().toString().trim();
            email = mEmail.getText().toString().trim();
            msg = mMsg.getText().toString().trim();
            phone = mPhone.getText().toString().trim();

            NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

            Call<LoginResponse.forgetPassResponse> call = api.createContactMessage(name, email, phone, msg);

            call.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
                @Override
                public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {
                    if (response.isSuccessful() && response.code() == 200) {

                        Toast.makeText(getApplicationContext(), response.body().getMessage() + "", Toast.LENGTH_LONG).show();

                        finish();


                    } else {

                        Toast.makeText(getApplicationContext(), "Error :  " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Please Check Your Entered Data!!!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}