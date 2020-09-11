package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.RetrofitClient;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.RegistrationResponse;
import com.metacoders.communityapp.models.UserModel;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1000;
    private TextInputEditText mName, mUserName, mEmail, mPassword;
    private Button mSignUpBtn;
    CheckBox isCehcked;
    Button googleBtn ;
    GoogleSignInOptions gso ;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        isCehcked = findViewById(R.id.termsCheck);
// google sign in builder
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestServerAuthCode("377758534352-4onskrq3rvnr7d8m3uekddh1hb5n178q.apps.googleusercontent.com")
                .requestEmail()
                .build();

        /*
        *if account is null then not yet signed in ;
        *
        */
       //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        getSupportActionBar().hide();
        initializations();

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCehcked.isChecked()) {
                    register();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Agree To ur Terms & Conditions", Toast.LENGTH_SHORT).show();
                }

            }
        });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });


    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void register() {

        String name, userName, email, password;

        if (!TextUtils.isEmpty(mUserName.getText().toString().trim()) && !TextUtils.isEmpty(mEmail.getText().toString().trim()) && !TextUtils.isEmpty(mPassword.getText().toString().trim())) {

            userName = mUserName.getText().toString().trim();
            name = mName.getText().toString().trim();
            email = mEmail.getText().toString().trim();
            password = mPassword.getText().toString().trim();

            NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

            Call<RegistrationResponse> call = api.registration(name, userName, email, password);

            call.enqueue(new Callback<RegistrationResponse>() {
                @Override
                public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().getError()) {
                            Toast.makeText(RegistrationActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(Constants.TAG, "onResponse: register" + response.body().toString());

                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {

                        Toast.makeText(RegistrationActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                    Log.d(Constants.TAG, "onResponse: register" + t.toString());
                    Toast.makeText(RegistrationActivity.this, "" + t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void initializations() {
        mName = (TextInputEditText) findViewById(R.id.regi_name);
        mUserName = (TextInputEditText) findViewById(R.id.regi_user_name);
        mEmail = (TextInputEditText) findViewById(R.id.regi_email);
        mPassword = (TextInputEditText) findViewById(R.id.regi_pass);
        mSignUpBtn = (Button) findViewById(R.id.signUPBtn);
        googleBtn = findViewById(R.id.gButton) ;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Log.d("TAG", "handleSignInResult: "+ account.getDisplayName()
            + account.getId() + " " + account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());

        }
    }
}