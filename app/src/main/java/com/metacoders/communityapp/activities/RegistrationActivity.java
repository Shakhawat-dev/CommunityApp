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

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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

import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1000;
    private TextInputEditText mName, mUserName, mEmail, mPassword;
    private Button mSignUpBtn;
    CheckBox isCehcked;
    Button googleBtn  , fbBtn,vk;
    GoogleSignInOptions gso ;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        isCehcked = findViewById(R.id.termsCheck);
// google sign in builder
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestServerAuthCode("377780946002-5p5rvr3l34iroj52k02o2i1956ljgb76.apps.googleusercontent.com")
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


        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(RegistrationActivity.this , Arrays.asList("email" , "public_profile"));
                callbackManager = CallbackManager.Factory.create();

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // App code
                                Log.d("TAG", "onSuccess: "+ loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                                // App code
                                Log.d("TAG", "onCancel: Fb Cancel " );
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                Log.d("TAG", "Error:  "+ exception.getMessage()  );
                            }

                        });
            }
        });



// token traker
        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                if(currentAccessToken == null){
                    Log.d("TAG", "onCurrentAccessTokenChanged:  nuLl"  );
                }
                else loadUserInfo(currentAccessToken);

            }
        } ;

    }

    private  void loadUserInfo(AccessToken accessToken ){
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String first_name = object.getString("first_name") ;
                    String last_name = object.getString("last_name") ;
                    String mail = object.getString("email") ;


                    Log.d("TAG", "onCompleted: "+ first_name + " " + last_name + " " + mail)  ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }) ;

        Bundle parameters = new Bundle() ;
        parameters.putString("fields" , "first_name,last_name,email,id" );
        request.setParameters(parameters);
        request.executeAsync() ;
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
        vk = findViewById(R.id.vkIcon2) ;
        fbBtn = findViewById(R.id.fbBtn) ;


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

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

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